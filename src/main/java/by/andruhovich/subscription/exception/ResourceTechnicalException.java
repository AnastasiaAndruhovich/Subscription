package by.andruhovich.subscription.exception;

public class ResourceTechnicalException extends Exception {
    public ResourceTechnicalException() {
    }

    public ResourceTechnicalException(String message) {
        super(message);
    }

    public ResourceTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceTechnicalException(Throwable cause) {
        super(cause);
    }

    public ResourceTechnicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
