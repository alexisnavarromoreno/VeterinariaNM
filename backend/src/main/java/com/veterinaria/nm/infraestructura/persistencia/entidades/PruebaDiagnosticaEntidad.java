package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.PruebaDiagnostica.EstadoPrueba;
import com.veterinaria.nm.dominio.modelo.PruebaDiagnostica.TipoPrueba;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "pruebas_diagnosticas", indexes = {
        @Index(name = "idx_prueba_historial", columnList = "historial_id")
})
@Getter
@Setter
@NoArgsConstructor
public class PruebaDiagnosticaEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "historial_id", nullable = false)
    private HistorialClinicoEntidad historial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TipoPrueba tipo;

    @Column(length = 300)
    private String especificacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrueba estado;

    @Column(columnDefinition = "TEXT")
    private String resultado;

    @Column(name = "resultado_alterado", nullable = false)
    private boolean resultadoAlterado;
}
