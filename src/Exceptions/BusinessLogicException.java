package Exceptions;

/**
 * BusinessLogicException: Se declanșează pentru erori legate de regulile de afaceri.
 */
public class BusinessLogicException extends RuntimeException {
    public BusinessLogicException(String message) {
        super(message);
    }
}
