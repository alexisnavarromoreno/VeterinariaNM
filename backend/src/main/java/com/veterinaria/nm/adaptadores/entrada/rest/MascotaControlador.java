package com.veterinaria.nm.adaptadores.entrada.rest;

import com.veterinaria.nm.aplicacion.casos_uso.mascota.CrearMascotaCasoUso;
import com.veterinaria.nm.aplicacion.dto.peticion.CrearMascotaPeticion;
import com.veterinaria.nm.aplicacion.dto.respuesta.MascotaRespuesta;
import com.veterinaria.nm.dominio.puertos.entrada.ServicioMascota;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Adaptador de entrada REST para el módulo de mascotas.
 * <p>
 * Responsabilidades exclusivas de este controlador:
 * <ul>
 *   <li>Deserialización y validación del JSON de entrada</li>
 *   <li>Delegación al puerto de entrada ({@link ServicioMascota})</li>
 *   <li>Serialización de la respuesta</li>
 *   <li>Establecimiento del código HTTP correcto</li>
 * </ul>
 * No contiene lógica de negocio.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/mascotas")
@RequiredArgsConstructor
public class MascotaControlador {

    private final ServicioMascota servicioMascota;
    private final CrearMascotaCasoUso crearMascotaCasoUso;

    /**
     * POST /api/v1/mascotas
     * Registra una nueva mascota en la clínica.
     * Accesible por VETERINARIO y RECEPCIONISTA.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('VETERINARIO', 'RECEPCIONISTA', 'ADMINISTRADOR')")
    public ResponseEntity<MascotaRespuesta> crear(@Valid @RequestBody CrearMascotaPeticion peticion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(crearMascotaCasoUso.ejecutar(peticion));
    }

    /**
     * GET /api/v1/mascotas/{id}
     * Obtiene la ficha completa de una mascota.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MascotaRespuesta> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicioMascota.obtenerMascotaPorId(id));
    }

    /**
     * GET /api/v1/mascotas/microchip/{numero}
     * Búsqueda por número de microchip (identificación rápida en urgencias).
     */
    @GetMapping("/microchip/{numero}")
    public ResponseEntity<MascotaRespuesta> obtenerPorMicrochip(@PathVariable String numero) {
        return ResponseEntity.ok(servicioMascota.obtenerMascotaPorMicrochip(numero));
    }

    /**
     * GET /api/v1/mascotas?propietario={idPropietario}
     * Lista todas las mascotas de un propietario.
     */
    @GetMapping
    public ResponseEntity<List<MascotaRespuesta>> listar(
            @RequestParam(required = false) UUID propietario,
            @RequestParam(required = false) String nombre) {
        if (propietario != null) {
            return ResponseEntity.ok(servicioMascota.listarMascotasDePropietario(propietario));
        }
        if (nombre != null) {
            return ResponseEntity.ok(servicioMascota.buscarPorNombre(nombre));
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * PATCH /api/v1/mascotas/{id}/peso
     * Actualiza el peso tras tomar medida en consulta.
     */
    @PatchMapping("/{id}/peso")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'RECEPCIONISTA')")
    public ResponseEntity<MascotaRespuesta> actualizarPeso(
            @PathVariable UUID id, @RequestParam double peso) {
        return ResponseEntity.ok(servicioMascota.actualizarPeso(id, peso));
    }

    /**
     * DELETE /api/v1/mascotas/{id}
     * Desactiva la mascota (eliminación lógica).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> desactivar(@PathVariable UUID id) {
        servicioMascota.desactivarMascota(id);
        return ResponseEntity.noContent().build();
    }
}
