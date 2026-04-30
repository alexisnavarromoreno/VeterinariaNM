package com.veterinaria.nm.dominio.puertos.salida;

import com.veterinaria.nm.dominio.modelo.Cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para la persistencia de clientes.
 * <p>
 * Define el contrato que el dominio exige a cualquier mecanismo de persistencia.
 * La implementación concreta (JPA, MongoDB, en memoria...) vive en la capa
 * de infraestructura y es invisible para el dominio y la aplicación.
 * </p>
 */
public interface RepositorioCliente {

    Cliente guardar(Cliente cliente);

    Optional<Cliente> buscarPorId(UUID id);

    Optional<Cliente> buscarPorEmail(String email);

    List<Cliente> listarActivos();

    List<Cliente> buscarPorNombreOApellidos(String termino);

    boolean existePorEmail(String email);

    void eliminar(UUID id);
}
