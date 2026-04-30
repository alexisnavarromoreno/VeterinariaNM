package com.veterinaria.nm.dominio.modelo;

/**
 * Value Object que representa una predisposición genética conocida de una raza.
 * <p>
 * Se modela como Record porque no tiene identidad propia: dos predisposiciones
 * con la misma condición y descripción son equivalentes.
 * Se asocia a {@link Raza} para que el sistema alerte al veterinario cuando
 * trate un paciente de esa raza.
 * </p>
 *
 * @param condicion     Tipo de condición hereditaria con la que se relaciona
 * @param descripcion   Descripción adicional específica de la raza
 *                      Ej: "Alta incidencia en Bulldogs por conformación anatómica"
 * @param recomendacion Acción preventiva o revisión sugerida
 *                      Ej: "Radiografía de cadera anual a partir de los 18 meses"
 */
public record PredisposicionGenetica(
        CondicionCronica.TipoCondicion condicion,
        String descripcion,
        String recomendacion
) {
    public PredisposicionGenetica {
        if (condicion == null) throw new IllegalArgumentException("La condición es obligatoria");
    }

    /** Constructor simplificado para predisposiciones sin recomendación específica. */
    public static PredisposicionGenetica de(CondicionCronica.TipoCondicion condicion,
                                             String descripcion) {
        return new PredisposicionGenetica(condicion, descripcion, null);
    }
}
