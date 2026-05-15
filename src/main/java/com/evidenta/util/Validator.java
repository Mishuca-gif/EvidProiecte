package com.evidenta.util;

import com.evidenta.exception.ValidationException;

public class Validator {

    public static void notEmpty(String value, String field) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("Campul '" + field + "' nu poate fi gol!");
        }
    }

    public static void validEmail(String email) throws ValidationException {
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("Email invalid: " + email);
        }
    }

    public static void pozitiv(int value, String field) throws ValidationException {
        if (value <= 0) {
            throw new ValidationException("Campul '" + field + "' trebuie sa fie pozitiv!");
        }
    }

    public static void validData(String data, String field) throws ValidationException {
        if (!data.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new ValidationException("Data '" + field + "' trebuie in formatul YYYY-MM-DD!");
        }
    }
}