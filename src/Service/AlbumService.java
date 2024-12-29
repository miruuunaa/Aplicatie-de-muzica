package Service;
import Domain.Album;
import Domain.Artist;
import Domain.Genre;
import Domain.Song;
import Exceptions.DatabaseException;
import Repository.IRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import Exceptions.BusinessLogicException;
import Exceptions.EntityNotFoundException;
import Exceptions.ValidationException;


/**
 * The AlbumService class provides various methods to manage albums within the music system.
 * It allows for the retrieval of available albums, the addition and removal of songs from albums,
 * and the validation of albums based on the number of songs.
 */
public class AlbumService {
    private final IRepository<Album> albumRepository;

    /**
     * Constructor that initializes the AlbumService with the given album repository.
     *
     * @param albumRepository The repository used to store and manage album data.
     */
    public AlbumService(IRepository<Album> albumRepository) {
        this.albumRepository = albumRepository;
    }

    /**
     * Retrieves a list of all available albums in the repository.
     *
     * @return A list of all albums in the repository.
     * @throws EntityNotFoundException if no albums are found.
     */
    public List<Album> getAvailableAlbums() {
        try {
            if (albumRepository.getAll().isEmpty()) {
                throw new EntityNotFoundException("No albums available in the repository.");
            }
            return new ArrayList<>(albumRepository.getAll().values());
        }catch(DatabaseException e){
            throw new DatabaseException("Database error while retrieving available albums: " + e.getMessage());
        }
    }

    /**
     * Retrieves the list of songs that belong to a specific album.
     *
     * @param album The album from which to retrieve the list of songs.
     * @return A list of songs contained in the specified album.
     * @throws ValidationException if the album is null.
     */
    public List<Song> getSongsInAlbum(Album album) {
        if (album == null) {
            throw new ValidationException("Album cannot be null.");
        }
        return album.getSongs();
    }

    /**
     * Removes a specific song from an album.
     * Prints a message confirming whether the song was removed or not.
     *
     * @param album The album from which the song will be removed.
     * @param song  The song to be removed from the album.
     * @throws ValidationException      if album or song is null.
     * @throws EntityNotFoundException if the song is not found in the album.
     */
    public void removeSongFromAlbum(Album album, Song song) {
        if (album == null) {
            throw new ValidationException("Album cannot be null.");
        }
        if (song == null) {
            throw new ValidationException("Song cannot be null.");
        }
        if (!album.getSongs().contains(song)) {
            throw new EntityNotFoundException("Song '" + song.getTitle() + "' not found in album '" + album.getTitle() + "'.");
        }
        try{
            album.getSongs().remove(song);
            System.out.println(song.getTitle() + " removed from album " + album.getTitle() + ".");
        }catch(Exception e){
            throw new BusinessLogicException("Failed to remove song from album: " + e.getMessage());
        }


    }

    /**
     * Validates if the album exceeds the maximum allowed number of songs.
     *
     * @param album   The album to validate.
     * @param maxSongs The maximum allowed number of songs in the album.
     * @return true if the album contains fewer or equal to the maximum number of songs, false otherwise.
     * @throws ValidationException      if the album is null or maxSongs is invalid.
     * @throws BusinessLogicException if the album exceeds the maximum number of songs.
     */
    public boolean validateAlbumSongs(Album album, int maxSongs) {
        if (album == null) {
            throw new ValidationException("Album cannot be null.");
        }
        if (maxSongs <= 0) {
            throw new ValidationException("Maximum number of songs must be greater than 0.");
        }

        if (album.getSongs().size() > maxSongs) {
            throw new BusinessLogicException("Album '" + album.getTitle() + "' exceeds the maximum allowed songs (" + maxSongs + ").");
        }

        return true;
    }


    /**
     * Adds a new album to the repository.
     *
     * @param album The album to be added to the repository.
     * @throws ValidationException  if the album is null.
     * @throws DatabaseException if a database error occurs while adding the album.
     */
    public void addAlbum(Album album) {
        if (album == null) {
            throw new ValidationException("Album cannot be null.");
        }
        try {
            albumRepository.create(album);
        }catch (Exception e){
            throw new DatabaseException("Failed to add album to the repository: " + e.getMessage());
        }
    }

    /**
     * Retrieves an album from the repository based on its name.
     *
     * @param albumName The name of the album to retrieve.
     * @return The album with the specified name, or null if no album is found.
     * @throws ValidationException      if the album name is invalid.
     * @throws EntityNotFoundException if the album is not found.
     */
    public Album getAlbumByName(String albumName) {
        if (albumName == null || albumName.isEmpty()) {
            throw new ValidationException("Album name cannot be null or empty.");
        }
        try {
            for (Album album : albumRepository.getAll().values()) {
                if (album.getTitle().equalsIgnoreCase(albumName)) {
                    return album;
                }
            }
            throw new EntityNotFoundException("Album with name '" + albumName + "' not found.");
        }catch (DatabaseException e){
            throw new DatabaseException("Database error while retrieving album by name: " + e.getMessage());
        }
    }

    /**
     * Sort songs of an album by the title
     *
     * @param albumId id of the album
     * @return list of songs sorted by the title
     * @throws EntityNotFoundException if the album is not found.
     * @throws DatabaseException if a database error occurs while retrieving the album.
     */
    public List<Song> sortSongsByTitle(int albumId) {
        try {
            Album album = albumRepository.get(albumId);
            if (album == null) {
                throw new EntityNotFoundException("Album with ID '" + albumId + "' not found.");
            }
            return album.getSongs().stream()
                    .sorted(Comparator.comparing(Song::getTitle))
                    .collect(Collectors.toList());
        }catch (DatabaseException e){
            throw new DatabaseException("Database error while retrieving album for sorting: " + e.getMessage());
        }
    }
    public boolean deleteAlbum(int albumId) throws DatabaseException {
        try {
            albumRepository.delete(albumId);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("An error occurred while deleting the album: " + e.getMessage());
        }
    }

    public boolean updateAlbumDetails(int albumId, String newName, String newGenre)
            throws EntityNotFoundException, ValidationException {

        if (newName == null || newName.trim().isEmpty()) {
            throw new ValidationException("Album name cannot be empty.");
        }

        if (newGenre == null || newGenre.trim().isEmpty()) {
            throw new ValidationException("Album genre cannot be empty.");
        }
        Album album = albumRepository.get(albumId);
        if (album == null) {
            throw new EntityNotFoundException("Album with ID " + albumId + " not found.");
        }
        album.setTitle(newName);
        album.setGenre(new Genre(newGenre));
        albumRepository.update(album);
        return true;
    }


}
