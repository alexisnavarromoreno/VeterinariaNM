package com.veterinaria.nm.dominio.puertos.entrada;

import com.veterinaria.nm.dominio.modelo.HistorialClinico;
import com.veterinaria.nm.dominio.modelo.valor.SignosVitales;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Puerto de entrada para el historial clínico.
 * <p>
 * El historial es el núcleo médico de la aplicación. Los métodos reflejan
 * el flujo real de una consulta: iniciar → explorar → diagnosticar →
 * registrar pruebas → vincular prescripción → cerrar.
 * </p>
 */
public interface ServicioHistorial {

    /** Inicia un registro de historial al comenzar la consulta con los signos vitales. */
    HistorialClinico iniciarConsulta(UUID idMascota, UUID idCita, UUID idVeterinario,
                                     HistorialClinico.TipoRegistro tipo,
                                     SignosVitales signosVitales, String sintomasReferidos);

    HistorialClinico registrarExploracion(UUID idHistorial, String hallazgos);

    HistorialClinico registrarDiagnostico(UUID idHistorial, String principal,
                                           String secundarios, String diferencial);

    HistorialClinico registrarPruebasDiagnosticas(UUID idHistorial, String pruebas, String resultados);

    HistorialClinico vincularPrescripcion(UUID idHistorial, UUID idPrescripcion);

    HistorialClinico registrarTratamientoEnConsulta(UUID idHistorial, String descripcion);

    HistorialClinico registrarSeguimiento(UUID idHistorial, String recomendaciones, LocalDate proximaRevision);

    /** Cierra la consulta haciendo el diagnóstico inmutable. */
    HistorialClinico cerrarConsulta(UUID idHistorial);

    /** Permite añadir resultados de laboratorio tardíos sin reabrir el historial. */
    HistorialClinico actualizarResultadosPruebas(UUID idHistorial, String resultados);

    HistorialClinico obtenerPorId(UUID id);

    List<HistorialClinico> obtenerHistorialDeMascota(UUID idMascota);

    List<HistorialClinico> obtenerHistorialPorFechas(UUID idMascota, LocalDate desde, LocalDate hasta);
}
