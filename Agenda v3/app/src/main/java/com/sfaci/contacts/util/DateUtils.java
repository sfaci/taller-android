package com.sfaci.contacts.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utilidades de fecha
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 */
public class DateUtils {

    private static String DATE_FORMAT = "dd/mm/yyyy";
    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    public static Date toDate(String date) throws ParseException {
        return DATE_FORMATTER.parse(date);
    }

    public static String toString(Date date) {
        return DATE_FORMATTER.format(date);
    }
}
