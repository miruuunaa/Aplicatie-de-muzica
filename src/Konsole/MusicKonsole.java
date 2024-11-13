package Konsole;
import Controller.MusicController;
import Domain.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
/**The MusicKonsole class represents the console interface for the MTIFY Music application.
 * It interacts with the user to allow listeners and artists to manage their accounts, playlists, and concerts.
 * It provides a menu-driven system for navigation between different functionalities.*/
public class MusicKonsole {
    private final MusicController musicController;
    private final Scanner scanner;
    /*** Constructs a MusicKonsole object with the provided MusicController.
     * Initializes the scanner for user input
     * @param musicController The controller used to handle user requests and interactions.*/
    public MusicKonsole(MusicController musicController) {
        this.musicController = musicController;
        this.scanner = new Scanner(System.in);
    }
    /** Displays the main menu and navigates to the appropriate sub-menu based on the user's choice.
     * This method loops infinitely until the user exits the application.*/
    public void showMainMenu() {
        Listener currentListener = null;
        Artist currentArtist = null;
        while (true) {
            System.out.println("Welcome to the MTIFY Music!");
            System.out.println("1. Log in as Listener");
            System.out.println("2. Log in as Artist");
            System.out.println("3. Create new Account");
            System.out.println("4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    currentListener = listenerLogin();
                    if (currentListener != null) {
                        listenerMenu();
                        listenerLogout(currentListener);  // Log out the listener after exiting menu
                        currentListener = null;
                    }
                    break;
                case 2:
                    currentArtist = artistLogin();
                    if (currentArtist != null) {
                        artistMenu();
                        artistLogout(currentArtist);      // Log out the artist after exiting menu
                        currentArtist = null;             // Reset the current artist
                    }
                    break;
                case 3:
                    createNewAccount();
                    break;
                case 4:
                    System.out.println("Exiting the application. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    /** Displays the listener-specific menu and allows the listener to manage their playlists,
     * songs, subscriptions, and other functionalities.
     * This method loops infinitely until the listener exits the menu.*/
    private void listenerMenu() {
        while (true) {
            System.out.println("Listener Menu:");
            System.out.println("1. Follow Artist");
            System.out.println("2. Create Playlist");
            System.out.println("3. Add Song to Playlist");
            System.out.println("4. Remove Song from Playlist");
            System.out.println("5. Play Playlist");
            System.out.println("6. Pause Playlist");
            System.out.println("7. Stop Playlist");
            System.out.println("8. Play Song");
            System.out.println("9. Pause Song");
            System.out.println("10. Stop Song");
            System.out.println("11. View Songs in Playlist");
            System.out.println("12. Create Subscription");
            System.out.println("13. Upgrade Subscription");
            System.out.println("14. Cancel Subscription");
            System.out.println("15. Recommend Songs");
            System.out.println("16. Recommend Artists");
            System.out.println("17. Get Top Genres");
            System.out.println("18. Add Song to history");
            System.out.println("19. Get History");
            System.out.println("20. View Available Concerts");
            System.out.println("21. Check user acces");
            System.out.println("22. Check Ticket Availability");
            System.out.println("23. Attend Concert");
            System.out.println("24. View artist Discography");
            System.out.println("25. Back to Main Menu");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    followArtist();
                    break;
                case 2:
                    createPlaylist();
                    break;
                case 3:
                    addSongToPlaylist();
                    break;
                case 4:
                    removeSongFromPlaylist();
                    break;
                case 5:
                    playPlaylist();
                    break;
                case 6:
                    pausePlaylist();
                    break;
                case 7:
                    stopPlaylist();
                    break;
                case 8:
                    playSong();
                    break;
                case 9:
                    pauseSong();
                    break;
                case 10:
                    stopSong();
                    break;
                case 11:
                    viewSongsInPlaylist();
                    break;
                case 12:
                    createSubscription();
                    break;
                case 13:
                    upgradeSubscription();
                    break;
                case 14:
                    cancelSubscription();
                    break;
                case 15:
                    recommendSongs();
                    break;
                case 16:
                    recommendArtists();
                    break;
                case 17:
                    getTopGenres();
                    break;
                case 18:
                    addSongToHistory();
                    break;
                case 19:
                    getHistory();
                    break;
                case 20:
                    viewAvailableConcerts();
                    break;
                case 21:
                    checkConcertAccess();
                    break;
                case 22:
                    checkTicketAvailability();
                    break;
                case 23:
                    attendConcert();
                    break;
                case 24:
                    viewArtistDiscography();
                case 25:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    /**Displays the artist-specific menu and allows the artist to manage their albums, songs,
     * concerts, and other functionalities.
     * This method loops infinitely until the artist exits the menu.*/
    private void artistMenu() {
        while (true) {
            System.out.println("Artist Menu:");
            System.out.println("1. Create Album");
            System.out.println("2. Upload Song");
            System.out.println("3. Start Concert");
            System.out.println("4. End Concert");
            System.out.println("5. Get Available Albums");
            System.out.println("6. Get Songs in Album");
            System.out.println("7. Remove Songs from Album");
            System.out.println("8. Validate Album Songs");
            System.out.println("9. View Followers");
            System.out.println("10. View Attendees for Concert");
            System.out.println("11. Back to Main Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createAlbum();
                    break;
                case 2:
                    uploadSong();
                    break;
                case 3:
                    startConcert();
                    break;
                case 4:
                    endConcert();
                    break;
                case 5:
                    listAvailableAlbums();
                    break;
                case 6:
                    getSongsInAlbum();
                    break;
                case 7:
                    removeSongFromAlbum();
                    break;
                case 8:
                    validateAlbumSongs();
                    break;
                case 9:
                    viewFollowers();
                    break;
                case 10:
                    viewAttendeesForConcert();
                case 11:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    /** Attempts to authenticate as a listener by prompting for a username.
     * If a valid listener is found, logs them in and returns the Listener object.
     * @return the logged-in Listener object if successful, null otherwise.*/
    private Listener listenerLogin() {
        System.out.println("Enter Listener username:");
        String username = scanner.nextLine();
        Listener listener = musicController.getListenerByName(username);
        if (listener != null) {
            musicController.setCurrentListener(listener);
            listener.login();
            return listener;
        } else {
            System.out.println("Invalid username. Please try again.");
            return null;
        }
    }
    /** Logs out the current listener.
     * @param listener the currently logged-in Listener object.*/
    private void listenerLogout(Listener listener) {
        listener.logout();
    }
    /** Attempts to authenticate as an artist by prompting for a username.
     * If a valid artist is found, logs them in and returns the Artist object.
     * @return the logged-in Artist object if successful, null otherwise.*/
    private Artist artistLogin() {
        System.out.println("Enter Artist username:");
        String username = scanner.nextLine();
        Artist artist = musicController.getArtistByName(username);
        if (artist != null) {
            artist.login();
            return artist;
        } else {
            System.out.println("Invalid username. Please try again.");
            return null;
        }
    }
    /** Logs out the current artist.
     * @param artist the currently logged-in Artist object.*/
    private void artistLogout(Artist artist) {
        artist.logout();
    }
    /** This method displays a menu allowing the user to choose between creating a Listener or Artist account.
     * Based on the user's choice, the corresponding account creation method is called.*/
    private void createNewAccount() {
        System.out.println("Choose account type to create:");
        System.out.println("1. Listener");
        System.out.println("2. Artist");
        int accountType = scanner.nextInt();
        scanner.nextLine();
        switch (accountType) {
            case 1:
                createListenerAccount();
                break;
            case 2:
                createArtistAccount();
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }
    /** Prompts the user to enter details for creating a Listener account. The user is asked for their name and email.
     * A new Listener object is created and registered using the MusicController instance.
     * A success message is displayed after the account is created.*/
    private void createListenerAccount() {
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        Listener newListener = new Listener(name, email);
        musicController.registerListener(newListener);
        System.out.println("Listener account created successfully! You can now log in as a Listener.");
    }
    /** Prompts the user to enter details for creating an Artist account. The user is asked for their name and email.
     * A new Artist object is created and registered using the MusicController instance.
     * A success message is displayed after the account is created.*/
    private void createArtistAccount() {
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        Artist newArtist = new Artist(name, email);
        musicController.registerArtist(newArtist);
        System.out.println("Artist account created successfully! You can now log in as an Artist.");
    }
    // ----------------- LISTENER MENU  METHODS -----------------
    /**1Follows an artist for a specific listener.
     * Prompts the user for their listener name and the artist they wish to follow,
     * and then enrolls the listener to follow that artist.*/
    private void followArtist() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String listenerName = scanner.nextLine();
        Listener listener = musicController.getListenerByName(listenerName);// Căutăm listener-ul pe baza numelui (sau emailului dacă este cazul)
        if (listener != null) {
            System.out.println("Enter artist name to follow:");
            String artistName = scanner.nextLine();
            Artist artist = musicController.getArtistByName(artistName);// Căutăm artistul după nume
            if (artist != null) {
                musicController.enrollListenerToArtist(listener, artist);// Înscriem listenerul la artist
                System.out.println(listener.getName() + " is now following " + artist.getName() + ".");
            } else {
                System.out.println("Artist not found.");
            }
        } else {
            System.out.println("Listener not found.");
        }
    }
    /**2Creates a new playlist for a listener.
     * Prompts the user for their username and the name of the playlist they want to create.
     * The new playlist is then added to the user's collection of playlists.*/
    private void createPlaylist() {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        Listener user = musicController.getListenerByName(username); // Adjust if needed
        if (user == null) {
            System.out.println("User not found. Please make sure you are logged in.");
            return;
        }
        System.out.println("Enter playlist name:");
        String playlistName = scanner.nextLine();
        Playlist playlist = new Playlist(playlistName, user);
        musicController.addPlaylist(playlist); // Adjust to call controller method
        System.out.println("Playlist " + playlistName + " created successfully.");
    }
    /**3Adds a song to a specific playlist.
     * Prompts the user for the playlist and song they wish to add. If both are valid,
     * the song is added to the specified playlist.*/
    private void addSongToPlaylist() {
        System.out.println("Enter playlist name:");
        String playlistName = scanner.nextLine();
        Playlist playlist = musicController.getPlaylistByName(playlistName);// Apelăm funcția din controller pentru a obține playlist-ul
        if (playlist != null) {
            System.out.println("Enter song title to add:");
            String songTitle = scanner.nextLine();
            Song song = musicController.getSongByTitle(songTitle);// Apelăm funcția din controller pentru a obține piesa
            if (song != null) {
                musicController.addSongToPlaylist(playlist.getName(), song.getTitle());// Apelăm funcția din controller pentru a adăuga piesa la playlist
                System.out.println(song.getTitle() + " added to playlist " + playlistName + ".");
            } else {
                System.out.println("Song not found.");
            }
        } else {
            System.out.println("Playlist not found.");
        }
    }
    /**4Removes a song from a specified playlist.
     * Prompts the user for the playlist and song to remove. If both are valid,
     * the song is removed from the playlist.*/
    private void removeSongFromPlaylist() {
        Scanner scanner = new Scanner(System.in);
        String playlistName = scanner.nextLine();// Cerem numele playlist-ului
        System.out.println("Enter playlist name:");
        Playlist playlist = musicController.getPlaylistByName(playlistName);
        if (playlist != null) {
            System.out.println("Enter song title to remove:");
            String songTitle = scanner.nextLine();
            Song song = musicController.getSongByTitle(songTitle);
            if (song != null) {
                musicController.removeSongFromPlaylist(playlistName, songTitle);// Apelăm funcția din Controller pentru a elimina piesa din playlist
            } else {
                System.out.println("Song not found.");
            }
        } else {
            System.out.println("Playlist not found.");
        }
    }
    /**5Plays a specified playlist.
     * Prompts the user for the playlist they want to play, and then starts playing the playlist if it exists.*/
    private void playPlaylist() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the playlist name:");
        String playlistName = scanner.nextLine();
        musicController.playPlaylist(playlistName);// Apelăm funcția din controller care va interacționa cu serviciul
    }
    /**6Pauses a specified playlist.
     * Prompts the user for the playlist they want to pause, and pauses it if it is currently playing.*/
    private void pausePlaylist() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the playlist name:");
        String playlistName = scanner.nextLine();
        musicController.pausePlaylist(playlistName);// Apelăm funcția din controller care va interacționa cu serviciul
    }
    /**7Stops a specified playlist.
     * Prompts the user for the playlist they want to stop, and stops it if it is currently playing.*/
    private void stopPlaylist() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the playlist name:");
        String playlistName = scanner.nextLine();
        musicController.stopPlaylist(playlistName);// Apelăm funcția din controller care va interacționa cu serviciul
    }
    /**8Plays a specific song.
     * Prompts the user for the song title they want to play, and then plays the song if it exists.*/
    private void playSong() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the song title:");
        String songTitle = scanner.nextLine();
        musicController.playSong(songTitle);// Apelăm funcția din controller care va interacționa cu serviciul
    }
    /**9Pauses a specific song.
     * Prompts the user for the song title they want to pause, and pauses it if it is currently playing.*/
    private void pauseSong() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the song title:");
        String songTitle = scanner.nextLine();
        musicController.pauseSong(songTitle);// Apelăm funcția din controller care va interacționa cu serviciul
    }
    /**10Stops a specific song.
     * Prompts the user for the song title they want to stop, and stops it if it is currently playing.*/
    private void stopSong() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the song title:");
        String songTitle = scanner.nextLine();
        musicController.stopSong(songTitle);// Apelăm funcția din controller care va interacționa cu serviciul
    }
    /**11Displays all songs in a specified playlist.
     * Prompts the user for the playlist name and displays the list of songs in the playlist if it exists.*/
    private void viewSongsInPlaylist() {
        System.out.println("Enter playlist name:");
        String playlistName = scanner.nextLine();
        Playlist playlist = musicController.getPlaylistByName(playlistName); // Adjust if needed
        if (playlist != null) {
            List<Song> songs = playlist.getSongs();
            System.out.println("Songs in playlist " + playlistName + ":");
            for (Song song : songs) {
                System.out.println("- " + song.getTitle());
            }
        } else {
            System.out.println("Playlist not found.");
        }
    }
    /**Prompts the user to create a subscription for an existing listener.
     * This method checks if the listener exists by username, then allows the
     * user to select a subscription type (Basic or Premium). If the listener
     * is not found, an account creation prompt is displayed.*/
    private void createSubscription() {
        System.out.println("Enter Listener username:");// Verificăm dacă un listener este deja logat
        String username = scanner.nextLine();
        Listener listener = musicController.getListenerByName(username);// Căutăm listenerul după nume
        if (listener == null) {
            System.out.println("Listener not found. Please create an account first.");
            return;
        }
        System.out.println("Choose subscription type to create:");// Dacă listener-ul există, continuăm să creăm abonamentul
        System.out.println("1. Basic");
        System.out.println("2. Premium");
        int subscriptionChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        String subscriptionType = "";
        switch (subscriptionChoice) {
            case 1:
                subscriptionType = "basic";
                break;
            case 2:
                subscriptionType = "premium";
                break;
            default:
                System.out.println("Invalid subscription type.");
                return;
        }
        musicController.createListenerSubscription(listener.getName(), subscriptionType);// Apelăm funcția din Controller pentru a crea abonamentul
    }
    /**12Upgrades the subscription type for a listener.
     * Prompts the user for their username and the new subscription type, then upgrades their subscription accordingly.*/
    private void upgradeSubscription() {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        Listener user = musicController.getListenerByName(username);
        if (user == null) {
            System.out.println("User not found. Please make sure you are logged in.");
            return;
        }
        System.out.println("Enter your subscription type (basic/premium):");
        String type = scanner.nextLine();
        musicController.upgradeSubscription(user, type);// Apelăm SubscriptionService pentru a upgradui abonamentul
    }
    /**13Cancels the active subscription for a listener.
     * Prompts the user for their username and cancels their subscription if it is active.*/
    private void cancelSubscription() {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        Listener listener = musicController.getListenerByName(username);
        if (listener == null) {
            System.out.println("User not found. Please make sure you are logged in.");
            return;
        }
        boolean isCanceled = musicController.cancelSubscription(listener);// Apelăm cancelSubscription din controller, care va delega la SubscriptionService
        if (isCanceled) {
            System.out.println("Subscription canceled for " + username + ".");
        } else {
            System.out.println("No active subscription found for " + username + ".");
        }
    }
    /**14Recommends songs to a listener based on their preferences or activity.
     * Prompts the user for the listener's name and displays recommended songs for that listener.*/
    private void recommendSongs() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username to recommend songs:");
        String listenerName = scanner.nextLine();
        Listener listener = musicController.getListenerByName(listenerName);// Găsim listenerul pe baza numelui
        if (listener != null) {
            List<Song> recommendedSongs = musicController.recommendSongsForListener(listener);// Obținem recomandările de piese
            System.out.println("Recommended Songs for " + listener.getName() + ":");
            if (recommendedSongs.isEmpty()) {
                System.out.println("No recommendations available.");
            } else {
                for (Song song : recommendedSongs) {
                    System.out.println("- " + song.getTitle() + " by " + song.getAlbum().getArtist().getName());
                }
            }
        } else {
            System.out.println("Listener not found.");
        }
    }
    /**15Recommends a list of artists based on the listener's preferences.
     * Prompts the user for their username, retrieves the listener, and fetches artist recommendations based on their listening habits.
     * Displays the list of recommended artists.*/
    private void recommendArtists() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username to recommend artists:");
        String listenerName = scanner.nextLine();
        Listener listener = musicController.getListenerByName(listenerName);// Găsim listenerul pe baza numelui
        if (listener != null) {
            List<Artist> recommendedArtists = musicController.recommendArtistsForListener(listener);// Obținem recomandările de artiști
            System.out.println("Recommended Artists for " + listener.getName() + ":");
            if (recommendedArtists.isEmpty()) {
                System.out.println("No recommendations available.");
            } else {
                for (Artist artist : recommendedArtists) {
                    System.out.println("- " + artist.getName());
                }
            }
        } else {
            System.out.println("Listener not found.");
        }
    }
    /**16Retrieves and displays the top genres for a specific listener.
     * Prompts the user for their username and retrieves the top genres based on the listener's preferences.
     * Displays a list of top genres for the listener.*/
    private void getTopGenres() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username to get top genres:");
        String listenerName = scanner.nextLine();
        Listener listener = musicController.getListenerByName(listenerName);// Găsim listenerul pe baza numelui
        if (listener != null) {
            List<Genre> topGenres = musicController.getTopGenresForListener(listener);// Obținem topul genurilor pentru listener
            System.out.println("Top Genres for " + listener.getName() + ":");
            if (topGenres.isEmpty()) {
                System.out.println("No genres found.");
            } else {
                for (Genre genre : topGenres) {
                    System.out.println("- " + genre.getName());
                }
            }
        } else {
            System.out.println("Listener not found.");
        }
    }
    /**17Adds a song to the listener's listening history.
     * Prompts the user for their username and the song they want to add to their history.
     * If the song exists, it will be added to the listener's history.*/
    private void addSongToHistory() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username:");// Cerem numele listener-ului
        String listenerName = scanner.nextLine();

