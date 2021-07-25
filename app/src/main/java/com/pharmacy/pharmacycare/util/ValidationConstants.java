package com.pharmacy.pharmacycare.util;

/**
 * Created by mohammed on 7/4/15.
 */
public interface ValidationConstants {
    //    String PASSWORD_PATTERN = "^(?=.*[\\w])(?=.*[\\d])(?=\\S+$).{6,12}$";
//    String PASSWORD_PATTERN1 = "^(?=.*[\\w])(?=\\S+$).{6,12}$";
//
//    String PASSWORD_PATTERN2 = "^(?=.*[\\w]).{6,12}$";
    String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    //^[\$ a-zA-Z0-9](?=.*\d)(?=.*[A-Za-z])[\$ \w]{8,4096}$

    //  String PASSWORD_PATTERN = "^(?=.*[a-zA-Z\\u0600-\\u065F\\u066A-\\u06EF\\u06FA-\\u06FF])(?=.*[0-9\\u0660-\\u0669\\u06F0-\\u06F9])(?=\\S+$).{6,12}$";
    String PASSWORD_PATTERN_ARABIC = "^(?=.*[\\u0600-\\u065F\\u066A-\\u06EF\\u06FA-\\u06FF])(?=.*[\\u0660-\\u0669\\u06F0-\\u06F9])(?=\\S+$).{6,12}$";

    String EMAIL_PATTERN = "^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z]+(\\.[a-zA-Z]{2,4}+)*(\\.[a-zA-Z]{2,4})$";
    String FULL_NAME_PATTERN = "^[a-z0-9_-]{3,25}$";
    //    String UAE_MOBILE_NUMBER = "^(?:\\+971|00971|0)?(?:50|51|52|55|56|2|3|4|6|7|9)\\d{7}$";
    //  String UAE_MOBILE_NUMBER = "^(?:\\+971|00971|0)?(?:50|54|52|55|56|58)\\d{7}$";
    String UAE_MOBILE_NUMBER = "^05\\d{8}$";
    //    50-52-54-55-58-56
    String VERIFICATION_CODE_REGEX = "^[0-9]{4}$";
    String EGYPT_MOBILE_NUMBER = "^201\\d{9}$";


//
//    ^                 # start-of-string
//            (?=.*[0-9])       # a digit must occur at least once
//            (?=.*[a-z])       # a lower case letter must occur at least once
//            (?=.*[A-Z])       # an upper case letter must occur at least once
//            (?=.*[@#$%^&+=])  # a special character must occur at least once
//            (?=\S+$)          # no whitespace allowed in the entire string
//            .{8,}             # anything, at least eight places though
//    $                 # end-of-string

//    /[\d\w]{6,12}/
}
