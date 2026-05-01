package com.veterinaria.nm.infraestructura.persistencia.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "prescripciones", indexes = {
        @Index(name = "idx_prescripcion_historial", columnList = "historial_id"),
        @Index(name = "idx_prescripcion_mascota", columnList = "mascota_id")
})
@Getter
@Setter
@NoArgsConstructor
public class PrescripcionEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "historial_id", nullable = false)
    private HistorialClinicoEntidad historial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntidad mascota;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private UsuarioEntidad veterinario;

    @Column(name = "fecha_emision", nullable = false, updatable = false)
    private LocalDateTime fechaEmision;

    // Código PRESVET: se genera al emitir, null hasta entonces
    @Column(name = "codigo_oficial", length = 100)
    private String codigoOficial;

    @Column(name = "observaciones_generales", columnDefinition = "TEXT")
    private String observacionesGenerales;

    @Column(nullable = false)
    private boolean emitida;
}
