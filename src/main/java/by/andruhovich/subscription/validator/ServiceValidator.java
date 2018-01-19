package by.andruhovich.subscription.validator;

import java.util.regex.Pattern;

public class ServiceValidator {
    private static final String REGEX_POSTAL_INDEX_ = "\\d{6}";
    private static final String REGEX_LOGIN = "\\b[a-zA-Z][a-zA-Z0-9\\-._]{3,30}\\b";
    private static final String REGEX_MONEY_VALUE = "^[\\d]+?\\.[\\d]{2}$";

    public static boolean verifyPostalIndex(String postalIndex) {
        return Pattern.matches(REGEX_POSTAL_INDEX_, postalIndex);
    }

    public static boolean verifyLogin(String login) {
        return Pattern.matches(REGEX_LOGIN, login);
    }

    public static boolean verifyMoneyValue(String value) {
        return Pattern.matches(REGEX_MONEY_VALUE, value);
    }
}
