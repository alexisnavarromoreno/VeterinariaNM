package com.veterinaria.nm.dominio.modelo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidad hija del agregado HistorialClinico que representa
 * una prueba diagnóstica solicitada o realizada durante la consulta.
 * <p>
 * Permite seguimiento individual de cada prueba: solicitada, pendiente
 * o completada con resultado. Un historial puede tener múltiples pruebas
 * (ej: hemograma + ecografía + radiografía de tórax en la misma consulta).
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PruebaDiagnostica {

    private final UUID id;
    private final UUID idHistorialClinico;

    private TipoPrueba tipo;

    /**
     * Especificación adicional cuando el tipo no es suficientemente descriptivo.
     * Ej: para RADIOGRAFIA → "Tórax 2 proyecciones"; para CULTIVO → "Orina cateterismo".
     */
    private String especificacion;

    private EstadoPrueba estado;

    /**
     * Resultado de la prueba en texto libre o referencia a documento externo.
     * Ej: "Hemograma: leucocitosis 18.500, neutrofilia. Resto normal.",
     *     "Ecografía: hepatomegalia moderada, parénquima homogéneo."
     */
    private String resultado;

    /** Indica si el resultado está fuera del rango de referencia. */
    private boolean resultadoAlterado;

    // ── Factory methods ───────────────────────────────────────────────────────

    public static PruebaDiagnostica solicitar(UUID idHistorial, TipoPrueba tipo, String especificacion) {
        Objects.requireNonNull(idHistorial, "La prueba debe estar asociada a un historial");
        Objects.requireNonNull(tipo, "El tipo de prueba es obligatorio");

        return PruebaDiagnostica.builder()
                .id(UUID.randomUUID())
                .idHistorialClinico(idHistorial)
                .tipo(tipo)
                .especificacion(especificacion)
                .estado(EstadoPrueba.SOLICITADA)
                .build();
    }

    /** Registra el resultado cuando está disponible. */
    public void registrarResultado(String resultado, boolean alterado) {
        if (resultado == null || resultado.isBlank())
            throw new IllegalArgumentException("El resultado de la prueba no puede estar vacío");
        this.resultado = resultado;
        this.resultadoAlterado = alterado;
        this.estado = EstadoPrueba.COMPLETADA;
    }

    public void marcarPendienteResultado() {
        this.estado = EstadoPrueba.PENDIENTE_RESULTADO;
    }

    // ── Enums ─────────────────────────────────────────────────────────────────

    public enum TipoPrueba {
        // Analíticas de sangre
        HEMOGRAMA_COMPLETO,
        BIOQUIMICA_BASICA,           // glucosa, urea, creatinina, ALT, FA
        BIOQUIMICA_COMPLETA,         // + colesterol, triglicéridos, proteínas, electrolitos
        COAGULACION,
        GASOMETRIA,
        PERFIL_TIROIDEO,             // T4, TSH
        PERFIL_SUPRARRENAL,          // cortisol basal, ACTH estimulation test
        SEROLOGIA_INFECCIOSA,        // ehrlichia, borrelia, anaplasma, leishmania, FIV, FeLV

        // Analíticas de orina
        URINALISIS,
        CULTIVO_ORINA,
        RELACION_PROTEINA_CREATININA, // UPC

        // Microbiología
        CULTIVO_Y_ANTIBIOGRAMA,
        FROTIS_CITOLOGIA,

        // Imagen
        RADIOGRAFIA,
        ECOGRAFIA_ABDOMINAL,
        ECOGRAFIA_CARDIACA,          // ecocardiografía
        ECOGRAFIA_OCULAR,
        TAC,
        RESONANCIA_MAGNETICA,

        // Histopatología
        BIOPSIA,
        PUNCION_ASPIRACION_AGUJA_FINA, // PAAF

        // Pruebas funcionales
        TEST_FUNCIONAMIENTO_HEPATICO,
        MEDICION_PRESION_ARTERIAL,
        ELECTROCARDIOGRAMA,

        // Oftalmológicas
        TONOMETRIA,                  // presión intraocular
        FLUORESCEINA,

        // Otros
        PCR,
        ELISA,
        TEST_RAPIDO,
        OTRA
    }

    public enum EstadoPrueba {
        /** Prueba solicitada pero no realizada aún. */
        SOLICITADA,
        /** Prueba realizada, pendiente de resultado (laboratorio externo). */
        PENDIENTE_RESULTADO,
        /** Resultado disponible y registrado. */
        COMPLETADA,
        /** Prueba cancelada o no realizada finalmente. */
        CANCELADA
    }
}
