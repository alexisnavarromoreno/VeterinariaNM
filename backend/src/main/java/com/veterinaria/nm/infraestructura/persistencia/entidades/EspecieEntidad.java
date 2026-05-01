package com.veterinaria.nm.infraestructura.persistencia.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "especies")
@Getter
@Setter
@NoArgsConstructor
public class EspecieEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nombre_comun", nullable = false, length = 100)
    private String nombreComun;

    @Column(name = "nombre_cientifico", length = 200)
    private String nombreCientifico;

    @Column(name = "notas_clinicas", columnDefinition = "TEXT")
    private String notasClinicas;

    @Column(name = "requiere_especialista", nullable = false)
    private boolean requiereEspecialista;

    @Column(nullable = false)
    private boolean activa;
}
