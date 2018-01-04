package by.andruhovich.subscription.pool;

import java.util.ResourceBundle;

class DatabaseManager {
    private static DatabaseManager instance;
    private static ResourceBundle resourceBundle;

    private DatabaseManager() {}

    static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
            resourceBundle = ResourceBundle.getBundle("database");
        }
        return instance;
    }

    String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
