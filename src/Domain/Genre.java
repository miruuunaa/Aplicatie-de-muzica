package Domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Genre {
    private String name;
    private List<Song> songs;
    private List<Artist> artists;

    public Genre(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
        this.artists = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artist> getArtists() {
        return artists; // Returnează lista de artiști
    }

    public void addArtist(Artist artist) {
        artists.add(artist);
    }

    public void addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
            System.out.println("Song " + song.getTitle() + " has been added to genre " + name + ".");
        } else {
            System.out.println("Song " + song.getTitle() + " is already in genre " + name + ".");
        }
    }

    public List<Song> get_songs() {
        return new ArrayList<>(songs);
    }

    public List<Song> getSongs() {
        List<Song> songs = new ArrayList<>();
        for (Artist artist : artists) {
            for (Album album : artist.getAlbums()) {
                songs.addAll(album.getSongs());
            }
        }
        return songs;
    }
}
