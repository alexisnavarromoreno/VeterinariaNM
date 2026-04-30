package com.veterinaria.nm.dominio.puertos.entrada;

import com.veterinaria.nm.aplicacion.dto.respuesta.TokenRespuesta;
import com.veterinaria.nm.dominio.modelo.Usuario;

/**
 * Puerto de entrada para la autenticación de usuarios.
 */
public interface ServicioAutenticacion {

    /**
     * Autentica al usuario con email y contraseña.
     *
     * @return {@link TokenRespuesta} con el JWT y datos del usuario
     * @throws org.springframework.security.authentication.BadCredentialsException si las credenciales no son válidas
     */
    TokenRespuesta login(String email, String password);

    Usuario obtenerUsuarioPorEmail(String email);

    boolean validarToken(String token);
}
