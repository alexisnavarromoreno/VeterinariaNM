package com.veterinaria.nm.dominio.modelo.valor;

/**
 * Las 52 provincias españolas (50 provincias + Ceuta y Melilla).
 * Se modela como enum porque el conjunto es cerrado y estable.
 * Proporciona validación de dominio: una dirección con provincia
 * inexistente es rechazada en construcción.
 */
public enum Provincia {

    // Andalucía
    ALMERIA("Almería"),
    CADIZ("Cádiz"),
    CORDOBA("Córdoba"),
    GRANADA("Granada"),
    HUELVA("Huelva"),
    JAEN("Jaén"),
    MALAGA("Málaga"),
    SEVILLA("Sevilla"),

    // Aragón
    HUESCA("Huesca"),
    TERUEL("Teruel"),
    ZARAGOZA("Zaragoza"),

    // Asturias
    ASTURIAS("Asturias"),

    // Baleares
    BALEARES("Illes Balears"),

    // Canarias
    LAS_PALMAS("Las Palmas"),
    SANTA_CRUZ_DE_TENERIFE("Santa Cruz de Tenerife"),

    // Cantabria
    CANTABRIA("Cantabria"),

    // Castilla-La Mancha
    ALBACETE("Albacete"),
    CIUDAD_REAL("Ciudad Real"),
    CUENCA("Cuenca"),
    GUADALAJARA("Guadalajara"),
    TOLEDO("Toledo"),

    // Castilla y León
    AVILA("Ávila"),
    BURGOS("Burgos"),
    LEON("León"),
    PALENCIA("Palencia"),
    SALAMANCA("Salamanca"),
    SEGOVIA("Segovia"),
    SORIA("Soria"),
    VALLADOLID("Valladolid"),
    ZAMORA("Zamora"),

    // Cataluña
    BARCELONA("Barcelona"),
    GIRONA("Girona"),
    LLEIDA("Lleida"),
    TARRAGONA("Tarragona"),

    // Comunidad Valenciana
    ALICANTE("Alicante / Alacant"),
    CASTELLON("Castellón / Castelló"),
    VALENCIA("Valencia / València"),

    // Extremadura
    BADAJOZ("Badajoz"),
    CACERES("Cáceres"),

    // Galicia
    A_CORUNA("A Coruña"),
    LUGO("Lugo"),
    OURENSE("Ourense"),
    PONTEVEDRA("Pontevedra"),

    // La Rioja
    LA_RIOJA("La Rioja"),

    // Madrid
    MADRID("Madrid"),

    // Murcia
    MURCIA("Murcia"),

    // Navarra
    NAVARRA("Navarra"),

    // País Vasco
    ALAVA("Álava / Araba"),
    BIZKAIA("Bizkaia"),
    GIPUZKOA("Gipuzkoa"),

    // Ciudades autónomas
    CEUTA("Ceuta"),
    MELILLA("Melilla");

    private final String nombre;

    Provincia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
