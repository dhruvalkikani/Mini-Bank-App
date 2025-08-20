package com.hdfcbank.util;

public class Validator {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PHONE_REGEX = "^[6-9]\\d{9}$";

    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public static boolean isValidPhone(String phone) {
        return phone.matches(PHONE_REGEX);
    }
}
