package com.veterinaria.nm.dominio.modelo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad que registra la administración de una vacuna a una mascota concreta.
 * <p>
 * Forma parte del historial de vacunaciones de la mascota y sirve como base
 * para calcular cuándo vence el próximo recuerdo. Las alertas de vacunación
 * pendiente se generan consultando estos registros.
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RegistroVacunacion {

    private final UUID id;
    private final UUID idMascota;
    private final UUID idVacuna;
    private final UUID idVeterinario;

    /** Fecha en que se administró la vacuna. */
    private final LocalDate fechaAdministracion;

    /**
     * Fecha calculada del próximo recuerdo.
     * Se calcula sumando {@link Vacuna#getIntervaloDosisRecuerdoMeses()} a {@code fechaAdministracion}.
     * El sistema generará una alerta cuando se aproxime esta fecha.
     */
    private LocalDate fechaProximaDosis;

    /**
     * Número de lote del vial utilizado. Obligatorio para trazabilidad farmacológica
     * y en caso de alerta sanitaria del laboratorio.
     */
    private String numerolote;

    /**
     * Nombre del laboratorio en el momento de la vacunación.
     * Desnormalizado para que el historial sea autocontenido aunque cambie el catálogo.
     */
    private String laboratorio;

    /** Nombre comercial de la vacuna en el momento de la vacunación. */
    private String nombreVacuna;

    /** Observaciones post-vacunales: reacciones, lugar de inoculación, etc. */
    private String observaciones;

    public static RegistroVacunacion crear(UUID idMascota, UUID idVacuna, UUID idVeterinario,
                                           LocalDate fechaAdministracion, String numerolote,
                                           String laboratorio, String nombreVacuna) {
        Objects.requireNonNull(idMascota, "El registro de vacunación debe tener una mascota");
        Objects.requireNonNull(idVacuna, "El registro de vacunación debe tener una vacuna");
        Objects.requireNonNull(idVeterinario, "El registro de vacunación debe tener un veterinario");
        Objects.requireNonNull(fechaAdministracion, "La fecha de administración es obligatoria");

        return RegistroVacunacion.builder()
                .id(UUID.randomUUID())
                .idMascota(idMascota)
                .idVacuna(idVacuna)
                .idVeterinario(idVeterinario)
                .fechaAdministracion(fechaAdministracion)
                .numerolote(numerolote)
                .laboratorio(laboratorio)
                .nombreVacuna(nombreVacuna)
                .build();
    }

    /** Programa la fecha del próximo recuerdo sumando los meses del protocolo. */
    public void programarProximaDosis(int mesesHastaRecuerdo) {
        if (mesesHastaRecuerdo <= 0)
            throw new IllegalArgumentException("El intervalo hasta el próximo recuerdo debe ser positivo");
        this.fechaProximaDosis = fechaAdministracion.plusMonths(mesesHastaRecuerdo);
    }

    /**
     * Indica si el recuerdo está vencido a la fecha indicada.
     * Útil para generar alertas en el dashboard.
     */
    public boolean recuerdoVencidoEn(LocalDate fecha) {
        return fechaProximaDosis != null && fechaProximaDosis.isBefore(fecha);
    }

    public void agregarObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
