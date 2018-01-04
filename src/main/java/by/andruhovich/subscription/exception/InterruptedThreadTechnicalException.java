package by.andruhovich.subscription.exception;

public class InterruptedThreadTechnicalException extends Exception {
    public InterruptedThreadTechnicalException() {
    }

    public InterruptedThreadTechnicalException(String message) {
        super(message);
    }

    public InterruptedThreadTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterruptedThreadTechnicalException(Throwable cause) {
        super(cause);
    }

    public InterruptedThreadTechnicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
