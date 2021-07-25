package com.pharmacy.pharmacycare.util;

/**
 * Created by mohammed on 7/4/15.
 */
public class Validation {
    public static Validation getInstance(){
        return new Validation();
    }
    public boolean validateStringAccordingToPattern(String textToValidate, String pattern){
        return textToValidate.matches(pattern);
    }
}
