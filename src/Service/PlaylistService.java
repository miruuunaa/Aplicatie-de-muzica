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
     */
    public void addSongToPlaylist(Playlist playlist, Song song) {
        if (playlist == null) {
            throw new EntityNotFoundException("Playlist not found.");
        }
        if (song == null) {
            throw new EntityNotFoundException("Song not found.");
        }

        playlist.addSong(song);
    }


    /**
     * Removes a song from a specified playlist.
     *
     * @param playlist The playlist from which the song will be removed.
     * @param song The song to be removed from the playlist.
     * @throws EntityNotFoundException if the playlist or song is not found.
     */
    public void removeSongFromPlaylist(Playlist playlist, Song song) {
        if (playlist == null) {
            throw new EntityNotFoundException("Playlist not found.");
        }
        if (song == null) {
            throw new EntityNotFoundException("Song not found.");
        }

        if (playlist.removeSong(song)) {
            System.out.println(song.getTitle() + " removed from playlist.");
        } else {
            System.out.println("Song not found in playlist.");
        }
    }

    /**
     * Starts playing the songs in a specified playlist.
     *
     * @param playlist The playlist to be played.
     * @throws EntityNotFoundException if the playlist is not found.
     */
    public void playPlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new EntityNotFoundException("Playlist not found.");
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
     * @throws ValidationException if the playlist is null.
     */
    public void addPlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new ValidationException("Playlist cannot be null.");
        }

        playlistRepository.create(playlist);
    }


    /**
     * Retrieves a playlist by its name.
     *
     * @param name The name of the playlist to retrieve.
     * @return The playlist with the specified name, or throws EntityNotFoundException if no playlist is found.
     * @throws EntityNotFoundException if no playlist with the specified name is found.
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
