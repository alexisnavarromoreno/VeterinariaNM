package com.veterinaria.nm.dominio.modelo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Entidad de catálogo que representa una raza dentro de una especie.
 * <p>
 * Asociar la raza a la mascota permite mostrar alertas automáticas sobre
 * predisposiciones genéticas ({@link PredisposicionGenetica}) al abrir
 * la ficha del paciente. Cada predisposición está vinculada a una
 * {@link CondicionCronica.TipoCondicion}, lo que permite cruzar la información
 * con las condiciones crónicas activas del paciente.
 * </p>
 * <p>
 * Ejemplos reales:
 * <ul>
 *   <li>Labrador Retriever → displasia de cadera, obesidad</li>
 *   <li>Maine Coon → miocardiopatía hipertrófica, PKD</li>
 *   <li>Bulldog Francés → braquicefalismo, problemas espinales</li>
 *   <li>Dálmata → urolitiasis por uratos, sordera</li>
 * </ul>
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Raza {

    private final UUID id;
    private final UUID idEspecie;

    /** Nombre oficial de la raza (ej: "Golden Retriever", "Persa", "Rex Enano"). */
    private String nombre;

    /**
     * Tamaño medio para calcular dosis y protocolos anestésicos.
     * Los rangos de peso de referencia están definidos en el enum {@link TamanoRaza}.
     */
    private TamanoRaza tamano;

    /**
     * Predisposiciones genéticas conocidas de esta raza.
     * Cada una está asociada a un {@link CondicionCronica.TipoCondicion}
     * para permitir alertas cruzadas con el historial del paciente.
     */
    @Builder.Default
    private final List<PredisposicionGenetica> predisposicionesGeneticas = new ArrayList<>();

    /**
     * Esperanza de vida media en años.
     * Contextualiza síntomas por edad: a los 8 años un Gran Danés es un anciano,
     * mientras que un Chihuahua está en edad media.
     */
    private Integer esperanzaVidaAnios;

    /** Si es {@code false}, la raza deja de aparecer en el selector de registro. */
    private boolean activa;

    // ── Factory method ────────────────────────────────────────────────────────

    public static Raza crear(UUID idEspecie, String nombre, TamanoRaza tamano,
                             Integer esperanzaVidaAnios) {
        if (idEspecie == null) throw new IllegalArgumentException("La raza debe pertenecer a una especie");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre de la raza es obligatorio");

        return Raza.builder()
                .id(UUID.randomUUID())
                .idEspecie(idEspecie)
                .nombre(nombre.trim())
                .tamano(tamano)
                .esperanzaVidaAnios(esperanzaVidaAnios)
                .activa(true)
                .build();
    }

    // ── Comportamiento de negocio ─────────────────────────────────────────────

    public void agregarPredisposicion(PredisposicionGenetica predisposicion) {
        if (predisposicion == null) return;
        boolean existe = predisposicionesGeneticas.stream()
                .anyMatch(p -> p.condicion() == predisposicion.condicion());
        if (!existe) predisposicionesGeneticas.add(predisposicion);
    }

    public void desactivar() { this.activa = false; }

    public List<PredisposicionGenetica> getPredisposicionesGeneticas() {
        return Collections.unmodifiableList(predisposicionesGeneticas);
    }

    // ── Enum de tamaño ────────────────────────────────────────────────────────

    public enum TamanoRaza {
        /** < 4 kg — ej: Chihuahua, Yorkshire Terrier */
        MINI,
        /** 4–10 kg — ej: Beagle, Bichón Frisé */
        PEQUEÑO,
        /** 10–25 kg — ej: Border Collie, Springer Spaniel */
        MEDIANO,
        /** 25–45 kg — ej: Labrador, Pastor Alemán */
        GRANDE,
        /** > 45 kg — ej: San Bernardo, Gran Danés */
        GIGANTE
    }
}
