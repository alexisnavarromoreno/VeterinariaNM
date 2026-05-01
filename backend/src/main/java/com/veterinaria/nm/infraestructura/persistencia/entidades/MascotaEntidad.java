package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.Mascota.Sexo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mascotas", indexes = {
        @Index(name = "idx_mascota_microchip", columnList = "numero_microchip", unique = true),
        @Index(name = "idx_mascota_propietario", columnList = "propietario_id")
})
@Getter
@Setter
@NoArgsConstructor
public class MascotaEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieEntidad especie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raza_id")
    private RazaEntidad raza;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Sexo sexo;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "peso_actual")
    private Double pesoActual;

    @Column(name = "color_pelaje", length = 100)
    private String colorPelaje;

    @Column(name = "numero_microchip", unique = true, length = 15)
    private String numeroMicrochip;

    @Column(nullable = false)
    private boolean esterilizado;

    @Column(name = "notas_manejo", columnDefinition = "TEXT")
    private String notasManejo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "propietario_id", nullable = false)
    private ClienteEntidad propietario;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private boolean activo;
}
