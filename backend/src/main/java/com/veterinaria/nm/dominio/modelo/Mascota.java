package com.veterinaria.nm.dominio.modelo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

/**
 * Entidad raíz del agregado Mascota: el paciente de la clínica.
 * <p>
 * El diseño refleja la realidad clínica: la especie y raza condicionan
 * los protocolos terapéuticos y las dosis farmacológicas; las alergias
 * ({@link Alergia}) se cruzan con cada prescripción; las condiciones crónicas
 * ({@link CondicionCronica}) alertan al veterinario en cada consulta.
 * </p>
 * <p>
 * {@code idRaza} es nullable porque los animales mestizos o de raza desconocida
 * son la mayoría en la práctica clínica real.
 * El microchip es nullable para especies que no lo requieren legalmente,
 * pero se valida como número ISO 11784/11785 cuando se proporciona.
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Mascota {

    private final UUID id;
    private String nombre;

    /** Referencia a {@link Especie}. Determina los protocolos farmacológicos aplicables. */
    private final UUID idEspecie;

    /**
     * Referencia a {@link Raza}. Nullable: un mestizo o raza desconocida es perfectamente válido.
     * Al tener raza, el sistema puede mostrar alertas de predisposiciones genéticas.
     */
    private UUID idRaza;

    private Sexo sexo;
    private LocalDate fechaNacimiento;

    /**
     * Peso en kg actualizado en cada consulta.
     * Es crítico para el cálculo de dosis de medicamentos relativos al peso (mg/kg).
     * El histórico de pesos queda registrado en {@link HistorialClinico} (campo {@code signosVitales.peso}).
     */
    private Double pesoActual;

    private String colorPelaje;

    /**
     * Número de microchip ISO 11784/11785 (15 dígitos numéricos).
     * Obligatorio por ley en España para perros, gatos y hurones.
     * Nullable para especies que no lo requieren.
     */
    private String numeroMicrochip;

    private boolean esterilizado;

    /**
     * Alergias conocidas del paciente: a medicamentos, alimentos, ambientales o químicas.
     * Se verifican automáticamente al crear una prescripción para alertar de incompatibilidades.
     * Se gestiona a través de {@link #agregarAlergia(Alergia)}.
     */
    @Builder.Default
    private final List<Alergia> alergias = new ArrayList<>();

    /**
     * Condiciones crónicas diagnosticadas que condicionan el tratamiento.
     * El veterinario las ve al abrir la ficha para ajustar protocolos terapéuticos.
     * Se gestiona a través de {@link #agregarCondicionCronica(CondicionCronica)}.
     */
    @Builder.Default
    private final List<CondicionCronica> condicionesCronicas = new ArrayList<>();

    /**
     * Notas de manejo clínico del personal: comportamiento, técnicas de sujeción recomendadas.
     * Ej: "Muy nervioso. Requiere bozal y dos personas para la exploración",
     *     "Solo acepta la exploración con el dueño presente".
     */
    private String notasManejo;

    /** UUID del propietario ({@link Cliente}). Inmutable: el dueño no cambia. */
    private final UUID idPropietario;
    private final LocalDateTime fechaRegistro;
    private boolean activo;

    // ── Factory method ────────────────────────────────────────────────────────

    public static Mascota crear(String nombre, UUID idEspecie, UUID idRaza,
                                Sexo sexo, LocalDate fechaNacimiento, Double pesoActual,
                                String colorPelaje, String numeroMicrochip,
                                boolean esterilizado, UUID idPropietario) {
        validarNombre(nombre);
        Objects.requireNonNull(idEspecie, "La especie de la mascota es obligatoria");
        Objects.requireNonNull(idPropietario, "La mascota debe tener un propietario");
        validarFechaNacimiento(fechaNacimiento);
        validarMicrochip(numeroMicrochip);

        return Mascota.builder()
                .id(UUID.randomUUID())
                .nombre(nombre.trim())
                .idEspecie(idEspecie)
                .idRaza(idRaza)
                .sexo(sexo)
                .fechaNacimiento(fechaNacimiento)
                .pesoActual(pesoActual)
                .colorPelaje(colorPelaje)
                .numeroMicrochip(numeroMicrochip)
                .esterilizado(esterilizado)
                .idPropietario(idPropietario)
                .fechaRegistro(LocalDateTime.now())
                .activo(true)
                .build();
    }

    // ── Comportamiento de negocio ─────────────────────────────────────────────

    /**
     * Actualiza el peso tras una consulta.
     * El histórico queda en {@link HistorialClinico#getSignosVitales()}.
     */
    public void actualizarPeso(double nuevoPeso) {
        if (nuevoPeso <= 0) throw new IllegalArgumentException("El peso debe ser un valor positivo");
        this.pesoActual = nuevoPeso;
    }

    /**
     * Añade una alergia al paciente. Previene duplicados por sustancia (case-insensitive).
     * Las alergias ANAFILAXIA se añaden siempre, sin verificar duplicados,
     * para no perder esa información crítica.
     */
    public void agregarAlergia(Alergia alergia) {
        Objects.requireNonNull(alergia, "La alergia no puede ser nula");
        if (alergia.getGravedad() != Alergia.GravedadAlergia.ANAFILAXIA) {
            boolean existe = alergias.stream()
                    .anyMatch(a -> a.getSustancia().equalsIgnoreCase(alergia.getSustancia()));
            if (existe) return;
        }
        alergias.add(alergia);
    }

    /** Añade una condición crónica. Previene duplicados por tipo. */
    public void agregarCondicionCronica(CondicionCronica condicion) {
        Objects.requireNonNull(condicion, "La condición crónica no puede ser nula");
        boolean existe = condicionesCronicas.stream()
                .anyMatch(c -> c.getTipo() == condicion.getTipo());
        if (existe)
            throw new IllegalStateException("Ya existe una condición de tipo " + condicion.getTipo());
        condicionesCronicas.add(condicion);
    }

    /**
     * Comprueba si el paciente podría ser alérgico a una sustancia.
     * Comparación case-insensitive. Se usa en el caso de uso de prescripción.
     */
    public boolean posibleAlergiA(String sustancia) {
        if (sustancia == null) return false;
        return alergias.stream()
                .anyMatch(a -> a.getSustancia().toLowerCase().contains(sustancia.toLowerCase()));
    }

    /** Devuelve las alergias que generan alerta crítica (GRAVE o ANAFILAXIA). */
    public List<Alergia> getAlertasCriticas() {
        return alergias.stream().filter(Alergia::esAlertaCritica).toList();
    }

    /** Devuelve las condiciones crónicas que requieren consideración clínica activa. */
    public List<CondicionCronica> getCondicionesActivasOControladas() {
        return condicionesCronicas.stream()
                .filter(CondicionCronica::requiereConsideracionClinica).toList();
    }

    public void marcarEsterilizado() { this.esterilizado = true; }

    public void registrarMicrochip(String numeroMicrochip) {
        validarMicrochip(numeroMicrochip);
        this.numeroMicrochip = numeroMicrochip;
    }

    public void actualizarNotasManejo(String notas) { this.notasManejo = notas; }

    public void desactivar() { this.activo = false; }

    // ── Utilidades calculadas ─────────────────────────────────────────────────

    public Optional<Period> getEdad() {
        return fechaNacimiento != null
                ? Optional.of(Period.between(fechaNacimiento, LocalDate.now()))
                : Optional.empty();
    }

    /** Ej: "3 años y 4 meses", "8 meses", "Edad desconocida". */
    public String getEdadDescriptiva() {
        return getEdad().map(p -> {
            if (p.getYears() == 0) return p.getMonths() + " meses";
            if (p.getMonths() == 0) return p.getYears() + " años";
            return p.getYears() + " años y " + p.getMonths() + " meses";
        }).orElse("Edad desconocida");
    }

    /** Vistas defensivas de colecciones. */
    public List<Alergia> getAlergias() { return Collections.unmodifiableList(alergias); }
    public List<CondicionCronica> getCondicionesCronicas() { return Collections.unmodifiableList(condicionesCronicas); }

    // ── Validaciones ─────────────────────────────────────────────────────────

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre de la mascota es obligatorio");
    }

    private static void validarFechaNacimiento(LocalDate fecha) {
        if (fecha != null && fecha.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
    }

    private static void validarMicrochip(String microchip) {
        // ISO 11784: 15 dígitos numéricos. Se tolera null para especies que no lo requieren.
        if (microchip != null && !microchip.matches("\\d{15}"))
            throw new IllegalArgumentException("El número de microchip debe tener exactamente 15 dígitos (ISO 11784)");
    }

    public enum Sexo { MACHO, HEMBRA }
}
