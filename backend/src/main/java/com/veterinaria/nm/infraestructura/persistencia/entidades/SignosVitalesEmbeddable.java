package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.valor.CondicionCorporal;
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
public class SignosVitalesEmbeddable {

    @Column(name = "sv_temperatura")
    private Double temperatura;

    @Column(name = "sv_frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Column(name = "sv_frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    @Column(name = "sv_peso")
    private Double peso;

    @Enumerated(EnumType.STRING)
    @Column(name = "sv_condicion_corporal", length = 10)
    private CondicionCorporal condicionCorporal;

    @Column(name = "sv_presion_arterial", length = 20)
    private String presionArterial;
}
