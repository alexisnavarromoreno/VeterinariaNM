package com.veterinaria.nm.dominio.puertos.entrada;

import com.veterinaria.nm.dominio.modelo.Mascota;

import java.util.List;
import java.util.UUID;

/**
 * Puerto de entrada para el módulo de mascotas.
 * <p>
 * Define los casos de uso disponibles hacia el exterior (controladores REST,
 * portal web, tests de integración). Cada método corresponde a una operación
 * de negocio con nombre en español del dominio.
 * </p>
 */
public interface ServicioMascota {

    Mascota crearMascota(Mascota mascota);

    Mascota obtenerMascotaPorId(UUID id);

    Mascota obtenerMascotaPorMicrochip(String microchip);

    List<Mascota> listarMascotasDePropietario(UUID idPropietario);

    Mascota actualizarMascota(UUID id, Mascota datos);

    void desactivarMascota(UUID id);

    /**
     * Actualiza el peso de la mascota y retorna la entidad actualizada.
     * Caso de uso frecuente: el técnico toma el peso al inicio de cada consulta.
     */
    Mascota actualizarPeso(UUID id, double nuevoPeso);

    void agregarAlergia(UUID idMascota, String alergia);

    void agregarEnfermedadCronica(UUID idMascota, String enfermedad);

    List<Mascota> buscarPorNombre(String nombre);
}
