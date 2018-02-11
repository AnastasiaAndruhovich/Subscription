package by.andruhovich.subscription.exception;

/**
 * An exception that provides information on a database connection error.
 */
public class ConnectionTechnicalException extends Exception {
    /**
     * @param message a description of the exception
     */
    public ConnectionTechnicalException(String message) {
        super(message);
    }

}
