package com.veterinaria.nm.aplicacion.dto.peticion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO anidado para la dirección postal dentro de peticiones de cliente.
 */
public record DireccionPeticion(

        @NotBlank(message = "La calle es obligatoria")
        String calle,

        String numero,

        @NotBlank(message = "La ciudad es obligatoria")
        String ciudad,

        String provincia,

        @Pattern(regexp = "\\d{5}", message = "El código postal debe tener 5 dígitos")
        String codigoPostal
) {}
