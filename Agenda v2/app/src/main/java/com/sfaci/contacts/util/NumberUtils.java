package com.sfaci.contacts.util;

import java.text.DecimalFormat;

/**
 * Utilidades para números
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 */
public class NumberUtils {

    public static String toMoney(float amount) {

        DecimalFormat df = new DecimalFormat("#.00 €");
        return df.format(amount);
    }
}
