package com.veterinaria.nm.aplicacion.dto.respuesta;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de respuesta para un cliente. No expone la lista completa de mascotas
 * para evitar respuestas masivas en el listado. El detalle completo con mascotas
 * se devuelve en {@link ClienteDetalleRespuesta}.
 */
public record ClienteRespuesta(
        UUID id,
        String nombreCompleto,
        String email,
        String telefono,
        String telefonoAdicional,
        String direccionFormateada,
        int totalMascotas,
        LocalDateTime fechaRegistro,
        boolean activo
) {}
