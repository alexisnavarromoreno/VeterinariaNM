package com.veterinaria.nm.dominio.puertos.salida;

import com.veterinaria.nm.dominio.modelo.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para la persistencia de usuarios del sistema.
 */
public interface RepositorioUsuario {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(UUID id);

    Optional<Usuario> buscarPorEmail(String email);

    List<Usuario> listarPorRol(Usuario.Rol rol);

    /** Lista todos los veterinarios activos. Usado en el selector de asignación de citas. */
    List<Usuario> listarVeterinariosActivos();

    boolean existePorEmail(String email);
}
