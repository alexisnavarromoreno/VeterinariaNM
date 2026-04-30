package com.veterinaria.nm.aplicacion.dto.respuesta;

import com.veterinaria.nm.dominio.modelo.Cita.EstadoCita;
import com.veterinaria.nm.dominio.modelo.Cita.OrigenCita;
import com.veterinaria.nm.dominio.modelo.Cita.TipoCita;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de respuesta para una cita.
 * Incluye nombres desnormalizados de mascota y veterinario
 * para que el frontend no necesite múltiples llamadas para renderizar la agenda.
 */
public record CitaRespuesta(
        UUID id,
        UUID idMascota,
        String nombreMascota,
        String especieMascota,
        UUID idVeterinario,
        String nombreVeterinario,
        LocalDateTime fechaHora,
        LocalDateTime fechaFin,
        int duracionMinutos,
        TipoCita tipoCita,
        EstadoCita estado,
        String motivoConsulta,
        String notasVeterinario,
        OrigenCita origen,
        UUID idHistorialGenerado,
        LocalDateTime fechaCreacion
) {}
