package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.Alergia.GravedadAlergia;
import com.veterinaria.nm.dominio.modelo.Alergia.TipoAlergia;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "mascotas_alergias", indexes = {
        @Index(name = "idx_alergia_mascota", columnList = "mascota_id")
})
@Getter
@Setter
@NoArgsConstructor
public class AlergiaEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntidad mascota;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoAlergia tipo;

    @Column(nullable = false, length = 200)
    private String sustancia;

    // Nullable: solo si tipo == MEDICAMENTO y el medicamento está en catálogo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento_id")
    private MedicamentoEntidad medicamento;

    @Column(name = "descripcion_reaccion", columnDefinition = "TEXT")
    private String descripcionReaccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private GravedadAlergia gravedad;

    @Column(name = "fecha_deteccion")
    private LocalDate fechaDeteccion;
}
