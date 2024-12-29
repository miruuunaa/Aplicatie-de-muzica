package Konsole;
import Controller.MusicController;
import Domain.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import Exceptions.EntityNotFoundException;
import Exceptions.ValidationException;

import static org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.Conversions.toLowerCase;

/**The MusicKonsole class represents the console interface for the MTIFY Music application.
 * It interacts with the user to allow listeners and artists to manage their accounts, playlists, and concerts.
 * It provides a menu-driven system for navigation between different functionalities.*/
public class MusicKonsole {
    private final MusicController musicController;
    private final Scanner scanner;
    private Listener currentListener = null;
    private Artist currentArtist = null;

    /**
     * Constructs a MusicKonsole object with the provided MusicController.
     *
     * @param musicController The controller used to handle user requests and interactions.
     */
    public MusicKonsole(MusicController musicController) {
        this.musicController = musicController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the main menu and navigates to the appropriate sub-menu based on the user's choice.
     * This method loops infinitely until the user exits the application.
     */
    public void showMainMenu() {
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
                        listenerLogout();
                        currentListener = null;
                    }
                    break;
                case 2:
                    currentArtist = artistLogin();
                    if (currentArtist != null) {
                        artistMenu();
                        artistLogout();
                        currentArtist = null;
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

    /**
     * Displays the listener-specific menu and allows the listener to manage their playlists,songs, subscriptions, and other functionalities.
     * This method loops infinitely until the listener exits the menu.
     */
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
            System.out.println("21. Check Ticket Availability");
            System.out.println("22. Attend Concert");
            System.out.println("23. View artist Discography");
            System.out.println("24. Calculate Vip Score");
            System.out.println("25. Filter Albums by Genre");
            System.out.println("26. Filter Songs by Duration");
            System.out.println("27. Sort albums by release date");
            System.out.println("28. Sort songs by title");
            System.out.println("29. Display artists by total number of songs and albums");
            System.out.println("30. Back to Main Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();
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
                    checkTicketAvailability();
                    break;
                case 22:
                    attendConcert();
                    break;
                case 23:
                    viewArtistDiscography();
                    break;
                case 24:
                    showVIPScore();
                    break;
                case 25:
                    filterAlbumsByGenre();
                    break;
                case 26:
                    filterSongsByDuration();
                    break;
                case 27:
                    sortAlbumsByReleaseDate();
                    break;
                case 28:
                    sortSongsByTitle();
                    break;
                case 29:
                    displayArtistsWithMostSongsAndAlbums();
                case 30:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the artist-specific menu and allows the artist to manage their albums, songs,concerts, and other functionalities.
     * This method loops infinitely until the artist exits the menu.
     */
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
            System.out.println("11. Delete Album");
            System.out.println("12. Update Album Details");
            System.out.println("13. Delete Song");
            System.out.println("14. Update Song Details");
            System.out.println("15. Back to Main Menu");
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
                    break;
                case 11:
                    deleteAlbum();
                    break;
                case 12:
                    updateAlbumDetails();
                    break;
                case 13:
                    deleteSong();
                    break;
                case 14:
                    updateSongDetails();
                case 15:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Attempts to authenticate as a listener by prompting for a username and password.
     * If a valid listener is found and the password is correct, logs them in and returns the Listener object.
     *
     * @return the logged-in Listener object if successful, null otherwise.
     */
    private Listener listenerLogin() {
        System.out.println("Enter Listener username:");
        String username = scanner.nextLine();
        Listener listener = musicController.getListenerByName(username);
        if (listener != null) {
            System.out.println("Enter password:");
            String inputPassword = scanner.nextLine();
            if (listener.authenticate(inputPassword)) {
                currentListener = listener;
                musicController.setCurrentListener(listener);
                System.out.println("Login successful!");
                return listener;
            } else {
                System.out.println("Incorrect password. Please try again.");
                return null;
            }
        } else {
            System.out.println("Invalid username. Please try again.");
            return null;
        }
    }

    /**
     * Logs out the current listener.
     */
    private void listenerLogout() {
        if (currentListener != null) {
            currentListener.logout();
            currentListener = null;
        } else {
            System.out.println("No listener is currently logged in.");
        }
    }

    /**
     * Attempts to authenticate as an artist by prompting for a username and password.
     * If a valid artist is found and the password is correct, logs them in and returns the Artist object.
     *
     * @return the logged-in Artist object if successful, null otherwise.
     */
    private Artist artistLogin() {
        System.out.println("Enter Artist username:");
        String username = scanner.nextLine();
        Artist artist = musicController.getArtistByName(username);
        if (artist != null) {
            System.out.println("Enter password:");
            String inputPassword = scanner.nextLine();
            if (artist.authenticate(inputPassword)) {
                artist.login();
                System.out.println("Login successful!");
                return artist;
            } else {
                System.out.println("Incorrect password. Please try again.");
                return null;
            }
        } else {
            System.out.println("Invalid username. Please try again.");
            return null;
        }
    }

    /**
     * Logs out the current artist.
     */
    private void artistLogout() {
        if (currentArtist != null) {
            currentArtist.logout();
            currentArtist = null;
        } else {
            System.out.println("No artist is currently logged in.");
        }
    }

    /**
     * This method displays a menu allowing the user to choose between creating a Listener or Artist account.
     * Based on the user's choice, the corresponding account creation method is called.
     */
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

    /**
     * Prompts the user to enter details for creating a Listener account.
     * The user is asked for their name, email, and password.
     * A new Listener object is created and registered using the MusicController instance.
     */
    private void createListenerAccount() {
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter a password:");
        String password = scanner.nextLine();
        if (!validatePassword(password)) {
            System.out.println("Password must be at least 6 characters long. Please try again.");
            return;
        }
        Listener newListener = new Listener(name, email);
        newListener.setPassword(password);
        musicController.registerListener(newListener);
        System.out.println("Listener account created successfully! You can now log in as a Listener.");
    }

    /**
     * Prompts the user to enter details for creating an Artist account.
     * The user is asked for their name, email, and password.
     * A new Artist object is created and registered using the MusicController instance.
     */
    private void createArtistAccount() {
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter a password:");
        String password = scanner.nextLine();
        if (!validatePassword(password)) {
            System.out.println("Password must be at least 6 characters long. Please try again.");
            return;
        }
        Artist newArtist = new Artist(name, email);
        newArtist.setPassword(password);
        musicController.registerArtist(newArtist);
        System.out.println("Artist account created successfully! You can now log in as an Artist.");
    }
    /**
     * Validates the password entered by the user.
     * A valid password must be at least 6 characters long.
     *
     * @param password The password to validate.
     * @return True if the password is valid, false otherwise.
     */
    private boolean validatePassword(String password) {
        return password.length() >= 6;
    }

    // ----------------- LISTENER MENU  METHODS -----------------

    /** Follows an artist for a specific listener. */
    private void followArtist() {
        try {
            if (currentListener == null) {
                System.out.println("Please log in as a listener before proceeding.");
                currentListener = listenerLogin();
                if (currentListener == null) {
                    throw new ValidationException("Listener login failed.");
                }
            }
            System.out.println("Enter artist name to follow:");
            String artistName = scanner.nextLine().trim();
            if (artistName.isEmpty()) {
                throw new ValidationException("Artist name cannot be empty.");
            }

            Artist artist = musicController.getArtistByName(artistName);
            if (artist == null) {
                throw new EntityNotFoundException("Artist not found.");
            }

            musicController.enrollListenerToArtist(currentListener, artist);
            System.out.println(currentListener.getName() + " is now following " + artist.getName() + ".");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Creates a new playlist for a listener. */
    private void createPlaylist() {
        try {
            if (currentListener == null) {
                System.out.println("Please log in as a listener before proceeding.");
                currentListener = listenerLogin();
                if (currentListener == null) {
                    throw new ValidationException("Listener login failed.");
                }
            }
            System.out.println("Enter playlist name:");
            String playlistName = scanner.nextLine().trim();
            if (playlistName.isEmpty()) {
                throw new ValidationException("Playlist name cannot be empty.");
            }
            if (playlistName.length() > 100) {
                throw new ValidationException("Playlist name is too long (max 100 characters).");
            }
            Playlist playlist = new Playlist(playlistName, currentListener);
            musicController.addPlaylist(playlist);
            System.out.println("Playlist " + playlistName + " created successfully.");
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Adds a song to a specific playlist. */
    private void addSongToPlaylist() {
        try {
            System.out.println("Enter playlist name:");
            String playlistName = scanner.nextLine().trim();

            if (playlistName.isEmpty()) {
                throw new ValidationException("Playlist name cannot be empty.");
            }

            Playlist playlist = musicController.getPlaylistByName(playlistName);
            if (playlist == null) {
                throw new EntityNotFoundException("Playlist not found.");
            }

            System.out.println("Enter song title to add:");
            String songTitle = scanner.nextLine().trim();

            if (songTitle.isEmpty()) {
                throw new ValidationException("Song title cannot be empty.");
            }

            Song song = musicController.getSongByTitle(songTitle);
            if (song == null) {
                throw new EntityNotFoundException("Song not found.");
            }

            musicController.addSongToPlaylist(playlist.getName(), song.getTitle());
            System.out.println(song.getTitle() + " added to playlist " + playlistName + ".");
        } catch (EntityNotFoundException | ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Removes a song from a specified playlist. */
    private void removeSongFromPlaylist() {
        try {
            System.out.println("Enter playlist name:");
            String playlistName = scanner.nextLine().trim();

            if (playlistName.isEmpty()) {
                throw new ValidationException("Playlist name cannot be empty.");
            }


            Playlist playlist = musicController.getPlaylistByName(playlistName);
            if (playlist == null) {
                throw new EntityNotFoundException("Playlist not found.");
            }

            System.out.println("Enter song title to remove:");
            String songTitle = scanner.nextLine().trim();

            if (songTitle.isEmpty()) {
                throw new ValidationException("Song title cannot be empty.");
            }

            Song song = musicController.getSongByTitle(songTitle);
            if (song == null) {
                throw new EntityNotFoundException("Song not found.");
            }

            if (!playlist.getSongs().contains(song)) {
                throw new ValidationException("The song " + songTitle + " is not in the playlist " + playlistName + ".");
            }

            musicController.removeSongFromPlaylist(playlistName, songTitle);
            System.out.println(song.getTitle() + " removed from playlist " + playlistName + ".");
        } catch (EntityNotFoundException | ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Plays a specified playlist. */
    private void playPlaylist() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the playlist name:");
            String playlistName = scanner.nextLine();

            if (playlistName == null || playlistName.trim().isEmpty()) {
                throw new ValidationException("Playlist name cannot be empty.");
            }

            Playlist playlist = musicController.getPlaylistByName(playlistName);
            if (playlist == null) {
                throw new EntityNotFoundException("Playlist not found.");
            }

            musicController.playPlaylist(playlistName);
            System.out.println("Playing playlist: " + playlistName);
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Pauses a specified playlist. */
    private void pausePlaylist() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the playlist name:");
            String playlistName = scanner.nextLine();

            if (playlistName == null || playlistName.trim().isEmpty()) {
                throw new ValidationException("Playlist name cannot be empty.");
            }

            Playlist playlist = musicController.getPlaylistByName(playlistName);
            if (playlist == null) {
                throw new EntityNotFoundException("Playlist not found.");
            }

            musicController.pausePlaylist(playlistName);
            System.out.println("Playlist " + playlistName + " has been paused.");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Stops a specified playlist. */
    private void stopPlaylist() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the playlist name:");
            String playlistName = scanner.nextLine();

            if (playlistName == null || playlistName.trim().isEmpty()) {
                throw new ValidationException("Playlist name cannot be empty.");
            }

            Playlist playlist = musicController.getPlaylistByName(playlistName);
            if (playlist == null) {
                throw new EntityNotFoundException("Playlist not found.");
            }

            musicController.stopPlaylist(playlistName);
            System.out.println("Playlist " + playlistName + " has been stopped.");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Plays a specific song. */
    private void playSong() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the song title:");
            String songTitle = scanner.nextLine();

            if (songTitle == null || songTitle.trim().isEmpty()) {
                throw new ValidationException("Song title cannot be empty.");
            }

            Song song = musicController.getSongByTitle(songTitle);
            if (song == null) {
                throw new EntityNotFoundException("Song not found.");
            }

            musicController.playSong(songTitle);
            System.out.println("Playing song: " + songTitle);
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Pauses a specific song. */
    private void pauseSong() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the song title:");
            String songTitle = scanner.nextLine();

            if (songTitle == null || songTitle.trim().isEmpty()) {
                throw new ValidationException("Song title cannot be empty.");
            }

            Song song = musicController.getSongByTitle(songTitle);
            if (song == null) {
                throw new EntityNotFoundException("Song not found.");
            }

            musicController.pauseSong(songTitle);
            System.out.println("Song " + songTitle + " has been paused.");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Stops a specific song. */
    private void stopSong() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the song title:");
            String songTitle = scanner.nextLine();

            if (songTitle == null || songTitle.trim().isEmpty()) {
                throw new ValidationException("Song title cannot be empty.");
            }

            Song song = musicController.getSongByTitle(songTitle);
            if (song == null) {
                throw new EntityNotFoundException("Song not found.");
            }

            musicController.stopSong(songTitle);
            System.out.println("Song " + songTitle + " has been stopped.");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Displays all songs in a specified playlist. */
    private void viewSongsInPlaylist() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter playlist name:");
            String playlistName = scanner.nextLine();

            if (playlistName == null || playlistName.trim().isEmpty()) {
                throw new ValidationException("Playlist name cannot be empty.");
            }

            Playlist playlist = musicController.getPlaylistByName(playlistName);
            if (playlist == null) {
                throw new EntityNotFoundException("Playlist not found.");
            }

            List<Song> songs = playlist.getSongs();
            if (songs.isEmpty()) {
                System.out.println("Playlist " + playlistName + " has no songs.");
                return;
            }
            System.out.println("Songs in playlist " + playlistName + ":");
            for (Song song : songs) {
                System.out.println("- " + song.getTitle());
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Prompts the user to create a subscription for an existing listener.
     * This method checks if the listener exists by username, then allows the user to select a subscription type (Basic or Premium).
     * If the listener is not found, an account creation prompt is displayed.
     */
    private void createSubscription() {
        try{
            if (currentListener == null) {
                System.out.println("Please log in as a listener before proceeding.");
                currentListener = listenerLogin();
                if (currentListener == null) {
                    throw new ValidationException("Listener login failed.");
                }
            }
            String username = currentListener.getName();
            Listener listener = musicController.getListenerByName(username);
            if (listener == null) {
                System.out.println("Listener not found. Please create an account first.");
                return;
            }
            System.out.println("Choose subscription type to create:");
            System.out.println("1. Basic");
            System.out.println("2. Premium");
            int subscriptionChoice = scanner.nextInt();
            scanner.nextLine();
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
            musicController.createListenerSubscription(listener.getName(), subscriptionType);
            System.out.println("Subscription created successfully for " + listener.getName() + " with " + subscriptionType + " plan.");
        }catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Upgrades the subscription type for a listener.
     * Prompts the user for their username and the new subscription type, then upgrades their subscription accordingly.
     */
    private void upgradeSubscription() {
        try{
            if (currentListener == null) {
                System.out.println("Please log in as a listener before proceeding.");
                currentListener = listenerLogin();
                if (currentListener == null) {
                    throw new ValidationException("Listener login failed.");
                }
            }
            String username = currentListener.getName();
            Listener listener = musicController.getListenerByName(username);
            if (listener == null) {
                throw new EntityNotFoundException("Listener not found. Please create an account first.");
            }
            System.out.println("Enter your subscription type (basic/premium):");
            String type = scanner.nextLine().toLowerCase();
            if (!type.equals("basic") && !type.equals("premium")) {
                throw new ValidationException("Invalid subscription type. Please enter either 'basic' or 'premium'.");
            }
            musicController.upgradeSubscription(listener, type);
            System.out.println("Subscription upgraded successfully to " + type + " for " + listener.getName() + ".");
        }catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }


    }

    /** Cancels the active subscription for a listener. */
    private void cancelSubscription() {
        try {
            if (currentListener == null) {
                System.out.println("Please log in as a listener before proceeding.");
                currentListener = listenerLogin();
                if (currentListener == null) {
                    throw new ValidationException("Listener login failed.");
                }
            }
            String username = currentListener.getName();
            Listener listener = musicController.getListenerByName(username);
            if (listener == null) {
                throw new EntityNotFoundException("User not found. Please make sure you are logged in.");
            }

            boolean isCanceled = musicController.cancelSubscription(listener);
            if (isCanceled) {
                System.out.println("Subscription canceled for " + username + ".");
            } else {
                System.out.println("No active subscription found for " + username + ".");
            }
        } catch (EntityNotFoundException | ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Recommends songs to a listener based on their preferences or activity. */
    private void recommendSongs() {
        try {
            if (currentListener == null) {
                System.out.println("Please log in as a listener before proceeding.");
                currentListener = listenerLogin();
                if (currentListener == null) {
                    throw new ValidationException("Listener login failed.");
                }
            }

            List<Song> recommendedSongs = musicController.recommendSongsForListener(currentListener);
            System.out.println("Recommended Songs for " + currentListener.getName() + ":");

            if (recommendedSongs.isEmpty()) {
                System.out.println("No recommendations available.");
            } else {
                for (Song song : recommendedSongs) {
                    System.out.println("- " + song.getTitle() + " by " + song.getAlbum().getArtist().getName());
                }
            }
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Recommends a list of artists based on the listener's preferences. */
    private void recommendArtists() {
        try {
            if (currentListener == null) {
                System.out.println("Please log in as a listener before proceeding.");
                currentListener = listenerLogin();
                if (currentListener == null) {
                    throw new ValidationException("Listener login failed.");
                }
            }

            List<Artist> recommendedArtists = musicController.recommendArtistsForListener(currentListener);
            System.out.println("Recommended Artists for " + currentListener.getName() + ":");

            if (recommendedArtists.isEmpty()) {
                System.out.println("No recommendations available.");
            } else {
                for (Artist artist : recommendedArtists) {
                    System.out.println("- " + artist.getName());
                }
            }
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Retrieves and displays the top genres for a specific listener. */
    private void getTopGenres() {
        try {
            if (currentListener == null) {
                System.out.println("Please log in as a listener before proceeding.");
                currentListener = listenerLogin();
                if (currentListener == null) {
                    throw new ValidationException("Listener login failed.");
                }
            }

            List<Genre> topGenres = musicController.getTopGenresForListener(currentListener);
            System.out.println("Top Genres for " + currentListener.getName() + ":");

            if (topGenres.isEmpty()) {
                System.out.println("No genres found.");
            } else {
                for (Genre genre : topGenres) {
                    System.out.println("- " + genre.getName());
                }
            }
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Adds a song to the listener's listening history. */
    private void addSongToHistory() {
        try {
            if (currentListener == null) {
                System.out.println("Please log in as a listener first.");
                currentListener = listenerLogin();
                if (currentListener == null) {
                    throw new ValidationException("Listener login failed.");
                }
            }

            System.out.println("Enter the title of the song you want to add to " + currentListener.getName() + "'s history:");
            String songTitle = scanner.nextLine();
            Song song = musicController.getSongByTitle(songTitle);

            if (song != null) {
                musicController.addSongToHistory(currentListener.getName(), songTitle);
                System.out.println("Song " + songTitle + " has been added to " + currentListener.getName() + "'s history.");
            } else {
                throw new EntityNotFoundException("Song not found.");
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Retrieves and displays the listening history of a specific listener. */
    private void getHistory() {
        try {
            if (currentListener == null) {
                System.out.println("Please log in as a listener first.");
                currentListener = listenerLogin();
                if (currentListener == null) {
                    throw new ValidationException("Listener login failed.");
                }
            }

            String listenerName = currentListener.getName();
            List<Song> songs = musicController.getHistory(listenerName);

            if (songs != null && !songs.isEmpty()) {
                System.out.println("History for " + listenerName + ":");
                for (Song song : songs) {
                    System.out.println("- " + song.getTitle());
                }
            } else {
                System.out.println("No songs found in history for " + listenerName + ".");
            }
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Displays the list of available concerts. */
    private void viewAvailableConcerts() {
        try {
            List<LiveConcert> concerts = musicController.getAvailableConcerts();

            if (concerts != null && !concerts.isEmpty()) {
                System.out.println("Available concerts:");
                for (LiveConcert concert : concerts) {
                    System.out.println("- " + concert.getTitle());
                }
            } else {
                System.out.println("No available concerts found.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    /** Checks the availability of tickets for a specific concert. */
    private void checkTicketAvailability() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the concert title:");
            String concertTitle = scanner.nextLine();

            boolean isAvailable = musicController.checkTicketAvailability(concertTitle);

            if (isAvailable) {
                System.out.println("Tickets are available for the concert: " + concertTitle);
            } else {
                System.out.println("No tickets available for the concert: " + concertTitle);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            System.out.println("Error: Concert not found. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /** Views the discography of a specific artist. */
    private void viewArtistDiscography() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the name of the artist to view their discography:");
            String artistName = scanner.nextLine();
            if (artistName == null || artistName.trim().isEmpty()) {
                throw new ValidationException("Artist name cannot be empty.");
            }
            musicController.viewArtistDiscography(artistName);
        } catch (ValidationException e) {
            System.out.println("Input error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /** Registers a listener for a specific concert. */
    private void attendConcert() {
        try {
            if (currentListener == null) {
                System.out.println("Please log in as a listener first.");
                currentListener = listenerLogin();
                if (currentListener == null) {
                    throw new ValidationException("Listener login failed.");
                }
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the title of the concert you want to attend:");
            String concertTitle = scanner.nextLine();
            LiveConcert concert = musicController.getConcertByTitle(concertTitle);
            if (concert == null) {
                throw new EntityNotFoundException("Concert not found.");
            }
            Listener listener = currentListener;
            boolean added = musicController.addAttendee(concert.getId(), listener);
            if (added) {
                System.out.println("You have successfully registered for the concert: " + concertTitle);
            } else {
                System.out.println("You are already registered for this concert.");
            }
        } catch (EntityNotFoundException | ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Displays the VIP score for a listener at a specific concert. */
    private void showVIPScore() {
        try {
            if (currentListener == null) {
                System.out.println("Please log in as a listener first.");
                currentListener = listenerLogin();
                if (currentListener == null) {
                    throw new ValidationException("Listener login failed.");
                }
            }

            String listenerName = currentListener.getName();
            System.out.println("Logged in as " + listenerName);
            System.out.print("Enter the concert ID: ");
            int concertId = scanner.nextInt();

            double vipScore = musicController.getConcertVIPScore(currentListener, concertId);
            System.out.println("The VIP score for the concert is: " + vipScore);
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // ----------------- ARTIST MENU  METHODS -----------------

    /**1* Prompts the user to enter details to create a new album. */
    private void createAlbum() {
        try {
            if (currentArtist == null) {
                System.out.println("Please log in as an artist first.");
                currentArtist = artistLogin();
                if (currentArtist == null) {
                    throw new ValidationException("Artist login failed.");
                }
            }
            System.out.println("Enter album title:");
            String albumTitle = scanner.nextLine();
            System.out.println("Enter release date (YYYY-MM-DD):");
            String dateInput = scanner.nextLine();
            try {
                LocalDate releaseDate = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
                Artist artist = currentArtist;
                if (artist == null) {
                    throw new EntityNotFoundException("Artist not found.");
                }

                System.out.println("Available genres:");
                List<Genre> genres = musicController.getAllGenres();
                for (int i = 0; i < genres.size(); i++) {
                    System.out.println((i + 1) + ". " + genres.get(i).getName());
                }
                System.out.println("Select a genre by number:");
                int genreChoice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                if (genreChoice <= 0 || genreChoice > genres.size()) {
                    throw new ValidationException("Invalid genre selection.");
                }

                Genre selectedGenre = genres.get(genreChoice - 1);
                Album album = new Album(albumTitle, releaseDate, artist);
                album.setGenre(selectedGenre);
                musicController.addAlbum(album);
                System.out.println("Album '" + albumTitle + "' added to artist " + artist.getName());

            } catch (DateTimeParseException e) {
                throw new ValidationException("Invalid date format. Please use YYYY-MM-DD.");
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**2* Prompts the user to upload a new song. */
    private void uploadSong() {
        try {
            if (currentArtist == null) {
                System.out.println("Please log in as an artist first.");
                currentArtist = artistLogin();
                if (currentArtist == null) {
                    throw new ValidationException("Artist login failed.");
                }
            }
            System.out.println("Enter song title:");
            String songTitle = scanner.nextLine();


            Artist artist = currentArtist;
            if (artist == null) {
                throw new EntityNotFoundException("Artist not found.");
            }

            System.out.println("Enter song duration (in minutes, e.g., 3.5 for 3 minutes and 30 seconds):");
            float duration = Float.parseFloat(scanner.nextLine());
            System.out.println("Enter album name:");
            String albumName = scanner.nextLine();

            Album album = musicController.getAlbumByName(albumName);
            if (album == null) {
                throw new EntityNotFoundException("Album not found.");
            }

            Genre albumGenre = album.getGenre();
            if (albumGenre != null) {
                Song song = new Song(songTitle, duration, album);
                song.setGenre(albumGenre);
                musicController.addSong(song);
                System.out.println("Song '" + songTitle + "' added to album '" + album.getTitle() + "' with genre '" + albumGenre.getName() + "'.");
            } else {
                System.out.println("Album does not have a genre. Please select a genre for this song.");
                List<Genre> genres = musicController.getAllGenres();
                for (int i = 0; i < genres.size(); i++) {
                    System.out.println((i + 1) + ". " + genres.get(i).getName());
                }
                System.out.println("Select a genre by number:");
                int genreChoice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                if (genreChoice <= 0 || genreChoice > genres.size()) {
                    throw new ValidationException("Invalid genre selection.");
                }

                Genre selectedGenre = genres.get(genreChoice - 1);
                Song song = new Song(songTitle, duration, album);
                song.setGenre(selectedGenre);
                musicController.addSong(song);
                System.out.println("Song '" + songTitle + "' added to album '" + album.getTitle() + "' with genre '" + selectedGenre.getName() + "'.");
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }


    /**3* Prompts the user to start a concert. */
    private void startConcert() {
        try {
            if (currentArtist == null) {
                System.out.println("Please log in as an artist first.");
                currentArtist = artistLogin();
                if (currentArtist == null) {
                    throw new ValidationException("Artist login failed.");
                }
            }
            System.out.println("Enter concert title to start:");
            String concertTitle = scanner.nextLine();

            LiveConcert concert = musicController.getConcertByTitle(concertTitle);
            if (concert == null) {
                throw new EntityNotFoundException("Concert not found.");
            }

            Artist artist = concert.getArtist();
            if (artist == null || !artist.equals(currentArtist)) {
                throw new EntityNotFoundException("This concert does not belong to the current artist.");
            }
            System.out.println("Artist for this concert: " + artist.getName());
            String eventType = concert.getEventType();
            System.out.println("Event Type: " + eventType);

            System.out.println("Enter ticket count:");
            int ticketCount = Integer.parseInt(scanner.nextLine());
            System.out.println("Is the concert available post-live? (true/false):");
            boolean isAvailablePostLive = Boolean.parseBoolean(scanner.nextLine());

            concert.setTicketCount(ticketCount);
            concert.setAvailablePostLive(isAvailablePostLive);

            boolean areTicketsAvailable = musicController.checkTicketAvailability(concertTitle);
            if (areTicketsAvailable) {
                System.out.println("Tickets are available for the concert: " + concertTitle);
            } else {
                System.out.println("No tickets available for the concert: " + concertTitle);
            }

            musicController.startConcert(concertTitle);
        } catch (EntityNotFoundException | ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /***4 Prompts the user to enter a concert title and ends the specified concert.
     * Calls the method from the MusicController to stop the concert*/
    private void endConcert() {
        try {
            if (currentArtist == null) {
                System.out.println("Please log in as an artist first.");
                currentArtist = artistLogin();
                if (currentArtist == null) {
                    throw new ValidationException("Artist login failed.");
                }
            }
            System.out.println("Enter concert title to end:");
            String concertTitle = scanner.nextLine();
            LiveConcert concert = musicController.getConcertByTitle(concertTitle);
            if (concert == null) {
                throw new EntityNotFoundException("Concert not found.");
            }
            Artist artist = concert.getArtist();
            if (artist == null || !artist.equals(currentArtist)) {
                throw new EntityNotFoundException("This concert does not belong to the current artist.");
            }
            musicController.endConcert(concertTitle);
        } catch (EntityNotFoundException | ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    /**5* Lists all available albums in the system. Displays each album's title. */
    private void listAvailableAlbums() {
        try {
            List<Album> albums = musicController.getAvailableAlbums();
            if (albums.isEmpty()) {
                throw new EntityNotFoundException("No albums found in the system.");
            }

            Set<String> seenAlbums = new HashSet<>();
            System.out.println("Available albums:");
            for (Album album : albums) {
                if (!seenAlbums.contains(album.getTitle())) {
                    System.out.println("- " + album.getTitle());
                    seenAlbums.add(album.getTitle());
                }
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**6* Prompts the user to enter the title of an album and retrieves a list of songs in that album. */
    private void getSongsInAlbum() {
        try {
            System.out.println("Enter album title:");
            String albumTitle = scanner.nextLine().trim();

            Album album = musicController.getAlbumByName(albumTitle);
            if (album == null) {
                throw new EntityNotFoundException("Album not found.");
            }

            List<Song> songs = musicController.getSongsInAlbum(albumTitle);
            if (songs.isEmpty()) {
                throw new EntityNotFoundException("No songs found in the album.");
            }

            System.out.println("Songs in " + album.getTitle() + ":");
            for (Song song : songs) {
                System.out.println("- " + song.getTitle());
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**7* Prompts the user to enter an album title and a song title to remove from the album. */
    private void removeSongFromAlbum() {
        try {
            System.out.println("Enter album title:");
            String albumTitle = scanner.nextLine().trim();

            Album album = musicController.getAlbumByName(albumTitle);
            if (album == null) {
                throw new EntityNotFoundException("Album not found.");
            }

            System.out.println("Enter song title to remove:");
            String songTitle = scanner.nextLine().trim();
            Song song = musicController.getSongByTitle(songTitle);

            if (song == null || !album.getSongs().contains(song)) {
                throw new EntityNotFoundException("Song not found in album.");
            }

            musicController.removeSongFromAlbum(albumTitle, songTitle);
            System.out.println("Song removed from album: " + songTitle);
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }


    /**8* Prompts the user to enter an album title and the maximum number of songs allowed in an album. */
    private void validateAlbumSongs() {
        try {
            System.out.println("Enter album title:");
            String albumTitle = scanner.nextLine().trim();

            Album album = musicController.getAlbumByName(albumTitle);
            if (album == null) {
                throw new EntityNotFoundException("Album not found.");
            }

            System.out.println("Enter maximum number of songs allowed:");
            int maxSongs = scanner.nextInt();

            if (maxSongs <= 0) {
                throw new ValidationException("Maximum number of songs must be greater than zero.");
            }

            boolean isValid = musicController.validateAlbumSongs(album, maxSongs);
            System.out.println(isValid ? "Album is valid." : "Album exceeds the maximum allowed songs.");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**9* Prompts the user to enter the name of an artist and displays the list of listeners who follow that artist. */
    private void viewFollowers() {
        try {
            if (currentArtist == null) {
                System.out.println("Please log in as an artist first.");
                currentArtist = artistLogin();
                if (currentArtist == null) {
                    throw new ValidationException("Artist login failed.");
                }
            }
            Artist artist = currentArtist;
            System.out.println("Viewing followers for artist: " + artist.getName());

            List<Listener> followers = artist.getFollowers();
            if (followers.isEmpty()) {
                throw new EntityNotFoundException("No followers found for this artist.");
            }

            System.out.println("Followers of " + artist.getName() + ":");
            for (Listener follower : followers) {
                System.out.println("- " + follower.getName());
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /***10 Prompts the user to enter a concert title and displays the list of attendees for that concert.
     * Calls the method from MusicController to display the concert attendees.*/
    private void viewAttendeesForConcert() {
        try {
            if (currentArtist == null) {
                System.out.println("Please log in as an artist first.");
                currentArtist = artistLogin();
                if (currentArtist == null) {
                    throw new ValidationException("Artist login failed.");
                }
            }
            System.out.println("Enter the title of the concert to view attendees:");
            String concertTitle = scanner.nextLine().trim();
            if (concertTitle.isEmpty()) {
                throw new ValidationException("Concert title cannot be empty.");
            }
            musicController.showAttendees(concertTitle);
        }catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * 11* Filters albums by genre for a specific artist.
     */
    private void filterAlbumsByGenre() {
        try {
            System.out.println("Enter artist ID:");
            int artistId = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter genre name:");
            String genreName = scanner.nextLine();
            if (genreName.isEmpty()) {
                throw new ValidationException("Genre name cannot be empty.");
            }

            List<Album> albums = musicController.filterAlbumsByGenre(artistId, genreName);
            if (albums.isEmpty()) {
                throw new EntityNotFoundException("No albums found for the specified genre.");
            } else {
                System.out.println("Albums found for the specified genre:");
                albums.forEach(album -> System.out.println(album.getTitle()));
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * 12* Filters songs by minimum duration for a specific artist.
     */
    private void filterSongsByDuration() {
        try {
            System.out.println("Enter artist ID:");
            int artistId = scanner.nextInt();
            System.out.println("Enter minimum duration in seconds:");
            float minDuration = scanner.nextFloat();

            if (minDuration <= 0) {
                throw new ValidationException("Minimum duration must be greater than 0.");
            }

            List<Song> songs = musicController.filterSongsByMinimumDuration(artistId, minDuration);
            if (songs.isEmpty()) {
                throw new EntityNotFoundException("No songs found with the specified duration.");
            } else {
                System.out.println("Songs found with the specified duration:");
                songs.forEach(song -> System.out.println(song.getTitle()));
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * 13* Prompts the user for an artist ID, retrieves and sorts the artist's albums by release date, then displays the album titles.
     * If no albums are found, a message is displayed. Otherwise, the album titles are printed in order from the earliest to the most recent.
     */
    private void sortAlbumsByReleaseDate() {
        try {
            System.out.println("Enter artist ID:");
            int artistId = scanner.nextInt();
            System.out.println("Sorting albums by release date:");
            List<Album> albums = musicController.sortAlbumsByReleaseDate(artistId);
            if (albums.isEmpty()) {
                System.out.println("No albums found.");
            } else {
                albums.forEach(song -> System.out.println(song.getTitle()));
            }
        }catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * 14* Prompts the user to enter an album ID and then sorts and displays the songs of that album by their title.
     */
    private void sortSongsByTitle() {
        try {
            System.out.println("Enter album ID:");
            int albumId = scanner.nextInt();
            System.out.println("Sorting songs by title:");

            if (albumId <= 0) {
                throw new ValidationException("Invalid album ID.");
            }

            List<Song> songs = musicController.sortSongsByTitle(albumId);
            if (songs.isEmpty()) {
                throw new EntityNotFoundException("No songs found.");
            } else {
                songs.forEach(song -> System.out.println(song.getTitle()));
            }
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * 15* Displays a list of artists sorted by the total number of songs and albums they have.
     */
    private void displayArtistsWithMostSongsAndAlbums() {
        try {
            List<Artist> artists = musicController.getArtistsWithMostSongsAndAlbums();
            if (artists.isEmpty()) {
                throw new EntityNotFoundException("No artists found.");
            }

            System.out.println("Artists sorted by total number of songs and albums:");
            for (Artist artist : artists) {
                int totalSongs = artist.getAlbums().stream()
                        .mapToInt(album -> album.getSongs().size())
                        .sum();
                int totalAlbums = artist.getAlbums().size();
                System.out.println(artist.getName() + " - Total Songs: " + totalSongs + " - Total Albums: " + totalAlbums);
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    //MUST BE TESTED
    private void deleteAlbum() {
        try {
            if (currentArtist == null) {
                System.out.println("Please log in as an artist first.");
                currentArtist = artistLogin();
                if (currentArtist == null) {
                    throw new ValidationException("Artist login failed.");
                }
            }
            System.out.print("Enter Album ID to delete: ");
            int albumId = scanner.nextInt();
            scanner.nextLine();
            musicController.deleteAlbum(albumId);
        }catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

    }
    //MUST BE TESTED
    private void deleteSong() {
        try {
            if (currentArtist == null) {
                System.out.println("Please log in as an artist first.");
                currentArtist = artistLogin();
                if (currentArtist == null) {
                    throw new ValidationException("Artist login failed.");
                }
            }
            System.out.print("Enter Song ID to delete: ");
            int songId = scanner.nextInt();
            scanner.nextLine();
            musicController.deleteSong(songId);
        }catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

    }

    //MUST BE TESTED
    private void updateAlbumDetails() {
        try {
            if (currentArtist == null) {
                System.out.println("Please log in as an artist first.");
                currentArtist = artistLogin();
                if (currentArtist == null) {
                    throw new ValidationException("Artist login failed.");
                }
            }
            System.out.print("Enter Album ID to update: ");
            int albumId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter new Album Name: ");
            String newName = scanner.nextLine();
            System.out.print("Enter new Album Genre: ");
            String newGenre = scanner.nextLine();
            musicController.updateAlbumDetails(albumId, newName, newGenre);
        }catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    //MUST BE TESTED
    private void updateSongDetails() {
        try {
            if (currentArtist == null) {
                System.out.println("Please log in as an artist first.");
                currentArtist = artistLogin();
                if (currentArtist == null) {
                    throw new ValidationException("Artist login failed.");
                }
            }
            System.out.print("Enter Song ID to update: ");
            int songId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter new Song Title: ");
            String newTitle = scanner.nextLine();
            System.out.print("Enter new Song Genre: ");
            String newGenre = scanner.nextLine();
            musicController.updateSongDetails(songId, newTitle, newGenre);
        }catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }




}