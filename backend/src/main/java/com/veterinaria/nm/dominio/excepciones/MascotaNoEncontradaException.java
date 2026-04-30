package com.veterinaria.nm.dominio.excepciones;

import java.util.UUID;

public class MascotaNoEncontradaException extends RecursoNoEncontradoException {
    public MascotaNoEncontradaException(UUID id) {
        super("Mascota", id);
    }
    public MascotaNoEncontradaException(String microchip) {
        super("Mascota con microchip " + microchip + " no encontrada");
    }
}
