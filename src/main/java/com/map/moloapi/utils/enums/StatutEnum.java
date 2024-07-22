package com.map.moloapi.utils.enums;

/**
 * @author DIOMANDE Souleymane
 * @project molo-api
 * @Date 10/06/2024 17:33
 */
public enum StatutEnum {
    SUCCES("SUCCES", "Succes"),
    ECHEC("ECHEC", "Echec");

    private final String libelle;
    private final String description;

    StatutEnum(String libelle, String description) {
        this.libelle = libelle;
        this.description = description;
    }

    public static StatutEnum value(String label) {
        for (StatutEnum e : values()) {
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
