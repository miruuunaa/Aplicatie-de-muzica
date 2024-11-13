package Service;

import Domain.Listener;
import Domain.Subscription;
import Repository.IRepository;

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
        this.listenerRepository = listenerRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Adds a new listener to the repository.
     *
     * @param listener The listener to be added to the repository.
     */
    public void addListener(Listener listener) {
        listenerRepository.create(listener);
    }

    /**
     * Retrieves a listener by their name from the repository.
     *
     * @param name The name of the listener to retrieve.
     * @return The listener with the specified name, or null if no listener is found.
     */
    public Listener getListenerByName(String name) {
        for (Listener listener : listenerRepository.getAll().values()) {
            if (listener.getName().equalsIgnoreCase(name)) {
                return listener;
            }
        }
        return null;
    }



}
