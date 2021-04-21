package com.example.koohestantest1.Utils;

import android.content.Context;
import android.widget.Toast;

public class ValidationCheck {
    public static boolean postalCodeValidation(String postalCode) {
        final String reg = "\\b(?!(\\d)\\1{3})[13-9]{4}[1346-9][013-9]{5}\\b";
        if (postalCode.matches(reg) && postalCode.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean mobileValidation(String mobile) {

        final String reg = "09(1[0-9]|3[1-9]|2[1-9])-?[0-9]{3}-?[0-9]{4}";
        if (mobile.matches(reg) && mobile.length() > 0) {
            return true;
        } else {
            return false;
        }


    }

    public static boolean emailValidation(String email) {

        final String reg = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        if (email.matches(reg) && email.length() > 0) {
            return true;
        } else {
            return false;
        }


    }

    public static boolean phoneValidation(String phone) {
        final String reg = "^0\\d{2,3}\\d{7}$";

        if (phone.matches(reg) && phone.length() > 0) {
            return true;
        } else {
            return false;
        }


    }

    public static boolean validateMelliCode(String melliCode, Context context) {


        if (melliCode.trim().isEmpty()) {
            Toast.makeText(context, "Melli Code is empty", Toast.LENGTH_LONG).show();
            return false; // Melli Code is empty
        } else if (melliCode.length() != 10) {
            Toast.makeText(context, "Melli Code must be exactly 10 digits", Toast.LENGTH_LONG).show();
            return false; // Melli Code is less or more than 10 digits
        } else {
            int sum = 0;

            for (int i = 0; i < 9; i++) {
                sum += Character.getNumericValue(melliCode.charAt(i)) * (10 - i);
            }

            int lastDigit;
            int divideRemaining = sum % 11;

            if (divideRemaining < 2) {
                lastDigit = divideRemaining;
            } else {
                lastDigit = 11 - (divideRemaining);
            }

            if (Character.getNumericValue(melliCode.charAt(9)) == lastDigit) {
                Toast.makeText(context, "MelliCode is valid", Toast.LENGTH_LONG).show();
                return true;
            } else {
                Toast.makeText(context, "MelliCode is not valid", Toast.LENGTH_LONG).show();
                return false; // Invalid MelliCode
            }
        }
    }



}
