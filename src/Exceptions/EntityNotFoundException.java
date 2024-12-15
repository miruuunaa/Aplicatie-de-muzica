package Exceptions;

/**
 * EntityNotFoundException: Se declanșează când o entitate nu este găsită.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
