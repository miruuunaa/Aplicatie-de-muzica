package Controller;
import Domain.*;
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
                           LiveConcertService liveConcertService, RecommendationService recommendationService,SubscriptionService subscriptionService
    ) {
        this.artistService = artistService;
        this.albumService = albumService;
        this.songService = songService;
        this.playlistService = playlistService;
        this.listenerService = listenerService;
        this.liveConcertService = liveConcertService;
        this.recommendationService = recommendationService;
        this.subscriptionService=subscriptionService;

    }

    /**
     * Registers a listener in the system.
     *
     * @param listener The listener to be registered.
     */
    public void registerListener(Listener listener) {
        listenerService.addListener(listener);
    }

    /**
     * Registers an artist in the system.
     *
     * @param artist The artist to be registered.
     */
    public void registerArtist(Artist artist) {
        artistService.addArtist(artist);
    }

    // ----------------- ARTIST METHODS -----------------

    /**
     * Enrolls a listener to an artist's fanbase.
     *
     * @param listener The listener to be enrolled.
     * @param artist The artist to enroll the listener to.
     */
    public void enrollListenerToArtist(Listener listener, Artist artist) {
        if (artist != null) {
            artistService.enrollListenerToArtist(listener, artist);
        } else {
            System.out.println("Artist not found.");
        }
    }

    /**
     * Retrieves an artist by name.
     *
     * @param name The name of the artist.
     * @return The artist object, or null if not found.
     */
    public Artist getArtistByName(String name){
        return artistService.getArtistByName(name);
    }

    /**
     * Displays the artist's discography.
     *
     * @param artistName The name of the artist.
     */
    public void viewArtistDiscography(String artistName) {
        artistService.showDiscography(artistName);
    }



    // ----------------- ALBUM METHODS -----------------

    /**
     * Retrieves a list of all available albums.
     *
     * @return A list of albums.
     */
    public List<Album> getAvailableAlbums() {
        return albumService.getAvailableAlbums();
    }

    /**
     * Retrieves the songs in an album by the album name.
     *
     * @param albumName The name of the album.
     * @return A list of songs in the album, or an empty list if the album is not found.
     */
    public List<Song> getSongsInAlbum(String albumName) {
        Album album = albumService.getAlbumByName(albumName);
        if (album != null) {
            return albumService.getSongsInAlbum(album);
        }
        System.out.println("Album not found.");
        return List.of();
    }

    /**
     * Adds an album to the system.
     *
     * @param album The album to be added.
     */
    public void addAlbum(Album album) {
        albumService.addAlbum(album);
    }

    /**
     * Removes a song from an album.
     *
     * @param albumName The name of the album.
     * @param songTitle The title of the song.
     */
    public void removeSongFromAlbum(String albumName, String songTitle) {
        Album album = albumService.getAlbumByName(albumName);
        Song song = songService.getSongByTitle(songTitle);

        if (album != null && song != null) {
            albumService.removeSongFromAlbum(album, song);
            System.out.println("Song " + songTitle + " removed from album " + albumName + ".");
        } else {
            System.out.println("Album or Song not found.");
        }
    }

    /**
     * Validates if the number of songs in an album does not exceed the maximum allowed.
     *
     * @param album The album to validate.
     * @param maxSongs The maximum number of songs allowed.
     * @return True if the album is valid, false otherwise.
     */
    public boolean validateAlbumSongs(Album album,int maxSongs){
        return albumService.validateAlbumSongs(album,maxSongs);
    }

    /**
     * Retrieves an album by name.
     *
     * @param albumName The name of the album.
     * @return The album object, or null if not found.
     */
    public Album getAlbumByName(String albumName){
        return albumService.getAlbumByName(albumName);
    }

    // ----------------- SONG METHODS -----------------

    /**
     * Adds a song to the system.
     *
     * @param song The song to be added.
     */
    public void addSong(Song song) {
        songService.addSong(song);
    }


    /**
     * Retrieves a song by title.
     *
     * @param title The title of the song.
     * @return The song object, or null if not found.
     */
    public Song getSongByTitle(String title) {
        return songService.getSongByTitle(title);
    }



    /**
     * Plays a song by its title.
     *
     * @param songTitle The title of the song to play.
     */
    public void playSong(String songTitle) {
        Song song= songService.getSongByTitle(songTitle);
        if (song != null) {
            songService.playSong(song);
        } else {
            System.out.println("Playlist not found.");
        }
    }

    /**
     * Stops a song by its title.
     *
     * @param songTitle The title of the song to stop.
     */
    public void stopSong(String  songTitle){
        Song song= songService.getSongByTitle(songTitle);
        if (song != null) {
            songService.stopSong(song);
        } else {
            System.out.println("Playlist not found.");
        }
    }
    /**
     * Pauses a song by its title.
     *
     * @param songTitle The title of the song to pause.
     */
    public void pauseSong(String  songTitle){
        Song song= songService.getSongByTitle(songTitle);
        if (song != null) {
            songService.pauseSong(song);
        } else {
            System.out.println("Playlist not found.");
        }
    }

    // ----------------- PLAYLIST METHODS -----------------

    /**
     * Adds a song to a playlist.
     *
     * @param playlistName The name of the playlist.
     * @param songTitle The title of the song to add.
     */
    public void addSongToPlaylist(String playlistName, String songTitle) {
        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        Song song = songService.getSongByTitle(songTitle);

        if (playlist != null && song != null) {
            playlistService.addSongToPlaylist(playlist, song);

        } else {
            System.out.println("Playlist or Song not found.");
        }
    }

    /**
     * Removes a song from a playlist.
     *
     * @param playlistName The name of the playlist.
     * @param songTitle The title of the song to remove.
     */
    public void removeSongFromPlaylist(String playlistName, String songTitle) {
        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        Song song = songService.getSongByTitle(songTitle);

        if (playlist != null && song != null) {
            playlistService.removeSongFromPlaylist(playlist, song);

        } else {
            System.out.println("Playlist or Song not found.");
        }
    }

    /**
     * Plays a playlist by name.
     *
     * @param playlistName The name of the playlist to play.
     */
    public void playPlaylist(String playlistName) {
        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        if (playlist != null) {
            playlistService.playPlaylist(playlist);
        } else {
            System.out.println("Playlist not found.");
        }
    }

    /**
     * Stops a playlist by name.
     *
     * @param playlistName The name of the playlist to stop.
     */
    public void stopPlaylist(String playlistName){
        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        if (playlist != null) {
            playlistService.stopPlaylist(playlist);
        } else {
            System.out.println("Playlist not found.");
        }
    }

    /**
     * Pauses a playlist by name.
     *
     * @param playlistName The name of the playlist to pause.
     */
    public void pausePlaylist(String playlistName){
        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        if (playlist != null) {
            playlistService.pausePlaylist(playlist);
        } else {
            System.out.println("Playlist not found.");
        }
    }

    /**
     * Adds a new playlist to the system.
     *
     * @param playlist The playlist to be added.
     */
    public void addPlaylist(Playlist playlist){
        playlistService.addPlaylist(playlist);
    }

    /**
     * Retrieves a playlist by its name.
     *
     * @param name The name of the playlist.
     * @return The playlist object, or null if not found.
     */
    public Playlist getPlaylistByName(String name){
        return playlistService.getPlaylistByName(name);
    }


    // ----------------- LISTENER METHODS -----------------

    /**
     * Retrieves a listener by name.
     *
     * @param name The name of the listener.
     * @return The listener object, or null if not found.
     */
    public Listener getListenerByName(String name) {
        return listenerService.getListenerByName(name);
    }


    // ----------------- LIVE CONCERT METHODS -----------------

    /**
     * Starts a live concert by title.
     *
     * @param concertTitle The title of the concert to start.
     */
    public void startConcert(String concertTitle) {
        liveConcertService.startConcert(concertTitle);
    }

    /**
     * Checks the availability of tickets for a concert.
     *
     * @param concertTitle The title of the concert.
     * @return True if tickets are available, false otherwise.
     */
    public boolean checkTicketAvailability(String concertTitle) {
        return liveConcertService.checkTicketAvailability(concertTitle);
    }

    /**
     * Checks if a listener has access to a concert.
     *
     * @param listener The listener to check.
     * @param concert The concert to check access for.
     * @return True if the listener has access, false otherwise.
     */
    public boolean checkUserAccess(Listener listener,LiveConcert concert){
        return liveConcertService.checkUserAccess(listener,concert);
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
     */
    public LiveConcert getConcertByTitle(String concertTitle) {
        return liveConcertService.getConcertByTitle(concertTitle);
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
     */
    public void showAttendees(String concertTitle) {
        liveConcertService.showAttendees(concertTitle);
    }




    // ----------------- RECOMMENDATION METHODS -----------------

    /**
     * Retrieves song recommendations for a listener.
     *
     * @param listener The listener to get recommendations for.
     * @return A list of recommended songs.
     */
    public List<Song> recommendSongsForListener(Listener listener) {
        History userHistory = listener.getHistory();
        return recommendationService.recommend_songs(userHistory);
    }

    /**
     * Retrieves artist recommendations for a listener.
     *
     * @param listener The listener to get artist recommendations for.
     * @return A list of recommended artists.
     */
    public List<Artist> recommendArtistsForListener(Listener listener) {
        History userHistory = listener.getHistory();
        return recommendationService.recommend_artists(userHistory);
    }

    /**
     * Retrieves the top genres recommended for a listener based on their listening history.
     * This method uses the listener's history to provide genre recommendations.
     *
     * @param listener The listener for whom to retrieve the top genres.
     * @return A list of the top genres for the given listener.
     */
    public List<Genre> getTopGenresForListener(Listener listener) {
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
     */
    public void addSongToHistory(String listenerName, String songTitle) {
        Listener listener = listenerService.getListenerByName(listenerName);
        Song song = songService.getSongByTitle(songTitle);

        if (listener != null && song != null) {
            listener.getHistory().addSongToHistory(song);
            System.out.println(song.getTitle() + " added to history for " + listener.getName());
        } else {
            System.out.println("Listener or Song not found.");
        }
    }

    /**
     * Retrieves the listening history of a specific listener. This method returns the list of songs
     * that the listener has added to their history.
     *
     * @param listenerName The name of the listener whose history is to be retrieved.
     * @return A list of songs from the listener's history, or an empty list if the listener is not found.
     */
    public List<Song> getHistory(String listenerName) {
        Listener listener = listenerService.getListenerByName(listenerName);
        if (listener != null) {
            return listener.getHistory().getSongs();
        } else {
            System.out.println("Listener not found.");
            return List.of();
        }
    }

    // ----------------- SUBSCRIPTION METHODS -----------------
    /**
     * Subscribes a listener to a music service.
     *
     * @param listener The listener to subscribe.
     */
    public void upgradeSubscription(Listener listener, String newType) {
        subscriptionService.upgradeSubscription(listener, newType);
    }

    /**
     * Unsubscribes a listener from a music service.
     *
     * @param listener The listener to unsubscribe.
     */
    public boolean cancelSubscription(Listener listener) {
        return subscriptionService.cancelSubscription(listener);
    }

    // Crearea unui abonament pentru un listener
    public void createListenerSubscription(String username, String subscriptionType) {
        // Găsește listener-ul după nume
        Listener listener = getListenerByName(username);

        if (listener == null) {
            System.out.println("Listener not found. Please create an account first.");
            return;
        }

        // Apelăm metoda din SubscriptionService pentru a crea abonamentul
        subscriptionService.createSubscription(listener, subscriptionType);
    }
    public void setCurrentListener(Listener listener){
        this.currentListener=listener;
    }
}

