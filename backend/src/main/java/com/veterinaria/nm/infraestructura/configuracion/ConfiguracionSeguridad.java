package com.veterinaria.nm.infraestructura.configuracion;

import com.veterinaria.nm.infraestructura.seguridad.FiltroAutenticacionJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración central de seguridad de la aplicación.
 * <p>
 * Estrategia: stateless JWT — no hay sesiones HTTP.
 * Cada petición debe incluir el token Bearer en la cabecera Authorization.
 * </p>
 * <p>
 * CORS está configurado para el frontend en desarrollo (localhost:5173 — Vite).
 * En producción, se configurará el dominio real del frontend.
 * </p>
 * <p>
 * {@code @EnableMethodSecurity} habilita {@code @PreAuthorize} en controladores
 * para control de acceso a nivel de método (ej: solo VETERINARIO puede crear historial).
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class ConfiguracionSeguridad {

    private final FiltroAutenticacionJwt filtroJwt;

    @Bean
    public SecurityFilterChain cadenaFiltros(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(fuenteConfiguracionCors()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos: login y documentación (si se añade Swagger)
                        .requestMatchers(HttpMethod.POST, "/api/v1/autenticacion/login").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource fuenteConfiguracionCors() {
        CorsConfiguration config = new CorsConfiguration();
        // En producción, reemplazar con el dominio real del frontend
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    /**
     * BCrypt con factor de coste 12 (valor recomendado en 2024 para equilibrio seguridad/rendimiento).
     * Factor 10 es el mínimo aceptable; no superar 14 en producción sin benchmark previo.
     */
    @Bean
    public PasswordEncoder codificadorPassword() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager gestorAutenticacion(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
