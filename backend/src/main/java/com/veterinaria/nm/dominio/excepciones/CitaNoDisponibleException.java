package com.veterinaria.nm.dominio.excepciones;

import java.time.LocalDateTime;

/**
 * Se lanza cuando el horario solicitado para una cita ya está ocupado
 * o el veterinario no tiene disponibilidad.
 */
public class CitaNoDisponibleException extends RuntimeException {
    public CitaNoDisponibleException(LocalDateTime fechaHora) {
        super("El horario %s no está disponible para la agenda solicitada".formatted(fechaHora));
    }
    public CitaNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
