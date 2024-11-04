package Domain;

public class Song implements Playable {
    private String title;
    private float duration;
    private Artist artist;
    private Album album;
    private Genre genre;

    public Song(String title, float duration, Artist artist, Album album) {
        this.title = title;
        this.duration = duration;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public void play() {
        System.out.println("Playing song: " + title);
    }

    @Override
    public void pause() {
        System.out.println("Pausing song: " + title);
    }

    @Override
    public void stop() {
        System.out.println("Stopping song: " + title);
    }

}
