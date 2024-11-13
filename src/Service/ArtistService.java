package Service;

import Domain.Album;
import Domain.Artist;
import Domain.Listener;
import Domain.Song;
import Repository.IRepository;
import java.util.List;

/**
 * The ArtistService class provides methods for managing artists within the music system.
 * It allows for the retrieval of artist data, enrolling listeners to follow artists,
 * adding new artists, and retrieving the artist's discography.
 */
public class ArtistService {
    private final IRepository<Artist> artistRepository;


    /**
     * Constructor that initializes the ArtistService with the given artist repository.
     *
     * @param artistRepository The repository used to store and manage artist data.
     */
    public ArtistService(IRepository<Artist> artistRepository) {
        this.artistRepository = artistRepository;
    }

    /**
     * Adds a new artist to the repository.
     *
     * @param artist The artist to be added to the repository.
     */
    public void addArtist(Artist artist) {
        artistRepository.create(artist);
    }

    /**
     * Enrolls a listener to follow a specific artist.
     *
     * @param listener The listener who will follow the artist.
     * @param artist The artist to be followed by the listener.
     */
    public void enrollListenerToArtist(Listener listener, Artist artist) {
        artist.getFollowers().add(listener);
    }

    /**
     * Retrieves an artist from the repository based on their name.
     *
     * @param name The name of the artist to retrieve.
     * @return The artist with the specified name, or null if no artist is found.
     */
    public Artist getArtistByName(String name) {
        for (Artist artist : artistRepository.getAll().values()) {
            if (artist.getName().equalsIgnoreCase(name)) {
                return artist;
            }
        }
        return null;
    }

    /**
     * Retrieves and displays the discography of a specific artist by their name.
     * This includes all albums and their respective songs.
     *
     * @param artistName The name of the artist whose discography will be displayed.
     */
    public void showDiscography(String artistName) {
        Artist artist = artistRepository.getAll().values()
                .stream()
                .filter(a -> a.getName().equalsIgnoreCase(artistName))
                .findFirst()
                .orElse(null);

        if (artist == null) {
            System.out.println("Artist not found.");
            return;
        }

        List<Album> albums = artist.getAlbums();
        if (albums.isEmpty()) {
            System.out.println("No albums found for this artist.");
        } else {
            System.out.println("Discography of " + artistName + ":");
            for (Album album : albums) {
                System.out.println("Album: " + album.getTitle() + " (" + album.getReleaseDate() + ")");
                for (Song song : album.getSongs()) {
                    System.out.println(" - " + song.getTitle() + " (" + song.getDuration() + " mins)");
                }
            }
        }
    }


}
