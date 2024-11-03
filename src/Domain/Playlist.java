package Domain;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
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
        return songs;
    }

    public void add_song(Song song) {
        songs.add(song);
        System.out.println("Song " + song.getTitle() + " has been added to playlist " + name + ".");
    }

    public void remove_song(Song song) {
        if (songs.remove(song)) {
            System.out.println("Song " + song.getTitle() + " has been removed from playlist " + name + ".");
        } else {
            System.out.println("Song " + song.getTitle() + " not found in playlist " + name + ".");
        }
    }

    public void play() {
        System.out.println("Playing playlist: " + name);
        for (Song song : songs) {
            song.play();
        }
    }

    public void pause() {
        System.out.println("Pausing playlist: " + name);
        for (Song song : songs) {
            song.pause();
        }
    }

    public void stop() {
        System.out.println("Stopping playlist: " + name);
        for (Song song : songs) {
            song.stop();
        }
    }
}
