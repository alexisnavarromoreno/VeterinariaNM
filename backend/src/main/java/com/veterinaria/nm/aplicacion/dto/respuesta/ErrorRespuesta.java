package com.veterinaria.nm.aplicacion.dto.respuesta;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO estándar para respuestas de error de la API.
 * Estructura uniforme para todos los errores: 4xx y 5xx.
 * El frontend puede confiar en que siempre tendrá este formato.
 *
 * @param timestamp Momento en que ocurrió el error
 * @param estado    Código HTTP
 * @param error     Título del error (ej: "No encontrado", "Petición inválida")
 * @param mensaje   Descripción legible del error
 * @param ruta      Path de la petición que falló
 * @param errores   Lista de errores de validación (solo en 400)
 */
public record ErrorRespuesta(
        LocalDateTime timestamp,
        int estado,
        String error,
        String mensaje,
        String ruta,
        List<String> errores
) {
    /** Crea una respuesta de error simple sin lista de errores de validación. */
    public static ErrorRespuesta of(int estado, String error, String mensaje, String ruta) {
        return new ErrorRespuesta(LocalDateTime.now(), estado, error, mensaje, ruta, List.of());
    }

    /** Crea una respuesta de error con errores de validación (para 400 Bad Request). */
    public static ErrorRespuesta validacion(String mensaje, String ruta, List<String> errores) {
        return new ErrorRespuesta(LocalDateTime.now(), 400, "Petición inválida", mensaje, ruta, errores);
    }
}
