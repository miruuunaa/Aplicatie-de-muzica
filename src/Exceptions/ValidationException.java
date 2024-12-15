package Exceptions;

/**
 * ValidationException: Se declanșează pentru validări de bază eșuate.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
