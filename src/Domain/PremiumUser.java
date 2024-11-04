package Domain;

public class PremiumUser extends User {
    private String subscriptionType;
    private float subscriptionPrice;

    public PremiumUser(String name, String email, String subscriptionType, float subscriptionPrice) {
        super(name, email);
        this.subscriptionType = subscriptionType;
        this.subscriptionPrice = subscriptionPrice;
    }

    @Override
    public void login() {
        System.out.println(getName() + " has logged in as Premium User.");
    }

    @Override
    public void logout() {
        System.out.println(getName() + " has logged out.");
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public float getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(float subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }
}
