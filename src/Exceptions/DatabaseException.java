package Exceptions;

/** This exception extends RuntimeException and can be used to clearly
 * identify and handle problems that occur while interacting with the database.*/
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
