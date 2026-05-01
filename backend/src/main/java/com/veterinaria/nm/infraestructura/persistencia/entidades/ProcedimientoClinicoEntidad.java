package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.ProcedimientoClinico.TipoProcedimiento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "procedimientos_clinicos", indexes = {
        @Index(name = "idx_procedimiento_historial", columnList = "historial_id")
})
@Getter
@Setter
@NoArgsConstructor
public class ProcedimientoClinicoEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "historial_id", nullable = false)
    private HistorialClinicoEntidad historial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TipoProcedimiento tipo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "zona_anatomica", length = 200)
    private String zonaAnatomica;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "material_utilizado", length = 300)
    private String materialUtilizado;
}
