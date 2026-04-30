package com.veterinaria.nm.adaptadores.entrada.rest;

import com.veterinaria.nm.aplicacion.dto.respuesta.ErrorRespuesta;
import com.veterinaria.nm.dominio.excepciones.CitaNoDisponibleException;
import com.veterinaria.nm.dominio.excepciones.MedicamentoContraindicadoException;
import com.veterinaria.nm.dominio.excepciones.RecursoNoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Manejador global de excepciones para todos los controladores REST.
 * <p>
 * Centraliza la traducción de excepciones del dominio a respuestas HTTP
 * con el formato estándar {@link ErrorRespuesta}. Esto garantiza que
 * el frontend siempre reciba la misma estructura de error.
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> manejarNoEncontrado(
            RecursoNoEncontradoException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorRespuesta.of(404, "No encontrado", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(CitaNoDisponibleException.class)
    public ResponseEntity<ErrorRespuesta> manejarCitaNoDisponible(
            CitaNoDisponibleException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorRespuesta.of(409, "Conflicto de agenda", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MedicamentoContraindicadoException.class)
    public ResponseEntity<ErrorRespuesta> manejarMedicamentoContraindicado(
            MedicamentoContraindicadoException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErrorRespuesta.of(422, "Prescripción inválida", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRespuesta> manejarArgumentoInvalido(
            IllegalArgumentException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorRespuesta.of(400, "Petición inválida", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorRespuesta> manejarEstadoInvalido(
            IllegalStateException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorRespuesta.of(409, "Operación no permitida", ex.getMessage(), request.getRequestURI()));
    }

    /** Maneja errores de validación de @Valid en los DTOs de petición. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> manejarValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorRespuesta.validacion("Los datos de la petición no son válidos",
                        request.getRequestURI(), errores));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorRespuesta> manejarAccesoDenegado(
            AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorRespuesta.of(403, "Acceso denegado",
                        "No tienes permisos para realizar esta operación", request.getRequestURI()));
    }

    /** Captura cualquier excepción no controlada para evitar exponer stack traces. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> manejarErrorGeneral(
            Exception ex, HttpServletRequest request) {
        log.error("Error no controlado en {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorRespuesta.of(500, "Error interno",
                        "Ha ocurrido un error inesperado. Por favor, contacte con el administrador.",
                        request.getRequestURI()));
    }
}
