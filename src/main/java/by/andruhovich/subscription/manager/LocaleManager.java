package by.andruhovich.subscription.manager;

import by.andruhovich.subscription.exception.MissingResourceTechnicalException;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LocaleManager {
    private static LocaleManager instance;
    private static ResourceBundle resourceBundle;
    private static final String MESSAGES = "locale/locale";

    private LocaleManager() {}

    public static LocaleManager getInstance(Locale locale) {
        if (instance == null) {
            instance = new LocaleManager();
            try {
                resourceBundle = ResourceBundle.getBundle(MESSAGES, locale);
            } catch (MissingResourceException e) {
                throw new RuntimeException("There is no locale file", e);
            }
        }
        return instance;
    }

    public String getProperty(String key) throws MissingResourceTechnicalException {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            throw new MissingResourceTechnicalException("Resource " + key + " is not found in source" + MESSAGES, e);
        }

    }
}
