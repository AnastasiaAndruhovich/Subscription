package by.andruhovich.subscription.pool;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

class DatabaseManager {
    private static DatabaseManager instance;
    private static ResourceBundle resourceBundle;
    private static final String DATABASE_CONFIG = "config/database";

    private DatabaseManager() {}

    static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
            try {
                resourceBundle = ResourceBundle.getBundle(DATABASE_CONFIG);
            } catch (MissingResourceException e) {
                //TODO log
                throw new RuntimeException("There is no database configuration file", e);
            }
        }
        return instance;
    }

    String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
