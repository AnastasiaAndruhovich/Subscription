package by.andruhovich.subscription.exception;

public class DAOTechnicalException extends Exception{
    public DAOTechnicalException(String message) {
        super(message);
    }

    public DAOTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOTechnicalException(Throwable cause) {
        super(cause);
    }
}
