package by.andruhovich.subscription.exception;

public class ServiceTechnicalException extends Exception{
    public ServiceTechnicalException(String message) {
        super(message);
    }

    public ServiceTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceTechnicalException(Throwable cause) {
        super(cause);
    }
}
