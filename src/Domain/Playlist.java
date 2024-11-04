package Domain;

import java.util.List;
import java.util.ArrayList;

public class Playlist implements Playable {
    private String name;
    private Listener user;
    private List<Song> songs;

    public Playlist(String name, Listener user) {
        this.name = name;
        this.user = user;
        this.songs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Listener getUser() {
        return user;
    }

    public void setUser(Listener user) {
        this.user = user;
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    public void setSongs(List<Song> songs) {
        this.songs = new ArrayList<>(songs);
    }

    public void addSong(Song song) {
        songs.add(song);
        System.out.println("Added song: " + song.getTitle() + " to playlist: " + name);
    }

    public void removeSong(Song song) {
        if (songs.remove(song)) {
            System.out.println("Removed song: " + song.getTitle() + " from playlist: " + name);
        } else {
            System.out.println("Song: " + song.getTitle() + " not found in playlist: " + name);
        }
    }

    @Override
    public void play() {
        System.out.println("Playing playlist: " + name);
        for (Song song : songs) {
            song.play(); // Redăm fiecare melodie din playlist
        }
    }

    @Override
    public void pause() {
        System.out.println("Pausing playlist: " + name);
    }

    @Override
    public void stop() {
        System.out.println("Stopping playlist: " + name);
    }
}
