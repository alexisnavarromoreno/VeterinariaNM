package com.veterinaria.nm.dominio.modelo;

/**
 * Zonas anatómicas en las que se puede realizar un procedimiento clínico.
 * Granularidad suficiente para documentación clínica y trazabilidad legal.
 */
public enum ZonaAnatomica {

    // ── Cabeza ────────────────────────────────────────────────────────────────
    CABEZA("Cabeza (general)"),
    FRENTE_VERTEX("Frente / Vértex"),
    HOCICO_NARIZ("Hocico / Nariz"),
    OJO_DERECHO("Ojo derecho"),
    OJO_IZQUIERDO("Ojo izquierdo"),
    AMBOS_OJOS("Ambos ojos"),
    OIDO_DERECHO("Oído derecho"),
    OIDO_IZQUIERDO("Oído izquierdo"),
    AMBOS_OIDOS("Ambos oídos"),
    CAVIDAD_ORAL("Cavidad oral / Encías"),
    DIENTES("Dientes (odontología)"),
    FARINGE_GARGANTA("Faringe / Garganta"),

    // ── Cuello ────────────────────────────────────────────────────────────────
    CUELLO_VENTRAL("Cuello ventral"),
    CUELLO_DORSAL("Cuello dorsal"),
    CUELLO_LATERAL_DERECHO("Cuello lateral derecho"),
    CUELLO_LATERAL_IZQUIERDO("Cuello lateral izquierdo"),

    // ── Tórax ─────────────────────────────────────────────────────────────────
    TORAX_GENERAL("Tórax (general)"),
    TORAX_LATERAL_DERECHO("Tórax lateral derecho"),
    TORAX_LATERAL_IZQUIERDO("Tórax lateral izquierdo"),
    TORAX_VENTRAL("Tórax ventral / Esternón"),
    TORAX_DORSAL("Tórax dorsal / Columna torácica"),

    // ── Abdomen ───────────────────────────────────────────────────────────────
    ABDOMEN_GENERAL("Abdomen (general)"),
    ABDOMEN_CRANEAL("Abdomen craneal / Epigastrio"),
    ABDOMEN_MEDIO("Abdomen medio / Mesogastrio"),
    ABDOMEN_CAUDAL("Abdomen caudal / Hipogastrio"),
    FLANCO_DERECHO("Flanco derecho"),
    FLANCO_IZQUIERDO("Flanco izquierdo"),

    // ── Zona caudal ───────────────────────────────────────────────────────────
    ZONA_LUMBAR("Zona lumbar"),
    ZONA_SACRA("Zona sacra / Grupa"),
    REGION_PERIANAL("Región perianal"),
    PERINEO("Perineo"),
    COLA("Cola"),

    // ── Extremidades anteriores ───────────────────────────────────────────────
    HOMBRO_DERECHO("Hombro / Escápula derecha"),
    HOMBRO_IZQUIERDO("Hombro / Escápula izquierda"),
    BRAZO_DERECHO("Brazo derecho (húmero)"),
    BRAZO_IZQUIERDO("Brazo izquierdo (húmero)"),
    CODO_DERECHO("Codo derecho"),
    CODO_IZQUIERDO("Codo izquierdo"),
    ANTEBRAZO_DERECHO("Antebrazo derecho (radio-cúbito)"),
    ANTEBRAZO_IZQUIERDO("Antebrazo izquierdo (radio-cúbito)"),
    CARPO_DERECHO("Carpo derecho"),
    CARPO_IZQUIERDO("Carpo izquierdo"),
    GARRA_ANTERIOR_DERECHA("Garra / Mano anterior derecha"),
    GARRA_ANTERIOR_IZQUIERDA("Garra / Mano anterior izquierda"),

    // ── Extremidades posteriores ──────────────────────────────────────────────
    CADERA_DERECHA("Cadera derecha (articulación coxofemoral)"),
    CADERA_IZQUIERDA("Cadera izquierda (articulación coxofemoral)"),
    MUSLO_DERECHO("Muslo derecho (fémur)"),
    MUSLO_IZQUIERDO("Muslo izquierdo (fémur)"),
    RODILLA_DERECHA("Rodilla derecha (articulación femorotibial)"),
    RODILLA_IZQUIERDA("Rodilla izquierda (articulación femorotibial)"),
    TIBIA_PERONE_DERECHO("Tibia-Peroné derecho"),
    TIBIA_PERONE_IZQUIERDO("Tibia-Peroné izquierdo"),
    TARSO_DERECHO("Tarso derecho (corvejón)"),
    TARSO_IZQUIERDO("Tarso izquierdo (corvejón)"),
    GARRA_POSTERIOR_DERECHA("Garra / Pata posterior derecha"),
    GARRA_POSTERIOR_IZQUIERDA("Garra / Pata posterior izquierda"),

    // ── Piel y superficie ─────────────────────────────────────────────────────
    PIEL_DORSAL("Piel dorsal / Lomo"),
    PIEL_VENTRAL("Piel ventral / Vientre"),
    PIEL_LATERAL_DERECHO("Piel lateral derecha"),
    PIEL_LATERAL_IZQUIERDO("Piel lateral izquierda"),
    SUPERFICIE_CORPORAL_GENERAL("Superficie corporal general"),

    // ── Vías sistémicas ───────────────────────────────────────────────────────
    VIA_INTRAVENOSA("Vía intravenosa (IV)"),
    VIA_SUBCUTANEA("Vía subcutánea (SC)"),
    VIA_INTRAMUSCULAR("Vía intramuscular (IM)"),
    VIA_INTRAPERITONEAL("Vía intraperitoneal (IP)"),

    OTRA("Otra zona anatómica");

    private final String descripcion;

    ZonaAnatomica(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
