package by.andruhovich.subscription.pool;

import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Provides methods to get information from database property files
 */
class DatabaseManager {
    private static DatabaseManager instance;
    private static ResourceBundle resourceBundle;
    private static final String DATABASE_CONFIG = "database/database";

    private static final Logger LOGGER = LogManager.getLogger(DatabaseManager.class);

    private DatabaseManager() {}

    /**
     * @return DatabaseManager reference
     */
    static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
            try {
                resourceBundle = ResourceBundle.getBundle(DATABASE_CONFIG);
            } catch (MissingResourceException e) {
                LOGGER.log(Level.FATAL, "Database configuration file " + DATABASE_CONFIG + "is not found");
                throw new RuntimeException("Database configuration file " + DATABASE_CONFIG + "is not found", e);
            }
        }
        return instance;
    }

    /**
     * @param key String key for property file relevant required variable
     * @return Required variable
     * @throws MissingResourceTechnicalException
     *          If there is no such key in property file
     */
    String getProperty(String key) throws MissingResourceTechnicalException {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            throw new MissingResourceTechnicalException("Resource " + key + " is not fount in source" + DATABASE_CONFIG, e);
        }

    }
}
