package Exceptions;

/** This exception extends RuntimeException and can be used in scenarios
 * where input data, configurations, or business rules do not meet
 * the required criteria.*/
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
