package com.erp.assurance.tunisie.shared.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyUtils {

    public static final String TND = "TND";
    public static final BigDecimal TVA_RATE = new BigDecimal("0.19"); // Tunisian TVA 19%
    private static final Locale TUNISIA_LOCALE = new Locale("fr", "TN");

    private CurrencyUtils() {}

    public static BigDecimal round(BigDecimal amount) {
        return amount.setScale(3, RoundingMode.HALF_UP); // TND has 3 decimal places
    }

    public static BigDecimal calculateTVA(BigDecimal amount) {
        return round(amount.multiply(TVA_RATE));
    }

    public static BigDecimal addTVA(BigDecimal amount) {
        return round(amount.add(calculateTVA(amount)));
    }

    public static String format(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(TUNISIA_LOCALE);
        return formatter.format(amount);
    }
}
