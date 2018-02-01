package by.andruhovich.subscription.validator;

import java.util.regex.Pattern;

public class ServiceValidator {
    private static final String REGEX_POSTAL_INDEX_ = "[0-9]{6}";
    private static final String REGEX_LOGIN = "^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){6,18}[a-zA-Z0-9]$";
    private static final String REGEX_PASSWORD = "(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{3,15})$";
    private static final String REGEX_PRICE = "^[\\d]+?\\.[\\d]{2}$";
    private static final String REGEX_DATE = "((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";

    public static boolean verifyPostalIndex(String postalIndex) {
        return postalIndex != null && Pattern.matches(REGEX_POSTAL_INDEX_, postalIndex);
    }

    public static boolean verifyLogin(String login) {
        return login != null && Pattern.matches(REGEX_LOGIN, login);
    }

    public static boolean verifyPrice(String price) {
        return price != null && Pattern.matches(REGEX_PRICE, price);
    }

    public static boolean verifyPassword(String password) {
        return password != null && Pattern.matches(REGEX_PASSWORD, password);
    }

    public static boolean confirmPassword(String password, String duplicatedPassword) {
        return password != null && password.equals(duplicatedPassword);
    }

    public static boolean verifyDate(String date) {
        return date != null && Pattern.matches(REGEX_DATE, date);
    }
}

