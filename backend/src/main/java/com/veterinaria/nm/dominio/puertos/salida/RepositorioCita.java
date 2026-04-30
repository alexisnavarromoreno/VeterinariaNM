package com.veterinaria.nm.dominio.puertos.salida;

import com.veterinaria.nm.dominio.modelo.Cita;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para la persistencia de citas.
 */
public interface RepositorioCita {

    Cita guardar(Cita cita);

    Optional<Cita> buscarPorId(UUID id);

    List<Cita> listarPorFecha(LocalDate fecha);

    List<Cita> listarPorMascota(UUID idMascota);

    List<Cita> listarPorVeterinario(UUID idVeterinario);

    List<Cita> listarPorVeterinarioYFecha(UUID idVeterinario, LocalDate fecha);

    /**
     * Comprueba si el veterinario tiene citas solapadas en el rango horario indicado.
     * Fundamental para el control de disponibilidad en la agenda.
     *
     * @param idVeterinario UUID del veterinario
     * @param inicio        Inicio del rango a verificar
     * @param fin           Fin del rango a verificar
     * @param excluirCitaId UUID de la cita a excluir (para reprogramaciones)
     */
    boolean existeSolapamiento(UUID idVeterinario, LocalDateTime inicio,
                               LocalDateTime fin, UUID excluirCitaId);

    List<Cita> listarCitasPendientesDeHoy();
}
