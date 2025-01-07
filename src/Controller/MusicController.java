package Controller;
import Domain.*;
import Exceptions.DatabaseException;
import Exceptions.EntityNotFoundException;
import Exceptions.ValidationException;
import Service.*;
import java.util.List;

/**
 * MusicController class is responsible for managing operations related to music entities like
 * Artists, Albums, Songs, Playlists, Listeners, Live Concerts, Recommendations, and Subscriptions.
 * It interacts with multiple service classes to perform high-level operations such as adding songs
 * to playlists, enrolling listeners to artists, managing album songs, starting/stopping concerts,
 * and handling subscriptions.
 */
public class MusicController {
    private final ArtistService artistService;
    private final AlbumService albumService;
    private final SongService songService;
    private final PlaylistService playlistService;
    private final ListenerService listenerService;
    private final LiveConcertService liveConcertService;
    private final RecommendationService recommendationService;
    private final SubscriptionService subscriptionService;
    private final GenreService genreService;
    private Listener currentListener;


    /**
     * Initializes the MusicController with all necessary services.
     *
     * @param artistService The service responsible for artist-related operations.
     * @param albumService The service responsible for album-related operations.
     * @param songService The service responsible for song-related operations.
     * @param playlistService The service responsible for playlist-related operations.
     * @param listenerService The service responsible for listener-related operations.
     * @param liveConcertService The service responsible for live concert-related operations.
     * @param recommendationService The service responsible for song and artist recommendations.
     * @param subscriptionService The service responsible for subscription-related operations.
     */
    public MusicController(ArtistService artistService, AlbumService albumService, SongService songService,
                           PlaylistService playlistService, ListenerService listenerService,
                           LiveConcertService liveConcertService, RecommendationService recommendationService,SubscriptionService subscriptionService,GenreService genreService
    ) {
        this.artistService = artistService;
        this.albumService = albumService;
        this.songService = songService;
        this.playlistService = playlistService;
        this.listenerService = listenerService;
        this.liveConcertService = liveConcertService;
        this.recommendationService = recommendationService;
        this.subscriptionService=subscriptionService;
        this.genreService=genreService;

    }

    /**
     * Registers a listener in the system.
     *
     * @param listener The listener to be registered.
     * @throws ValidationException if the listener object is invalid.
     * @throws DatabaseException if there is an issue while interacting with the database.
     */
    public void registerListener(Listener listener) throws ValidationException, DatabaseException {
        if (listener == null) {
            throw new ValidationException("Listener cannot be null.");
        }

        try {
            listenerService.addListener(listener);
        } catch (DatabaseException e) {
            throw new DatabaseException("Error occurred while adding listener to the database.", e);
        }
    }

