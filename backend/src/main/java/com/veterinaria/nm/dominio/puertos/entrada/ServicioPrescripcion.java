package com.veterinaria.nm.dominio.puertos.entrada;

import com.veterinaria.nm.dominio.modelo.LineaPrescripcion;
import com.veterinaria.nm.dominio.modelo.Prescripcion;

import java.util.List;
import java.util.UUID;

/**
 * Puerto de entrada para la gestión de prescripciones veterinarias.
 * <p>
 * El flujo normal es: crear → agregar líneas → verificar compatibilidad → emitir.
 * La verificación de compatibilidad cruza los medicamentos con las alergias y
 * la especie del paciente antes de permitir la emisión.
 * </p>
 */
public interface ServicioPrescripcion {

    Prescripcion crearPrescripcion(UUID idHistorialClinico, UUID idMascota, UUID idVeterinario);

    Prescripcion agregarLineaMedicamento(UUID idPrescripcion, LineaPrescripcion linea);

    Prescripcion eliminarLineaMedicamento(UUID idPrescripcion, UUID idLinea);

    /**
     * Verifica que ningún medicamento de la prescripción esté contraindicado
     * para la especie del paciente ni en conflicto con sus alergias conocidas.
     *
     * @return Lista de alertas encontradas (vacía si todo es compatible)
     */
    List<String> verificarCompatibilidad(UUID idPrescripcion);

    /** Emite la prescripción haciéndola inmutable y generando código oficial si aplica. */
    Prescripcion emitirPrescripcion(UUID idPrescripcion);

    Prescripcion obtenerPorId(UUID id);

    List<Prescripcion> listarPorMascota(UUID idMascota);
}
