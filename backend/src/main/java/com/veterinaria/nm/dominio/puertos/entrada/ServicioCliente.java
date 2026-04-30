package com.veterinaria.nm.dominio.puertos.entrada;

import com.veterinaria.nm.dominio.modelo.Cliente;

import java.util.List;
import java.util.UUID;

public interface ServicioCliente {

    Cliente crearCliente(Cliente cliente);

    Cliente obtenerClientePorId(UUID id);

    Cliente obtenerClientePorEmail(String email);

    List<Cliente> listarClientesActivos();

    Cliente actualizarCliente(UUID id, Cliente cliente);

    void desactivarCliente(UUID id);

    List<Cliente> buscarPorNombre(String nombre);
}
