package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.CondicionCronica;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredisposicionGeneticaEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "condicion", length = 50)
    private CondicionCronica.TipoCondicion condicion;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "recomendacion", length = 500)
    private String recomendacion;
}
