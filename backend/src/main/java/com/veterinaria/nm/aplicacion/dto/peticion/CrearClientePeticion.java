package com.veterinaria.nm.aplicacion.dto.peticion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de petición para registrar un nuevo cliente.
 */
public record CrearClientePeticion(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        String nombre,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 150)
        String apellidos,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        String email,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "[\\d\\s\\-+]{9,15}", message = "El teléfono no tiene un formato válido")
        String telefono,

        String telefonoAdicional,

        @Valid
        DireccionPeticion direccion
) {}
