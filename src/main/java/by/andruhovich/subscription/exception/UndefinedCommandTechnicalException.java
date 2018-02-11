package by.andruhovich.subscription.exception;

/**
 * An exception that provides information on Define command error.
 */
public class UndefinedCommandTechnicalException extends Exception{
    /**
     * @param message a description of the exception
     */
    public UndefinedCommandTechnicalException(String message) {
        super(message);
    }
}
