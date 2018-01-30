package by.andruhovich.subscription.exception;

public class UndefinedCommandTechnicalException extends Exception{
    public UndefinedCommandTechnicalException() {
    }

    public UndefinedCommandTechnicalException(String message) {
        super(message);
    }

    public UndefinedCommandTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public UndefinedCommandTechnicalException(Throwable cause) {
        super(cause);
    }

    public UndefinedCommandTechnicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
