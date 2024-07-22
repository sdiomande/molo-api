package com.map.moloapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.map.moloapi.entities.Param;
import com.map.moloapi.repositories.ParamRepository;
import com.map.moloapi.securities.UserDetailsImpl;
import com.map.moloapi.utils.constants.TechnicalMessage;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.awt.datatransfer.*;
import java.awt.Toolkit;
import java.awt.HeadlessException;
import java.awt.datatransfer.StringSelection;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.map.moloapi.utils.constants.TechnicalMessage.*;

/**
 * @author DIOMANDE Souleymane
 * @Date 08/10/2022 13:29
 */
@Component
@Slf4j
public class Utilities {
    @Autowired
    ParamRepository paramRepository;

    public static String SYSTEM = "SYSTEM";

    public String getBearerToken(String parameter) {
        return TechnicalMessage.BEARER.concat(" ").concat(getParam(parameter));
//        return "";
    }

    public String getParam(String parameter) {
        Param param = paramRepository.findByName(parameter);
        if (param != null && param.getValeur() != null) {
            return param.getValeur();
        }
        log.error("-- AUCUNE DONNEE DE PARAMETRE POUR : {} --", parameter);
        return null;
    }

    public String getParam(String parameter, String defaultValue) {
        Param param = paramRepository.findByName(parameter);
        if (param != null && param.getValeur() != null) {
            return param.getValeur();
        }
        return defaultValue;
    }


    public static String now() {
        return Instant.now().toString();
    }

    public static String userConnectedID() {
        return userConnected().getId();
    }

    public static String userConnectedLogin() {
        return userConnected().getLogin();
    }

