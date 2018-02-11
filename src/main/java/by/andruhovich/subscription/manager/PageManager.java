package by.andruhovich.subscription.manager;

import by.andruhovich.subscription.exception.MissingResourceTechnicalException;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Provides methods to get information from database property file
 */
public class PageManager {
    private static PageManager instance;
    private static ResourceBundle resourceBundle;
    private static final String PAGES = "page/page";

    private PageManager() {
    }

    public static PageManager getInstance() {
        if (instance == null) {
            instance = new PageManager();
            try {
                resourceBundle = ResourceBundle.getBundle(PAGES);
            } catch (MissingResourceException e) {
                throw new RuntimeException("There is no pages paths file", e);
            }
        }
        return instance;
    }

    public String getProperty(String key) throws MissingResourceTechnicalException {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            throw new MissingResourceTechnicalException("Resource " + key + " is not fount in resource " + PAGES, e);
        }
    }
}
