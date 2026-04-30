package com.veterinaria.nm.dominio.modelo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad hija del agregado Mascota que representa una alergia conocida.
 * <p>
 * Distingue entre alergias a medicamentos (que se cruzan con el catálogo
 * de {@link Medicamento} en el momento de prescribir) y otros tipos:
 * alimentarias, ambientales o por contacto químico.
 * </p>
 * <p>
 * La gravedad determina el nivel de alerta que se muestra en la UI:
 * ANAFILAXIA requiere un aviso visual crítico en rojo antes de cualquier prescripción.
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Alergia {

    private final UUID id;
    private final UUID idMascota;

    /** Categoría de la alergia. Condiciona cómo se valida al prescribir. */
    private TipoAlergia tipo;

    /**
     * Nombre de la sustancia alergénica.
     * Para {@link TipoAlergia#MEDICAMENTO}: coincide con el principio activo de {@link Medicamento}.
     * Para el resto: descripción libre (ej: "Atún", "Polen de gramíneas", "Champú con clorhexidina").
     */
    private String sustancia;

    /**
     * UUID del medicamento del catálogo si {@code tipo == MEDICAMENTO}.
     * Permite cruzar con {@link Medicamento#getPrincipioActivo()} al crear prescripciones.
     */
    private UUID idMedicamento;

    /** Descripción de la reacción observada. Ej: "Urticaria generalizada", "Anafilaxia". */
    private String descripcionReaccion;

    private GravedadAlergia gravedad;

    /** Fecha en que se diagnosticó o se detectó la alergia. */
    private LocalDate fechaDeteccion;

    // ── Factory methods ───────────────────────────────────────────────────────

    /** Crea una alergia a un medicamento del catálogo. */
    public static Alergia aMedicamento(UUID idMascota, String principioActivo,
                                       UUID idMedicamento, String descripcionReaccion,
                                       GravedadAlergia gravedad) {
        Objects.requireNonNull(idMascota, "La alergia debe asociarse a una mascota");
        if (principioActivo == null || principioActivo.isBlank())
            throw new IllegalArgumentException("El principio activo del medicamento es obligatorio");

        return Alergia.builder()
                .id(UUID.randomUUID())
                .idMascota(idMascota)
                .tipo(TipoAlergia.MEDICAMENTO)
                .sustancia(principioActivo.trim())
                .idMedicamento(idMedicamento)
                .descripcionReaccion(descripcionReaccion)
                .gravedad(gravedad != null ? gravedad : GravedadAlergia.MODERADA)
                .fechaDeteccion(LocalDate.now())
                .build();
    }

    /** Crea una alergia de tipo alimentario, ambiental o químico. */
    public static Alergia deOtroTipo(UUID idMascota, TipoAlergia tipo,
                                     String sustancia, String descripcionReaccion,
                                     GravedadAlergia gravedad) {
        Objects.requireNonNull(idMascota, "La alergia debe asociarse a una mascota");
        if (tipo == TipoAlergia.MEDICAMENTO)
            throw new IllegalArgumentException("Para alergias a medicamentos use el factory method aMedicamento()");
        if (sustancia == null || sustancia.isBlank())
            throw new IllegalArgumentException("La sustancia es obligatoria");

        return Alergia.builder()
                .id(UUID.randomUUID())
                .idMascota(idMascota)
                .tipo(tipo)
                .sustancia(sustancia.trim())
                .descripcionReaccion(descripcionReaccion)
                .gravedad(gravedad != null ? gravedad : GravedadAlergia.MODERADA)
                .fechaDeteccion(LocalDate.now())
                .build();
    }

    /** Indica si esta alergia debe generar una alerta crítica (roja) en la UI. */
    public boolean esAlertaCritica() {
        return gravedad == GravedadAlergia.GRAVE || gravedad == GravedadAlergia.ANAFILAXIA;
    }

    // ── Enums ─────────────────────────────────────────────────────────────────

    public enum TipoAlergia {
        /** Alergia a un principio activo farmacológico. Se valida al prescribir. */
        MEDICAMENTO,
        /** Reacción adversa a un alimento (proteína, aditivo, conservante...). */
        ALIMENTARIA,
        /** Alergia a alérgenos del entorno: pólenes, ácaros, hongos, picadura de insecto. */
        AMBIENTAL,
        /** Reacción de contacto con sustancias químicas: champús, antisépticos, látex. */
        CONTACTO_QUIMICO,
        /** Tipo no clasificado o en estudio. */
        DESCONOCIDA
    }

    public enum GravedadAlergia {
        /** Síntomas leves: prurito localizado, eritema. */
        LEVE,
        /** Síntomas moderados: urticaria, vómitos, diarrea. */
        MODERADA,
        /** Síntomas graves: angioedema, dificultad respiratoria. */
        GRAVE,
        /** Reacción sistémica potencialmente mortal. Alerta máxima en UI. */
        ANAFILAXIA
    }
}
