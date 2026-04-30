package com.veterinaria.nm.dominio.modelo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Entidad que representa una prescripción veterinaria completa.
 * <p>
 * Una prescripción está compuesta por una o varias {@link LineaPrescripcion},
 * cada una con su medicamento, dosis, frecuencia y duración.
 * Se genera en el contexto de una consulta ({@link HistorialClinico}) y está
 * firmada por un veterinario ({@link Usuario}).
 * </p>
 * <p>
 * En España, las prescripciones de antibióticos y otros medicamentos de uso
 * veterinario (MUV) están sujetas a normativa del RD 666/2023, que exige
 * registro electrónico. El campo {@code codigoOficial} reserva ese identificador.
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Prescripcion {

    private final UUID id;

    /** Historial clínico al que está asociada esta prescripción. */
    private final UUID idHistorialClinico;

    private final UUID idMascota;
    private final UUID idVeterinario;

    private final LocalDateTime fechaEmision;

    /**
     * Código oficial de la prescripción cuando sea registrada en el sistema
     * nacional (PRESVET en España). Null hasta que se emite oficialmente.
     */
    private String codigoOficial;

    /**
     * Líneas de medicamentos prescritos. Mínimo una línea para que tenga sentido.
     * Se gestiona a través de {@link #agregarLinea} para mantener integridad.
     */
    @Builder.Default
    private final List<LineaPrescripcion> lineas = new ArrayList<>();

    /**
     * Observaciones generales de la prescripción, visibles en el documento impreso.
     * Ej: "Volver a consulta en 10 días", "Si aparece vómito, suspender el tratamiento"
     */
    private String observacionesGenerales;

    /**
     * Si es {@code true}, la prescripción ha sido entregada al propietario o
     * enviada por email. No se puede modificar una vez emitida.
     */
    private boolean emitida;

    // ── Factory method ────────────────────────────────────────────────────────

    public static Prescripcion crear(UUID idHistorialClinico, UUID idMascota, UUID idVeterinario) {
        Objects.requireNonNull(idHistorialClinico, "La prescripción debe estar asociada a un historial clínico");
        Objects.requireNonNull(idMascota, "La prescripción debe tener una mascota");
        Objects.requireNonNull(idVeterinario, "La prescripción debe tener un veterinario");

        return Prescripcion.builder()
                .id(UUID.randomUUID())
                .idHistorialClinico(idHistorialClinico)
                .idMascota(idMascota)
                .idVeterinario(idVeterinario)
                .fechaEmision(LocalDateTime.now())
                .emitida(false)
                .build();
    }

    // ── Comportamiento de negocio ─────────────────────────────────────────────

    /**
     * Agrega una línea de medicamento a la prescripción.
     * Solo se puede modificar antes de emitir.
     */
    public void agregarLinea(LineaPrescripcion linea) {
        validarNoEmitida();
        Objects.requireNonNull(linea, "La línea de prescripción no puede ser nula");
        lineas.add(linea);
    }

    public void eliminarLinea(UUID idLinea) {
        validarNoEmitida();
        lineas.removeIf(l -> l.getId().equals(idLinea));
    }

    /**
     * Marca la prescripción como emitida. A partir de este momento es inmutable.
     * Se debe llamar cuando se entrega al propietario o se registra en PRESVET.
     */
    public void emitir(String codigoOficial) {
        if (lineas.isEmpty())
            throw new IllegalStateException("No se puede emitir una prescripción sin medicamentos");
        this.emitida = true;
        this.codigoOficial = codigoOficial;
    }

    public void actualizarObservaciones(String observaciones) {
        validarNoEmitida();
        this.observacionesGenerales = observaciones;
    }

    public List<LineaPrescripcion> getLineas() {
        return Collections.unmodifiableList(lineas);
    }

    private void validarNoEmitida() {
        if (emitida)
            throw new IllegalStateException("No se puede modificar una prescripción ya emitida");
    }
}
