package by.andruhovich.subscription.exception;

/**
 * An exception that provides information on DAO error.
 */
public class DAOTechnicalException extends Exception{
    /**
     * @param message a description of the exception
     */
    public DAOTechnicalException(String message) {
        super(message);
    }

    /**
     * @param message a description of the exception
     * @param cause the underlying reason for this exception
     */
    public DAOTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause the underlying reason for this exception
     */
    public DAOTechnicalException(Throwable cause) {
        super(cause);
    }
}
