package Domain;

public class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public void login() {
        System.out.println(name + " has logged in.");
    }


    public void logout() {
        System.out.println(name + " has logged out.");
    }
}
