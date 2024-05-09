package com.example.watch_selling.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class DateParser {
    public static Optional<LocalDate> parse(String date) {
        if (date == null) {
            return Optional.empty();
        }

        List<DateTimeFormatter> formaters = List.of(
                DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", Locale.ENGLISH),
                DateTimeFormatter.ISO_DATE_TIME,
                DateTimeFormatter.ISO_DATE);
        for (var format : formaters) {
            try {
                LocalDate validDate = LocalDate.parse(date, format);
                return Optional.of(validDate);
            } catch (Exception e) {
                // TODO: create a logger system for this
                System.out.println("[Date parser] Exception: " + e.getMessage());
            }
        }

        return Optional.empty();
    }
}
