package Service;

import Domain.Playlist;
import Domain.Song;
import Repository.IRepository;

import Exceptions.EntityNotFoundException;
import Exceptions.ValidationException;

/**
 * The PlaylistService class provides methods for managing playlists in the system.
 * It allows for adding songs to playlists, removing songs from playlists, and playing,
 * stopping, and pausing playlists.
 */
public class PlaylistService {
    private final IRepository<Playlist> playlistRepository;

    /**
     * Constructor that initializes the PlaylistService with the given playlist repository.
     *
     * @param playlistRepository The repository used to store and manage playlist data.
     */
    public PlaylistService(IRepository<Playlist> playlistRepository) {
        if (playlistRepository == null) {
            throw new ValidationException("Playlist repository cannot be null.");
        }
        this.playlistRepository = playlistRepository;
    }

    /**
     * Adds a song to a specified playlist.
     *
     * @param playlist The playlist to which the song will be added.
     * @param song The song to be added to the playlist.
     * @throws EntityNotFoundException if the playlist or song is not found.
     * @throws ValidationException if the playlist already contains the song.
     */
    public void addSongToPlaylist(Playlist playlist, Song song) {
        if (playlist == null) {
            throw new EntityNotFoundException("Playlist not found.");
        }
        if (song == null) {
            throw new EntityNotFoundException("Song not found.");
        }
        if (playlist.getSongs().contains(song)) {
            throw new ValidationException("The song is already in the playlist.");
        }
        playlist.addSong(song);
    }


    /**
     * Removes a song from a specified playlist.
     *
     * @param playlist The playlist from which the song will be removed.
     * @param song The song to be removed from the playlist.
     * @throws EntityNotFoundException if the playlist or song is not found.
     * @throws ValidationException if the song is not present in the playlist.
     */
    public void removeSongFromPlaylist(Playlist playlist, Song song) {
        if (playlist == null) {
            throw new EntityNotFoundException("Playlist not found.");
        }
        if (song == null) {
            throw new EntityNotFoundException("Song not found.");
        }
        if (!playlist.getSongs().contains(song)) {
            throw new ValidationException("The song is not in the playlist.");
        }
        if (playlist.removeSong(song)) {
            System.out.println(song.getTitle() + " removed from playlist.");
        }
    }

    /**
     * Starts playing the songs in a specified playlist.
     *
     * @param playlist The playlist to be played.
     * @throws EntityNotFoundException if the playlist is not found.
     * @throws ValidationException if the playlist is empty.
     */
    public void playPlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new EntityNotFoundException("Playlist not found.");
        }
        if (playlist.getSongs().isEmpty()) {
            throw new ValidationException("Cannot play an empty playlist.");
        }
        playlist.play();
    }

    /**
     * Stops the playback of a specified playlist.
     *
     * @param playlist The playlist to be stopped.
     * @throws EntityNotFoundException if the playlist is not found.
     */
    public void stopPlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new EntityNotFoundException("Playlist not found.");
        }

        playlist.stop();
    }

    /**
     * Pauses the playback of a specified playlist.
     *
     * @param playlist The playlist to be paused.
     * @throws EntityNotFoundException if the playlist is not found.
     */
    public void pausePlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new EntityNotFoundException("Playlist not found.");
        }
        playlist.pause();
    }

    /**
     * Adds a playlist to the repository.
     *
     * @param playlist The playlist to be added to the repository.
     * @throws ValidationException if the playlist is null or already exists in the repository.
     */
    public void addPlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new ValidationException("Playlist cannot be null.");
        }
        if (playlistRepository.getAll().values().stream().anyMatch(p -> p.getName().equalsIgnoreCase(playlist.getName()))) {
            throw new ValidationException("A playlist with the same name already exists.");
        }
        playlistRepository.create(playlist);
    }


    /**
     * Retrieves a playlist by its name.
     *
     * @param name The name of the playlist to retrieve.
     * @return The playlist with the specified name, or throws EntityNotFoundException if no playlist is found.
     * @throws EntityNotFoundException if no playlist with the specified name is found.
     * @throws ValidationException if the name is null or empty.
     */
    public Playlist getPlaylistByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Playlist name cannot be null or empty.");
        }

        for (Playlist playlist : playlistRepository.getAll().values()) {
            if (playlist.getName().equalsIgnoreCase(name)) {
                return playlist;
            }
        }
        throw new EntityNotFoundException("Playlist with name " + name + " not found.");
    }
}
