package com.veterinaria.nm.infraestructura.seguridad;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

/**
 * Componente de infraestructura responsable de la generación y validación de tokens JWT.
 * <p>
 * Pertenece exclusivamente a la capa de infraestructura: el dominio y la capa
 * de aplicación no conocen esta clase ni el concepto de JWT.
 * El servicio de aplicación solo recibe/devuelve el token como String opaco.
 * </p>
 * <p>
 * El algoritmo HMAC-SHA256 (HS256) es suficiente para un sistema monolítico.
 * Si se necesita verificación por terceros sin compartir el secreto,
 * se migraría a RS256 (clave asimétrica).
 * </p>
 */
@Slf4j
@Component
public class GestorJwt {

    /**
     * Clave secreta base64 para firmar los tokens.
     * Debe tener al menos 256 bits (32 bytes) para HS256.
     * Se configura en application.yml y NO debe estar en repositorios de código.
     */
    @Value("${veterinaria.jwt.secreto}")
    private String secretoBase64;

    /**
     * Duración del token en milisegundos.
     * Por defecto: 8 horas (jornada laboral). Configurable por entorno.
     */
    @Value("${veterinaria.jwt.expiracion-ms:28800000}")
    private long expiracionMs;

    /**
     * Genera un token JWT firmado para el usuario autenticado.
     *
     * @param idUsuario     UUID del usuario
     * @param email         Email (subject del token)
     * @param rol           Rol incluido como claim personalizado
     * @return Token JWT firmado
     */
    public String generarToken(UUID idUsuario, String email, String rol) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expiracionMs);

        return Jwts.builder()
                .subject(email)
                .claim("idUsuario", idUsuario.toString())
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(obtenerClave())
                .compact();
    }

    /** Extrae el email (subject) del token. */
    public String extraerEmail(String token) {
        return extraerClaims(token).getSubject();
    }

    /** Extrae el UUID del usuario del token. */
    public UUID extraerIdUsuario(String token) {
        return UUID.fromString(extraerClaims(token).get("idUsuario", String.class));
    }

    /** Extrae el rol del usuario del token. */
    public String extraerRol(String token) {
        return extraerClaims(token).get("rol", String.class);
    }

    /** Devuelve la duración configurada en ms. Útil para incluirla en la respuesta de login. */
    public long getExpiracionMs() {
        return expiracionMs;
    }

    /**
     * Valida el token verificando firma y expiración.
     *
     * @return {@code true} si el token es válido
     */
    public boolean esTokenValido(String token) {
        try {
            extraerClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token JWT expirado: {}", e.getMessage());
        } catch (JwtException e) {
            log.warn("Token JWT inválido: {}", e.getMessage());
        }
        return false;
    }

    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey obtenerClave() {
        byte[] bytes = Decoders.BASE64.decode(secretoBase64);
        return Keys.hmacShaKeyFor(bytes);
    }
}
