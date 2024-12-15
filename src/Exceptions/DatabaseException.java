package Exceptions;

/**
 * DatabaseException: Se declanșează pentru erori legate de baza de date.
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
