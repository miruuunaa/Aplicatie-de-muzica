package Service;

import Domain.Album;
import Domain.Artist;
import Domain.Listener;
import Domain.Song;
import Exceptions.BusinessLogicException;
import Exceptions.DatabaseException;
import Repository.IRepository;

import java.util.*;
import java.util.stream.Collectors;

import Exceptions.EntityNotFoundException;
import Exceptions.ValidationException;

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
     * @throws ValidationException if the artist is null.
     * @throws DatabaseException if there is an error adding the artist to the repository.
     */
    public void addArtist(Artist artist) {
        if (artist == null) {
            throw new ValidationException("Artist cannot be null.");
        }
        try {
            artistRepository.create(artist);
        }catch (Exception e){
            throw new DatabaseException("Failed to add artist to the repository: " + e.getMessage());
        }
    }

    /**
     * Enrolls a listener to follow a specific artist.
     *
     * @param listener The listener who will follow the artist.
     * @param artist The artist to be followed by the listener.
     * @throws ValidationException if the listener is null.
     * @throws EntityNotFoundException if the artist is null or not found.
     */
    public void enrollListenerToArtist(Listener listener, Artist artist) {
        if (listener == null) {
            throw new ValidationException("Listener cannot be null.");
        }
        if (artist == null) {
            throw new EntityNotFoundException("Artist not found.");
        }
        try {
            artist.getFollowers().add(listener);
        }catch (Exception e){
            throw new BusinessLogicException("Failed to enroll listener to artist: " + e.getMessage());
        }
    }

    /**
     * Retrieves an artist from the repository based on their name.
     *
     * @param name The name of the artist to retrieve.
     * @return The artist with the specified name, or throws an exception if no artist is found.
     * @throws ValidationException if the artist name is null or empty.
     * @throws EntityNotFoundException if the artist is not found.
     */
    public Artist getArtistByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Artist name cannot be null or empty.");
        }
        try {
            return artistRepository.getAll().values().stream()
                    .filter(a -> a.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Artist with name '" + name + "' not found."));
        }catch (DatabaseException e){
            throw new DatabaseException("Database error while retrieving artist by name: " + e.getMessage());
        }
    }

    /**
     * Retrieves and displays the discography of a specific artist by their name.
     * This includes all albums and their respective songs.
     *
     * @param artistName The name of the artist whose discography will be displayed.
     * @throws ValidationException if the artist name is null or empty.
     * @throws EntityNotFoundException if the artist is not found.
     */
    public void showDiscography(String artistName) {
        if (artistName == null || artistName.isEmpty()) {
            throw new ValidationException("Artist name cannot be null or empty.");
        }
        Artist artist;
        try {
            artist = artistRepository.getAll().values()
                    .stream()
                    .filter(a -> a.getName().equalsIgnoreCase(artistName))
                    .findFirst()
                    .orElseThrow(()-> new EntityNotFoundException("Artist with name '" + artistName + "' not found."));
        }catch(DatabaseException e){
            throw new DatabaseException("Database error while retrieving artist discography: " + e.getMessage());
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

    /**
     * Filters albums by genre for a specific artist.
     *
     * @param artistId The ID of the artist.
     * @param genreName The genre name to filter albums by.
     * @return List of albums matching the genre.
     * @throws ValidationException if the genre name is null or empty.
     * @throws EntityNotFoundException if the artist is not found.
     */
    public List<Album> filterAlbumsByGenre(int artistId, String genreName) {
        if (genreName == null || genreName.isEmpty()) {
            throw new ValidationException("Genre name cannot be null or empty.");
        }

        Artist artist = artistRepository.get(artistId);
        if (artist == null) {
            throw new EntityNotFoundException("Artist with ID '" + artistId + "' not found.");
        }

        return artist.getAlbums().stream()
                .filter(album -> album.getGenre().getName().equalsIgnoreCase(genreName))
                .collect(Collectors.toList());
    }

    /**
     * Filters songs by minimum duration for a specific artist.
     *
     * @param artistId The ID of the artist.
     * @param minDuration Minimum duration of the songs to be returned.
     * @return List of songs with duration greater than or equal to minDuration.
     * @throws ValidationException if the minimum duration is invalid.
     * @throws EntityNotFoundException if the artist is not found.
     */
    public List<Song> filterSongsByMinimumDuration(int artistId, float minDuration) {
        if (minDuration <= 0) {
            throw new ValidationException("Minimum duration must be greater than 0.");
        }

        Artist artist = artistRepository.get(artistId);
        if (artist == null) {
            throw new EntityNotFoundException("Artist with ID '" + artistId + "' not found.");
        }

        return artist.getAlbums().stream()
                .flatMap(album -> album.getSongs().stream())
                .filter(song -> song.getDuration() >= minDuration)
                .collect(Collectors.toList());
    }

    /**
     * Sort albums by their release date
     *
     * @param artistId the id of the artist
     * @return list of sorted albums by the release date
     * @throws EntityNotFoundException if the artist is not found.
     */
    public List<Album> sortAlbumsByReleaseDate(int artistId) {
        Artist artist = artistRepository.get(artistId);
        if (artist == null) {
            throw new EntityNotFoundException("Artist with ID '" + artistId + "' not found.");
        }
        return artist.getAlbums().stream()
                .sorted(Comparator.comparing(Album::getReleaseDate))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the total number of songs for each artist and sorts them by the number of songs.
     * It also considers the total number of albums for each artist.
     *
     * @return List of artists sorted by the total number of songs and albums.
     * @throws DatabaseException if a database error occurs while retrieving the data.
     */
    public List<Artist> getArtistsWithMostSongsAndAlbums() {
        try {
            Map<Artist, Integer> artistSongCountMap = new HashMap<>();
            for (Artist artist : artistRepository.getAll().values()) {
                int totalSongs = artist.getAlbums().stream()
                        .mapToInt(album -> album.getSongs().size())
                        .sum();
                artistSongCountMap.put(artist, totalSongs);
            }

            return artistSongCountMap.entrySet().stream()
                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }catch (DatabaseException e){
            throw new DatabaseException("Database error while calculating artists with most songs and albums: " + e.getMessage());
        }
    }

}
