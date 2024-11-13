package Domain;
import java.util.ArrayList;
import java.util.List;

/**
 * The Listener class represents a user who listens to music.
 * It extends the User class, adding functionality for managing playlists,
 * listening history, and subscription services.
 * Each listener can log in and out, maintain playlists, and keep track of song history.
 */
public class Listener extends User {
    protected int id;
    private List<Playlist> playlists;
    private History history;
    private Subscription subscription;
    /**
     * Constructs a new Listener with a specified name and email.
     * Initializes an empty list of playlists and a new History object.
     *
     * @param name  The name of the listener
     * @param email The email address of the listener
     */
    public Listener(String name, String email) {
        super(name, email);
        this.playlists = new ArrayList<>();
        this.history = new History(this);
    }

    /**
     * Retrieves the ID of the listener.
     *
     * @return The listener's unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the listener.
     *
     * @param id The unique identifier to assign to this listener
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Logs the listener into the system, displaying a message indicating login status.
     */
    @Override
    public void login() {
        System.out.println(Listener.super.getName() + " logged in.");
    }
    /**
     * Logs the listener out of the system, displaying a message indicating logout status.
     */
    @Override
    public void logout() {
        System.out.println(Listener.super.getName() + " logged out.");
    }

    /**
     * Logs the listener out of the system, displaying a message indicating logout status.
     */
    public History getHistory() {
        return history;
    }
    /**
     * Retrieves the subscription plan of the listener.
     *
     * @return The Subscription object representing the listener's current subscription
     */
    public Subscription getSubscription() {
        return subscription;
    }
    /**
     * Sets or updates the subscription plan of the listener.
     *
     * @param subscription The Subscription object to assign to this listener
     */
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
    /**
     * Retrieves the playlists created by the listener.
     *
     * @return A list of the listener's playlists
     */
    public List<Playlist> getPlaylists() {
        return playlists;
    }
    /**
     * Provides a string representation of the listener, including name, email, and the count of history items.
     *
     * @return A string describing the listener
     */
    @Override
    public String toString() {
        return "Listener{" +
                "name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", historyCount=" + history.size() +
                '}';
    }
}