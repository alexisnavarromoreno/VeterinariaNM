package com.veterinaria.nm.aplicacion.dto.respuesta;

import com.veterinaria.nm.dominio.modelo.Usuario.Rol;

import java.util.UUID;

/**
 * DTO de respuesta para el login exitoso.
 * Incluye el token JWT y los datos mínimos del usuario para
 * que el frontend configure el estado de autenticación sin llamada adicional.
 */
public record TokenRespuesta(
        String token,
        String tipo,
        long expiraEn,
        UUID idUsuario,
        String nombreCompleto,
        String email,
        Rol rol
) {
    /** Factory method para respuesta estándar con Bearer token. */
    public static TokenRespuesta bearer(String token, long expiraEn, UUID id,
                                        String nombre, String email, Rol rol) {
        return new TokenRespuesta(token, "Bearer", expiraEn, id, nombre, email, rol);
    }
}
