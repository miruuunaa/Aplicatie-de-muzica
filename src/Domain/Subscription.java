package Domain;

public class Subscription {
    private String type;
    private float price;
    private Listener user;

    public Subscription(String type,float price,Listener user){
        this.type=type;
        this.price=price;
        this.user=user;
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

    public void upgrade(){

    }

    public void cancel(){

    }
}
