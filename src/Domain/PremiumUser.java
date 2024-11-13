package Domain;
/**
 * The PremiumUser class represents a user with a premium subscription.
 * This class extends the `User` class and adds properties for subscription type and price.
 * It allows logging in and logging out, as well as providing access to subscription details.
 */
public class PremiumUser extends User{
    protected Integer id;
    private String subscriptionType;
    private float subscriptionPrice;
    /**
     * Constructs a PremiumUser object with the specified name, email, subscription type, and price.
     *
     * @param name            the name of the user
     * @param email           the email of the user
     * @param subscriptionType the type of the subscription
     * @param subscriptionPrice the price of the subscription
     */
    public PremiumUser(String name, String email, String subscriptionType, float subscriptionPrice) {
        super(name, email);
        this.subscriptionType = subscriptionType;
        this.subscriptionPrice = subscriptionPrice;
    }
    /**
     * Logs in the user as a Premium User, printing a login message.
     * Overrides the login method from the `User` class.
     */
    @Override
    public void login() {
        System.out.println(getName() + " has logged in as Premium User.");
    }
    /**
     * Logs out the user, printing a logout message.
     * Overrides the logout method from the `User` class.
     */
    @Override
    public void logout() {
        System.out.println(getName() + " has logged out.");
    }
    /**
     * Gets the type of the subscription.
     *
     * @return the subscription type (e.g., "Premium")
     */
    public String getSubscriptionType() {
        return subscriptionType;
    }
    /**
     * Sets the type of the subscription.
     *
     * @param subscriptionType the new subscription type
     */
    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    /**
     * Gets the price of the subscription.
     *
     * @return the subscription price
     */
    public float getSubscriptionPrice() {
        return subscriptionPrice;
    }
    /**
     * Sets the price of the subscription.
     *
     * @param subscriptionPrice the new subscription price
     */
    public void setSubscriptionPrice(float subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }

    /**
     * Returns a string representation of the PremiumUser, including the subscription type and price.
     *
     * @return a string containing PremiumUser details
     */

    @Override
    public String toString() {
        return "PremiumUser{" +
                "id=" + id +
                ", subscriptionType='" + subscriptionType + '\'' +
                ", subscriptionPrice=" + subscriptionPrice +
                '}';
    }
}