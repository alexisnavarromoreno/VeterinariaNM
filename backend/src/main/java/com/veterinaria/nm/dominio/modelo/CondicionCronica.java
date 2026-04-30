package com.veterinaria.nm.dominio.modelo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad hija del agregado Mascota que representa una condición crónica diagnosticada.
 * <p>
 * Las condiciones crónicas son visibles al abrir cualquier historial del paciente
 * para alertar al veterinario antes de prescribir o intervenir.
 * Por ejemplo: un paciente epiléptico no puede recibir ciertos anestésicos;
 * un paciente con IRC requiere ajuste de dosis de muchos medicamentos.
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CondicionCronica {

    private final UUID id;
    private final UUID idMascota;

    private TipoCondicion tipo;

    /**
     * Descripción libre para casos que no encajan en el tipo enumerado,
     * o para añadir especificidad (ej: "Epilepsia idiopática — convulsiones focales").
     */
    private String descripcionAdicional;

    /** Fecha en la que se estableció el diagnóstico. */
    private LocalDate fechaDiagnostico;

    private EstadoCondicion estado;

    /**
     * Implicaciones clínicas que el veterinario debe tener en cuenta.
     * Ej: "Ajustar dosis de AINE por insuficiencia renal CKD grado 3",
     *     "Evitar anestesia con ketamina".
     */
    private String implicacionesTerapeuticas;

    // ── Factory method ────────────────────────────────────────────────────────

    public static CondicionCronica registrar(UUID idMascota, TipoCondicion tipo,
                                              String descripcionAdicional,
                                              LocalDate fechaDiagnostico,
                                              String implicacionesTerapeuticas) {
        Objects.requireNonNull(idMascota, "La condición crónica debe asociarse a una mascota");
        Objects.requireNonNull(tipo, "El tipo de condición es obligatorio");

        return CondicionCronica.builder()
                .id(UUID.randomUUID())
                .idMascota(idMascota)
                .tipo(tipo)
                .descripcionAdicional(descripcionAdicional)
                .fechaDiagnostico(fechaDiagnostico)
                .estado(EstadoCondicion.ACTIVA)
                .implicacionesTerapeuticas(implicacionesTerapeuticas)
                .build();
    }

    public void actualizarEstado(EstadoCondicion nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void actualizarImplicaciones(String implicaciones) {
        this.implicacionesTerapeuticas = implicaciones;
    }

    /** Indica si la condición requiere consideración activa antes de prescribir o intervenir. */
    public boolean requiereConsideracionClinica() {
        return estado == EstadoCondicion.ACTIVA || estado == EstadoCondicion.CONTROLADA;
    }

    // ── Enums ─────────────────────────────────────────────────────────────────

    public enum TipoCondicion {
        // Endocrinológicas
        DIABETES_MELLITUS,
        HIPOTIROIDISMO,
        HIPERTIROIDISMO,
        HIPERADRENOCORTICISMO,       // Cushing
        HIPOADRENOCORTICISMO,        // Addison

        // Renales y urinarias
        INSUFICIENCIA_RENAL_CRONICA,
        UROLITIASIS_RECIDIVANTE,

        // Hepáticas
        HEPATOPATIA_CRONICA,
        SHUNT_PORTOSISTEMICO,

        // Cardiovasculares
        INSUFICIENCIA_CARDIACA,
        CARDIOPATIA_VALVULAR,
        CARDIOMIOPATIA,
        HIPERTENSION_ARTERIAL,

        // Neurológicas
        EPILEPSIA,
        ENFERMEDAD_DEGENERATIVA_MEDULAR,

        // Locomotoras
        DISPLASIA_DE_CADERA,
        ENFERMEDAD_DEGENERATIVA_ARTICULAR,
        LUXACION_ROTULA,

        // Dermatológicas
        ATOPIA,
        DERMATITIS_ALERGICA,

        // Digestivas
        ENFERMEDAD_INFLAMATORIA_INTESTINAL,
        PANCREATITIS_CRONICA,
        MEGAESOFAGO,

        // Respiratorias
        ASMA_FELINO,
        BRONQUITIS_CRONICA,
        BRAQUICEFALISMO,             // Bulldogs, Persas...

        // Oncológicas
        NEOPLASIA_EN_TRATAMIENTO,
        NEOPLASIA_EN_REMISION,

        // Reproductivas
        HIPOPLASIA_UTERINA,
        PROSTATITIS_CRONICA,

        // Otras
        INMUNODEFICIENCIA,
        FIV,                         // Virus inmunodeficiencia felina
        FELV,                        // Leucemia felina
        LEISHMANIOSIS,
        OTRA
    }

    public enum EstadoCondicion {
        /** Condición activa, con impacto directo en las decisiones clínicas. */
        ACTIVA,
        /** Condición estable bajo tratamiento continuado. Sigue siendo relevante. */
        CONTROLADA,
        /** Mejora significativa, pero con posibilidad de recidiva. */
        EN_REMISION,
        /** Curación completa o condición ya no aplicable. */
        RESUELTA
    }
}
