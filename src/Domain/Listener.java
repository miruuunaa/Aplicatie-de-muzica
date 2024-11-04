package Domain;

import java.util.ArrayList;
import java.util.List;

public class Listener extends User {
    private List<Playlist> playlists;

    public Listener(String name, String email) {
        super(name, email);
        this.playlists = new ArrayList<>();
    }

    @Override
    public void login() {
        System.out.println(getName() + " has logged in as Listener.");
    }

    @Override
    public void logout() {
        System.out.println(getName() + " has logged out.");
    }

    public void createPlaylist(String playlistName) {
        Playlist newPlaylist = new Playlist(playlistName, this);
        playlists.add(newPlaylist);
        System.out.println("Created playlist: " + playlistName);
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
}
