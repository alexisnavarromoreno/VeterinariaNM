package com.veterinaria.nm.dominio.modelo;

/**
 * Especialidades veterinarias reconocidas.
 * <p>
 * Un veterinario puede tener varias especialidades. Se usa en la asignación
 * de citas para filtrar qué profesional es más adecuado según el tipo
 * de consulta solicitada.
 * </p>
 */
public enum EspecialidadVeterinaria {

    MEDICINA_INTERNA("Medicina Interna"),
    CIRUGIA("Cirugía General"),
    CIRUGIA_ORTOPEDICA("Cirugía Ortopédica"),
    DERMATOLOGIA("Dermatología"),
    CARDIOLOGIA("Cardiología"),
    NEUROLOGIA("Neurología"),
    OFTALMOLOGIA("Oftalmología"),
    ONCOLOGIA("Oncología"),
    REPRODUCCION_OBSTETRICIA("Reproducción y Obstetricia"),
    URGENCIAS_CUIDADOS_INTENSIVOS("Urgencias y Cuidados Intensivos"),
    ANIMALES_EXOTICOS("Animales Exóticos"),
    AVES("Aves"),
    REPTILES("Reptiles"),
    FAUNA_SILVESTRE("Fauna Silvestre"),
    NUTRICION("Nutrición y Dietética"),
    COMPORTAMIENTO("Medicina del Comportamiento"),
    ANESTESIOLOGIA("Anestesiología"),
    DIAGNOSTICO_POR_IMAGEN("Diagnóstico por Imagen"),
    REHABILITACION("Rehabilitación y Fisioterapia"),
    MEDICINA_GENERAL("Medicina General");

    private final String descripcion;

    EspecialidadVeterinaria(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() { return descripcion; }
}
