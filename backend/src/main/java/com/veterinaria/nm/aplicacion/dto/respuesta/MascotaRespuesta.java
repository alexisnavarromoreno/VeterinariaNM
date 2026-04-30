package com.veterinaria.nm.aplicacion.dto.respuesta;

import com.veterinaria.nm.dominio.modelo.Mascota.Sexo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de respuesta para una mascota.
 * <p>
 * Incluye campos calculados ({@code edadDescriptiva}) para que el frontend
 * no necesite calcularlos. El propietario se devuelve resumido para evitar
 * datos innecesarios en el listado.
 * </p>
 */
public record MascotaRespuesta(
        UUID id,
        String nombre,
        UUID idEspecie,
        String nombreEspecie,
        UUID idRaza,
        String nombreRaza,
        Sexo sexo,
        LocalDate fechaNacimiento,
        String edadDescriptiva,
        Double pesoActual,
        String colorPelaje,
        String numeroMicrochip,
        boolean esterilizado,
        List<String> alergias,
        List<String> enfermedadesCronicas,
        String notasManejo,
        UUID idPropietario,
        String nombrePropietario,
        LocalDateTime fechaRegistro,
        boolean activo
) {}
