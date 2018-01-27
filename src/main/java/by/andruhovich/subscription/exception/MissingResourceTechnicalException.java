package by.andruhovich.subscription.exception;

public class MissingResourceTechnicalException extends Exception {
    public MissingResourceTechnicalException() {
    }

    public MissingResourceTechnicalException(String message) {
        super(message);
    }

    public MissingResourceTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingResourceTechnicalException(Throwable cause) {
        super(cause);
    }

    public MissingResourceTechnicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
