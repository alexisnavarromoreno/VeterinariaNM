package com.veterinaria.nm.aplicacion.dto.peticion;

import com.veterinaria.nm.dominio.modelo.Mascota.Sexo;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO de petición para registrar una nueva mascota.
 * <p>
 * Se modela como Record para garantizar inmutabilidad y eliminar boilerplate.
 * La validación se realiza en la capa del adaptador REST (controlador),
 * antes de que los datos lleguen al caso de uso.
 * </p>
 */
public record CrearMascotaPeticion(

        @NotBlank(message = "El nombre de la mascota es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @NotNull(message = "La especie es obligatoria")
        UUID idEspecie,

        /** Nullable: las mascotas mestizas o de raza desconocida no tienen raza. */
        UUID idRaza,

        @NotNull(message = "El sexo es obligatorio")
        Sexo sexo,

        /** Nullable si se desconoce la fecha de nacimiento exacta. */
        @Past(message = "La fecha de nacimiento no puede ser futura")
        LocalDate fechaNacimiento,

        @Positive(message = "El peso debe ser un valor positivo")
        Double pesoActual,

        String colorPelaje,

        /**
         * 15 dígitos numéricos según ISO 11784/11785.
         * Nullable para especies que no requieren microchip.
         */
        @Pattern(regexp = "\\d{15}", message = "El microchip debe tener exactamente 15 dígitos")
        String numeroMicrochip,

        boolean esterilizado,

        @NotNull(message = "El propietario es obligatorio")
        UUID idPropietario
) {}
