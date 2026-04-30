package com.veterinaria.nm.dominio.modelo.valor;

import java.util.Objects;

/**
 * Value Object que representa una dirección postal.
 * <p>
 * Al ser inmutable y sin identidad propia, se modela como Record.
 * Dos direcciones iguales en contenido son consideradas el mismo valor.
 * </p>
 *
 * @param calle        Nombre de la calle o vía
 * @param numero       Número del portal (puede incluir piso, letra)
 * @param ciudad       Ciudad de residencia
 * @param provincia    Provincia o comunidad autónoma
 * @param codigoPostal CP de 5 dígitos en España
 */
public record Direccion(
        String calle,
        String numero,
        String ciudad,
        String provincia,
        String codigoPostal
) {
    /** Validación compacta ejecutada en la construcción del record. */
    public Direccion {
        Objects.requireNonNull(calle, "La calle es obligatoria");
        Objects.requireNonNull(ciudad, "La ciudad es obligatoria");
        if (codigoPostal != null && !codigoPostal.matches("\\d{5}")) {
            throw new IllegalArgumentException("El código postal debe tener 5 dígitos");
        }
    }

    /** Representación legible para mostrar en UI o documentos. */
    public String formatoCompleto() {
        return String.format("%s %s, %s %s (%s)", calle, numero, codigoPostal, ciudad, provincia);
    }
}
