package Domain;

import java.util.ArrayList;
import java.util.List;

public class History {
    private Listener user;
    private List<Song> songs;

    public History(Listener user) {
        this.user = user;
        this.songs = new ArrayList<>();
    }

    public Listener getUser() {
        return user;
    }

    public void setUser(Listener user) {
        this.user = user;
    }

    public void add_song_to_history(Song song) {
        songs.add(song);
        System.out.println("Song " + song.getTitle() + " has been added to the history of user: " + user.getName());
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }
}