    public static UserDetailsImpl userConnected() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Authentication authenticationConnected() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static List<String> userConnectedRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(Object::toString).collect(Collectors.toList());
    }

    public static String formatIsoDate(String outputFormat, String date) throws ParseException {
        return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(date));
    }

    public static String formatDate(String outputFormat, Date date) throws ParseException {
        return new SimpleDateFormat(outputFormat).format(date);
    }

    public static String formatDate(String outputFormat) throws ParseException {
        return new SimpleDateFormat(outputFormat).format(new Date());
    }

    public static String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(PASSWORD_PREFIX.concat(password).concat(PASSWORD_SUFFIX));
    }

    public static String arroundPassword(String password) {
        return PASSWORD_PREFIX.concat(password).concat(PASSWORD_SUFFIX);
    }

    public static boolean match(String rawPassword, String encryptedPassword) {
        return new BCryptPasswordEncoder().matches(arroundPassword(rawPassword), encryptedPassword);
    }

    public static String generateDigitPassword() {
        return generateDigitPassword(PASSWORD_GENERATED_LENGTH);
    }

    public static String generateDigitPassword(int length) {
        return new PasswordGenerator().generatePassword(length, Arrays.asList(
//                new CharacterRule(EnglishCharacterData.UpperCase, 0),
//                new CharacterRule(EnglishCharacterData.LowerCase, 0),
                new CharacterRule(EnglishCharacterData.Digit, 6)));
    }

    public static String generateSecurePassword() {
        Stream<Character> demoPassword = Stream.concat(getRandomNumbers(2),
                Stream.concat(getRandomSpecialChars(2),
                        Stream.concat(getRandomAlphabets(2, true), getRandomAlphabets(2, false))));
        List<Character> listOfChar = demoPassword.collect(Collectors.toList());
        Collections.shuffle(listOfChar);
        String password = listOfChar.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return password;
    }
    public static Stream<Character> getRandomSpecialChars(int length) {
        Stream<Character> specialCharsStream;
        Random random = new SecureRandom();
        IntStream specialChars = random.ints(length, 33, 45);
        specialCharsStream =  specialChars.mapToObj(data -> (char) data);
        return specialCharsStream;
    }
    public static Stream<Character> getRandomNumbers(int length) {
        Stream<Character> numberStream;
        Random random = new SecureRandom();
        IntStream numberIntStream = random.ints(length, 48, 57);
        numberStream = numberIntStream.mapToObj(data -> (char) data);
        return numberStream;
    }
    public static Stream<Character> getRandomAlphabets(int length, boolean check) {
        Stream<Character> lowerUpperStream;
        if(check) {
            Random random = new SecureRandom();
            IntStream lCaseStream = random.ints(length, 'a', 'z');
            lowerUpperStream =  lCaseStream.mapToObj(data -> (char) data);
        } else {
            Random random = new SecureRandom();
            IntStream uCaseStream = random.ints(length, 'A', 'Z');
            lowerUpperStream =  uCaseStream.mapToObj(data -> (char) data);
        }
        return lowerUpperStream;
    }

    public static Long timestamp() {
        return System.currentTimeMillis();
    }

    public static String valueToString(Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> monthRange() {
        return monthRange(0);
    }

    public static Map<String, String> monthRange(int previous) {
        Map<String, String> map = new HashMap<>();
        map.put("start", LocalDate.now().withDayOfMonth(1).minusMonths(previous).toString().concat(TZ_START_SUFFIX));
        map.put("end", LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).toString().concat(TZ_END_SUFFIX));
        return map;
    }

    public static String bourrageGauche(String chaine, int longueur, String bourrage) {
        if (chaine == null) {
            chaine = "";
        }
        chaine = chaine.trim();
        if (chaine.length() > longueur) {
            chaine = chaine.substring(chaine.length() - longueur, chaine.length());
        }
        while (chaine.length() < longueur) {
            chaine = bourrage + chaine;
        }
        return chaine;
    }

    public static String bourrageDroite(String chaine, int longueur, String bourrage) {
        if (chaine == null) {
            chaine = "";
        }
        chaine = chaine.trim();
        if (chaine.length() > longueur) {
            chaine = chaine.substring(0, longueur);

        }
        while (chaine.length() < longueur) {
            chaine += bourrage;
        }
        return chaine;
    }


    public static void fillNullObject(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.get(object) != null) {
                    continue;
                } else if (field.getType().equals(Integer.class)) {
                    field.set(object, 0);
                } else if (field.getType().equals(String.class)) {
                    field.set(object, "");
                } else if (field.getType().equals(Boolean.class)) {
                    field.set(object, false);
                } else if (field.getType().equals(Character.class)) {
                    field.set(object, '\u0000');
                } else if (field.getType().equals(Byte.class)) {
                    field.set(object, (byte) 0);
                } else if (field.getType().equals(Float.class)) {
                    field.set(object, 0.0f);
                } else if (field.getType().equals(Double.class)) {
                    field.set(object, 0.0d);
                } else if (field.getType().equals(Short.class)) {
                    field.set(object, (short) 0);
                } else if (field.getType().equals(Long.class)) {
                    field.set(object, 0L);
                } else if (field.getType().getDeclaredFields().length > 0) {
                    for (Constructor<?> constructor : field.getClass().getConstructors()) {
                        if (constructor.getParameterTypes().length == 0) {
                            field.set(object, constructor.newInstance());
                            fillNullObject(field.get(object));
                        }
                    }
                }
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static long minutes(Long start) {
        return TimeUnit.MINUTES.convert(
                start - timestamp(), TimeUnit.MILLISECONDS);
    }

    public String nextInternalId() {
        Param param = paramRepository.findByName("INTERNAL_ID");
        Long compteur;
        if (param != null) {
            compteur = Long.parseLong(param.getValeur()) + 1;
            synchronized (param) {
                param.setValeur(compteur.toString());
                paramRepository.save(param);
            }
            return String.valueOf(compteur);
        }
        return "000000";
    }

    public String generateGrantToken(String partnerCode) {
        Param param = paramRepository.findByName("TOKEN_PREFIX");
        if (param != null) {
            return new BCryptPasswordEncoder().encode(param.getValeur().concat(partnerCode).concat(now()));
        }
        return null;
    }

    public static Date toDate(String date, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(date);
    }

    public static Date toDate(String date) throws ParseException {
        return toDate(date, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    public static boolean after(String begin, String end) {
        try {
            return toDate(begin).after(toDate(end));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean after(String begin) {
        try {
            return toDate(begin).after(toDate(now()));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String add(int field, int amount){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(field, amount);
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(calendar.getTime());
    }

    public static String addDays(int amount){
        return add(Calendar.DAY_OF_WEEK, amount);
    }
}



