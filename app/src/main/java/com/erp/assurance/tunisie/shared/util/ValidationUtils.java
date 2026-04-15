package com.erp.assurance.tunisie.shared.util;

import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern CIN_PATTERN = Pattern.compile("^\\d{8}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+216)?[0-9]{8}$");
    private static final Pattern IBAN_TN_PATTERN = Pattern.compile("^TN\\d{22}$");

    private ValidationUtils() {}

    public static boolean isValidCIN(String cin) {
        return cin != null && CIN_PATTERN.matcher(cin).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidTunisianPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.replaceAll("\\s", "")).matches();
    }

    public static boolean isValidTunisianIBAN(String iban) {
        return iban != null && IBAN_TN_PATTERN.matcher(iban.replaceAll("\\s", "")).matches();
    }
}
