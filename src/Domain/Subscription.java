package Domain;
/**
 * The Subscription class represents a user's subscription to a music service.
 * It includes information about the subscription type (e.g., Basic, Premium), the price of the subscription,
 * and the associated listener (user). It also provides functionality to upgrade or cancel the subscription.
 */
public class Subscription  {
    private int id;
    private String type;
    private float price;
    private Listener user;
    /**
     * Constructs a Subscription object with the specified type, price, and associated user.
     *
     * @param type The type of the subscription (e.g., "Basic", "Premium")
     * @param price The price of the subscription
     * @param user The Listener (user) associated with this subscription
     */
    public Subscription(String type,float price,Listener user) {
        this.type = type;
        this.price = price;
        this.user = user;
    }
    /**
     * Gets the type of the subscription (e.g., "Basic", "Premium").
     *
     * @return The subscription type
     */
    public String getType() {
        return type;
    }
    /**
     * Sets the type of the subscription (e.g., "Basic", "Premium").
     *
     * @param type The subscription type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Gets the price of the subscription.
     *
     * @return The price of the subscription
     */
    public float getPrice() {
        return price;
    }
    /**
     * Sets the price of the subscription.
     *
     * @param price The price to set for the subscription
     */
    public void setPrice(float price) {
        this.price = price;
    }
    /**
     * Gets the Listener (user) associated with this subscription.
     *
     * @return The user associated with the subscription
     */
    public Listener getUser() {
        return user;
    }
    /**
     * Sets the Listener (user) associated with this subscription.
     *
     * @param user The user to set for the subscription
     */
    public void setUser(Listener user) {
        this.user = user;
    }

    /**
     * Gets the unique identifier for the subscription.
     *
     * @return The ID of the subscription
     */
    public int getId() {
        return id;
    }
    /**
     * Sets the unique identifier for the subscription.
     *
     * @param id The ID to set for the subscription
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Returns a string representation of the Subscription object, including the ID, type, price, and user.
     *
     * @return A string representation of the Subscription object
     */
    @Override
    public String toString() {
        return "Subscription{" +
                "id=" +id  +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", user=" + user +
                '}';
    }
}

