package by.andruhovich.subscription.exception;

/**
 * An exception that provides information on Service error.
 */
public class ServiceTechnicalException extends Exception{
    /**
     * @param message a description of the exception
     */
    public ServiceTechnicalException(String message) {
        super(message);
    }

    /**
     * @param message a description of the exception
     * @param cause the underlying reason for this exception
     */
    public ServiceTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause the underlying reason for this exception
     */
    public ServiceTechnicalException(Throwable cause) {
        super(cause);
    }
}
