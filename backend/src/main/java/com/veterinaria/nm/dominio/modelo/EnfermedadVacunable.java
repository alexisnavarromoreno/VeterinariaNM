package com.veterinaria.nm.dominio.modelo;

/**
 * Enfermedades frente a las que existe vacuna en medicina veterinaria.
 * Conjunto medianamente cerrado: nuevas vacunas aparecen ocasionalmente
 * pero el catálogo es estable a corto/medio plazo.
 */
public enum EnfermedadVacunable {

    // ── Caninas ───────────────────────────────────────────────────────────────
    MOQUILLO_CANINO("Moquillo Canino (CDV)"),
    HEPATITIS_INFECCIOSA_CANINA("Hepatitis Infecciosa Canina (CAV-1/CAV-2)"),
    PARVOVIRUS_CANINO("Parvovirus Canino (CPV-2)"),
    PARAINFLUENZA_CANINA("Parainfluenza Canina (CPI)"),
    LEPTOSPIROSIS("Leptospirosis (L. canicola, L. icterohaemorrhagiae)"),
    RABIA("Rabia (Lyssavirus)"),
    BORDETELLA_BRONCHISEPTICA("Tos de las perreras (Bordetella bronchiseptica)"),
    LEISHMANIOSIS("Leishmaniosis canina (Leishmania infantum)"),
    CORONAVIRUS_CANINO("Coronavirus Entérico Canino (CCoV)"),
    BORRELIOSIS("Borreliosis de Lyme (Borrelia burgdorferi)"),
    ADENOVIRUS_CANINO_2("Adenovirus Canino Tipo 2 (CAV-2)"),

    // ── Felinas ───────────────────────────────────────────────────────────────
    RINOTRAQUEITIS_VIRAL_FELINA("Rinotraqueítis Viral Felina (FHV-1)"),
    CALICIVIRUS_FELINO("Calicivirus Felino (FCV)"),
    PANLEUCOPENIA_FELINA("Panleucopenia Felina / Parvo Felino (FPV)"),
    LEUCEMIA_FELINA("Leucemia Felina (FeLV)"),
    PERITONITIS_INFECCIOSA_FELINA("Peritonitis Infecciosa Felina (FIP / FCoV)"),
    CLAMIDIOSIS_FELINA("Clamidiosis Felina (Chlamydophila felis)"),
    INMUNODEFICIENCIA_FELINA("Inmunodeficiencia Felina (FIV)"),

    // ── Lagomorfos (conejos y liebres) ────────────────────────────────────────
    ENFERMEDAD_HEMORRAGICA_VIRAL_1("Enfermedad Hemorrágica Viral Tipo 1 (RHDV-1)"),
    ENFERMEDAD_HEMORRAGICA_VIRAL_2("Enfermedad Hemorrágica Viral Tipo 2 (RHDV-2)"),
    MIXOMATOSIS("Mixomatosis"),

    // ── Hurones ───────────────────────────────────────────────────────────────
    MOQUILLO_HURON("Moquillo en Hurón (CDV adaptado)"),
    RABIA_HURON("Rabia en Hurón"),

    // ── Équidos ───────────────────────────────────────────────────────────────
    GRIPE_EQUINA("Gripe Equina (EIV H3N8 / H7N7)"),
    TETANOS("Tétanos (Clostridium tetani)"),
    HERPESVIRUS_EQUINO_1("Herpesvirus Equino Tipo 1 (EHV-1) — aborto / mielitis"),
    HERPESVIRUS_EQUINO_4("Herpesvirus Equino Tipo 4 (EHV-4) — respiratorio"),
    ARTERITIS_VIRAL_EQUINA("Arteritis Viral Equina (EAV)"),
    ENCEFALOMIELITIS_EQUINA_OESTE("Encefalomielitis Equina del Oeste (WEE)"),
    ENCEFALOMIELITIS_EQUINA_ESTE("Encefalomielitis Equina del Este (EEE)"),
    ENCEFALOMIELITIS_VENEZOLANA("Encefalomielitis Venezolana Equina (VEE)"),
    WEST_NILE("Fiebre del Nilo Occidental (West Nile Virus)"),
    ROTAVIRUS_EQUINO("Rotavirus Equino"),

    // ── Aves ──────────────────────────────────────────────────────────────────
    ENFERMEDAD_NEWCASTLE("Enfermedad de Newcastle (NDV)"),
    GRIPE_AVIAR("Influenza Aviar (H5N1 / H5N8)"),
    MAREK("Enfermedad de Marek (MDV)"),
    VIRUELA_AVIAR("Viruela Aviar (FPV)"),
    BRONQUITIS_INFECCIOSA_AVIAR("Bronquitis Infecciosa Aviar (IBV)"),
    LARINGOTRAQUEITIS_AVIAR("Laringotraqueítis Infecciosa Aviar (ILTV)"),
    GUMBORO("Enfermedad de Gumboro / Bursitis Infecciosa (IBD)"),

    // ── Rumiantes y ganadería ─────────────────────────────────────────────────
    BRUCELOSIS("Brucelosis (Brucella spp.)"),
    FIEBRE_Q("Fiebre Q (Coxiella burnetii)"),
    LENGUA_AZUL("Lengua Azul / Bluetongue (BTV)"),
    RINOTRAQUEITIS_INFECCIOSA_BOVINA("Rinotraqueítis Infecciosa Bovina (IBR / BHV-1)"),
    DIARREA_VIRAL_BOVINA("Diarrea Viral Bovina (BVD)"),
    PARAINFLUENZA_BOVINA_3("Parainfluenza Bovina Tipo 3 (BPI-3)"),
    FIEBRE_CATARRAL_OVINA("Fiebre Catarral Ovina"),

    // ── Porcino ───────────────────────────────────────────────────────────────
    PARVOVIRUS_PORCINO("Parvovirus Porcino (PPV)"),
    ERISIPELA_PORCINA("Erisipela Porcina (Erysipelothrix rhusiopathiae)"),
    CIRCOVIRUS_PORCINO_2("Circovirus Porcino Tipo 2 (PCV-2)"),
    PRRS("Síndrome Respiratorio y Reproductivo Porcino (PRRS)"),
    MYCOPLASMA_HYOPNEUMONIAE("Neumonía Enzoótica Porcina (Mycoplasma hyopneumoniae)"),

    OTRA("Otra enfermedad");

    private final String descripcion;

    EnfermedadVacunable(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
