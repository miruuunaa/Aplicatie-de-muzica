package Domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Artist extends User {
    private List<Album> albums;

    public Artist(String name, String email) {
        super(name, email);
        this.albums = new ArrayList<>();
    }

    @Override
    public void login() {
        System.out.println(getName() + " has logged in as Artist.");
    }

    @Override
    public void logout() {
        System.out.println(getName() + " has logged out.");
    }

    public void uploadSong(Song song) {
        System.out.println(getName() + " uploaded the song: " + song.getTitle());
    }

    public void createAlbum(String albumTitle, Date releaseDate) {
        Album newAlbum = new Album(albumTitle, releaseDate, this);
        albums.add(newAlbum);
        System.out.println("Created album: " + albumTitle);
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}
