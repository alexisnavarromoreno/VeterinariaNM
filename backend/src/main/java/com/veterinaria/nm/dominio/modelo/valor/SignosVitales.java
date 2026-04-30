package com.veterinaria.nm.dominio.modelo.valor;

import java.util.Objects;

/**
 * Value Object que agrupa los signos vitales tomados durante una consulta.
 * <p>
 * Se registran en cada entrada del historial clínico para permitir
 * el seguimiento evolutivo del paciente a lo largo del tiempo.
 * Los rangos de referencia varían significativamente por especie:
 * un gato adulto tiene FC normal de 120–240 lpm, un perro grande de 60–140 lpm.
 * La interpretación queda a cargo del veterinario.
 * </p>
 *
 * @param temperatura            Temperatura corporal en grados Celsius
 * @param frecuenciaCardiaca     Latidos por minuto (FC)
 * @param frecuenciaRespiratoria Respiraciones por minuto (FR)
 * @param peso                   Peso en kilogramos en el momento de la consulta
 * @param condicionCorporal      Escala BCS (Body Condition Score) 1–9 para mamíferos
 * @param presionArterial        Presión arterial en mmHg (sistólica/diastólica), opcional
 */
public record SignosVitales(
        Double temperatura,
        Integer frecuenciaCardiaca,
        Integer frecuenciaRespiratoria,
        Double peso,
        CondicionCorporal condicionCorporal,
        String presionArterial
) {
    /**
     * Crea unos signos vitales con solo los campos básicos,
     * suficiente para la mayoría de consultas rutinarias.
     */
    public static SignosVitales basico(Double temperatura, Integer frecuenciaCardiaca,
                                       Integer frecuenciaRespiratoria, Double peso,
                                       CondicionCorporal condicionCorporal) {
        return new SignosVitales(temperatura, frecuenciaCardiaca, frecuenciaRespiratoria,
                peso, condicionCorporal, null);
    }

    /**
     * Indica si se registró al menos un signo vital.
     * Un historial sin signos vitales puede ser un registro telefónico o administrativo.
     */
    public boolean tieneRegistro() {
        return temperatura != null || frecuenciaCardiaca != null
                || frecuenciaRespiratoria != null || peso != null;
    }
}
