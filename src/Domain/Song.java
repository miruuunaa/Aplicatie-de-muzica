package Domain;
/**
 * The Song class represents a musical track with properties such as title, duration, album, and genre.
 * It implements the Playable interface, providing functionality to play, pause, and stop a song.
 */
public class Song implements Playable {
    protected int id;
    private String title;
    private float duration;
    private Album album;
    private Genre genre;
    /**
     * Constructs a Song object with the specified title, duration, and album.
     * Throws an IllegalArgumentException if the album is null.
     *
     * @param title  The title of the song
     * @param duration The duration of the song in minutes
     * @param album  The album the song belongs to
     */
    public Song(String title, float duration, Album album) {
        if (album == null) {
            throw new IllegalArgumentException("Album cannot be null.");
        }
        this.title=title;
        this.duration = duration;
        this.album = album;
        album.addSong(this);
    }
    /**
     * Gets the genre of the song.
     *
     * @return The genre of the song
     */
    public Genre getGenre() {
        return genre;
    }
    /**
     * Sets the genre for the song.
     *
     * @param genre The genre of the song
     */
    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    /**
     * Plays the song. Outputs a message to indicate that the song is playing.
     */
    @Override
    public void play() {
        System.out.println("Playing song: " + title);
    }
    /**
     * Pauses the song. Outputs a message to indicate that the song is paused.
     */
    @Override
    public void pause() {
        System.out.println("Pausing song: " + title);
    }
    /**
     * Stops the song. Outputs a message to indicate that the song has stopped.
     */
    @Override
    public void stop() {
        System.out.println("Stopping song: " + title);
    }
    /**
     * Gets the title of the song.
     *
     * @return The title of the song
     */
    public String getTitle() {
        return title;
    }
    /**
     * Gets the duration of the song in minutes.
     *
     * @return The duration of the song in minutes
     */
    public float getDuration() {
        return duration;
    }

    /**
     * Gets the album that the song belongs to.
     *
     * @return The album of the song
     */
    public Album getAlbum() {
        return album;
    }
    /**
     * Gets the unique identifier for the song.
     *
     * @return The ID of the song
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the song.
     *
     * @param id The ID to be set for the song
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Returns a string representation of the Song object, including the title, duration, and album ID.
     *
     * @return A string representation of the Song object
     */
    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", duration=" + duration + " minutes" +
                ", albumId=" + album.getId() +
                '}';
    }


}