        Listener listener = musicController.getListenerByName(listenerName);// Verificăm dacă există listener-ul
        if (listener == null) {
            System.out.println("Listener not found.");
            return;
        }
        System.out.println("Enter the title of the song you want to add to " + listenerName + "'s history:");
        String songTitle = scanner.nextLine();

        Song song = musicController.getSongByTitle(songTitle);// Verificăm dacă piesa există
        if (song != null) {
            musicController.addSongToHistory(listenerName, songTitle);// Apelăm metoda din controller pentru a adăuga piesa în istoricul listener-ului
        } else {
            System.out.println("Song not found.");
        }
    }
    /**18 Retrieves and displays the listening history of a specific listener.
     * Prompts the user for their username and fetches the list of songs that the listener has previously listened to.
     * Displays the songs in the listener's history.*/
    private void getHistory() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        String listenerName = scanner.nextLine();
        List<Song> songs = musicController.getHistory(listenerName);// Apelăm corect funcția din Controller
        if (songs != null && !songs.isEmpty()) {
            System.out.println("History for " + listenerName + ":");
            for (Song song : songs) {
                System.out.println("- " + song.getTitle());
            }
        } else {
            System.out.println("No songs found in history for " + listenerName + ".");
        }
    }
    /**19 Displays the list of available concerts.
     * Retrieves the list of concerts and displays their titles to the user.*/
    private void viewAvailableConcerts() {
        List<LiveConcert> concerts = musicController.getAvailableConcerts();
        System.out.println("Available concerts:");
        for (LiveConcert concert : concerts) {
            System.out.println("- " + concert.getTitle());
        }
    }
    /**20 Checks whether a specific listener has access to a concert.
     * Prompts the user for the concert title and listener's username.
     * If the concert and listener are found, checks if the listener has access to the concert and displays the result.*/
    private void checkConcertAccess() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the concert title:");
        String concertTitle = scanner.nextLine();
        LiveConcert concert = musicController.getAvailableConcerts() // Retrieve concert by title
                .stream()
                .filter(c -> c.getTitle().equalsIgnoreCase(concertTitle))
                .findFirst()
                .orElse(null);
        if (concert != null) {
            System.out.println("Enter your username  to check access:");
            String listenerName = scanner.nextLine();
            Listener listener = musicController.getListenerByName(listenerName);// Get listener by name
            if (listener != null) {
                boolean hasAccess = musicController.checkUserAccess(listener, concert);
                if (hasAccess) {
                    System.out.println(listener.getName() + " has access to the concert: " + concert.getTitle());
                } else {
                    System.out.println(listener.getName() + " does not have access to the concert: " + concert.getTitle());
                }
            } else {
                System.out.println("Listener not found.");
            }
        } else {
            System.out.println("Concert not found.");
        }
    }
    /**21 Checks the availability of tickets for a specific concert.
     * Prompts the user for the concert title and checks if tickets are available for that concert.
     * Displays the availability status of the concert tickets.*/
    private void checkTicketAvailability() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the concert title:");
        String concertTitle = scanner.nextLine();
        boolean isAvailable = musicController.checkTicketAvailability(concertTitle);// Apelează controllerul pentru a verifica disponibilitatea biletelor
        if (isAvailable) {
            System.out.println("Tickets are available for the concert: " + concertTitle);
        } else {
            System.out.println("No tickets available for the concert: " + concertTitle);
        }
    }
    /**22* Views the discography of a specific artist.
     * Prompts the user for the artist's name and displays a list of the artist's albums and songs.*/
    private void viewArtistDiscography() {
        System.out.println("Enter the name of the artist to view their discography:");
        String artistName = scanner.nextLine();
        musicController.viewArtistDiscography(artistName);
    }
    /***23 Registers a listener for a specific concert.
     * Prompts the user for the concert title and their username, checks if both the concert and the listener exist,
     * and registers the listener for the concert if they are not already registered.*/
    private void attendConcert() {
        System.out.println("Enter the title of the concert you want to attend:");
        String concertTitle = scanner.nextLine();
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        Listener listener = musicController.getListenerByName(username); // obține obiectul Listener după nume
        LiveConcert concert = musicController.getConcertByTitle(concertTitle); // obține concertul după titlu
        if (listener != null && concert != null) {
            boolean added = musicController.addAttendee(concert.getId(), listener);
            if (added) {
                System.out.println("You have successfully registered for the concert: " + concertTitle);
            } else {
                System.out.println("You are already registered for this concert.");
            }
        } else {
            System.out.println("Concert or listener not found.");
        }
    }
    // ----------------- ARTIST MENU  METHODS -----------------
    /**1* Prompts the user to enter details to create a new album. It asks for the album title,
     * release date, and artist name. If the artist exists, it creates an album and adds it to the system.
     * If the release date is invalid, an error message is shown.*/
    private void createAlbum() {
        System.out.println("Enter album title:");
        String albumTitle = scanner.nextLine();
        System.out.println("Enter release date (YYYY-MM-DD):");
        String dateInput = scanner.nextLine();
        try {
            LocalDate releaseDate = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
            System.out.println("Enter artist name:");
            String artistName = scanner.nextLine();
            Artist artist = musicController.getArtistByName(artistName);
            if (artist != null) {
                Album album = new Album(albumTitle, releaseDate, artist);
                musicController.addAlbum(album);
            } else {
                System.out.println("Artist not found.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        }
    }
    /**2
     * Prompts the user to upload a new song by entering the song title, artist name, song duration,
     * and album name. It creates the song and associates it with the album if found.
     * If the artist or album is not found, an error message is displayed.*/
    private void uploadSong() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter song title:");
        String songTitle = scanner.nextLine();
        System.out.println("Enter artist name:");
        String artistName = scanner.nextLine();
        Artist artist = musicController.getArtistByName(artistName);
        if (artist != null) {
            System.out.println("Enter song duration (in minutes, e.g., 3.5 for 3 minutes and 30 seconds):");
            float duration = Float.parseFloat(scanner.nextLine());
            System.out.println("Enter album name:");
            String albumName = scanner.nextLine();
            Album album = musicController.getAlbumByName(albumName);
            if (album == null) {
                System.out.println("Album not found. Proceeding without an album.");
            }
            Song song = new Song(songTitle, duration, album);
            musicController.addSong(song);
            System.out.println("Song " + songTitle + " uploaded successfully.");
        } else {
            System.out.println("Artist not found.");
        }
    }
    /**3* Prompts the user to start a concert by entering the concert title. It checks for concert details
     * and updates its properties like ticket count, ticket requirements, premium-only access, and availability
     * post-live. The concert is then started if all conditions are met.*/
    private void startConcert() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter concert title to start:");
        String concertTitle = scanner.nextLine();
        LiveConcert concert = musicController.getConcertByTitle(concertTitle);
        if (concert != null) {
            Artist artist = concert.getArtist();
            System.out.println("Artist for this concert: " + artist.getName());
            String eventType = concert.getEventType();
            System.out.println("Event Type: " + eventType);
            System.out.println("Enter ticket count:");
            int ticketCount = Integer.parseInt(scanner.nextLine());
            System.out.println("Is ticket required? (true/false):");
            boolean isTicketRequired = Boolean.parseBoolean(scanner.nextLine());
            System.out.println("Is this concert premium only? (true/false):");
            boolean isPremiumOnly = Boolean.parseBoolean(scanner.nextLine());
            System.out.println("Is the concert available post-live? (true/false):");
            boolean isAvailablePostLive = Boolean.parseBoolean(scanner.nextLine());
            concert.setTicketCount(ticketCount);
            concert.setTicketRequired(isTicketRequired);
            concert.setPremiumOnly(isPremiumOnly);
            concert.setAvailablePostLive(isAvailablePostLive);
            boolean areTicketsAvailable = musicController.checkTicketAvailability(concertTitle);
            if (areTicketsAvailable) {
                System.out.println("Tickets are available for the concert: " + concertTitle);
            } else {
                System.out.println("No tickets available for the concert: " + concertTitle);
            }
            musicController.startConcert(concertTitle);
        } else {
            System.out.println("Concert not found.");
        }
    }
    /***4 Prompts the user to enter a concert title and ends the specified concert.
     * Calls the method from the MusicController to stop the concert*/
    private void endConcert() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter concert title to end:");
        String concertTitle = scanner.nextLine();
        musicController.endConcert(concertTitle);
    }
    /**5* Lists all available albums in the system. Displays each album's title.*/
    private void listAvailableAlbums() {
        List<Album> albums = musicController.getAvailableAlbums();
        System.out.println("Available albums:");
        for (Album album : albums) {
            System.out.println("- " + album.getTitle());
        }
    }
    /**6* Prompts the user to enter the title of an album and retrieves a list of songs in that album.
     * If the album exists, it displays the song titles; otherwise, it displays an error message.*/
    private void getSongsInAlbum() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter album title:");
        String albumTitle = scanner.nextLine();
        Album album = musicController.getAlbumByName(albumTitle);
        if (album != null) {
            List<Song> songs = musicController.getSongsInAlbum(albumTitle);
            System.out.println("Songs in " + album.getTitle() + ":");
            for (Song song : songs) {
                System.out.println("- " + song.getTitle());
            }
        } else {
            System.out.println("Album not found.");
        }
    }
    /**7* Prompts the user to enter an album title and a song title to remove from the album.
     * If the album and song exist, the song is removed from the album.
     * If the song is not found, an error message is displayed.*/
    private void removeSongFromAlbum() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter album title:");
        String albumTitle = scanner.nextLine();
        Album album = musicController.getAlbumByName(albumTitle);
        if (album != null) {
            System.out.println("Enter song title to remove:");
            String songTitle = scanner.nextLine();
            Song song = musicController.getSongByTitle(songTitle);
            if (song != null && album.getSongs().contains(song)) {
                musicController.removeSongFromAlbum(albumTitle, songTitle);
                System.out.println("Song removed from album: " + songTitle);
            } else {
                System.out.println("Song not found in album.");
            }
        } else {
            System.out.println("Album not found.");
        }
    }
    /**8* Prompts the user to enter an album title and the maximum number of songs allowed in an album.
     * It validates whether the album exceeds the maximum allowed songs and displays the result.*/
    private void validateAlbumSongs() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter album title:");
        String albumTitle = scanner.nextLine();
        Album album = musicController.getAlbumByName(albumTitle);
        if (album != null) {
            System.out.println("Enter maximum number of songs allowed:");
            int maxSongs = scanner.nextInt();
            boolean isValid = musicController.validateAlbumSongs(album, maxSongs);
            System.out.println(isValid ? "Album is valid." : "Album exceeds the maximum allowed songs.");
        } else {
            System.out.println("Album not found.");
        }
    }
    /***9 Prompts the user to enter the name of an artist and displays the list of listeners who follow that artist.
     * If the artist is found, it lists their followers; otherwise, an error message is shown.*/
    private void viewFollowers() {
        System.out.println("Enter artist name:");
        String artistName = scanner.nextLine();
        Artist artist = musicController.getArtistByName(artistName);
        if (artist != null) {
            List<Listener> followers = artist.getFollowers();
            System.out.println("Followers of " + artist.getName() + ":");
            for (Listener follower : followers) {
                System.out.println("- " + follower.getName());
            }
        } else {
            System.out.println("Artist not found.");
        }
    }
    /***10 Prompts the user to enter a concert title and displays the list of attendees for that concert.
     * Calls the method from MusicController to display the concert attendees.*/
    private void viewAttendeesForConcert() {
        System.out.println("Enter the title of the concert to view attendees:");
        String concertTitle = scanner.nextLine();
        musicController.showAttendees(concertTitle);
    }
}
