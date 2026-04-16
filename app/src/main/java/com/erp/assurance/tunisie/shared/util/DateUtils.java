package com.erp.assurance.tunisie.shared.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private DateUtils() {}

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMAT) : null;
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMAT) : null;
    }

    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    public static boolean isExpired(LocalDate expiryDate) {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }
}
