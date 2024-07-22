package com.map.moloapi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * @author DIOMANDE Souleymane
 * @project molo-api
 * @Date 24/04/2024 09:48
 */
public class DateComparator implements Comparator<String> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public int compare(String date1, String date2) {
        try {
            // Convertir les chaînes de date en objets Date
            Date dateObj1 = dateFormat.parse(date1);
            Date dateObj2 = dateFormat.parse(date2);
            // Comparer les objets Date
            return dateObj1.compareTo(dateObj2);
        } catch (ParseException e) {
            // Gérer l'exception de parse si les chaînes de date ne sont pas au bon format
            e.printStackTrace();
            return 0; // ou une autre valeur de retour appropriée
        }
    }
}
