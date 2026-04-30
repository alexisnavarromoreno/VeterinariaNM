package com.veterinaria.nm.infraestructura.seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro de seguridad que intercepta cada petición HTTP para validar el JWT.
 * <p>
 * Se ejecuta una sola vez por petición ({@link OncePerRequestFilter}).
 * Si el token es válido, establece el contexto de autenticación de Spring Security
 * para que los controladores puedan acceder al usuario autenticado.
 * </p>
 * <p>
 * No lanza excepciones: si el token es inválido o no existe, simplemente
 * no establece el contexto y Spring Security rechazará la petición con 401.
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FiltroAutenticacionJwt extends OncePerRequestFilter {

    private static final String CABECERA_AUTORIZACION = "Authorization";
    private static final String PREFIJO_BEARER = "Bearer ";

    private final GestorJwt gestorJwt;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extraerToken(request);

        if (token != null && gestorJwt.esTokenValido(token)) {
            String email = gestorJwt.extraerEmail(token);
            String rol = gestorJwt.extraerRol(token);

            // Spring Security requiere el prefijo ROLE_ para el mecanismo hasRole()
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));
            var autenticacion = new UsernamePasswordAuthenticationToken(email, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(autenticacion);
            log.debug("Usuario autenticado: {} con rol: {}", email, rol);
        }

        filterChain.doFilter(request, response);
    }

    private String extraerToken(HttpServletRequest request) {
        String cabecera = request.getHeader(CABECERA_AUTORIZACION);
        if (StringUtils.hasText(cabecera) && cabecera.startsWith(PREFIJO_BEARER)) {
            return cabecera.substring(PREFIJO_BEARER.length());
        }
        return null;
    }
}
