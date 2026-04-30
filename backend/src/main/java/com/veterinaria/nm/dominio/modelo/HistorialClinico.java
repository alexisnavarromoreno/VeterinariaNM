package com.veterinaria.nm.dominio.modelo;

import com.veterinaria.nm.dominio.modelo.valor.SignosVitales;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Entidad raíz del agregado HistorialClinico.
 * <p>
 * Cada entrada representa un evento médico completo: captura los signos vitales,
 * la anamnesis, la exploración, el diagnóstico, las pruebas diagnósticas
 * ({@link PruebaDiagnostica}), los procedimientos realizados en clínica
 * ({@link ProcedimientoClinico}), la prescripción generada y el plan de seguimiento.
 * </p>
 * <p>
 * Una vez cerrado ({@link #cerrarConsulta()}), el diagnóstico es inmutable.
 * Los resultados de pruebas pendientes se pueden añadir después del cierre
 * sin reabrir el historial (caso de uso frecuente: laboratorio externo).
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class HistorialClinico {

    private final UUID id;
    private final UUID idMascota;

    /** Cita origen. Null en registros manuales o urgencias sin cita previa. */
    private final UUID idCita;

    private final UUID idVeterinario;
    private final LocalDateTime fechaConsulta;
    private final TipoRegistro tipoRegistro;

    // ── Signos vitales ────────────────────────────────────────────────────────

    /**
     * Signos vitales tomados al inicio de la consulta.
     * El peso aquí puede diferir del guardado en {@link Mascota} (que se actualiza después).
     */
    private final SignosVitales signosVitales;

    // ── Anamnesis ─────────────────────────────────────────────────────────────

    /** Lo que refiere el propietario: motivo, duración de síntomas, evolución. */
    private final String sintomasReferidos;

    /** Hallazgos de la exploración física del veterinario. */
    private String hallazgosExploracion;

    // ── Diagnóstico ───────────────────────────────────────────────────────────

    private String diagnosticoPrincipal;

    /** Diagnósticos secundarios o comorbilidades halladas. */
    private String diagnosticosSecundarios;

    /**
     * Lista de diagnósticos posibles mientras se esperan pruebas confirmatorias.
     * Ej: "Posible linfoma vs hepatopatía crónica — pendiente biopsia hepática".
     */
    private String diagnosticoDiferencial;

    // ── Pruebas diagnósticas ──────────────────────────────────────────────────

    /**
     * Pruebas diagnósticas solicitadas y/o realizadas en esta consulta.
     * Cada una tiene su propio estado y resultado ({@link PruebaDiagnostica}).
     */
    @Builder.Default
    private final List<PruebaDiagnostica> pruebasDiagnosticas = new ArrayList<>();

    // ── Procedimientos ────────────────────────────────────────────────────────

    /**
     * Procedimientos realizados físicamente en la consulta (curas, suturas, catéteres...).
     * Distinto de la prescripción, que son los medicamentos para administrar en casa.
     */
    @Builder.Default
    private final List<ProcedimientoClinico> procedimientosRealizados = new ArrayList<>();

    // ── Tratamiento ───────────────────────────────────────────────────────────

    /**
     * UUID de la prescripción generada en esta consulta.
     * Null si no se prescribió medicación para casa.
     */
    private UUID idPrescripcion;

    // ── Seguimiento ───────────────────────────────────────────────────────────

    /** Instrucciones domiciliarias para el propietario: dieta, reposo, cuidados. */
    private String recomendacionesDomiciliarias;

    private LocalDate fechaProximaRevision;

    /**
     * Observaciones añadidas después del cierre.
     * Ej: resultados de laboratorio tardíos, llamada de seguimiento.
     * Se acumulan con separador para mantener trazabilidad cronológica.
     */
    private String observacionesPosteriores;

    /** Una vez cerrado, el diagnóstico es inmutable. */
    private boolean cerrado;

    // ── Factory method ────────────────────────────────────────────────────────

    public static HistorialClinico iniciar(UUID idMascota, UUID idCita, UUID idVeterinario,
                                            TipoRegistro tipoRegistro, SignosVitales signosVitales,
                                            String sintomasReferidos) {
        Objects.requireNonNull(idMascota, "El historial debe asociarse a una mascota");
        Objects.requireNonNull(idVeterinario, "El historial debe tener un veterinario");
        Objects.requireNonNull(tipoRegistro, "El tipo de registro es obligatorio");

        return HistorialClinico.builder()
                .id(UUID.randomUUID())
                .idMascota(idMascota)
                .idCita(idCita)
                .idVeterinario(idVeterinario)
                .fechaConsulta(LocalDateTime.now())
                .tipoRegistro(tipoRegistro)
                .signosVitales(signosVitales)
                .sintomasReferidos(sintomasReferidos)
                .cerrado(false)
                .build();
    }

    // ── Comportamiento de negocio ─────────────────────────────────────────────

    public void registrarExploracion(String hallazgos) {
        validarAbierto();
        this.hallazgosExploracion = hallazgos;
    }

    public void diagnosticar(String principal, String secundarios, String diferencial) {
        validarAbierto();
        if (principal == null || principal.isBlank())
            throw new IllegalArgumentException("El diagnóstico principal es obligatorio");
        this.diagnosticoPrincipal = principal;
        this.diagnosticosSecundarios = secundarios;
        this.diagnosticoDiferencial = diferencial;
    }

    /** Añade una prueba diagnóstica a la consulta. */
    public void agregarPrueba(PruebaDiagnostica prueba) {
        validarAbierto();
        Objects.requireNonNull(prueba, "La prueba diagnóstica no puede ser nula");
        pruebasDiagnosticas.add(prueba);
    }

    /** Añade un procedimiento realizado en consulta. */
    public void registrarProcedimiento(ProcedimientoClinico procedimiento) {
        validarAbierto();
        Objects.requireNonNull(procedimiento, "El procedimiento no puede ser nulo");
        procedimientosRealizados.add(procedimiento);
    }

    public void vincularPrescripcion(UUID idPrescripcion) {
        validarAbierto();
        this.idPrescripcion = idPrescripcion;
    }

    public void registrarSeguimiento(String recomendaciones, LocalDate proximaRevision) {
        validarAbierto();
        this.recomendacionesDomiciliarias = recomendaciones;
        this.fechaProximaRevision = proximaRevision;
    }

    public void cerrarConsulta() {
        if (diagnosticoPrincipal == null || diagnosticoPrincipal.isBlank())
            throw new IllegalStateException("No se puede cerrar una consulta sin diagnóstico principal");
        this.cerrado = true;
    }

    /**
     * Registra el resultado de una prueba diagnóstica pendiente.
     * Permitido aunque la consulta esté cerrada (resultados tardíos de laboratorio).
     */
    public void registrarResultadoPrueba(UUID idPrueba, String resultado, boolean alterado) {
        pruebasDiagnosticas.stream()
                .filter(p -> p.getId().equals(idPrueba))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Prueba diagnóstica no encontrada: " + idPrueba))
                .registrarResultado(resultado, alterado);
    }

    /** Añade observaciones posteriores al cierre. No reabre el historial. */
    public void agregarObservacionPosterior(String observacion) {
        if (observacion == null || observacion.isBlank()) return;
        this.observacionesPosteriores = observacionesPosteriores == null
                ? observacion
                : observacionesPosteriores + "\n---\n" + observacion;
    }

    /** Indica si hay pruebas con resultado pendiente. Útil para mostrar alertas en UI. */
    public boolean tienePruebasPendientes() {
        return pruebasDiagnosticas.stream()
                .anyMatch(p -> p.getEstado() == PruebaDiagnostica.EstadoPrueba.PENDIENTE_RESULTADO
                        || p.getEstado() == PruebaDiagnostica.EstadoPrueba.SOLICITADA);
    }

    public List<PruebaDiagnostica> getPruebasDiagnosticas() { return Collections.unmodifiableList(pruebasDiagnosticas); }
    public List<ProcedimientoClinico> getProcedimientosRealizados() { return Collections.unmodifiableList(procedimientosRealizados); }

    private void validarAbierto() {
        if (cerrado)
            throw new IllegalStateException("No se puede modificar un historial clínico cerrado");
    }

    // ── Enum de tipo de registro ──────────────────────────────────────────────

    public enum TipoRegistro {
        CONSULTA_GENERAL,
        URGENCIA,
        VACUNACION,
        CIRUGIA,
        REVISION_POSTOPERATORIA,
        CONTROL_ENFERMEDAD_CRONICA,
        ANALISIS_LABORATORIO,
        PRUEBA_IMAGEN,
        DESPARASITACION,
        PELUQUERIA_ESTETICA,
        HOSPITALIZACION,
        TELECONSULTA
    }
}
