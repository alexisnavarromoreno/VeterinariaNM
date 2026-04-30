package com.veterinaria.nm.dominio.modelo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Entidad de catálogo que representa una especie animal tratada en la clínica.
 * <p>
 * No todas las clínicas tratan todas las especies. Esta entidad permite
 * configurar qué animales se atienden (perros, gatos, conejos, aves, reptiles, exóticos...)
 * y asociar notas clínicas relevantes para el personal.
 * </p>
 * <p>
 * Las razas se modelan aparte en {@link Raza} y están vinculadas a esta entidad,
 * lo que permite filtrar razas por especie en los formularios de registro de mascotas.
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Especie {

    private final UUID id;

    /** Nombre común de la especie (ej: "Perro", "Gato", "Conejo"). */
    private String nombreComun;

    /** Nombre científico (ej: "Canis lupus familiaris"). Útil en contextos clínicos formales. */
    private String nombreCientifico;

    /**
     * Notas de manejo clínico específicas de la especie.
     * Ej: "Los gatos son muy sensibles a los AINEs — usar con precaución",
     * "Los conejos son estrictamente herbívoros — no administrar antibióticos orales sin supervisión".
     */
    private String notasClinicas;

    /**
     * Indica si la especie requiere manejo especializado o instalaciones específicas.
     * Permite que la clínica filtre qué pacientes puede aceptar.
     */
    private boolean requiereEspecialista;

    private boolean activa;

    public static Especie crear(String nombreComun, String nombreCientifico,
                                String notasClinicas, boolean requiereEspecialista) {
        if (nombreComun == null || nombreComun.isBlank())
            throw new IllegalArgumentException("El nombre común de la especie es obligatorio");

        return Especie.builder()
                .id(UUID.randomUUID())
                .nombreComun(nombreComun)
                .nombreCientifico(nombreCientifico)
                .notasClinicas(notasClinicas)
                .requiereEspecialista(requiereEspecialista)
                .activa(true)
                .build();
    }

    public void actualizarNotasClinicas(String nuevasNotas) {
        this.notasClinicas = nuevasNotas;
    }

    public void desactivar() {
        this.activa = false;
    }
}
