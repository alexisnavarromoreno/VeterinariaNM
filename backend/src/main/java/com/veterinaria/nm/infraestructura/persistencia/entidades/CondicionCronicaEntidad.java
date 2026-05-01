package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.CondicionCronica.EstadoCondicion;
import com.veterinaria.nm.dominio.modelo.CondicionCronica.TipoCondicion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "mascotas_condiciones_cronicas", indexes = {
        @Index(name = "idx_condicion_mascota", columnList = "mascota_id")
})
@Getter
@Setter
@NoArgsConstructor
public class CondicionCronicaEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntidad mascota;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoCondicion tipo;

    @Column(name = "descripcion_adicional", columnDefinition = "TEXT")
    private String descripcionAdicional;

    @Column(name = "fecha_diagnostico")
    private LocalDate fechaDiagnostico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoCondicion estado;

    @Column(name = "implicaciones_terapeuticas", columnDefinition = "TEXT")
    private String implicacionesTerapeuticas;
}
