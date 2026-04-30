package com.veterinaria.nm.adaptadores.entrada.rest;

import com.veterinaria.nm.aplicacion.dto.peticion.LoginPeticion;
import com.veterinaria.nm.aplicacion.dto.respuesta.TokenRespuesta;
import com.veterinaria.nm.dominio.puertos.entrada.ServicioAutenticacion;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Adaptador REST para autenticación.
 * Endpoint público: no requiere token JWT para acceder.
 */
@RestController
@RequestMapping("/api/v1/autenticacion")
@RequiredArgsConstructor
public class AutenticacionControlador {

    private final ServicioAutenticacion servicioAutenticacion;

    /**
     * POST /api/v1/autenticacion/login
     * Autentica al usuario y devuelve el token JWT con datos del usuario.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenRespuesta> login(@Valid @RequestBody LoginPeticion peticion) {
        TokenRespuesta respuesta = servicioAutenticacion.login(peticion.email(), peticion.password());
        return ResponseEntity.ok(respuesta);
    }
}
