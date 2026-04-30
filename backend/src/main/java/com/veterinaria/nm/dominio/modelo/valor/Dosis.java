package com.veterinaria.nm.dominio.modelo.valor;

import java.util.Objects;

/**
 * Value Object que representa una dosis farmacológica.
 * <p>
 * Encapsula la cantidad y la unidad de medida. Las unidades por kilogramo
 * (MG_KG, ML_KG, UI_KG) permiten calcular la dosis real según el peso del animal,
 * lo cual es fundamental en veterinaria donde el rango de pesos va de un canario
 * a un caballo.
 * </p>
 *
 * @param cantidad Cantidad numérica de la dosis (debe ser positiva)
 * @param unidad   Unidad en la que se expresa la dosis
 */
public record Dosis(double cantidad, UnidadDosis unidad) {

    /**
     * Unidades de medida estándar en farmacología veterinaria.
     * Las que terminan en {@code _KG} son relativas al peso del animal.
     */
    public enum UnidadDosis {
        MG,           // miligramos (dosis fija)
        MG_KG,        // miligramos por kilogramo de peso
        ML,           // mililitros (dosis fija)
        ML_KG,        // mililitros por kilogramo de peso
        COMPRIMIDOS,  // número de comprimidos
        GOTAS,        // número de gotas
        UI,           // unidades internacionales (insulina, vitaminas)
        UI_KG,        // unidades internacionales por kilogramo
        APLICACIONES  // dosis tópicas (cremas, sprays)
    }

    public Dosis {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad de dosis debe ser mayor que cero");
        Objects.requireNonNull(unidad, "La unidad de la dosis es obligatoria");
    }

    /**
     * Calcula la dosis real a administrar según el peso del animal.
     * Para unidades fijas (MG, ML...) devuelve la cantidad directamente.
     * Para unidades por kg (MG_KG, ML_KG...) multiplica por el peso.
     *
     * @param pesoKg Peso del animal en kilogramos
     * @return Dosis absoluta a administrar
     */
    public double calcularParaPeso(double pesoKg) {
        if (pesoKg <= 0) throw new IllegalArgumentException("El peso debe ser positivo para calcular la dosis");
        return switch (unidad) {
            case MG_KG, ML_KG, UI_KG -> cantidad * pesoKg;
            default -> cantidad;
        };
    }

    /** Indica si la dosis es relativa al peso (requiere conocer el peso del animal). */
    public boolean esDosisRelativa() {
        return unidad == UnidadDosis.MG_KG
                || unidad == UnidadDosis.ML_KG
                || unidad == UnidadDosis.UI_KG;
    }

    @Override
    public String toString() {
        return cantidad + " " + unidad.name().replace("_", "/").toLowerCase();
    }
}
