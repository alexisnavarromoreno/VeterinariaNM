package com.veterinaria.nm.dominio.modelo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Entidad que representa a un usuario del sistema de la clínica.
 * <p>
 * Los tres roles cubren la operativa real:
 * <ul>
 *   <li>{@link Rol#VETERINARIO}: acceso a historial, diagnóstico y prescripciones</li>
 *   <li>{@link Rol#RECEPCIONISTA}: gestión de citas, clientes y cobros</li>
 *   <li>{@link Rol#ADMINISTRADOR}: configuración y gestión de usuarios</li>
 * </ul>
 * </p>
 * <p>
 * Las especialidades ({@link EspecialidadVeterinaria}) condicionan qué tipos
 * de cita se asignan automáticamente a este veterinario. Un RECEPCIONISTA o
 * ADMINISTRADOR no tiene especialidades.
 * </p>
 * <p>
 * El {@code passwordHash} es BCrypt generado en infraestructura.
 * El dominio solo lo almacena; la verificación delega en el servicio de seguridad.
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Usuario {

    private final UUID id;
    private String nombre;
    private String apellidos;

    /** Email único en el sistema. Identificador de login. */
    private final String email;

    /**
     * Hash BCrypt de la contraseña. El dominio no genera ni compara hashes;
     * esa responsabilidad pertenece al adaptador de seguridad.
     */
    private String passwordHash;

    private Rol rol;

    /**
     * Número de colegiado oficial (ej: "CO/12345" en España).
     * Obligatorio para {@link Rol#VETERINARIO} para poder firmar prescripciones
     * de antibióticos y otros MUV sujetos a normativa PRESVET.
     */
    private String numeroColegiado;

    /**
     * Especialidades veterinarias del profesional.
     * Se usa para filtrar qué veterinario es más adecuado en cada tipo de cita.
     * Vacío para roles que no son VETERINARIO.
     */
    @Builder.Default
    private final Set<EspecialidadVeterinaria> especialidades = new HashSet<>();

    private boolean activo;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime ultimoAcceso;

    // ── Factory method ────────────────────────────────────────────────────────

    public static Usuario crear(String nombre, String apellidos, String email,
                                String passwordHash, Rol rol) {
        validarEmail(email);
        Objects.requireNonNull(rol, "El rol del usuario es obligatorio");
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre del usuario es obligatorio");

        return Usuario.builder()
                .id(UUID.randomUUID())
                .nombre(nombre.trim())
                .apellidos(apellidos != null ? apellidos.trim() : "")
                .email(email.toLowerCase().trim())
                .passwordHash(passwordHash)
                .rol(rol)
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    // ── Comportamiento de negocio ─────────────────────────────────────────────

    public boolean tieneRol(Rol rolRequerido) { return this.rol == rolRequerido; }
    public boolean esVeterinario() { return this.rol == Rol.VETERINARIO; }
    public boolean esAdministrador() { return this.rol == Rol.ADMINISTRADOR; }

    /** Añade una especialidad al veterinario. Solo aplica al rol VETERINARIO. */
    public void agregarEspecialidad(EspecialidadVeterinaria especialidad) {
        if (!esVeterinario())
            throw new IllegalStateException("Solo los veterinarios pueden tener especialidades");
        especialidades.add(especialidad);
    }

    public void eliminarEspecialidad(EspecialidadVeterinaria especialidad) {
        especialidades.remove(especialidad);
    }

    /**
     * Comprueba si el veterinario tiene una especialidad concreta.
     * Útil para la lógica de asignación automática de citas.
     */
    public boolean tieneEspecialidad(EspecialidadVeterinaria especialidad) {
        return especialidades.contains(especialidad);
    }

    /** Registra el momento del último acceso exitoso al sistema. */
    public void registrarAcceso() {
        this.ultimoAcceso = LocalDateTime.now();
    }

    public void actualizarPassword(String nuevoHash) {
        if (nuevoHash == null || nuevoHash.isBlank())
            throw new IllegalArgumentException("El hash de contraseña no puede estar vacío");
        this.passwordHash = nuevoHash;
    }

    public void asignarNumeroColegiado(String numeroColegiado) {
        if (!esVeterinario())
            throw new IllegalStateException("Solo los veterinarios tienen número de colegiado");
        this.numeroColegiado = numeroColegiado;
    }

    public void cambiarRol(Rol nuevoRol) {
        Objects.requireNonNull(nuevoRol, "El nuevo rol no puede ser nulo");
        if (nuevoRol != Rol.VETERINARIO) especialidades.clear();
        this.rol = nuevoRol;
    }

    public void desactivar() { this.activo = false; }

    public String getNombreCompleto() { return nombre + " " + apellidos; }

    public Set<EspecialidadVeterinaria> getEspecialidades() { return Collections.unmodifiableSet(especialidades); }

    private static void validarEmail(String email) {
        if (email == null || !email.matches("^[\\w.+\\-]+@[\\w\\-]+\\.[\\w.]+$"))
            throw new IllegalArgumentException("El email del usuario no es válido: " + email);
    }

    // ── Enum de roles ─────────────────────────────────────────────────────────

    public enum Rol {
        ADMINISTRADOR,
        VETERINARIO,
        RECEPCIONISTA
    }
}
