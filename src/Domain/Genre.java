package Domain;
import java.util.ArrayList;
import java.util.List;
/**
 * The Genre class represents a music genre that categorizes songs.
 * Each genre has a name and a list of songs that belong to it.
 */
public class Genre {
    int id;
    private String name;
    private List<Song> songs;

    /**
     * Constructor to create a Genre with a specified name.
     *
     * @param name The name of the genre
     */
    public Genre(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }
    /**
     * Gets the list of songs associated with this genre.
     *
     * @return A list of songs that belong to this genre
     */
    public List<Song> getSongs() {
        return songs;
    }
    /**
     * Sets the name of the genre.
     *
     * @param name The new name of the genre
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Gets the name of the genre.
     *
     * @return The name of the genre
     */
    public String getName() {
        return name;
    }
    /**
     * Adds a song to the genre.
     *
     * @param song The song to be added to this genre's song list
     */
    public void addSong(Song song) {
        songs.add(song);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns a string representation of the genre,
     * showing its name and associated songs.
     *
     * @return A string representing the genre
     */
    @Override
    public String toString() {
        return "Genre{" +
                "name='" + name + '\'' +
                ", songs=" + songs +
                '}';
    }
}