package com.veterinaria.nm.dominio.modelo.valor;

import java.util.Objects;

/**
 * Value Object que representa una dirección postal española.
 * <p>
 * Al ser inmutable y sin identidad propia, se modela como Record.
 * La provincia se tipifica como {@link Provincia} para garantizar que
 * solo se aceptan las 52 provincias españolas válidas.
 * </p>
 *
 * @param calle        Nombre de la calle o vía
 * @param numero       Número del portal (puede incluir piso, letra)
 * @param ciudad       Ciudad de residencia
 * @param provincia    Provincia española (enum cerrado, 52 valores)
 * @param codigoPostal CP de 5 dígitos en España
 */
public record Direccion(
        String calle,
        String numero,
        String ciudad,
        Provincia provincia,
        String codigoPostal
) {
    public Direccion {
        Objects.requireNonNull(calle, "La calle es obligatoria");
        Objects.requireNonNull(ciudad, "La ciudad es obligatoria");
        if (codigoPostal != null && !codigoPostal.matches("\\d{5}")) {
            throw new IllegalArgumentException("El código postal debe tener 5 dígitos");
        }
    }

    public String formatoCompleto() {
        String prov = provincia != null ? provincia.getNombre() : "";
        return String.format("%s %s, %s %s (%s)", calle, numero, codigoPostal, ciudad, prov);
    }
}
