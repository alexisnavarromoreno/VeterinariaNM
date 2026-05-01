package com.veterinaria.nm.dominio.modelo;

/**
 * Planes de suscripción SaaS para clínicas veterinarias.
 * El plan determina los límites operativos de la clínica en la plataforma.
 */
public enum PlanSuscripcion {

    /**
     * 1 veterinario, funcionalidades core (clientes, mascotas, citas, historial básico).
     * Sin acceso a API externa ni módulos avanzados.
     */
    BASICO("Básico", 1, false, false),

    /**
     * Hasta 5 veterinarios. Todas las funcionalidades clínicas completas.
     * Sin acceso a API REST pública.
     */
    PROFESIONAL("Profesional", 5, true, false),

    /**
     * Veterinarios ilimitados. Todas las funcionalidades + API REST pública
     * para integración con sistemas de gestión propios.
     */
    ENTERPRISE("Enterprise", Integer.MAX_VALUE, true, true);

    private final String nombre;
    private final int maxVeterinarios;
    private final boolean moduloAvanzadoIncluido;
    private final boolean apiPublicaIncluida;

    PlanSuscripcion(String nombre, int maxVeterinarios,
                    boolean moduloAvanzadoIncluido, boolean apiPublicaIncluida) {
        this.nombre = nombre;
        this.maxVeterinarios = maxVeterinarios;
        this.moduloAvanzadoIncluido = moduloAvanzadoIncluido;
        this.apiPublicaIncluida = apiPublicaIncluida;
    }

    public String getNombre() { return nombre; }
    public int getMaxVeterinarios() { return maxVeterinarios; }
    public boolean isModuloAvanzadoIncluido() { return moduloAvanzadoIncluido; }
    public boolean isApiPublicaIncluida() { return apiPublicaIncluida; }

    public boolean permiteAnadir(int veterinariosActuales) {
        return veterinariosActuales < maxVeterinarios;
    }
}
