package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.Medicamento.CategoriaFarmacologica;
import com.veterinaria.nm.dominio.modelo.Medicamento.FormaFarmaceutica;
import com.veterinaria.nm.dominio.modelo.Medicamento.ViaAdministracion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "medicamentos")
@Getter
@Setter
@NoArgsConstructor
public class MedicamentoEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nombre_comercial", nullable = false, length = 200)
    private String nombreComercial;

    @Column(name = "principio_activo", nullable = false, length = 300)
    private String principioActivo;

    @Column(length = 100)
    private String concentracion;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_farmaceutica", length = 20)
    private FormaFarmaceutica formaFarmaceutica;

    @Enumerated(EnumType.STRING)
    @Column(name = "via_administracion", length = 15)
    private ViaAdministracion viaAdministracion;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_farmacologica", length = 40)
    private CategoriaFarmacologica categoriaFarmacologica;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "cantidad", column = @Column(name = "dosis_ref_cantidad")),
            @AttributeOverride(name = "unidad",   column = @Column(name = "dosis_ref_unidad", length = 15))
    })
    private DosisEmbeddable dosisReferencia;

    // FK real a especie: integridad referencial garantizada por JPA y la BD
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "medicamento_contraindicaciones",
            joinColumns = @JoinColumn(name = "medicamento_id"),
            inverseJoinColumns = @JoinColumn(name = "especie_id")
    )
    private Set<EspecieEntidad> especiesContraindicadas = new HashSet<>();

    @Column(name = "alertas_criticas", columnDefinition = "TEXT")
    private String alertasCriticas;

    @Column(name = "contraindicaciones_relativas", columnDefinition = "TEXT")
    private String contraindicacionesRelativas;

    @Column(name = "tiempo_espera_sacrificio")
    private Integer tiempoEsperaSacrificio;

    @Column(name = "requiere_receta", nullable = false)
    private boolean requiereReceta;

    @Column(nullable = false)
    private boolean activo;
}
