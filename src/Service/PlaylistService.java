package Service;

import Domain.Playlist;
import Domain.Song;
import Repository.IRepository;

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
        this.playlistRepository = playlistRepository;
    }

    /**
     * Adds a song to a specified playlist.
     *
     * @param playlist The playlist to which the song will be added.
     * @param song The song to be added to the playlist.
     */
    public void addSongToPlaylist(Playlist playlist, Song song) {
        playlist.addSong(song);

    }

    /**
     * Removes a song from a specified playlist.
     *
     * @param playlist The playlist from which the song will be removed.
     * @param song The song to be removed from the playlist.
     */
    public void removeSongFromPlaylist(Playlist playlist, Song song) {
        if (playlist.removeSong(song)) { // utilizează metoda removeSong din Playlist
            System.out.println(song.getTitle() + " removed from playlist.");
        } else {
            System.out.println("Song not found in playlist.");
        }
    }

    /**
     * Starts playing the songs in a specified playlist.
     *
     * @param playlist The playlist to be played.
     */
    public void playPlaylist(Playlist playlist) {
        playlist.play();

    }

    /**
     * Stops the playback of a specified playlist.
     *
     * @param playlist The playlist to be stopped.
     */
    public void stopPlaylist(Playlist playlist){
        playlist.stop();

    }

    /**
     * Pauses the playback of a specified playlist.
     *
     * @param playlist The playlist to be paused.
     */
    public void pausePlaylist(Playlist playlist){
        playlist.pause();

    }

    /**
     * Adds a playlist to the repository.
     *
     * @param playlist The playlist to be added to the repository.
     */
    public void addPlaylist(Playlist playlist) {
        playlistRepository.create(playlist);
    }

    /**
     * Retrieves a playlist by its name.
     *
     * @param name The name of the playlist to retrieve.
     * @return The playlist with the specified name, or null if no playlist is found.
     */
    public Playlist getPlaylistByName(String name) {
        for (Playlist playlist : playlistRepository.getAll().values()) {
            if (playlist.getName().equalsIgnoreCase(name)) {
                return playlist;
            }
        }
        return null;
    }
}
