package Service;
import Domain.Song;
import Repository.IRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * SongService is responsible for managing and interacting with songs in the repository.
 * It provides various methods to add songs, retrieve them by title, play, stop, and pause songs.
 * The class interacts with a repository that stores the songs and provides access to them.
 */

public class SongService {
    private final IRepository<Song> songRepository;

    /**
     * Constructor that initializes the SongService with a song repository.
     *
     * @param songRepository The repository used to store and retrieve songs.
     */
    public SongService(IRepository<Song> songRepository) {
        this.songRepository = songRepository;
    }

    /**
     * Retrieves a song by its title from the repository.
     * The search is case-insensitive, so "songTitle" and "SONGTITLE" will be treated the same.
     *
     * @param title The title of the song to retrieve.
     * @return The song with the specified title, or null if no song is found.
     */
    public Song getSongByTitle(String title) {
        for (Song song : songRepository.getAll().values()) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                return song;
            }
        }
        return null;
    }

    /**
     * Adds a new song to the song repository.
     * The song is stored in the repository for later retrieval and management.
     *
     * @param song The song to be added.
     */
    public void addSong(Song song) {
        songRepository.create(song);
    }

    /**
     * Retrieves a list of all songs stored in the song repository.
     * The list contains all the songs present in the repository.
     *
     * @return A list of all songs.
     */
    public List<Song> getAllSongs() {
        return new ArrayList<>(songRepository.getAll().values());
    }

    /**
     * Starts playing the specified song.
     * This method invokes the play method on the Song object to begin playback.
     *
     * @param song The song to be played.
     */
    public void playSong(Song song) {
        song.play();

    }

    /**
     * Stops the playback of a specified song.
     * This method invokes the stop method on the Song object to halt playback.
     *
     * @param song The song to be stopped.
     */
    public void stopSong(Song song){
        song.stop();

    }

    /**
     * Pauses the playback of a specified song.
     * This method invokes the pause method on the Song object to pause the playback.
     *
     * @param song The song to be paused.
     */
    public void pauseSong(Song song){
        song.pause();

    }
}
