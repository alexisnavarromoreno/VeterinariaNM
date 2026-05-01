package com.veterinaria.nm.infraestructura.persistencia.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "lineas_prescripcion", indexes = {
        @Index(name = "idx_linea_prescripcion", columnList = "prescripcion_id")
})
@Getter
@Setter
@NoArgsConstructor
public class LineaPrescripcionEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prescripcion_id", nullable = false)
    private PrescripcionEntidad prescripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private MedicamentoEntidad medicamento;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "cantidad", column = @Column(name = "dosis_cantidad")),
            @AttributeOverride(name = "unidad",   column = @Column(name = "dosis_unidad", length = 15))
    })
    private DosisEmbeddable dosis;

    @Column(nullable = false, length = 200)
    private String frecuencia;

    @Column(name = "duracion_dias", nullable = false)
    private int duracionDias;

    @Column(name = "instrucciones_administracion", columnDefinition = "TEXT")
    private String instruccionesAdministracion;

    // Desnormalizado: evita JOIN al mostrar la prescripción aunque el catálogo cambie
    @Column(name = "nombre_medicamento", length = 200)
    private String nombreMedicamento;
}
