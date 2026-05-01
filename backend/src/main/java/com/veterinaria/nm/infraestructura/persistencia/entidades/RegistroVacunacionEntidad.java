package com.veterinaria.nm.infraestructura.persistencia.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "registros_vacunacion", indexes = {
        @Index(name = "idx_vacunacion_mascota", columnList = "mascota_id"),
        @Index(name = "idx_vacunacion_proxima_dosis", columnList = "fecha_proxima_dosis")
})
@Getter
@Setter
@NoArgsConstructor
public class RegistroVacunacionEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntidad mascota;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vacuna_id", nullable = false)
    private VacunaEntidad vacuna;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private UsuarioEntidad veterinario;

    @Column(name = "fecha_administracion", nullable = false)
    private LocalDate fechaAdministracion;

    @Column(name = "fecha_proxima_dosis")
    private LocalDate fechaProximaDosis;

    @Column(name = "numero_lote", length = 50)
    private String numeroLote;

    // Desnormalizado: preserva el laboratorio histórico aunque el catálogo cambie
    @Column(length = 200)
    private String laboratorio;

    // Desnormalizado: preserva el nombre de la vacuna en el momento de administración
    @Column(name = "nombre_vacuna", length = 200)
    private String nombreVacuna;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}
