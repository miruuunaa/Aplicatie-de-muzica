package Service;
import Domain.Genre;
import Domain.Song;
import Exceptions.DatabaseException;
import Repository.IRepository;
import java.util.ArrayList;
import java.util.List;

import Exceptions.EntityNotFoundException;
import Exceptions.ValidationException;

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
     * @throws ValidationException if the repository is null.
     */
    public SongService(IRepository<Song> songRepository) {
        if (songRepository == null) {
            throw new ValidationException("Song repository cannot be null.");
        }
        this.songRepository = songRepository;
    }


    /**
     * Retrieves a song by its title from the repository.
     * The search is case-insensitive, so "songTitle" and "SONGTITLE" will be treated the same.
     *
     * @param title The title of the song to retrieve.
     * @return The song with the specified title, or throws an exception if not found.
     * @throws EntityNotFoundException if no song with the specified title is found.
     */
    public Song getSongByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Song title cannot be null or empty.");
        }

        for (Song song : songRepository.getAll().values()) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                return song;
            }
        }

        throw new EntityNotFoundException("Song with title " + title + " not found.");
    }

    /**
     * Adds a new song to the song repository.
     * The song is stored in the repository for later retrieval and management.
     *
     * @param song The song to be added.
     * @throws ValidationException if the song is null or already exists in the repository.
     */
    public void addSong(Song song) {
        if (song == null) {
            throw new ValidationException("Song cannot be null.");
        }
        if (songRepository.getAll().values().stream().anyMatch(existingSong -> existingSong.getTitle().equalsIgnoreCase(song.getTitle()))) {
            throw new ValidationException("A song with the same title already exists in the repository.");
        }
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
     * @throws EntityNotFoundException if the song is null.
     */
    public void playSong(Song song) {
        if (song == null) {
            throw new EntityNotFoundException("Song not found.");
        }

        song.play();
    }

    /**
     * Stops the playback of a specified song.
     * This method invokes the stop method on the Song object to halt playback.
     *
     * @param song The song to be stopped.
     * @throws EntityNotFoundException if the song is null.
     */
    public void stopSong(Song song) {
        if (song == null) {
            throw new EntityNotFoundException("Song not found.");
        }

        song.stop();
    }

    /**
     * Pauses the playback of a specified song.
     * This method invokes the pause method on the Song object to pause the playback.
     *
     * @param song The song to be paused.
     * @throws EntityNotFoundException if the song is null.
     */
    public void pauseSong(Song song) {
        if (song == null) {
            throw new EntityNotFoundException("Song not found.");
        }

        song.pause();
    }

    public boolean deleteSong(int songId) throws  DatabaseException {
        try {
            songRepository.delete(songId);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("An error occurred while deleting the song: " + e.getMessage());
        }
    }

    public boolean updateSongDetails(int songId, String newTitle, String newGenre)
            throws EntityNotFoundException, ValidationException {
        if (newTitle == null || newTitle.trim().isEmpty()) {
            throw new ValidationException("Song title cannot be empty.");
        }

        if (newGenre == null || newGenre.trim().isEmpty()) {
            throw new ValidationException("Song genre cannot be empty.");
        }
        Song song = songRepository.get(songId);
        if (song == null) {
            throw new EntityNotFoundException("Song with ID " + songId + " not found.");
        }

        song.setTitle(newTitle);
        song.setGenre(new Genre(newGenre));
        songRepository.update(song);
        return true;
    }
}
