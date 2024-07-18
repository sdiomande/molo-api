package com.map.moloapi.utils.constants;

/**
 * @author DIOMANDE Souleymane
 * @Date 04/10/2022 20:09
 */
public class FunctionalMessage {
    public static final String NOT_EXIST_MESSAGE = "%s ayant l'id = %s n'existe pas";
    public static final String NOT_FOUND_MESSAGE = "Aucun element trouvé, %s : (%s)";
    public static final String REFERENCE_NOT_EXIST_MESSAGE = "%s ayant la reference = %s n'existe pas";

    //OTP
    public static final String OTP_EXPIRED = "Code OTP expiré, veuillez généré un nouveau code";
    public static final String OTP_NOT_FOUND = "Désolé, le code OTP est erronée";
    // AUTH
    public static final String PASSWORD_NOT_MATCHING = "Les nouveaux mots de passe ne sont pas identique";
    public static final String OLD_PASSWORD_WRONG = "Le mot de passe n'est pas correct";
    public static final String ACCOUNT_NOT_FOUND = "Aucun compte trouve pour le %s";
    public static final String ROLE_NOT_FOUND = "Aucun role trouvé";
    public static final String FILE_NOT_CSV = "Le fichier uploadé n'est pas au format attendu";
    public static final String TOKEN_EXPIRY = "Votre demande de réinitialisation de mot de passe n'est pas valide.";
}
