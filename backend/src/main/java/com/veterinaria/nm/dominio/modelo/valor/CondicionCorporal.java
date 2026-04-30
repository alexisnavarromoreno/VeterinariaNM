package com.veterinaria.nm.dominio.modelo.valor;

/**
 * Escala BCS (Body Condition Score) para mamíferos.
 * <p>
 * Estándar de la WSAVA (World Small Animal Veterinary Association) de 9 puntos
 * utilizado en perros y gatos. Para otras especies existen escalas equivalentes.
 * Se registra en cada consulta como parte de los signos vitales para monitorizar
 * la evolución del peso y la composición corporal a lo largo del tiempo.
 * </p>
 */
public enum CondicionCorporal {

    BCS_1("1/9", "Caquéxico", "Costillas, columna y huesos pélvicos muy prominentes. Sin grasa visible."),
    BCS_2("2/9", "Muy delgado", "Costillas fácilmente palpables con mínima cobertura grasa."),
    BCS_3("3/9", "Delgado", "Costillas palpables sin exceso de grasa. Cintura visible."),
    BCS_4("4/9", "Ligeramente bajo peso", "Costillas palpables con ligera cobertura. Cintura evidente."),
    BCS_5("5/9", "Ideal", "Costillas palpables con cobertura grasa mínima. Cintura y abdomen proporcionados."),
    BCS_6("6/9", "Ligeramente sobrepeso", "Costillas con leve exceso de grasa. Cintura menos evidente."),
    BCS_7("7/9", "Sobrepeso", "Costillas difíciles de palpar. Sin cintura visible. Abdomen redondeado."),
    BCS_8("8/9", "Obeso", "Costillas muy difíciles de palpar. Abundante grasa. Abdomen prominente."),
    BCS_9("9/9", "Obeso mórbido", "Depósitos grasos masivos. Movilidad muy reducida.");

    private final String puntuacion;
    private final String etiqueta;
    private final String descripcion;

    CondicionCorporal(String puntuacion, String etiqueta, String descripcion) {
        this.puntuacion = puntuacion;
        this.etiqueta = etiqueta;
        this.descripcion = descripcion;
    }

    /** Indica si el animal necesita plan de pérdida de peso. */
    public boolean requierePerdidaPeso() {
        return this == BCS_7 || this == BCS_8 || this == BCS_9;
    }

    /** Indica si el animal necesita plan de ganancia de peso. */
    public boolean requiereGananciaPeso() {
        return this == BCS_1 || this == BCS_2 || this == BCS_3;
    }

    public boolean esIdeal() {
        return this == BCS_4 || this == BCS_5 || this == BCS_6;
    }

    public String getPuntuacion() { return puntuacion; }
    public String getEtiqueta() { return etiqueta; }
    public String getDescripcion() { return descripcion; }
}
