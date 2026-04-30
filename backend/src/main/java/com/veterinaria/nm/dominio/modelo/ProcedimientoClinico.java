package com.veterinaria.nm.dominio.modelo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidad hija de HistorialClinico que registra un procedimiento
 * realizado en consulta por el veterinario o el equipo clínico.
 * <p>
 * Distingue los procedimientos realizados in-situ (sutura, drenaje, cura...)
 * de la medicación prescrita para casa (que se modela en {@link Prescripcion}).
 * Múltiples procedimientos pueden realizarse en una misma consulta.
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ProcedimientoClinico {

    private final UUID id;
    private final UUID idHistorialClinico;

    private TipoProcedimiento tipo;

    /**
     * Descripción libre del procedimiento cuando la tipología no es suficiente.
     * Ej: para CIRUGIA → "Extirpación tumor subcutáneo región escapular derecha, 2 cm".
     */
    private String descripcion;

    /** Zona anatómica donde se realizó el procedimiento, si aplica. */
    private String zonaAnatomica;

    /** Observaciones intraoperatorias o durante la realización. */
    private String observaciones;

    /** Material usado cuando es relevante documentarlo. Ej: "Sutura absorbible Vicryl 3/0". */
    private String materialUtilizado;

    // ── Factory method ────────────────────────────────────────────────────────

    public static ProcedimientoClinico registrar(UUID idHistorial, TipoProcedimiento tipo,
                                                  String descripcion, String zonaAnatomica) {
        Objects.requireNonNull(idHistorial, "El procedimiento debe estar asociado a un historial");
        Objects.requireNonNull(tipo, "El tipo de procedimiento es obligatorio");

        return ProcedimientoClinico.builder()
                .id(UUID.randomUUID())
                .idHistorialClinico(idHistorial)
                .tipo(tipo)
                .descripcion(descripcion)
                .zonaAnatomica(zonaAnatomica)
                .build();
    }

    // ── Enums ─────────────────────────────────────────────────────────────────

    public enum TipoProcedimiento {
        // Curas y heridas
        LIMPIEZA_HERIDA,
        SUTURA,
        RETIRADA_PUNTOS,
        VENDAJE,
        CAMBIO_VENDAJE,
        DRENAJE_ABSCESO,

        // Cavidades y tomas
        CATETER_INTRAVENOSO,
        FLUIDOTERAPIA_IV,
        SONDA_URINARIA,
        SONDA_NASOGASTRICA,
        ABDOMINOCENTESIS,
        TORACOCENTESIS,
        ARTROCENTESIS,
        PUNCION_ASPIRACION_AGUJA_FINA,

        // Dentales
        LIMPIEZA_DENTAL_ULTRASONIDO,
        EXTRACCION_DENTAL,
        TRATAMIENTO_PERIODONTAL,

        // Oídos, ojos y piel
        LIMPIEZA_AUDITIVA,
        LIMPIEZA_OCULAR,
        LAVADO_OCULAR,
        EXTRACCION_CUERPO_EXTRANO,

        // Anestesia
        SEDACION_LEVE,
        ANESTESIA_GENERAL,
        ANALGESIA_LOCOREGIONAL,

        // Reproducción
        INSEMINACION_ARTIFICIAL,
        PALPACION_REPRODUCTIVA,

        // Cirugía
        CIRUGIA_TEJIDOS_BLANDOS,
        CIRUGIA_ORTOPEDICA,
        CIRUGIA_OFTALMOLOGICA,
        ENDOSCOPIA,

        // Hospitalización
        MONITORIZACION_HOSPITALARIA,
        TRANSFUSION_SANGUINEA,
        OXIGENOTERAPIA,

        // Rehabilitación
        FISIOTERAPIA,
        ACUPUNTURA,
        HIDROTERAPIA,

        // Otros
        MICROCHIP,
        TOMA_DE_MUESTRAS,
        OTRO
    }
}
