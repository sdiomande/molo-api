package com.map.moloapi.utils.enums;

/**
 * @author DIOMANDE Souleymane
 * @project socoprim-internal-api
 * @Date 10/06/2024 17:34
 */
public enum EtatEnum {
    ACTIF("ACTIF", "Badge actif"),
    BLACKLISTE("BLACKLISTE", ""),
    SUSPENDU("SUSPENDU", "");

    private final String libelle;
    private final String description;

    EtatEnum(String libelle, String description) {
        this.libelle = libelle;
        this.description = description;
    }

    public static EtatEnum value(String label) {
        for (EtatEnum e : values()) {
            if (e.getLibelle().equals(label)) {
                return e;
            }
        }
        return null;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDescription() {
        return description;
    }
}
