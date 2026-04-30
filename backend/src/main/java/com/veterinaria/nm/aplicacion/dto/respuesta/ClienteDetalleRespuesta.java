package com.veterinaria.nm.aplicacion.dto.respuesta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de respuesta detallado para un cliente, incluyendo su lista de mascotas.
 * Se usa en la pantalla de detalle del cliente, no en el listado.
 */
public record ClienteDetalleRespuesta(
        UUID id,
        String nombre,
        String apellidos,
        String email,
        String telefono,
        String telefonoAdicional,
        DireccionRespuesta direccion,
        String notasInternas,
        LocalDateTime fechaRegistro,
        boolean activo,
        List<MascotaRespuesta> mascotas
) {}
