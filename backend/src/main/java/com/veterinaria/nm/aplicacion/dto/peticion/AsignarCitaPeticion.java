package com.veterinaria.nm.aplicacion.dto.peticion;

import com.veterinaria.nm.dominio.modelo.Cita.OrigenCita;
import com.veterinaria.nm.dominio.modelo.Cita.TipoCita;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de petición para solicitar una nueva cita.
 */
public record AsignarCitaPeticion(

        @NotNull(message = "La mascota es obligatoria")
        UUID idMascota,

        /** Nullable si el propietario solicita sin preferencia de veterinario. */
        UUID idVeterinario,

        @NotNull(message = "La fecha y hora son obligatorias")
        @Future(message = "La cita debe ser en una fecha futura")
        LocalDateTime fechaHora,

        @Min(value = 10, message = "La duración mínima es de 10 minutos")
        @Max(value = 240, message = "La duración máxima es de 240 minutos")
        int duracionMinutos,

        @NotNull(message = "El tipo de cita es obligatorio")
        TipoCita tipoCita,

        @NotBlank(message = "El motivo de consulta es obligatorio")
        @Size(max = 500, message = "El motivo no puede superar los 500 caracteres")
        String motivoConsulta,

        OrigenCita origen
) {}
