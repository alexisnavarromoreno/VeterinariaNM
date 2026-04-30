package com.veterinaria.nm.dominio.modelo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad que modela una cita en la agenda de la clínica.
 * <p>
 * Implementa una máquina de estados explícita: cada transición de estado
 * tiene precondiciones que el dominio valida. Esto impide, por ejemplo,
 * confirmar una cita cancelada o completar una cita no iniciada.
 * </p>
 *
 * <pre>
 * SOLICITADA → CONFIRMADA → EN_CURSO → COMPLETADA
 *     ↓             ↓          ↓
 *  CANCELADA     CANCELADA  CANCELADA
 * </pre>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Cita {

    private final UUID id;
    private final UUID idMascota;

    /**
     * Veterinario asignado a esta cita.
     * Se elige en el momento de confirmar si no se asignó al solicitar.
     */
    private UUID idVeterinario;

    private LocalDateTime fechaHora;

    /** Duración estimada en minutos. Permite gestionar la agenda sin solapamientos. */
    private int duracionMinutos;

    private TipoCita tipoCita;
    private EstadoCita estado;

    /** Descripción del motivo de consulta aportada por el propietario al solicitar la cita. */
    private String motivoConsulta;

    /**
     * Notas del veterinario al preparar o completar la cita.
     * Ej: "Revisar analítica anterior", "Traer lista de medicamentos actuales"
     */
    private String notasVeterinario;

    /**
     * UUID del historial clínico generado al completar la cita.
     * Null hasta que la cita pasa a estado COMPLETADA.
     */
    private UUID idHistorialGenerado;

    private final LocalDateTime fechaCreacion;

    /** Quién solicitó la cita: propietario online, recepcionista, veterinario. */
    private OrigenCita origen;

    // ── Factory method ────────────────────────────────────────────────────────

    public static Cita solicitar(UUID idMascota, UUID idVeterinario, LocalDateTime fechaHora,
                                 int duracionMinutos, TipoCita tipoCita, String motivoConsulta,
                                 OrigenCita origen) {
        Objects.requireNonNull(idMascota, "La cita debe estar asociada a una mascota");
        Objects.requireNonNull(fechaHora, "La fecha y hora son obligatorias");
        Objects.requireNonNull(tipoCita, "El tipo de cita es obligatorio");
        validarFechaFutura(fechaHora);
        if (motivoConsulta == null || motivoConsulta.isBlank())
            throw new IllegalArgumentException("El motivo de consulta es obligatorio");

        return Cita.builder()
                .id(UUID.randomUUID())
                .idMascota(idMascota)
                .idVeterinario(idVeterinario)
                .fechaHora(fechaHora)
                .duracionMinutos(duracionMinutos > 0 ? duracionMinutos : 20)
                .tipoCita(tipoCita)
                .estado(EstadoCita.SOLICITADA)
                .motivoConsulta(motivoConsulta.trim())
                .origen(origen != null ? origen : OrigenCita.PRESENCIAL)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    // ── Máquina de estados ────────────────────────────────────────────────────

    /**
     * Confirma la cita. Permite asignar o reasignar veterinario en este momento.
     * Típicamente lo hace recepción tras verificar disponibilidad en agenda.
     */
    public void confirmar(UUID idVeterinario) {
        validarTransicion(EstadoCita.SOLICITADA, "confirmar");
        Objects.requireNonNull(idVeterinario, "Se debe asignar un veterinario al confirmar");
        this.idVeterinario = idVeterinario;
        this.estado = EstadoCita.CONFIRMADA;
    }

    /**
     * Inicia la consulta cuando el paciente está en sala.
     * A partir de aquí se puede crear el historial clínico.
     */
    public void iniciar() {
        validarTransicion(EstadoCita.CONFIRMADA, "iniciar");
        this.estado = EstadoCita.EN_CURSO;
    }

    /**
     * Completa la cita vinculándola al historial clínico generado.
     *
     * @param idHistorial UUID del {@link HistorialClinico} creado en esta consulta
     */
    public void completar(UUID idHistorial) {
        validarTransicion(EstadoCita.EN_CURSO, "completar");
        Objects.requireNonNull(idHistorial, "Debe vincularse un historial clínico al completar la cita");
        this.estado = EstadoCita.COMPLETADA;
        this.idHistorialGenerado = idHistorial;
    }

    /**
     * Cancela la cita desde cualquier estado activo.
     * Las citas completadas no se pueden cancelar.
     */
    public void cancelar(String motivo) {
        if (estado == EstadoCita.COMPLETADA)
            throw new IllegalStateException("No se puede cancelar una cita ya completada");
        this.estado = EstadoCita.CANCELADA;
        this.notasVeterinario = motivo;
    }

    /**
     * Reprograma la cita a una nueva fecha/hora.
     * Vuelve a estado SOLICITADA para que recepción confirme de nuevo.
     */
    public void reprogramar(LocalDateTime nuevaFechaHora) {
        if (estado == EstadoCita.COMPLETADA || estado == EstadoCita.CANCELADA)
            throw new IllegalStateException("No se puede reprogramar una cita en estado " + estado);
        validarFechaFutura(nuevaFechaHora);
        this.fechaHora = nuevaFechaHora;
        this.estado = EstadoCita.SOLICITADA;
    }

    public void agregarNotasVeterinario(String notas) {
        this.notasVeterinario = notas;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void validarTransicion(EstadoCita estadoRequerido, String accion) {
        if (this.estado != estadoRequerido)
            throw new IllegalStateException(
                    "No se puede %s una cita en estado %s. Estado requerido: %s"
                            .formatted(accion, this.estado, estadoRequerido));
    }

    private static void validarFechaFutura(LocalDateTime fecha) {
        if (fecha.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("La fecha de la cita debe ser futura");
    }

    // ── Enums ─────────────────────────────────────────────────────────────────

    public enum EstadoCita {
        SOLICITADA, CONFIRMADA, EN_CURSO, COMPLETADA, CANCELADA
    }

    public enum TipoCita {
        CONSULTA_GENERAL, URGENCIA, VACUNACION, CIRUGIA,
        REVISION_POSTOPERATORIA, DESPARASITACION, CONTROL_CRONICO,
        PRUEBA_DIAGNOSTICA, PELUQUERIA_ESTETICA
    }

    public enum OrigenCita {
        /** Registrada por recepción en el mostrador o por teléfono */
        PRESENCIAL,
        /** Registrada por el propietario desde el portal web */
        PORTAL_CLIENTE,
        /** Creada automáticamente por el sistema (ej: recordatorio de vacuna) */
        SISTEMA
    }
}
