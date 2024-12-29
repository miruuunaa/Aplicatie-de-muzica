package Domain;
/**
 * The User class serves as an abstract base class for users in the system.
 * It contains common attributes and methods for user identification and authentication.
 * Derived classes such as `Listener` and `PremiumUser` extend this class to provide specific implementations.
 */
public abstract class User {
    private int id;
    private String name;
    private String email;
    private String password;
    /**
     * Constructs a User object with the specified name and email.
     *
     * @param name  The name of the user
     * @param email The email address of the user
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.password=null;
    }

    public boolean authenticate(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the unique ID of the user.
     *
     * @return The user's ID
     */

    public int getId() {
        return id;
    }
    /**
     * Gets the name of the user.
     *
     * @return The user's name
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the user.
     *
     * @param name The name to set for the user
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Gets the email address of the user.
     *
     * @return The user's email address
     */
    public String getEmail() {
        return email;
    }
    /**
     * Sets the email address of the user.
     *
     * @param email The email to set for the user
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Abstract method to be implemented by subclasses to handle user login.
     * Each subclass must define its own behavior for logging in a user.
     */
    public abstract void login();
    /**
     * Abstract method to be implemented by subclasses to handle user logout.
     * Each subclass must define its own behavior for logging out a user.
     */
    public abstract void logout();
}
