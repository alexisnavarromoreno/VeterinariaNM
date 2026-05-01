package com.veterinaria.nm.infraestructura.persistencia.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clientes", indexes = {
        @Index(name = "idx_cliente_email", columnList = "email", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
public class ClienteEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String apellidos;

    @Column(unique = true, nullable = false, length = 200)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(name = "telefono_adicional", length = 20)
    private String telefonoAdicional;

    @Embedded
    private DireccionEmbeddable direccion;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private boolean activo;

    @Column(name = "notas_internas", columnDefinition = "TEXT")
    private String notasInternas;
}
