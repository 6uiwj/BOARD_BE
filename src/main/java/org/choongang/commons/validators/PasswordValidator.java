package org.choongang.commons.validators;

import java.util.regex.Pattern;

public interface PasswordValidator {

    default boolean alphaCheck(String password, boolean caseInsensitive) {

        if (caseInsensitive) { // 대소문자 구분 X
            Pattern pattern = Pattern.compile("[a-z]+", Pattern.CASE_INSENSITIVE);
            return pattern.matcher(password).find();
        }


        Pattern pattern1 = Pattern.compile("[a-z]+");
        Pattern pattern2 = Pattern.compile("[A-Z]+");

        return pattern1.matcher(password).find() && pattern2.matcher(password).find();

    }

    default boolean numberCheck(String password) {
        Pattern pattern = Pattern.compile("\\d+");

        return pattern.matcher(password).find();
    }

    default boolean specialCharsCheck(String password) {
        Pattern pattern = Pattern.compile("[`~!@#$%^&*()\\-_+={}]+");

        return pattern.matcher(password).find();
    }
}
