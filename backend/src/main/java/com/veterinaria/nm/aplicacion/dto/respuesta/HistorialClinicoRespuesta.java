package com.veterinaria.nm.aplicacion.dto.respuesta;

import com.veterinaria.nm.dominio.modelo.HistorialClinico.TipoRegistro;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de respuesta para una entrada del historial clínico.
 * Incluye los signos vitales y el diagnóstico completo.
 */
public record HistorialClinicoRespuesta(
        UUID id,
        UUID idMascota,
        String nombreMascota,
        UUID idCita,
        UUID idVeterinario,
        String nombreVeterinario,
        LocalDateTime fechaConsulta,
        TipoRegistro tipoRegistro,

        // Signos vitales
        Double temperatura,
        Integer frecuenciaCardiaca,
        Integer frecuenciaRespiratoria,
        Double peso,
        String condicionCorporal,

        // Clínica
        String sintomasReferidos,
        String hallazgosExploracion,
        String diagnosticoPrincipal,
        String diagnosticosSecundarios,
        String diagnosticoDiferencial,
        String pruebasDiagnosticasSolicitadas,
        String resultadosPruebas,

        // Tratamiento y seguimiento
        UUID idPrescripcion,
        String tratamientoAplicadoEnConsulta,
        String recomendacionesDomiciliarias,
        LocalDate fechaProximaRevision,
        String observacionesPosteriores,
        boolean cerrado
) {}
