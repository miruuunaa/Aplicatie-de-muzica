package Exceptions;

/**
 * This exception extends RuntimeException and is typically used in
 * scenarios where a search or retrieval operation fails to locate an
 * entity by its identifier or unique criteria.*/
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
