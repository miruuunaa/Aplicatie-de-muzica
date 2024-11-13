package Domain;
import java.time.LocalDateTime;
import java.util.*;
/**
 * The History class tracks a listener's song history, storing songs they have played
 * and the date and time each song was added to the history.
 * This class provides functionality to add songs to the listener's history and
 * retrieve the list of songs along with their playback timestamps.
 */
public class History {
    private Listener user;
    private List<Song> songs;
    private Map<Song, LocalDateTime> songHistory;

    /**
     * Constructor to create a new History object for a specified listener.
     *
     * @param user The listener whose history is being tracked
     */
    public History(Listener user) {
        this.user = user;
        this.songs = new ArrayList<>();
        this.songHistory = new HashMap<>();
    }

    /**
     * Adds a song to the listener's history with the current timestamp.
     *
     * @param song The song to be added to the history
     */
    public void addSongToHistory(Song song) {
        songHistory.put(song, LocalDateTime.now());
        songs.add(song);
    }
    /**
     * Retrieves the list of songs in the listener's history.
     *
     * @return A list of songs that the listener has played
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songHistory.keySet());
    }
    /**
     * Retrieves the map of songs and their playback timestamps.
     *
     * @return A map where each song is associated with the date and time it was played
     */
    public Map<Song, LocalDateTime> getSongHistory() {
        return songHistory;
    }
    /**
     * Retrieves the listener associated with this history.
     *
     * @return The listener whose history this object tracks
     */
    public Listener getUser() {
        return user;
    }
    /**
     * Returns the total number of songs in the listener's history.
     *
     * @return The number of songs in the song history
     */
    public int size() {
        return songHistory.size();
    }

}