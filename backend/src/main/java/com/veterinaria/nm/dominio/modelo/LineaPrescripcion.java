package com.veterinaria.nm.dominio.modelo;

import com.veterinaria.nm.dominio.modelo.valor.Dosis;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

/**
 * Línea de una prescripción veterinaria.
 * <p>
 * Representa un medicamento concreto dentro de una {@link Prescripcion},
 * con su dosis personalizada, frecuencia y duración del tratamiento.
 * La dosis aquí puede diferir de la dosis de referencia del medicamento
 * porque el veterinario la ajusta al caso clínico específico.
 * </p>
 * <p>
 * Si el medicamento tiene una {@link Dosis} relativa (mg/kg) y el veterinario
 * conoce el peso actual de la mascota, la dosis absoluta se calcula
 * automáticamente mediante {@link Dosis#calcularParaPeso(double)}.
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LineaPrescripcion {

    private final UUID id;

    /** UUID del medicamento prescrito. Se resuelve en la capa de aplicación. */
    private final UUID idMedicamento;

    /**
     * Dosis prescrita por el veterinario para este paciente.
     * Puede ser fija (mg, ml) o relativa al peso (mg/kg, ml/kg).
     */
    private final Dosis dosis;

    /**
     * Frecuencia de administración en texto libre.
     * Se usa texto para permitir expresiones clínicas naturales:
     * "cada 8 horas", "una vez al día en ayunas", "dos veces al día con comida"
     */
    private final String frecuencia;

    /**
     * Número de días que debe seguirse el tratamiento.
     * Un valor de 0 indica "hasta nueva consulta" o uso crónico indefinido.
     */
    private final int duracionDias;

    /**
     * Instrucciones específicas de administración para el propietario.
     * Ej: "Administrar con alimento para evitar molestias gástricas",
     *     "No triturar el comprimido", "Mantener en nevera una vez abierto"
     */
    private String instruccionesAdministracion;

    /** Nombre del medicamento desnormalizado para facilitar la visualización sin JOIN. */
    private String nombreMedicamento;

    // ── Factory method ────────────────────────────────────────────────────────

    public static LineaPrescripcion crear(UUID idMedicamento, Dosis dosis, String frecuencia,
                                          int duracionDias, String instrucciones) {
        Objects.requireNonNull(idMedicamento, "El medicamento de la línea es obligatorio");
        Objects.requireNonNull(dosis, "La dosis de la línea es obligatoria");
        if (frecuencia == null || frecuencia.isBlank())
            throw new IllegalArgumentException("La frecuencia de administración es obligatoria");
        if (duracionDias < 0)
            throw new IllegalArgumentException("La duración no puede ser negativa");

        return LineaPrescripcion.builder()
                .id(UUID.randomUUID())
                .idMedicamento(idMedicamento)
                .dosis(dosis)
                .frecuencia(frecuencia.trim())
                .duracionDias(duracionDias)
                .instruccionesAdministracion(instrucciones)
                .build();
    }

    /**
     * Calcula la cantidad total de medicamento necesaria para el tratamiento completo.
     * Solo aplicable si se conoce el peso y la dosis es por administración (no crónica).
     *
     * @param pesoKg       Peso actual del animal
     * @param dosesAlDia   Número de administraciones diarias (deriva de {@link #frecuencia})
     * @return Cantidad total del medicamento para la duración del tratamiento
     */
    public double calcularCantidadTotal(double pesoKg, int dosesAlDia) {
        double dosisPorToma = dosis.calcularParaPeso(pesoKg);
        return dosisPorToma * dosesAlDia * duracionDias;
    }

    public void enriquecerConNombreMedicamento(String nombre) {
        this.nombreMedicamento = nombre;
    }
}
