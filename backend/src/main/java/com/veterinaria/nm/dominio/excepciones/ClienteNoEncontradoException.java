package com.veterinaria.nm.dominio.excepciones;

import java.util.UUID;

public class ClienteNoEncontradoException extends RecursoNoEncontradoException {
    public ClienteNoEncontradoException(UUID id) {
        super("Cliente", id);
    }
    public ClienteNoEncontradoException(String email) {
        super("Cliente con email " + email + " no encontrado");
    }
}
