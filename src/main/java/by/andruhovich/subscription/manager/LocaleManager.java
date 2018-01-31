package by.andruhovich.subscription.manager;

import by.andruhovich.subscription.exception.MissingResourceTechnicalException;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LocaleManager {
    private static ResourceBundle resourceBundle;
    private static final String MESSAGES = "locale/locale";

    public LocaleManager(Locale locale) {
        try {
            resourceBundle = ResourceBundle.getBundle(MESSAGES, locale);
        } catch (MissingResourceException e) {
            throw new RuntimeException("There is no locale file", e);
        }
    }

    public String getProperty(String key) throws MissingResourceTechnicalException {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            throw new MissingResourceTechnicalException("Resource " + key + " is not found in source" + MESSAGES, e);
        }

    }
}
