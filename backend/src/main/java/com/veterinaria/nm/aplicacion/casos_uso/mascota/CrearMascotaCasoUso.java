package com.veterinaria.nm.aplicacion.casos_uso.mascota;

import com.veterinaria.nm.aplicacion.dto.peticion.CrearMascotaPeticion;
import com.veterinaria.nm.aplicacion.dto.respuesta.MascotaRespuesta;
import com.veterinaria.nm.aplicacion.mapeadores.MascotaMapeador;
import com.veterinaria.nm.dominio.excepciones.ClienteNoEncontradoException;
import com.veterinaria.nm.dominio.modelo.Mascota;
import com.veterinaria.nm.dominio.puertos.salida.RepositorioCliente;
import com.veterinaria.nm.dominio.puertos.salida.RepositorioMascota;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: registrar una nueva mascota en la clínica.
 * <p>
 * Orquesta la lógica de negocio sin contener reglas de dominio propias.
 * Verifica que el propietario exista, crea la mascota mediante el factory
 * method del dominio, y persiste a través del repositorio (puerto de salida).
 * </p>
 * <p>
 * Al estar anotado con {@code @Service}, Spring lo detecta y lo inyecta
 * donde se declare {@code ServicioMascota}. El controlador REST no conoce
 * esta clase directamente: trabaja con la interfaz del puerto de entrada.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CrearMascotaCasoUso {

    private final RepositorioMascota repositorioMascota;
    private final RepositorioCliente repositorioCliente;
    private final MascotaMapeador mapeador;

    /**
     * Ejecuta el caso de uso.
     *
     * @param peticion DTO con los datos de la nueva mascota
     * @return DTO de respuesta con la mascota persistida
     * @throws ClienteNoEncontradoException si el propietario no existe
     * @throws IllegalArgumentException     si el microchip ya está registrado
     */
    public MascotaRespuesta ejecutar(CrearMascotaPeticion peticion) {
        // Verificar que el propietario existe antes de crear la mascota
        repositorioCliente.buscarPorId(peticion.idPropietario())
                .orElseThrow(() -> new ClienteNoEncontradoException(peticion.idPropietario()));

        // Verificar unicidad del microchip si se proporciona
        if (peticion.numeroMicrochip() != null
                && repositorioMascota.existePorMicrochip(peticion.numeroMicrochip())) {
            throw new IllegalArgumentException(
                    "El microchip %s ya está registrado en el sistema".formatted(peticion.numeroMicrochip()));
        }

        // Crear la entidad de dominio a través del factory method — las reglas de negocio viven ahí
        Mascota mascota = Mascota.crear(
                peticion.nombre(),
                peticion.idEspecie(),
                peticion.idRaza(),
                peticion.sexo(),
                peticion.fechaNacimiento(),
                peticion.pesoActual(),
                peticion.colorPelaje(),
                peticion.numeroMicrochip(),
                peticion.esterilizado(),
                peticion.idPropietario()
        );

        Mascota guardada = repositorioMascota.guardar(mascota);

        return mapeador.aDtoRespuesta(guardada);
    }
}
