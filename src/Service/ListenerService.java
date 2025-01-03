package Service;

import Domain.Listener;
import Domain.Subscription;
import Exceptions.DatabaseException;
import Repository.IRepository;

import Exceptions.EntityNotFoundException;
import Exceptions.ValidationException;

/**
 * The ListenerService class provides methods for managing listeners (users) in the music system.
 * It allows for adding new listeners, retrieving listener data, upgrading or canceling subscriptions,
 * and managing the subscription status of listeners.
 */
public class ListenerService {
    private final IRepository<Listener> listenerRepository;
    private final IRepository<Subscription> subscriptionRepository;

    /**
     * Constructor that initializes the ListenerService with the given listener and subscription repositories.
     *
     * @param listenerRepository The repository used to store and manage listener data.
     * @param subscriptionRepository The repository used to store and manage subscription data.
     */
    public ListenerService(IRepository<Listener> listenerRepository, IRepository<Subscription> subscriptionRepository) {
        if (listenerRepository == null || subscriptionRepository == null) {
            throw new ValidationException("Listener or Subscription repository cannot be null.");
        }
        this.listenerRepository = listenerRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Adds a new listener to the repository.
     *
     * @param listener The listener to be added to the repository.
     * @throws ValidationException if the listener is null or has invalid data.
     * @throws DatabaseException if there is an error while saving the listener to the repository.
     */
    public void addListener(Listener listener) {
        if (listener == null) {
            throw new ValidationException("Listener cannot be null.");
        }
        try {
            listenerRepository.create(listener);
        }catch (Exception e){
            throw new DatabaseException("Error while adding listener: " + e.getMessage());
        }
    }

    /**
     * Retrieves a listener by their name from the repository.
     *
     * @param name The name of the listener to retrieve.
     * @return The listener with the specified name, or null if no listener is found.
     * @throws ValidationException if the name is null or empty.
     * @throws EntityNotFoundException if no listener with the specified name is found.
     * @throws DatabaseException if there is an error while accessing the repository.
     */
    public Listener getListenerByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Listener name cannot be null or empty.");
        }
        try {

            Listener listener = null;
            for (Listener currentListener : listenerRepository.getAll().values()) {
                if (currentListener.getName().equalsIgnoreCase(name)) {
                    listener = currentListener;
                    break;
                }
            }

            if (listener == null) {
                throw new EntityNotFoundException("Listener with name " + name + " not found.");
            }

            return listener;
        }catch (Exception e){
            throw new DatabaseException("Error while retrieving listener by name: " + e.getMessage());
        }
    }

}
