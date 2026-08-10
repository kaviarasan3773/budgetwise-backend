package com.kaviarasan.budgetwise.util;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtil {

    private DateUtil() {
    }

    public static String formatMonth(String monthKey) {

        if (monthKey == null || monthKey.isEmpty()) {
            return "";
        }

        String[] parts = monthKey.split("-");

        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        String monthName =
                Month.of(month)
                        .getDisplayName(
                                TextStyle.FULL,
                                Locale.ENGLISH);

        return monthName + "-" + year;
    }
}