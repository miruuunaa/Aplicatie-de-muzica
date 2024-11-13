package Domain;

import java.util.ArrayList;
import java.util.List;
/**
 * The Playlist class represents a collection of songs created by a listener.
 * It allows adding, removing, and playing songs within the playlist.
 * This class implements the Playable interface to support playback functionality.
 */
public class Playlist implements Playable {
    protected int id;
    private String name;
    private Listener user;
    private List<Song> songs;
    /**
     * Constructs a Playlist object with a specified name and user.
     *
     * @param name the name of the playlist
     * @param user the Listener who owns the playlist
     */
    public Playlist(String name, Listener user) {
        this.name = name;
        this.user = user;
        this.songs = new ArrayList<>();
    }
    /**
     * Gets the playlist ID.
     *
     * @return the ID of the playlist
     */
    public int getId() {
        return id;
    }
    /**
     * Sets the playlist ID.
     *
     * @param id the new ID of the playlist
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Gets the name of the playlist.
     *
     * @return the name of the playlist
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the playlist.
     *
     * @param name the new name of the playlist
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Gets the listener who owns the playlist.
     *
     * @return the Listener who owns the playlist
     */
    public Listener getUser() {
        return user;
    }
    /**
     * Sets the listener who owns the playlist.
     *
     * @param user the Listener who owns the playlist
     */
    public void setUser(Listener user) {
        this.user = user;
    }
    /**
     * Gets the list of songs in the playlist.
     *
     * @return the list of songs
     */
    public List<Song> getSongs() {
        return songs;
    }
    /**
     * Replaces the current list of songs with a new list.
     *
     * @param songs the new list of songs
     */
    public void setSongs(List<Song> songs) {
        this.songs = new ArrayList<>(songs);
    }
    /**
     * Adds a song to the playlist.
     *
     * @param song the song to be added
     */
    public void addSong(Song song) {
        songs.add(song);

    }
    /**
     * Removes a song from the playlist.
     *
     * @param song the song to be removed
     * @return true if the song was found and removed, false otherwise
     */
    public boolean removeSong(Song song) {
        return songs.remove(song); // elimină piesa și returnează true dacă a fost găsită și eliminată
    }
    /**
     * Plays all songs in the playlist in sequence.
     * Implements the play method from the Playable interface.
     */
    @Override
    public void play() {
        System.out.println("Playing playlist: " + name);
        for (Song song : songs) {
            song.play(); // Redăm fiecare melodie din playlist
        }
    }
    /**
     * Pauses playback of the playlist.
     * Implements the pause method from the Playable interface.
     */
    @Override
    public void pause() {
        System.out.println("Pausing playlist: " + name);
    }
    /**
     * Stops playback of the playlist.
     * Implements the stop method from the Playable interface.
     */
    @Override
    public void stop() {
        System.out.println("Stopping playlist: " + name);
    }

    /**
     * Returns a string representation of the playlist.
     *
     * @return a string containing playlist details
     */
    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user=" + user +
                ", songs=" + songs +
                '}';
    }
}
