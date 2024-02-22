package com.example.watch_selling.filters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class CustomFilters {
    public static Boolean nullValidation(Object obj) {
        return obj.equals(null);
    }

    public static Boolean idValidation(UUID id) {
        return !id.equals(null);
    }

    public static Boolean emailValidation(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }

    public static Boolean nameValidation(String name) {
        return !(name.isEmpty() || name.isBlank());
    }

    public static Boolean addressValidation(String address) {
        return !(address.isEmpty() || address.isBlank());
    }

    public static Boolean phoneNumberValidation(String phoneNumber) {
        return phoneNumber.matches("[0-9]{10}");
    }

    public static Boolean dateValidation(String dateString) {
        // Check if the format is YYYY-MM-DD
        if (!dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }

        // Attempt to parse the date string using DateTimeFormatter
        try {
            LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
