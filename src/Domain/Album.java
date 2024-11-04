package Domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Album {
    private String name;
    private String title;
    private Date release_date;
    private Artist artist;
    private List<Song> songs;

    public Album(String title, Date release_date, Artist artist) {
        this.title = title;
        this.release_date = release_date;
        this.artist = artist;
        this.songs = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(Date release_date) {
        this.release_date = release_date;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
            System.out.println("Song " + song.getTitle() + " has been added to album " + title + ".");
        } else {
            System.out.println("Song " + song.getTitle() + " is already in album " + title + ".");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
