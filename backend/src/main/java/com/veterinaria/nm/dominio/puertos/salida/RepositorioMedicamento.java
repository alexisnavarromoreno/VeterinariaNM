package com.veterinaria.nm.dominio.puertos.salida;

import com.veterinaria.nm.dominio.modelo.Medicamento;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para la persistencia del catálogo de medicamentos.
 */
public interface RepositorioMedicamento {

    Medicamento guardar(Medicamento medicamento);

    Optional<Medicamento> buscarPorId(UUID id);

    List<Medicamento> listarActivos();

    List<Medicamento> buscarPorPrincipioActivo(String principioActivo);

    List<Medicamento> listarPorCategoria(Medicamento.CategoriaFarmacologica categoria);

    /** Busca medicamentos aptos para una especie (excluye los contraindicados para ella). */
    List<Medicamento> listarCompatiblesConEspecie(String nombreEspecie);

    boolean existePorNombreComercial(String nombreComercial);
}
