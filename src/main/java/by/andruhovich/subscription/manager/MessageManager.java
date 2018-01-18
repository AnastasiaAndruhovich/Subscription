package by.andruhovich.subscription.manager;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageManager {
    private static MessageManager instance;
    private static ResourceBundle resourceBundle;
    private static final String MESSAGES = "message/messages";

    private MessageManager() {}

    static MessageManager getInstance() {
        if (instance == null) {
            instance = new MessageManager();
            try {
                resourceBundle = ResourceBundle.getBundle(MESSAGES);
            } catch (MissingResourceException e) {
                //TODO log
                throw new RuntimeException("There is no message file", e);
            }
        }
        return instance;
    }

    String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
