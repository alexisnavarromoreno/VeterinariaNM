package com.veterinaria.nm.dominio.puertos.salida;

import com.veterinaria.nm.dominio.modelo.Mascota;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para la persistencia de mascotas.
 */
public interface RepositorioMascota {

    Mascota guardar(Mascota mascota);

    Optional<Mascota> buscarPorId(UUID id);

    Optional<Mascota> buscarPorMicrochip(String numeroMicrochip);

    List<Mascota> listarPorPropietario(UUID idPropietario);

    List<Mascota> listarActivas();

    List<Mascota> buscarPorNombre(String nombre);

    /** Útil para cargar la agenda del veterinario: todas las mascotas de una especie/raza. */
    List<Mascota> listarPorEspecie(UUID idEspecie);

    boolean existePorMicrochip(String microchip);
}
