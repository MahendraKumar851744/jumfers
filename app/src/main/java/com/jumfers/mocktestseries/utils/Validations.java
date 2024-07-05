package com.jumfers.mocktestseries.utils;

import java.util.regex.Pattern;

public class Validations {
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    public boolean isValidMobileNumber(String mobileNumber) {
        String mobileRegex = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(mobileRegex);
        return pattern.matcher(mobileNumber).matches();
    }

}
