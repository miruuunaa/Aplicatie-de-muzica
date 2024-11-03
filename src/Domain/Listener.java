package Domain;

import java.util.ArrayList;
import java.util.List;

public class Listener extends User {
    private List<Playlist> playlists;

    public Listener(String name, String email) {
        super(name, email); // se apeleaza constructorul clasei parinte
        this.playlists = new ArrayList<>();
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void create_playlist(String playlistName) {
        Playlist newPlaylist = new Playlist(playlistName);
        playlists.add(newPlaylist);
        System.out.println("Playlist " + playlistName + " has been created.");
    }
}
