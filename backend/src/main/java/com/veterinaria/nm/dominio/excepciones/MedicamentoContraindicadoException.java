package com.veterinaria.nm.dominio.excepciones;

/**
 * Se lanza al intentar prescribir un medicamento contraindicado para
 * la especie del paciente o con una alergia conocida registrada.
 */
public class MedicamentoContraindicadoException extends RuntimeException {

    public MedicamentoContraindicadoException(String medicamento, String motivo) {
        super("El medicamento '%s' no puede prescribirse: %s".formatted(medicamento, motivo));
    }
}
