package by.andruhovich.subscription.manager;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ConfigurationManager {
    private static ConfigurationManager instance;
    private static ResourceBundle resourceBundle;
    private static final String DATABASE_CONFIG = "page/pages";

    private ConfigurationManager() {}

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
            try {
                resourceBundle = ResourceBundle.getBundle(DATABASE_CONFIG);
            } catch (MissingResourceException e) {
                //TODO log
                throw new RuntimeException("There is no pages paths file.", e);
            }
        }
        return instance;
    }

    public String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
