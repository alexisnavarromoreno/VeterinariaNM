package com.veterinaria.nm.aplicacion.dto.peticion;

import com.veterinaria.nm.dominio.modelo.HistorialClinico.TipoRegistro;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * DTO para iniciar una consulta y crear la entrada en el historial clínico.
 * Los signos vitales son opcionales porque hay tipos de consulta donde no aplican
 * (ej: teleconsulta, gestión administrativa).
 */
public record IniciarConsultaPeticion(

        @NotNull UUID idMascota,
        UUID idCita,

        @NotNull TipoRegistro tipoRegistro,

        String sintomasReferidos,

        // Signos vitales — todos opcionales
        Double temperatura,

        @Positive Integer frecuenciaCardiaca,

        @Positive Integer frecuenciaRespiratoria,

        @Positive Double peso,

        String condicionCorporal,
        String presionArterial
) {}
