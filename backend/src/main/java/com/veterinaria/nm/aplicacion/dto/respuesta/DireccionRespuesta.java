package com.veterinaria.nm.aplicacion.dto.respuesta;

/**
 * DTO de respuesta para una dirección postal.
 */
public record DireccionRespuesta(
        String calle,
        String numero,
        String ciudad,
        String provincia,
        String codigoPostal,
        String formatoCompleto
) {}
