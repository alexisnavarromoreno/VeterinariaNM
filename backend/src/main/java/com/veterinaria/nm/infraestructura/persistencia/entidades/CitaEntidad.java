package com.veterinaria.nm.infraestructura.persistencia.entidades;

import com.veterinaria.nm.dominio.modelo.Cita.EstadoCita;
import com.veterinaria.nm.dominio.modelo.Cita.OrigenCita;
import com.veterinaria.nm.dominio.modelo.Cita.TipoCita;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "citas", indexes = {
        @Index(name = "idx_cita_mascota", columnList = "mascota_id"),
        @Index(name = "idx_cita_veterinario_fecha", columnList = "veterinario_id, fecha_hora")
})
@Getter
@Setter
@NoArgsConstructor
public class CitaEntidad {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntidad mascota;

    // Nullable: se asigna al confirmar, no siempre al solicitar
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id")
    private UsuarioEntidad veterinario;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "duracion_minutos", nullable = false)
    private int duracionMinutos;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cita", nullable = false, length = 30)
    private TipoCita tipoCita;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoCita estado;

    @Column(name = "motivo_consulta", nullable = false, columnDefinition = "TEXT")
    private String motivoConsulta;

    @Column(name = "notas_veterinario", columnDefinition = "TEXT")
    private String notasVeterinario;

    // UUID plano: se vincula tarde (al completar la cita), el adaptador lo resuelve
    @Column(name = "historial_generado_id", columnDefinition = "uuid")
    private UUID idHistorialGenerado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OrigenCita origen;
}
