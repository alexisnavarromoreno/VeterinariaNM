package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.Raza.TamanoRaza;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "razas")
@Getter
@Setter
@NoArgsConstructor
public class RazaEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieEntidad especie;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private TamanoRaza tamano;

    @ElementCollection
    @CollectionTable(
            name = "raza_predisposiciones",
            joinColumns = @JoinColumn(name = "raza_id")
    )
    private List<PredisposicionGeneticaEmbeddable> predisposicionesGeneticas = new ArrayList<>();

    @Column(name = "esperanza_vida_anios")
    private Integer esperanzaVidaAnios;

    @Column(nullable = false)
    private boolean activa;
}
