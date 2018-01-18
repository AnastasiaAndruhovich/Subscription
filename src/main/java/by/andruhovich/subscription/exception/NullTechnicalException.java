package by.andruhovich.subscription.exception;

public class NullTechnicalException extends Exception{
    public NullTechnicalException() {
    }

    public NullTechnicalException(String message) {
        super(message);
    }

    public NullTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullTechnicalException(Throwable cause) {
        super(cause);
    }

    public NullTechnicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
