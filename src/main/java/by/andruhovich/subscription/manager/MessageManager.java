package by.andruhovich.subscription.manager;

import by.andruhovich.subscription.exception.MissingResourceTechnicalException;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageManager {
    private static MessageManager instance;
    private static ResourceBundle resourceBundle;
    private static final String MESSAGES = "message/messages";

    private MessageManager() {}

    public static MessageManager getInstance() {
        if (instance == null) {
            instance = new MessageManager();
            try {
                resourceBundle = ResourceBundle.getBundle(MESSAGES);
            } catch (MissingResourceException e) {
                throw new RuntimeException("There is no message file", e);
            }
        }
        return instance;
    }

    public String getProperty(String key) throws MissingResourceTechnicalException {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            throw new MissingResourceTechnicalException("Resource " + key + " is not fount in source" + MESSAGES, e);
        }

    }
}
