package com.veterinaria.nm.dominio.modelo;

import com.veterinaria.nm.dominio.modelo.valor.Direccion;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Entidad raíz del agregado Cliente.
 * <p>
 * Representa al propietario (o tutor legal) de las mascotas registradas
 * en la clínica. Es el punto de entrada para cualquier operación relacionada
 * con el dueño y sus animales.
 * </p>
 * <p>
 * Un cliente inactivo no puede crear nuevas citas, pero conserva todo su
 * historial y el de sus mascotas para cumplimiento legal.
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Cliente {

    /** Identificador único generado al crear el cliente. Nunca cambia. */
    private final UUID id;

    private String nombre;
    private String apellidos;

    /**
     * Email único en el sistema. Usado también como identificador
     * en el portal del cliente y para notificaciones de cita.
     */
    private String email;

    private String telefono;

    /** Segundo teléfono de contacto (familiar, trabajo). Opcional. */
    private String telefonoAdicional;

    private Direccion direccion;

    /**
     * Fecha de primera visita o registro en la clínica.
     * Inmutable: establece la antigüedad del cliente.
     */
    private final LocalDateTime fechaRegistro;

    /**
     * Un cliente desactivado conserva historial pero no puede operar.
     * Se desactiva en lugar de eliminarse para preservar trazabilidad.
     */
    private boolean activo;

    /**
     * Notas internas del personal sobre el cliente.
     * Ej: "Prefiere que le llamen por la mañana", "Necesita factura para seguro".
     */
    private String notasInternas;

    /**
     * Mascotas registradas bajo este cliente.
     * Lista controlada: solo se modifica a través de {@link #agregarMascota}.
     */
    @Builder.Default
    private final List<Mascota> mascotas = new ArrayList<>();

    /**
     * Factory method principal. Centraliza creación y validación.
     * Genera UUID y establece {@code fechaRegistro = ahora} y {@code activo = true}.
     */
    public static Cliente crear(String nombre, String apellidos, String email,
                                String telefono, Direccion direccion) {
        validarNombre(nombre);
        validarEmail(email);
        validarTelefono(telefono);

        return Cliente.builder()
                .id(UUID.randomUUID())
                .nombre(nombre.trim())
                .apellidos(apellidos != null ? apellidos.trim() : "")
                .email(email.toLowerCase().trim())
                .telefono(telefono.trim())
                .direccion(direccion)
                .fechaRegistro(LocalDateTime.now())
                .activo(true)
                .build();
    }

    /**
     * Agrega una mascota al cliente. Previene duplicados por UUID.
     * La relación se gestiona aquí (raíz del agregado) para mantener integridad.
     */
    public void agregarMascota(Mascota mascota) {
        Objects.requireNonNull(mascota, "La mascota no puede ser nula");
        boolean yaRegistrada = mascotas.stream().anyMatch(m -> m.getId().equals(mascota.getId()));
        if (yaRegistrada) throw new IllegalStateException("La mascota ya está registrada bajo este cliente");
        mascotas.add(mascota);
    }

    /**
     * Actualiza datos de contacto. Email y teléfono se re-validan
     * porque son canales críticos de comunicación con la clínica.
     */
    public void actualizarContacto(String email, String telefono,
                                   String telefonoAdicional, Direccion direccion) {
        validarEmail(email);
        validarTelefono(telefono);
        this.email = email.toLowerCase().trim();
        this.telefono = telefono.trim();
        this.telefonoAdicional = telefonoAdicional;
        this.direccion = direccion;
    }

    public void actualizarNotas(String notas) {
        this.notasInternas = notas;
    }

    /**
     * Desactiva el cliente sin eliminar sus datos.
     * Los registros históricos (citas, historial) permanecen intactos.
     */
    public void desactivar() {
        this.activo = false;
    }

    /** Vista defensiva de la lista: no se puede modificar desde fuera del agregado. */
    public List<Mascota> getMascotas() {
        return Collections.unmodifiableList(mascotas);
    }

    /** Nombre completo para visualización en UI. */
    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    // ── Validaciones ─────────────────────────────────────────────────────────

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
    }

    private static void validarEmail(String email) {
        if (email == null || !email.matches("^[\\w.+\\-]+@[\\w\\-]+\\.[\\w.]+$"))
            throw new IllegalArgumentException("El formato del email no es válido: " + email);
    }

    private static void validarTelefono(String telefono) {
        if (telefono == null || telefono.replaceAll("[\\s\\-]", "").length() < 9)
            throw new IllegalArgumentException("El teléfono debe tener al menos 9 dígitos");
    }
}
