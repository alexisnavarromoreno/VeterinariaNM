package com.veterinaria.nm.dominio.puertos.salida;

import com.veterinaria.nm.dominio.modelo.HistorialClinico;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para la persistencia del historial clínico.
 */
public interface RepositorioHistorialClinico {

    HistorialClinico guardar(HistorialClinico historial);

    Optional<HistorialClinico> buscarPorId(UUID id);

    Optional<HistorialClinico> buscarPorCita(UUID idCita);

    /** Historial completo de la mascota ordenado por fecha descendente. */
    List<HistorialClinico> listarPorMascota(UUID idMascota);

    List<HistorialClinico> listarPorVeterinario(UUID idVeterinario);

    List<HistorialClinico> listarPorMascotaYFecha(UUID idMascota, LocalDate desde, LocalDate hasta);
}
