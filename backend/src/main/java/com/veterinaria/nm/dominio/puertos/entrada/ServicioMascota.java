package com.veterinaria.nm.dominio.puertos.entrada;

import com.veterinaria.nm.aplicacion.dto.peticion.CrearMascotaPeticion;
import com.veterinaria.nm.aplicacion.dto.respuesta.MascotaRespuesta;

import java.util.List;
import java.util.UUID;

/**
 * Puerto de entrada para el módulo de mascotas.
 * Opera con DTOs en la frontera: el dominio no se expone hacia afuera.
 */
public interface ServicioMascota {

    MascotaRespuesta crearMascota(CrearMascotaPeticion peticion);

    MascotaRespuesta obtenerMascotaPorId(UUID id);

    MascotaRespuesta obtenerMascotaPorMicrochip(String microchip);

    List<MascotaRespuesta> listarMascotasDePropietario(UUID idPropietario);

    MascotaRespuesta actualizarPeso(UUID id, double nuevoPeso);

    void desactivarMascota(UUID id);

    List<MascotaRespuesta> buscarPorNombre(String nombre);
}
