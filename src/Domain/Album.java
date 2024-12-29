package Domain;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Album class represents a music album, containing information about the album's title,
 * release date, associated artist, genre, and the songs in the album.
 */

public class Album {
    private int id;
    private String title;
    private LocalDate releaseDate;
    private Artist artist;
    private Genre genre;
    private List<Song> songs;

    /**
     * Constructor for the Album class.
     * Creates a new album with the specified title, release date, and artist.
     * Automatically adds this album to the artist's list of albums.
     *
     * @param title The title of the album
     * @param releaseDate The release date of the album
     * @param artist The artist who created the album
     */

    public Album(String title, LocalDate releaseDate, Artist artist) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.artist = artist;
        this.songs = new ArrayList<>();
        artist.addAlbum(this);
    }
    /**
     * Gets the title of the album.
     *
     * @return The title of the album
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the list of songs in the album.
     *
     * @return A list of songs in the album
     */

    public List<Song> getSongs() {
        return songs;
    }


    public void setSongs(List<Song>songs){
        this.songs = songs;
    }

    /**
     * Gets the artist associated with the album.
     *
     * @return The artist of the album
     */

    public Artist getArtist() {
        return artist;
    }
    /**
     * Gets the genre of the album.
     *
     * @return The genre of the album
     */

    public Genre getGenre() {
        return genre;
    }
    /**
     * Gets the release date of the album.
     *
     * @return The release date of the album
     */
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    /**
     * Adds a song to the album if it is not already included.
     *
     * @param song The song to add to the album
     */
    public void addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
            System.out.println("Song '" + song.getTitle() + "' added to album '" + this.getTitle() + "'");
        }
    }
    /**
     * Gets the unique identifier for the album.
     *
     * @return The ID of the album
     */

    public int getId() {
        return id;
    }
    /**
     * Sets the unique identifier for the album.
     *
     * @param id The new ID to assign to the album
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    /**
     * Returns a string representation of the album, showing its title, release date,
     * and the associated artist's name.
     *
     * @return A string representing the album
     */
    @Override
    public String toString() {
        return "Album{" +
                "title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", artist=" + artist.getName() +
                '}';
    }

    public void setArtist(Artist artist) {
        this.artist= artist;
    }
}
