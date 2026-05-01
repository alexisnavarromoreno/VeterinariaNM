package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.valor.Provincia;
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
public class DireccionEmbeddable {

    @Column(name = "calle", length = 200)
    private String calle;

    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Enumerated(EnumType.STRING)
    @Column(name = "provincia", length = 30)
    private Provincia provincia;

    @Column(name = "codigo_postal", length = 5)
    private String codigoPostal;
}
