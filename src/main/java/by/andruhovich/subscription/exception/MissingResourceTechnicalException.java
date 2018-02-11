package by.andruhovich.subscription.exception;

/**
 * An exception that provides information on DAO error.
 */
public class MissingResourceTechnicalException extends Exception {
    /**
     * @param message a description of the exception
     */
    public MissingResourceTechnicalException(String message) {
        super(message);
    }

    /**
     * @param message a description of the exception
     * @param cause the underlying reason for this exception
     */
    public MissingResourceTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause the underlying reason for this exception
     */
    public MissingResourceTechnicalException(Throwable cause) {
        super(cause);
    }
}
