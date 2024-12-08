package Domain;
import java.util.ArrayList;
import java.util.List;
/**
 * The Artist class represents an artist user, extending from the User class.
 * It includes information specific to an artist, such as their albums and followers.
 */
public class Artist extends User  {
    protected int id;
    private List<Album> albums;
    private List<Listener> followers;

    /**
     * Constructor for the Artist class.
     * Initializes an artist with the specified name and email and initializes empty lists for albums and followers.
     *
     * @param name The name of the artist
     * @param email The email address of the artist
     */
    public Artist(String name, String email) {
        super(name, email);
        this.albums = new ArrayList<>();
        this.followers = new ArrayList<>();
    }
    /**
     * Gets the unique identifier of the artist.
     *
     * @return The ID of the artist
     */
    public int getId() {
        return id;
    }
    /**
     * Sets the unique identifier for the artist.
     *
     * @param id The new ID to assign to the artist
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Logs the artist in by displaying a login message.
     */
    @Override
    public void login() {
        System.out.println(Artist.super.getName() + " logged in as artist.");
    }
    /**
     * Logs the artist out by displaying a logout message.
     */
    @Override
    public void logout() {
        System.out.println(Artist.super.getName() + " logged out.");
    }
    /**
     * Retrieves the list of albums created by the artist.
     *
     * @return A list of the artist's albums
     */
    public List<Album> getAlbums() {
        return albums;
    }
    /**
     * Sets the list of albums for the artist.
     *
     * @param albums The list of albums to assign to the artist
     */
    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    /**
     * Adds an album to the artist's list if it is not already present.
     *
     * @param album The album to add to the artist's album list
     */

    public void addAlbum(Album album) {
        if (!albums.contains(album)) {
            albums.add(album);
            System.out.println("Album '" + album.getTitle() + "' added to artist " + this.getName());
        }
    }


    /**
     * Retrieves the list of followers for the artist.
     *
     * @return A list of listeners following the artist
     */
    public List<Listener> getFollowers() {
        return followers;
    }

    /**
     * Provides a string representation of the artist, including their ID, name, albums, and followers.
     *
     * @return A string representation of the artist
     */
    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + getName() + '\'' +
                ", albums=" + albums +
                ", followers=" + followers +
                '}';
    }
}
