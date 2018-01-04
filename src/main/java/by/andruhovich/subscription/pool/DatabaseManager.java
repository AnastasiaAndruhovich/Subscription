package by.andruhovich.subscription.pool;

import java.util.ResourceBundle;

class DatabaseManager {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("database");
    // класс извлекает информацию из файла messages.properties
    public DatabaseManager() { }
    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
