package com.veterinaria.nm.dominio.modelo.valor;

/**
 * Las 17 comunidades autónomas + 2 ciudades autónomas de España.
 * Se deriva de {@link Provincia} — no se almacena como columna independiente.
 */
public enum ComunidadAutonoma {

    ANDALUCIA("Andalucía"),
    ARAGON("Aragón"),
    ASTURIAS("Principado de Asturias"),
    BALEARES("Illes Balears"),
    CANARIAS("Canarias"),
    CANTABRIA("Cantabria"),
    CASTILLA_LA_MANCHA("Castilla-La Mancha"),
    CASTILLA_Y_LEON("Castilla y León"),
    CATALUNA("Catalunya"),
    COMUNIDAD_VALENCIANA("Comunitat Valenciana"),
    EXTREMADURA("Extremadura"),
    GALICIA("Galicia"),
    LA_RIOJA("La Rioja"),
    MADRID("Comunidad de Madrid"),
    MURCIA("Región de Murcia"),
    NAVARRA("Comunidad Foral de Navarra"),
    PAIS_VASCO("Euskadi"),
    CEUTA("Ciudad Autónoma de Ceuta"),
    MELILLA("Ciudad Autónoma de Melilla");

    private final String nombre;

    ComunidadAutonoma(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
