package com.veterinaria.nm.dominio.excepciones;

/**
 * Excepción base para recursos no encontrados en el dominio.
 * Las subclases específicas permiten un manejo diferenciado en el controlador.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public RecursoNoEncontradoException(String recurso, Object identificador) {
        super("%s no encontrado con identificador: %s".formatted(recurso, identificador));
    }
}
