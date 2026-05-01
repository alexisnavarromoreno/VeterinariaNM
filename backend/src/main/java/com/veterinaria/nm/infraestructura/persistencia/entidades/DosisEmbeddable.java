package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.valor.Dosis;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Columnas concretas se definen via @AttributeOverrides en la entidad que lo embebe
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DosisEmbeddable {

    private Double cantidad;

    @Enumerated(EnumType.STRING)
    private Dosis.UnidadDosis unidad;
}