    /**
     * Registers an artist in the system.
     *
     * @param artist The artist to be registered.
     * @throws ValidationException if the artist object is invalid.
     */
    public void registerArtist(Artist artist) throws ValidationException {
        if (artist == null) {
            throw new ValidationException("Artist cannot be null.");
        }
        try {
            artistService.addArtist(artist);
        }catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while registering the artist.", e);
        }
    }

    // ----------------- ARTIST METHODS -----------------

    /**
     * Enrolls a listener to an artist's fanbase.
     *
     * @param listener The listener to be enrolled.
     * @param artist The artist to enroll the listener to.
     * @throws EntityNotFoundException if the artist is not found.
     */
    public void enrollListenerToArtist(Listener listener, Artist artist) throws EntityNotFoundException {
        if (artist == null) {
            throw new EntityNotFoundException("Artist not found.");
        }
        try {
            artistService.enrollListenerToArtist(listener, artist);
        }catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while enrolling the listener to the artist's fanbase.", e);
        }

    }

    /**
     * Retrieves an artist by name.
     *
     * @param name The name of the artist.
     * @return The artist object, or null if not found.
     * @throws ValidationException if the name is null or empty.
     * @throws EntityNotFoundException if no artist is found with the given name.
     */
    public Artist getArtistByName(String name) throws ValidationException, EntityNotFoundException{
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Artist name cannot be null or empty.");
        }
        Artist artist= artistService.getArtistByName(name);
        if (artist == null) {
            throw new EntityNotFoundException("No artist found with the name " + name + ".");
        }

        return artist;
    }

    /**
     * Displays the artist's discography.
     *
     * @param artistName The name of the artist.
     * @throws ValidationException if the artistName is null or empty.
     * @throws EntityNotFoundException if no artist is found with the given name.
     */
    public void viewArtistDiscography(String artistName)  throws ValidationException, EntityNotFoundException {
        if (artistName == null || artistName.trim().isEmpty()) {
            throw new ValidationException("Artist name cannot be null or empty.");
        }
        try {
            artistService.showDiscography(artistName);
        }catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No artist found with the name " + artistName + ".", e);
        }
    }

    /**
     * Filters albums by genre for a specific artist.
     *
     * @param artistId The ID of the artist.
     * @param genreName The genre name to filter albums by.
     * @return List of albums matching the genre.
     * @throws EntityNotFoundException if the artist is not found or no albums match the genre.
     */
    public List<Album> filterAlbumsByGenre(int artistId, String genreName) throws EntityNotFoundException {
        try {
            return artistService.filterAlbumsByGenre(artistId, genreName);
        }catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No albums found for artist with ID " + artistId + " and genre " + genreName + ".", e);
        }
    }

    /**
     * Filters songs by minimum duration for a specific artist.
     *
     * @param artistId The ID of the artist.
     * @param minDuration Minimum duration of the songs to be returned.
     * @return List of songs with duration greater than or equal to minDurationInSeconds.
     * @throws EntityNotFoundException if no songs match the criteria for the given artist.
     */
    public List<Song> filterSongsByMinimumDuration(int artistId, float minDuration) throws EntityNotFoundException {
        try {
            return artistService.filterSongsByMinimumDuration(artistId, minDuration);
        }catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No songs found for artist with ID " + artistId + " and duration greater than " + minDuration + " seconds.", e);
        }
    }

    /**
     * Sorts albums by release date for a specific artist.
     * @param artistId  The ID of the artist.
     * @return List of albums sorted by release date.
     * @throws EntityNotFoundException if the artist or their albums are not found.
     */
    public List<Album> sortAlbumsByReleaseDate(int artistId) throws EntityNotFoundException {
        try {
            return artistService.sortAlbumsByReleaseDate(artistId);
        }catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No albums found for artist with ID " + artistId + " to sort by release date.", e);
        }
    }

    /**
     * Gets a sorted list of artists based on the number of songs and albums.
     *
     * @return A list of sorted artists.
     * @throws RuntimeException if an unexpected error occurs.
     */
    public List<Artist> getArtistsWithMostSongsAndAlbums() {
        try {
            return artistService.getArtistsWithMostSongsAndAlbums();
        }catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while retrieving sorted artists.", e);
        }
    }
    // ----------------- ALBUM METHODS -----------------

    /**
     * Retrieves a list of all available albums.
     *
     * @return A list of albums.
     * @throws DatabaseException if an error occurs while fetching albums from the database.
     */
    public List<Album> getAvailableAlbums() throws DatabaseException {
        try {
            return albumService.getAvailableAlbums();
        }catch (Exception e) {
            throw new DatabaseException("Error occurred while fetching available albums.", e);
        }
    }

    /**
     * Retrieves the songs in an album by the album name.
     *
     * @param albumName The name of the album.
     * @return A list of songs in the album, or an empty list if the album is not found.
     * @throws ValidationException if the album name is invalid or empty.
     * @throws EntityNotFoundException if the album is not found.
     */
    public List<Song> getSongsInAlbum(String albumName)  throws ValidationException, EntityNotFoundException {
        if (albumName == null || albumName.trim().isEmpty()) {
            throw new ValidationException("Album name cannot be null or empty.");
        }
        Album album = albumService.getAlbumByName(albumName);
        if (album == null) {
            throw new EntityNotFoundException("Album not found.");
        }
        return albumService.getSongsInAlbum(album);
    }

    /**
     * Adds an album to the system.
     *
     * @param album The album to be added.
     * @throws ValidationException if the album is null or invalid.
     * @throws DatabaseException if an error occurs while adding the album.
     */
    public void addAlbum(Album album) throws ValidationException, DatabaseException{
        if (album == null) {
            throw new ValidationException("Album cannot be null.");
        }
        try {
            albumService.addAlbum(album);
        } catch (Exception e) {
            throw new DatabaseException("Error occurred while adding the album.", e);
        }
    }

    /**
     * Removes a song from an album.
     *
     * @param albumName The name of the album.
     * @param songTitle The title of the song.
     * @throws EntityNotFoundException if the album or song is not found.
     */
    public void removeSongFromAlbum(String albumName, String songTitle) throws EntityNotFoundException {
        Album album = albumService.getAlbumByName(albumName);
        Song song = songService.getSongByTitle(songTitle);
        if (album == null) {
            throw new EntityNotFoundException("Album " + albumName + " not found.");
        }
        if (song == null) {
            throw new EntityNotFoundException("Song " + songTitle + " not found.");
        }
        albumService.removeSongFromAlbum(album, song);
        System.out.println("Song " + songTitle + " removed from album " + albumName + ".");
    }

    /**
     * Validates if the number of songs in an album does not exceed the maximum allowed.
     *
     * @param album The album to validate.
     * @param maxSongs The maximum number of songs allowed.
     * @return True if the album is valid, false otherwise.
     */
    public boolean validateAlbumSongs(Album album,int maxSongs) throws ValidationException{
        if (album == null) {
            throw new ValidationException("Album cannot be null.");
        }
        return albumService.validateAlbumSongs(album,maxSongs);
    }

    /**
     * Retrieves an album by name.
     *
     * @param albumName The name of the album.
     * @return The album object, or null if not found.
     * @throws EntityNotFoundException if the album is not found.
     */
    public Album getAlbumByName(String albumName) throws EntityNotFoundException{
        if (albumName == null || albumName.trim().isEmpty()) {
            throw new ValidationException("Album name cannot be null or empty.");
        }
        Album album= albumService.getAlbumByName(albumName);
        if (album == null) {
            throw new EntityNotFoundException("Album with name " + albumName + " not found.");
        }
        return album;
    }

    /**
     * Sorts songs in an album by their title.
     * @param albumId The ID of the album.
     * @return List of songs sorted by title.
     * @throws EntityNotFoundException if the album is not found.
     */
    public List<Song> sortSongsByTitle(int albumId) throws EntityNotFoundException{
        try {
            return albumService.sortSongsByTitle(albumId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No songs found for album with ID " + albumId, e);
        }
    }

    public void deleteAlbum(int albumId) {
        try {
            boolean isDeleted = albumService.deleteAlbum(albumId);
            if (isDeleted) {
                System.out.println("Album deleted successfully.");
            } else {
                System.out.println("Failed to delete album. The album might not exist.");
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void updateAlbumDetails(int albumId, String newName, String newGenre) {
        try {
            boolean isUpdated = albumService.updateAlbumDetails(albumId, newName, newGenre);
            if (isUpdated) {
                System.out.println("Album updated successfully.");
            } else {
                System.out.println("Failed to update album. The album might not exist.");
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("Validation error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // ----------------- SONG METHODS -----------------

    /**
     * Adds a song to the system.
     *
     * @param song The song to be added.
     * @throws ValidationException if the song is null or invalid.
     *  @throws DatabaseException if there is an error while adding the song.
     */
    public void addSong(Song song) throws ValidationException, DatabaseException{
        if (song == null) {
            throw new ValidationException("Song cannot be null.");
        }
        try {
            songService.addSong(song);
        } catch (Exception e) {
            throw new DatabaseException("Error occurred while adding the song.", e);
        }
    }


    /**
     * Retrieves a song by title.
     *
     * @param title The title of the song.
     * @return The song object, or null if not found.
     * @throws EntityNotFoundException if the song is not found.
     */
    public Song getSongByTitle(String title) throws EntityNotFoundException {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Song title cannot be null or empty.");
        }
        Song song=songService.getSongByTitle(title);
        if (song == null) {
            throw new EntityNotFoundException("Song with title " + title + " not found.");
        }

        return song;
    }



    /**
     * Plays a song by its title.
     *
     * @param songTitle The title of the song to play.
     * @throws EntityNotFoundException if the song is not found.
     */
    public void playSong(String songTitle) throws  EntityNotFoundException {
        Song song= songService.getSongByTitle(songTitle);
        if (song == null) {
            throw new EntityNotFoundException("Song with title " + songTitle + " not found.");
        }
        songService.playSong(song);
    }

    /**
     * Stops a song by its title.
     *
     * @param songTitle The title of the song to stop.
     * @throws EntityNotFoundException if the song is not found.
     */
    public void stopSong(String  songTitle) throws  EntityNotFoundException{
        Song song= songService.getSongByTitle(songTitle);
        if (song == null) {
            throw new EntityNotFoundException("Song with title " + songTitle + " not found.");
        }
        songService.stopSong(song);
    }

    /**
     * Pauses a song by its title.
     *
     * @param songTitle The title of the song to pause.
     * @throws EntityNotFoundException if the song is not found.
     */
    public void pauseSong(String  songTitle) throws EntityNotFoundException{
        Song song= songService.getSongByTitle(songTitle);
        if (song == null) {
            throw new EntityNotFoundException("Song with title " + songTitle + " not found.");
        }
        songService.pauseSong(song);
    }

    public void deleteSong(int songId) {
        try {
            boolean isDeleted = songService.deleteSong(songId);
            if (isDeleted) {
                System.out.println("Song deleted successfully.");
            } else {
                System.out.println("Failed to delete song. The song might not exist.");
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void updateSongDetails(int songId, String newTitle, String newGenre) {
        try {
            boolean isUpdated = songService.updateSongDetails(songId, newTitle, newGenre);
            if (isUpdated) {
                System.out.println("Song updated successfully.");
            } else {
                System.out.println("Failed to update song. The song might not exist.");
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("Validation error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // ----------------- PLAYLIST METHODS -----------------

    /**
     * Adds a song to a playlist.
     *
     * @param playlistName The name of the playlist.
     * @param songTitle The title of the song to add.
     * @throws EntityNotFoundException if the playlist or song is not found.
     */
    public void addSongToPlaylist(String playlistName, String songTitle) throws EntityNotFoundException {
        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        Song song = songService.getSongByTitle(songTitle);

        if (playlist == null) {
            throw new EntityNotFoundException("Playlist " + playlistName + " not found.");
        }

        if (song == null) {
            throw new EntityNotFoundException("Song " + songTitle + " not found.");
        }

        playlistService.addSongToPlaylist(playlist, song);
    }

    /**
     * Removes a song from a playlist.
     *
     * @param playlistName The name of the playlist.
     * @param songTitle The title of the song to remove.
     * @throws EntityNotFoundException if the playlist or song is not found.
     */
    public void removeSongFromPlaylist(String playlistName, String songTitle) throws EntityNotFoundException {
        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        Song song = songService.getSongByTitle(songTitle);

        if (playlist == null) {
            throw new EntityNotFoundException("Playlist " + playlistName + " not found.");
        }

        if (song == null) {
            throw new EntityNotFoundException("Song " + songTitle + " not found.");
        }

        playlistService.removeSongFromPlaylist(playlist, song);
    }

    /**
     * Plays a playlist by name.
     *
     * @param playlistName The name of the playlist to play.
     * @throws EntityNotFoundException if the playlist is not found.
     */
    public void playPlaylist(String playlistName) throws EntityNotFoundException {
        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        if (playlist == null) {
            throw new EntityNotFoundException("Playlist " + playlistName + " not found.");
        }

        playlistService.playPlaylist(playlist);
    }

    /**
     * Stops a playlist by name.
     *
     * @param playlistName The name of the playlist to stop.
     * @throws EntityNotFoundException if the playlist is not found.
     */
    public void stopPlaylist(String playlistName) throws EntityNotFoundException{
        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        if (playlist == null) {
            throw new EntityNotFoundException("Playlist " + playlistName + " not found.");
        }
        playlistService.stopPlaylist(playlist);
    }

    /**
     * Pauses a playlist by name.
     *
     * @param playlistName The name of the playlist to pause.
     * @throws EntityNotFoundException if the playlist is not found.
     */
    public void pausePlaylist(String playlistName) throws EntityNotFoundException{
        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        if (playlist == null) {
            throw new EntityNotFoundException("Playlist " + playlistName + " not found.");
        }

        playlistService.pausePlaylist(playlist);
    }

    /**
     * Adds a new playlist to the system.
     *
     * @param playlist The playlist to be added.
     * @throws ValidationException if the playlist is null or invalid.
     * @throws DatabaseException if an error occurs while adding the playlist.
     */
    public void addPlaylist(Playlist playlist)throws ValidationException, DatabaseException{
        if (playlist == null) {
            throw new ValidationException("Playlist cannot be null.");
        }
        try {
            playlistService.addPlaylist(playlist);
        } catch (Exception e) {
            throw new DatabaseException("Error occurred while adding the playlist.", e);
        }
    }

    /**
     * Retrieves a playlist by its name.
     *
     * @param name The name of the playlist.
     * @return The playlist object, or null if not found.
     * @throws EntityNotFoundException if the playlist is not found.
     */
    public Playlist getPlaylistByName(String name) throws EntityNotFoundException{
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Playlist name cannot be null or empty.");
        }

        Playlist playlist = playlistService.getPlaylistByName(name);

        if (playlist == null) {
            throw new EntityNotFoundException("Playlist with name " + name + " not found.");
        }

        return playlist;
    }


    // ----------------- LISTENER METHODS -----------------

    /**
     * Retrieves a listener by name.
     *
     * @param name The name of the listener.
     * @return The listener object, or null if not found.
     * @throws EntityNotFoundException if the listener is not found.
     */
    public Listener getListenerByName(String name) throws EntityNotFoundException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Listener name cannot be null or empty.");
        }

        Listener listener = listenerService.getListenerByName(name);

        if (listener == null) {
            throw new EntityNotFoundException("Listener with name " + name + " not found.");
        }

        return listener;
    }


    // ----------------- LIVE CONCERT METHODS -----------------

    /**
     * Starts a live concert by title.
     *
     * @param concertTitle The title of the concert to start.
     */
    public void startConcert(String concertTitle){
        liveConcertService.startConcert(concertTitle);
    }

    /**
     * Checks the availability of tickets for a concert.
     *
     * @param concertTitle The title of the concert.
     * @return True if tickets are available, false otherwise.
     * @throws EntityNotFoundException if the concert is not found.
     */
    public boolean checkTicketAvailability(String concertTitle) throws EntityNotFoundException {
        if (concertTitle == null || concertTitle.trim().isEmpty()) {
            throw new ValidationException("Concert title cannot be null or empty.");
        }

        boolean available = liveConcertService.checkTicketAvailability(concertTitle);
        if (!available) {
            throw new EntityNotFoundException("Concert with title " + concertTitle + " not found or no tickets available.");
        }

        return available;
    }
    /**
     * Ends a live concert by title.
     *
     * @param concertTitle The title of the concert to end.
     */
    public void endConcert(String concertTitle) {
        liveConcertService.endConcert(concertTitle);
    }

    /**
     * Retrieves a list of available live concerts.
     *
     * @return A list of live concerts.
     */
    public List<LiveConcert> getAvailableConcerts(){
        return liveConcertService.getAvailableConcerts();
    }

    /**
     * Retrieves a live concert by its title.
     *
     * @param concertTitle The title of the concert.
     * @return The concert object, or null if not found.
     *  @throws EntityNotFoundException if the concert is not found.
     */
    public LiveConcert getConcertByTitle(String concertTitle) throws EntityNotFoundException {
        if (concertTitle == null || concertTitle.trim().isEmpty()) {
            throw new ValidationException("Concert title cannot be null or empty.");
        }

        LiveConcert concert = liveConcertService.getConcertByTitle(concertTitle);
        if (concert == null) {
            throw new EntityNotFoundException("Concert with title " + concertTitle + " not found.");
        }

        return concert;
    }

    /**
     * Adds a listener as an attendee to a concert.
     *
     * @param concertid The ID of the concert.
     * @param listener The listener to add as an attendee.
     */
    public boolean addAttendee(int concertid,Listener listener){
        return liveConcertService.addAttendee(concertid,listener);
    }

    /**
     * Displays a list of attendees for a concert.
     *
     * @param concertTitle The title of the concert.
     * @throws EntityNotFoundException if the concert is not found.
     */
    public void showAttendees(String concertTitle) throws EntityNotFoundException {
        if (concertTitle == null || concertTitle.trim().isEmpty()) {
            throw new ValidationException("Concert title cannot be null or empty.");
        }

        LiveConcert concert = liveConcertService.getConcertByTitle(concertTitle);
        if (concert == null) {
            throw new EntityNotFoundException("Concert with title " + concertTitle + " not found.");
        }

        liveConcertService.showAttendees(concertTitle);
    }


    /**
     * Calculates the VIP score for a listener and a specific concert.
     *
     * @param listener The listener for whom the score is calculated.
     * @param concertId The ID of the concert.
     * @return The calculated VIP score.
     */
    public double getConcertVIPScore(Listener listener, int concertId) {
        return liveConcertService.calculateConcertVIPScore(listener, concertId);
    }

    // ----------------- RECOMMENDATION METHODS -----------------

    /**
     * Retrieves song recommendations for a listener.
     *
     * @param listener The listener to get recommendations for.
     * @return A list of recommended songs.
     * @throws EntityNotFoundException if the listener is not found.
     */
    public List<Song> recommendSongsForListener(Listener listener) throws EntityNotFoundException {
        if (listener == null) {
            throw new ValidationException("Listener cannot be null.");
        }
        History userHistory = listener.getHistory();
        return recommendationService.recommend_songs(userHistory);
    }

    /**
     * Retrieves artist recommendations for a listener.
     *
     * @param listener The listener to get artist recommendations for.
     * @return A list of recommended artists.
     * @throws EntityNotFoundException if the listener is not found.
     */
    public List<Artist> recommendArtistsForListener(Listener listener) throws EntityNotFoundException {
        if (listener == null) {
            throw new ValidationException("Listener cannot be null.");
        }
        History userHistory = listener.getHistory();
        return recommendationService.recommend_artists(userHistory);
    }

    /**
     * Retrieves the top genres recommended for a listener based on their listening history.
     * This method uses the listener's history to provide genre recommendations.
     *
     * @param listener The listener for whom to retrieve the top genres.
     * @return A list of the top genres for the given listener.
     *  @throws EntityNotFoundException if the listener is not found.
     */
    public List<Genre> getTopGenresForListener(Listener listener) throws EntityNotFoundException{
        if (listener == null) {
            throw new ValidationException("Listener cannot be null.");
        }
        History userHistory = listener.getHistory();
        return recommendationService.get_top_genres(userHistory);
    }

    // ----------------- HISTORY METHODS -----------------

    /**
     * Adds a song to the listener's history. This method updates the listener's listening history by
     * adding the specified song to their history list.
     *
     * @param listenerName The name of the listener.
     * @param songTitle The title of the song to add to the history.
     * @throws EntityNotFoundException if the listener or song is not found.
     */
    public void addSongToHistory(String listenerName, String songTitle) throws EntityNotFoundException{
        if (listenerName == null || listenerName.trim().isEmpty() || songTitle == null || songTitle.trim().isEmpty()) {
            throw new ValidationException("Listener name and song title cannot be null or empty.");
        }
        Listener listener = listenerService.getListenerByName(listenerName);
        Song song = songService.getSongByTitle(songTitle);

        if (listener == null) {
            throw new EntityNotFoundException("Listener with name " + listenerName + " not found.");
        }

        if (song == null) {
            throw new EntityNotFoundException("Song with title " + songTitle + " not found.");
        }

        listener.getHistory().addSongToHistory(song);
    }

    /**
     * Retrieves the listening history of a specific listener. This method returns the list of songs
     * that the listener has added to their history.
     *
     * @param listenerName The name of the listener whose history is to be retrieved.
     * @return A list of songs from the listener's history, or an empty list if the listener is not found.
     * @throws EntityNotFoundException if the listener is not found.
     */
    public List<Song> getHistory(String listenerName) throws EntityNotFoundException {
        if (listenerName == null || listenerName.trim().isEmpty()) {
            throw new ValidationException("Listener name cannot be null or empty.");
        }
        Listener listener = listenerService.getListenerByName(listenerName);
        if (listener == null) {
            throw new EntityNotFoundException("Listener with name " + listenerName + " not found.");
        }

        return listener.getHistory().getSongs();
    }

    // ----------------- SUBSCRIPTION METHODS -----------------
    /**
     * Subscribes a listener to a music service.
     *
     * @param listener The listener to subscribe.
     * @throws EntityNotFoundException if the listener is not found.
     * @throws ValidationException if the newType is invalid.
     */
    public void upgradeSubscription(Listener listener, String newType) throws EntityNotFoundException,ValidationException {
        if (listener == null) {
            throw new EntityNotFoundException("Listener not found.");
        }

        if (newType == null || newType.trim().isEmpty()) {
            throw new ValidationException("Subscription type cannot be null or empty.");
        }
        subscriptionService.upgradeSubscription(listener, newType);
    }

    /**
     * Unsubscribes a listener from a music service.
     *
     * @param listener The listener to unsubscribe.
     * @throws EntityNotFoundException if the listener is not found.
     */
    public boolean cancelSubscription(Listener listener) throws EntityNotFoundException {
        if (listener == null) {
            throw new EntityNotFoundException("Listener not found.");
        }
        return subscriptionService.cancelSubscription(listener);
    }


    /**
     * Create the subscription for the user
     *
     * @param username The username of the listener who will be assigned the subscription.
     * @param subscriptionType The type of subscription to be created for the listener (e.g., "Premium" or "Basic").
     * @throws EntityNotFoundException if the listener is not found.
     * @throws ValidationException if the subscriptionType is invalid.
     */
    public void createListenerSubscription(String username, String subscriptionType) throws EntityNotFoundException, ValidationException {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username cannot be null or empty.");
        }

        if (subscriptionType == null || subscriptionType.trim().isEmpty()) {
            throw new ValidationException("Subscription type cannot be null or empty.");
        }

        Listener listener = getListenerByName(username);
        if (listener == null) {
            throw new EntityNotFoundException("Listener with username " + username + " not found. Please create an account first.");
        }

        subscriptionService.createSubscription(listener, subscriptionType);
    }
    /**
     * Sets the current listener for the session.
     *
     * @param listener The listener to be set as the current listener in the system.
     */
    public void setCurrentListener(Listener listener){
        if (listener == null) {
            throw new ValidationException("Listener cannot be null.");
        }
        this.currentListener=listener;
    }

    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    public void addGenreToSystem(String genreName) {
        Genre genre = new Genre(genreName);
        genreService.addGenre(genre);
    }
}