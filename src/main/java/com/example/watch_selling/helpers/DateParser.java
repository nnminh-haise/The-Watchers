package com.example.watch_selling.helpers;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class DateParser {
    public static Optional<LocalDate> parse(String date) {
        if (date == null) {
            return Optional.empty();
        }

        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", Locale.ENGLISH),
                DateTimeFormatter.ISO_DATE_TIME,
                DateTimeFormatter.ISO_DATE,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME // Add ISO_OFFSET_DATE_TIME
        );

        for (var formatter : formatters) {
            try {
                LocalDate validDate;
                if (formatter.equals(DateTimeFormatter.ISO_DATE)) {
                    validDate = LocalDate.parse(date, formatter);
                } else {
                    // Parse as OffsetDateTime first and then convert to LocalDate
                    OffsetDateTime offsetDateTime = OffsetDateTime.parse(date, formatter);
                    validDate = offsetDateTime.toLocalDate();
                }
                System.out.println("[Date parser] Parse success!");
                return Optional.of(validDate.plusDays(1));
            } catch (Exception e) {
                // Log the exception and continue to try the next formatter
                System.out.println("[Date parser] Exception with formatter: " + e.getMessage());
            }
        }

        return Optional.empty();
    }
}
