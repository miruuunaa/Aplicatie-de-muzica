package Domain;

public class Subscription {
    private String type;
    private float price;
    private Listener user;

    public Subscription(String type, float price, Listener user) {
        this.type = type;
        this.price = price;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Listener getUser() {
        return user;
    }

    public void setUser(Listener user) {
        this.user = user;
    }

    public void upgrade() {
        if (type.equals("Basic")) {
            type = "Premium";
            price = 99.99f;
            System.out.println("Subscription upgraded to Premium for user: " + user.getName());
        } else {
            System.out.println("User " + user.getName() + " already has the highest subscription type.");
        }
    }

    public void cancel() {
        System.out.println("Subscription for user " + user.getName() + " has been canceled.");
        type = "None";
        price = 0.0f;
    }
}
