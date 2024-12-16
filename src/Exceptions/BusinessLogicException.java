package Exceptions;

/**
 * A custom exception used for business logic errors, extended to include
 * error codes for more detailed error reporting.*/
public class BusinessLogicException extends RuntimeException {
    public BusinessLogicException(String message) {
        super(message);
    }
}
