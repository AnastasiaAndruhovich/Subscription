package by.andruhovich.subscription.exception;

public class ConnectionTechnicalException extends Exception {
    public ConnectionTechnicalException() {
    }

    public ConnectionTechnicalException(String message) {
        super(message);
    }

    public ConnectionTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionTechnicalException(Throwable cause) {
        super(cause);
    }

    public ConnectionTechnicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
