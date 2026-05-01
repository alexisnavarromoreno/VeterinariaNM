package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.HistorialClinico.TipoRegistro;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historiales_clinicos", indexes = {
        @Index(name = "idx_historial_mascota", columnList = "mascota_id"),
        @Index(name = "idx_historial_fecha", columnList = "fecha_consulta")
})
@Getter
@Setter
@NoArgsConstructor
public class HistorialClinicoEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntidad mascota;

    // Nullable: registros manuales o urgencias pueden no tener cita previa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id")
    private CitaEntidad cita;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private UsuarioEntidad veterinario;

    @Column(name = "fecha_consulta", nullable = false, updatable = false)
    private LocalDateTime fechaConsulta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_registro", nullable = false, length = 35)
    private TipoRegistro tipoRegistro;

    @Embedded
    private SignosVitalesEmbeddable signosVitales;

    @Column(name = "sintomas_referidos", columnDefinition = "TEXT")
    private String sintomasReferidos;

    @Column(name = "hallazgos_exploracion", columnDefinition = "TEXT")
    private String hallazgosExploracion;

    @Column(name = "diagnostico_principal", columnDefinition = "TEXT")
    private String diagnosticoPrincipal;

    @Column(name = "diagnosticos_secundarios", columnDefinition = "TEXT")
    private String diagnosticosSecundarios;

    @Column(name = "diagnostico_diferencial", columnDefinition = "TEXT")
    private String diagnosticoDiferencial;

    // UUID plano: se vincula al cerrar la consulta, el adaptador lo resuelve
    @Column(name = "prescripcion_id", columnDefinition = "uuid")
    private UUID idPrescripcion;

    @Column(name = "recomendaciones_domiciliarias", columnDefinition = "TEXT")
    private String recomendacionesDomiciliarias;

    @Column(name = "fecha_proxima_revision")
    private LocalDate fechaProximaRevision;

    @Column(name = "observaciones_posteriores", columnDefinition = "TEXT")
    private String observacionesPosteriores;

    @Column(nullable = false)
    private boolean cerrado;
}
