package by.andruhovich.subscription.exception;

public class UndefinedCommandTechnicalException extends Exception{
    public UndefinedCommandTechnicalException(String message) {
        super(message);
    }

    public UndefinedCommandTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public UndefinedCommandTechnicalException(Throwable cause) {
        super(cause);
    }
}
