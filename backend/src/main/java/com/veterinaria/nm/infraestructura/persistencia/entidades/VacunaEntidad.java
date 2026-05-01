package com.veterinaria.nm.infraestructura.persistencia.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "vacunas")
@Getter
@Setter
@NoArgsConstructor
public class VacunaEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nombre_comercial", nullable = false, length = 200)
    private String nombreComercial;

    @Column(length = 200)
    private String laboratorio;

    // Enfermedades cubiertas: conjunto abierto y variable por vacuna, String es adecuado
    @ElementCollection
    @CollectionTable(
            name = "vacuna_enfermedades",
            joinColumns = @JoinColumn(name = "vacuna_id")
    )
    @Column(name = "enfermedad", length = 100)
    private Set<String> enfermedadesCubiertas = new HashSet<>();

    // FK real a especie: garantiza que solo se asignan especies existentes en catálogo
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "vacuna_especies",
            joinColumns = @JoinColumn(name = "vacuna_id"),
            inverseJoinColumns = @JoinColumn(name = "especie_id")
    )
    private Set<EspecieEntidad> especiesAplicables = new HashSet<>();

    @Column(name = "intervalo_dosis_recuerdo_meses")
    private Integer intervaloDosisRecuerdoMeses;

    @Column(name = "pauta_primovacunacion", columnDefinition = "TEXT")
    private String pautaPrimovacunacion;

    @Column(nullable = false)
    private boolean activa;
}
