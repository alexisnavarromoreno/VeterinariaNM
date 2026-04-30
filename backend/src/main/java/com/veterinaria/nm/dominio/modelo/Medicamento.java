package com.veterinaria.nm.dominio.modelo;

import com.veterinaria.nm.dominio.modelo.valor.Dosis;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entidad que representa un medicamento del vademécum veterinario de la clínica.
 * <p>
 * El catálogo de medicamentos es fundamental para el módulo de prescripciones.
 * Cada medicamento especifica en qué especies está contraindicado,
 * lo que permite al sistema alertar automáticamente cuando un veterinario
 * intenta prescribir un fármaco incompatible con la especie del paciente.
 * </p>
 * <p>
 * Ejemplo real: el paracetamol es muy tóxico para gatos (insuficiencia hepática fulminante);
 * las piretrinas son letales para los felinos; la xilitol es letal para perros.
 * Estas contraindicaciones se modelan en {@link #especiesContraindicadas}.
 * </p>
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Medicamento {

    private final UUID id;

    /** Nombre comercial del producto (ej: "Metacam", "Synulox", "Frontline"). */
    private String nombreComercial;

    /** Principio/s activo/s (ej: "Meloxicam", "Amoxicilina + Ácido clavulánico"). */
    private String principioActivo;

    /** Concentración del principio activo (ej: "1 mg/ml", "250 mg/comprimido"). */
    private String concentracion;

    private FormaFarmaceutica formaFarmaceutica;
    private ViaAdministracion viaAdministracion;

    /**
     * Categoría terapéutica a la que pertenece el medicamento.
     * Facilita filtrar por grupo farmacológico en el selector de prescripción.
     */
    private CategoriaFarmacologica categoriaFarmacologica;

    /**
     * Dosis de referencia sugerida. El veterinario puede ajustarla.
     * Para dosis relativas (mg/kg) se calcula automáticamente con el peso de la mascota.
     */
    private Dosis dosisReferencia;

    /**
     * Nombres de las especies en las que este medicamento está contraindicado.
     * Se usa el nombre común de la especie (ej: "Gato", "Conejo", "Ave").
     * Al prescribir, el sistema cruzará esta lista con la especie del paciente.
     */
    @Builder.Default
    private final Set<String> especiesContraindicadas = new HashSet<>();

    /**
     * Advertencias clínicas críticas que el veterinario debe ver al prescribir.
     * Ej: "TÓXICO EN GATOS — No administrar bajo ningún concepto",
     *     "Usar con extrema precaución en pacientes con insuficiencia renal".
     */
    private String alertasCriticas;

    /**
     * Condiciones que requieren ajuste de dosis o prohibición: hepatopatía, nefropatía,
     * gestación, lactancia, cachorros menores de X semanas, etc.
     */
    private String contraindicacionesRelativas;

    /**
     * Tiempo en horas que debe respetarse entre la administración del medicamento
     * y el sacrificio del animal, si se destina a consumo humano.
     * Aplica principalmente en fauna silvestre, équidos y animales de granja.
     */
    private Integer tiempoEsperaSacrificio;

    /**
     * Si es {@code true}, la dispensación requiere receta veterinaria oficial.
     * En España: obligatorio para antibióticos, antiparasitarios sistémicos, etc.
     */
    private boolean requiereReceta;

    /** Indica si el medicamento está activo en el catálogo de la clínica. */
    private boolean activo;

    // ── Factory method ────────────────────────────────────────────────────────

    public static Medicamento crear(String nombreComercial, String principioActivo,
                                    String concentracion, FormaFarmaceutica forma,
                                    ViaAdministracion via, CategoriaFarmacologica categoria,
                                    boolean requiereReceta) {
        if (nombreComercial == null || nombreComercial.isBlank())
            throw new IllegalArgumentException("El nombre comercial del medicamento es obligatorio");
        if (principioActivo == null || principioActivo.isBlank())
            throw new IllegalArgumentException("El principio activo es obligatorio");

        return Medicamento.builder()
                .id(UUID.randomUUID())
                .nombreComercial(nombreComercial.trim())
                .principioActivo(principioActivo.trim())
                .concentracion(concentracion)
                .formaFarmaceutica(forma)
                .viaAdministracion(via)
                .categoriaFarmacologica(categoria)
                .requiereReceta(requiereReceta)
                .activo(true)
                .build();
    }

    // ── Comportamiento de negocio ─────────────────────────────────────────────

    /**
     * Añade una especie al conjunto de contraindicadas.
     *
     * @param nombreEspecie Nombre común de la especie (ej: "Gato")
     */
    public void contraindicarParaEspecie(String nombreEspecie) {
        if (nombreEspecie != null && !nombreEspecie.isBlank())
            especiesContraindicadas.add(nombreEspecie.trim());
    }

    /**
     * Comprueba si el medicamento está contraindicado para una especie dada.
     * Comparación case-insensitive para evitar falsos negativos por capitalización.
     *
     * @param nombreEspecie Nombre común de la especie del paciente
     * @return {@code true} si está contraindicado
     */
    public boolean estaContraindicadoPara(String nombreEspecie) {
        if (nombreEspecie == null) return false;
        return especiesContraindicadas.stream()
                .anyMatch(e -> e.equalsIgnoreCase(nombreEspecie.trim()));
    }

    public void definirDosisReferencia(Dosis dosis) {
        this.dosisReferencia = dosis;
    }

    public void actualizarAlertas(String alertas) {
        this.alertasCriticas = alertas;
    }

    public void desactivar() {
        this.activo = false;
    }

    public Set<String> getEspeciesContraindicadas() {
        return Collections.unmodifiableSet(especiesContraindicadas);
    }

    // ── Enums ─────────────────────────────────────────────────────────────────

    public enum FormaFarmaceutica {
        COMPRIMIDO, CAPSULA, JARABE, SOLUCION_ORAL, INYECTABLE,
        TOPICO_CREMA, TOPICO_SPRAY, COLIRIO, OTICO, SUPOSITORIO,
        PASTA_ORAL, MASTICABLE, SPOT_ON
    }

    public enum ViaAdministracion {
        ORAL, INTRAVENOSA, INTRAMUSCULAR, SUBCUTANEA,
        TOPICA, OFTALMICA, OTICA, RECTAL, INTRANASAL
    }

    public enum CategoriaFarmacologica {
        ANTIBIOTICO, ANTIINFLAMATORIO_AINE, ANTIINFLAMATORIO_ESTEROIDEO,
        ANTIPARSITARIO_INTERNO, ANTIPARASITARIO_EXTERNO, ANALGESICO,
        ANESTESICO, ANTIEMETICO, GASTROPROTECTOR, ANTIFUNGICO,
        ANTIHISTAMINICO, INMUNOSUPRESOR, HORMONAL, SUPLEMENTO_VITAMINAS,
        VACUNA, OTRO
    }
}
