package by.andruhovich.subscription.exception;

public class InterruptedTechnicalException extends Exception {
    public InterruptedTechnicalException() {
    }

    public InterruptedTechnicalException(String message) {
        super(message);
    }

    public InterruptedTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterruptedTechnicalException(Throwable cause) {
        super(cause);
    }

    public InterruptedTechnicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
