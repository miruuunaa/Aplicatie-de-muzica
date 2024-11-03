package Domain;

import java.util.ArrayList;
import java.util.List;

public class Artist extends User {
    private List<Album> albums;

    public Artist(String name, String email) {
        super(name, email);
        this.albums = new ArrayList<>();
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void create_album(String albumName) {
        Album newAlbum = new Album(albumName);
        albums.add(newAlbum);
        System.out.println("Album " + albumName + " has been created.");
    }

    public void upload_song(String albumName, String songTitle) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                album.addSong(songTitle);
                System.out.println("Song " + songTitle + " has been added to album " + albumName + ".");
                return;
            }
        }
        System.out.println("Album " + albumName + " not found.");
    }
}
