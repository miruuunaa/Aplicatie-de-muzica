package Repository;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
/**
 * This class provides an in-memory implementation of the IRepository interface.
 * It stores entities in a HashMap and supports CRUD operations: create, read, update, and delete.
 *
 * @param <T> The type of the entity being managed by this repository.
 */
public class InMemoryRepository<T> implements IRepository<T> {
    private Map<Integer, T> data = new HashMap<>();
    private int currentId = 1;
    /**
     * Creates a new entity and adds it to the repository.
     * The entity must have an 'id' field, which will be set automatically when the entity is created.
     *
     * @param entity The entity to be created and added to the repository.
     * @throws RuntimeException if the entity does not have an 'id' field or it is inaccessible.
     */
    @Override
    public void create(T entity) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, currentId);
            data.put(currentId++, entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Entity does not have an 'id' field or cannot access it.", e);
        }
    }
    /**
     * Retrieves an entity by its id.
     *
     * @param id The id of the entity to retrieve.
     * @return The entity associated with the given id, or null if not found.
     */
    @Override
    public T get(int id) {
        return data.get(id);
    }
    /**
     * Reads an entity from the repository by its id.
     * If the entity with the specified id does not exist, an exception is thrown.
     *
     * @param id The id of the entity to read.
     * @return The entity associated with the given id.
     * @throws IllegalArgumentException if the entity does not exist.
     */
    @Override
    public T read(int id) {
        //if (!data.containsKey(id)) {
         //   throw new IllegalArgumentException("Entity with id " + id + " does not exist.");
        //}
        return data.get(id);
    }
    /**
     * Updates an existing entity in the repository.
     * The entity must have a valid 'id' field. If the entity with the given id exists in the repository,
     * it will be updated. Otherwise, an exception will be thrown.
     *
     * @param entity The entity to update.
     * @throws RuntimeException if the entity does not have an 'id' field or cannot access it.
     * @throws IllegalArgumentException if the entity with the specified id does not exist in the repository.
     */
    @Override
    public void update(T entity) {
        try {

            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Integer id = (Integer) idField.get(entity);

            if (data.containsKey(id)) {
                data.put(id, entity);
            } else {
                throw new IllegalArgumentException("Cannot update: Entity with id " + id + " does not exist.");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Entity does not have an 'id' field or cannot access it.", e);
        }
    }
    /**
     * Deletes an entity from the repository by its id.
     * If the entity exists, it will be removed from the repository. If the entity with the given id does not exist,
     * a message will be printed indicating that the entity could not be found.
     *
     * @param id The id of the entity to delete.
     */
    @Override
    public void delete(int id) {
        if (data.containsKey(id)) {
            data.remove(id);
            System.out.println("Entity with ID " + id + " has been deleted.");
        } else {
            throw new IllegalArgumentException("Cannot delete: Entity with id " + id + " does not exist.");
        }
    }

    /**
     * Retrieves all entities stored in the repository.
     *
     * @return A map containing all entities, where the keys are the entity ids and the values are the entities themselves.
     */
    @Override
    public Map<Integer, T> getAll() {
        return data;
    }


}
