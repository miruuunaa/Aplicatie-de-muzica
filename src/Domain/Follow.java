package Domain;
import java.util.List;
import java.util.ArrayList;
/**
 * The Follow class represents a "follow" relationship between a listener (user) and an artist.
 * It includes information about the listener who follows the artist, the artist being followed,
 * and a unique follow ID for each follow relationship.
 */
public class Follow {
    private static int lastId = 0;
    private Integer followID;
    private Listener user;
    private Artist artist;

    /**
     * Constructor for creating a Follow relationship between a listener and an artist.
     *
     * @param user The listener who follows the artist
     * @param artist The artist being followed
     */
    public Follow(Listener user, Artist artist) {
        this.followID = followID;
        this.user = user;
        this.artist = artist;
    }
    /**
     * Gets the unique identifier for the follow relationship.
     *
     * @return The follow ID
     */

    public Integer getId() {
        return followID;
    }
    /**
     * Retrieves a list containing the listener who is following the artist.
     * This method is useful if additional processing on followers is required.
     *
     * @return A list of users following the artist
     */
    public List<User> getFollowers() {
        return List.of(user);
    }
}