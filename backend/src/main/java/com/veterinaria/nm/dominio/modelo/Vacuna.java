package com.veterinaria.nm.dominio.modelo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

/**
 * Entidad de catálogo que representa una vacuna disponible en la clínica.
 * <p>
 * Las vacunas están vinculadas a especies específicas: el protocolo de vacunación
 * de un perro (moquillo, parvovirus, hepatitis, leptospira, rabia) es completamente
 * distinto al de un gato (rinotraqueítis, calicivirus, panleucopenia, leucemia).
 * </p>
 * <p>
 * Los registros de vacunación de cada paciente se modelan en {@link RegistroVacunacion}.
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Vacuna {

    private final UUID id;

    /** Nombre comercial de la vacuna (ej: "Nobivac DHPPi", "Leucofeligen"). */
    private String nombreComercial;

    /** Laboratorio fabricante (ej: "MSD Animal Health", "Virbac", "Zoetis"). */
    private String laboratorio;

    /**
     * Enfermedades que cubre la vacuna.
     * Ej: para "Nobivac DHPPi" → {"Moquillo", "Hepatitis", "Parvovirus", "Parainfluenza"}
     */
    private Set<String> enfermedadesCubiertas;

    /**
     * Nombres comunes de las especies a las que se aplica esta vacuna.
     * Evita prescribir vacunas caninas a felinos y viceversa.
     */
    private Set<String> especiesAplicables;

    /**
     * Intervalo en meses entre dosis de la primovacunación y los recuerdos.
     * Ej: 12 meses para la rabia, 1–3 años para algunas vacunas combinadas.
     */
    private Integer intervaloDosisRecuerdoMeses;

    /**
     * Pauta de primovacunación recomendada.
     * Ej: "3 dosis: a las 8, 12 y 16 semanas de vida"
     */
    private String pautaPrimovacunacion;

    private boolean activa;

    public static Vacuna crear(String nombreComercial, String laboratorio,
                               Set<String> enfermedadesCubiertas, Set<String> especiesAplicables,
                               Integer intervaloDosisRecuerdoMeses, String pautaPrimovacunacion) {
        if (nombreComercial == null || nombreComercial.isBlank())
            throw new IllegalArgumentException("El nombre comercial de la vacuna es obligatorio");
        if (especiesAplicables == null || especiesAplicables.isEmpty())
            throw new IllegalArgumentException("La vacuna debe aplicarse al menos a una especie");

        return Vacuna.builder()
                .id(UUID.randomUUID())
                .nombreComercial(nombreComercial.trim())
                .laboratorio(laboratorio)
                .enfermedadesCubiertas(enfermedadesCubiertas != null ? Set.copyOf(enfermedadesCubiertas) : Set.of())
                .especiesAplicables(Set.copyOf(especiesAplicables))
                .intervaloDosisRecuerdoMeses(intervaloDosisRecuerdoMeses)
                .pautaPrimovacunacion(pautaPrimovacunacion)
                .activa(true)
                .build();
    }

    /** Comprueba si esta vacuna está indicada para una especie dada. */
    public boolean aplicableA(String nombreEspecie) {
        if (nombreEspecie == null) return false;
        return especiesAplicables.stream().anyMatch(e -> e.equalsIgnoreCase(nombreEspecie.trim()));
    }
}
