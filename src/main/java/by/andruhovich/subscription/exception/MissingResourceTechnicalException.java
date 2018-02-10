package by.andruhovich.subscription.exception;

public class MissingResourceTechnicalException extends Exception {
    public MissingResourceTechnicalException(String message) {
        super(message);
    }

    public MissingResourceTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingResourceTechnicalException(Throwable cause) {
        super(cause);
    }
}
