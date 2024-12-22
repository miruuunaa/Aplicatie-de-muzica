import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import Controller.MusicController;
import Domain.*;
import Konsole.MusicKonsole;
import Repository.*;
import Service.*;


/**
 * The main application class that manages a music recommendation system.
 * It creates various service components, repositories, and controls the application through the music console.
 */
public class MusicApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the storage type:");
        System.out.println("1. In-Memory Storage");
        System.out.println("2. File-based Storage");
        System.out.println("3. Database Storage");
        System.out.println("4. Exit");
        int storageChoice = scanner.nextInt();
        scanner.nextLine();
        MusicAppComponents components = initializeAppComponents(storageChoice);

        if (components == null) {
            System.out.println("Invalid choice, exiting...");
            return;
        }
        MusicController musicController = new MusicController(
                components.artistService, components.albumService, components.songService, components.playlistService,
                components.listenerService, components.liveConcertService, components.recommendationService,
                components.subscriptionService, components.genreService
        );

        MusicKonsole musicKonsole = new MusicKonsole(musicController);
        musicKonsole.showMainMenu();
    }

    private static MusicAppComponents initializeAppComponents(int storageChoice) {
        IRepository<Artist> artistRepository = null;
        IRepository<Album> albumRepository = null;
        IRepository<Song> songRepository = null;
        IRepository<LiveConcert> concertRepository = null;
        IRepository<Playlist> playlistRepository = null;
        IRepository<Listener> listenerRepository = null;
        IRepository<Subscription> subscriptionRepository = null;
        IRepository<History> historyRepository=null;
        IRepository<Genre> genreRepository=null;

        switch (storageChoice) {
            case 1:
                // In-Memory Repositories
                artistRepository = createInMemoryArtistRepository();
                albumRepository = createInMemoryAlbumRepository(artistRepository);
                songRepository = createInMemorySongRepository(albumRepository);
                concertRepository = createInMemoryLiveConcertRepository(artistRepository);
                playlistRepository = new InMemoryRepository<>();
                listenerRepository = createInMemoryListenerRepository();
                subscriptionRepository = createInMemorySubscriptionRepository(listenerRepository);
                break;
            case 2:
                // File-based Repositories
                artistRepository = createFileBasedArtistRepository();
                albumRepository =  createFileBasedAlbumRepository(artistRepository);
                songRepository = createFileBasedSongRepository(albumRepository);
                listenerRepository = createFileBasedListenerRepository();
                concertRepository = createFileBasedLiveConcertRepository(artistRepository);
                playlistRepository=new FileRepository<>("playlist.csv");
                subscriptionRepository = createFileBasedSubscriptionRepository(listenerRepository);
                break;
            case 3:
                // Database Repositories
                String url = "jdbc:postgresql://localhost:5432/MTifyDatabase";
                String user = "postgres";
                String password = "1111";

                artistRepository=new ArtistDBRepository(url,user,password);
                albumRepository=new AlbumDBRepository(url,user,password);
                songRepository=new SongDBRepository(url,user,password);
                concertRepository=new LiveConcertDBRepository(url,user,password);
                playlistRepository=new PlaylistDBRepository(url,user,password);
                listenerRepository=new ListenerDBRepository(url,user,password);
                subscriptionRepository=new SubscriptionDBRepository(url,user,password);
                historyRepository = new HistoryDBRepository(url,user,password);
                genreRepository = new GenreDBRepository(url,user,password);
                break;
            case 4:
                return null;
            default:
                return null;
        }
        ArtistService artistService = new ArtistService(artistRepository);
        AlbumService albumService = new AlbumService(albumRepository);
        SongService songService = new SongService(songRepository);
        LiveConcertService liveConcertService = new LiveConcertService(concertRepository);
        PlaylistService playlistService = new PlaylistService(playlistRepository);
        ListenerService listenerService = new ListenerService(listenerRepository, subscriptionRepository);
        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);
        GenreService genreService = new GenreService(new InMemoryRepository<Genre>());

        Listener listener = listenerRepository.get(0);
        History userHistory = new History(listener);
        userHistory.addSongToHistory(songRepository.get(0));
        userHistory.addSongToHistory(songRepository.get(1));
        List<Song> allSongs = new ArrayList<>(songRepository.getAll().values());
        RecommendationService recommendationService = new RecommendationService(userHistory, allSongs);

        return new MusicAppComponents(
                artistService, albumService, songService, playlistService, listenerService,
                liveConcertService, recommendationService, subscriptionService, genreService
        );
        //---------------------------------------------------------
        //Repo-InMemory
        //IRepository<Artist> artistRepository = createInMemoryArtistRepository();
        //IRepository<Album> albumRepository =  createInMemoryAlbumRepository(artistRepository);
        //IRepository<Song> songRepository = createInMemorySongRepository(albumRepository);
        //IRepository<LiveConcert> concertRepository = createInMemoryLiveConcertRepository(artistRepository);
        //IRepository<Playlist> playlistRepository = new InMemoryRepository<>();
        //IRepository<Listener> listenerRepository = createInMemoryListenerRepository();
        //IRepository<Subscription> subscriptionRepository = createInMemorySubscriptionRepository(listenerRepository);
        //---------------------------------------------------------
        //Repo-File
        //IRepository<Artist> artistRepository = createFileBasedArtistRepository();
        //IRepository<Album> albumRepository = createFileBasedAlbumRepository(artistRepository);
        //IRepository<Song> songRepository = createFileBasedSongRepository(albumRepository);
        //IRepository<Listener> listenerRepository = createFileBasedListenerRepository();
        //IRepository<LiveConcert> concertRepository = createFileBasedLiveConcertRepository(artistRepository);
        //IRepository<Playlist> playlistRepository=new FileRepository<>("playlist.csv");
        //IRepository<Subscription> subscriptionRepository = createFileBasedSubscriptionRepository(listenerRepository);
        //---------------------------------------------------------
        //Repo-DB
        //String url = "jdbc:postgresql://localhost:5432/MTifyDatabase";
        // String user ="postgres";
        //String password = "1111";
        //ArtistDBRepository artistRepository=new ArtistDBRepository(url,user,password);
        //AlbumDBRepository albumRepository=new AlbumDBRepository(url,user,password);
        //SongDBRepository songRepository=new SongDBRepository(url,user,password);
        //LiveConcertDBRepository concertRepository=new LiveConcertDBRepository(url,user,password);
        //PlaylistDBRepository playlistRepository=new PlaylistDBRepository(url,user,password);
        //ListenerDBRepository listenerRepository=new ListenerDBRepository(url,user,password);
        //SubscriptionDBRepository subscriptionRepository=new SubscriptionDBRepository(url,user,password);
        //HistoryDBRepository historyDBRepository=new HistoryDBRepository(url,user,password);
        //GenreDBRepository genreDBRepository=new GenreDBRepository(url,user,password);
        //---------------------------------------------------------
        //Service
        //ArtistService artistService=new ArtistService(artistRepository);
        //AlbumService albumService=new AlbumService(albumRepository);
        //SongService songService=new SongService(songRepository);
        //LiveConcertService liveConcertService=new LiveConcertService(concertRepository);
        //PlaylistService playlistService=new PlaylistService(playlistRepository);
        //ListenerService listenerService=new ListenerService(listenerRepository,subscriptionRepository);
        //SubscriptionService subscriptionService=new SubscriptionService(subscriptionRepository);
        //GenreService genreService = new GenreService(new InMemoryRepository<Genre>());
        //---------------------------------------------------------
        //Listener listener = listenerRepository.get(0);
        //History userHistory = new History(listener);
        //userHistory.addSongToHistory(songRepository.get(0));
        //userHistory.addSongToHistory(songRepository.get(1));
        //List<Song> allSongs = new ArrayList<>(songRepository.getAll().values());
        //RecommendationService recommendationService = new RecommendationService(userHistory, allSongs);
        //---------------------------------------------------------
        //Controller
        //MusicController musicController=new MusicController(artistService,albumService,songService,playlistService,listenerService,
                //liveConcertService,recommendationService,subscriptionService,genreService);
        //---------------------------------------------------------
        // Konsole
        ///MusicKonsole musicKonsole=new MusicKonsole(musicController);
        //musicKonsole.showMainMenu();
    }

    static class MusicAppComponents {
        ArtistService artistService;
        AlbumService albumService;
        SongService songService;
        PlaylistService playlistService;
        ListenerService listenerService;
        LiveConcertService liveConcertService;
        RecommendationService recommendationService;
        SubscriptionService subscriptionService;
        GenreService genreService;

        public MusicAppComponents(
                ArtistService artistService, AlbumService albumService, SongService songService,
                PlaylistService playlistService, ListenerService listenerService, LiveConcertService liveConcertService,
                RecommendationService recommendationService, SubscriptionService subscriptionService, GenreService genreService) {
            this.artistService = artistService;
            this.albumService = albumService;
            this.songService = songService;
            this.playlistService = playlistService;
            this.listenerService = listenerService;
            this.liveConcertService = liveConcertService;
            this.recommendationService = recommendationService;
            this.subscriptionService = subscriptionService;
            this.genreService = genreService;
        }
    }
    /**
     * Creates an in-memory repository for artists and populates it with initial data.
     *
     * @return The in-memory repository for artists.
     */
    private static IRepository<Artist> createInMemoryArtistRepository() {
        IRepository<Artist> artistRepo = new InMemoryRepository<>();
        artistRepo.create(new Artist("The Beatles", "beatles@gmail.com"));
        artistRepo.create(new Artist("Beyonce", "beyonceles@gmail.com"));
        artistRepo.create(new Artist("Adele", "adele@gmail.com"));
        artistRepo.create(new Artist("Ed Sheeran", "sheeran@gmail.com"));
        artistRepo.create(new Artist("Metallica", "metallica@gmail.com"));
        artistRepo.create(new Artist("Taylor Swift", "taylor.swift@example.com"));
        artistRepo.create(new Artist("Drake", "drake@example.com"));
        artistRepo.create(new Artist("Ariana Grande", "ariana.grande@example.com"));
        artistRepo.create(new Artist("Billie Eilish", "billie.eilish@example.com"));
        artistRepo.create(new Artist("Bruno Mars", "bruno.mars@example.com"));
        artistRepo.create(new Artist("Justin Bieber", "justin.bieber@example.com"));
        artistRepo.create(new Artist("The Weeknd", "the.weeknd@example.com"));
        artistRepo.create(new Artist("Lady Gaga", "lady.gaga@example.com"));
        artistRepo.create(new Artist("Kendrick Lamar", "kendrick.lamar@example.com"));
        artistRepo.create(new Artist("Post Malone", "post.malone@example.com"));
        artistRepo.create(new Artist("Cardi B", "cardi.b@example.com"));
        artistRepo.create(new Artist("Shawn Mendes", "shawn.mendes@example.com"));
        artistRepo.create(new Artist("Dua Lipa", "dua.lipa@example.com"));
        artistRepo.create(new Artist("Harry Styles", "harry.styles@example.com"));
        artistRepo.create(new Artist("Eminem", "eminem@example.com"));
        artistRepo.getAll().values().forEach(System.out::println);
        return artistRepo;
    }

    /**
     * Creates an in-memory repository for songs and populates it with initial data.
     *
     * @param albumRepo The in-memory repository for albums.
     * @return The in-memory repository for songs.
     */
    private static IRepository<Song> createInMemorySongRepository(IRepository<Album> albumRepo) {
        IRepository<Song> songRepo = new InMemoryRepository<>();
        Genre rock = new Genre("Rock");
        Genre pop = new Genre("Pop");
        Genre rap = new Genre("Rap");
        Genre hipHop = new Genre("Hip Hop");
        Genre electronic = new Genre("Electronic");
        createSongIfAlbumExists(songRepo, albumRepo, 1, "Here Comes the Sun", 3.05f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 1, "Come Together", 4.20f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 2, "Formation", 4.15f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 3, "Hello", 4.55f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 4, "Shape of You", 3.54f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 5, "Battery", 5.10f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 6, "Style", 3.50f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 7, "God's Plan", 3.19f,hipHop);
        createSongIfAlbumExists(songRepo, albumRepo, 8, "Thank U, Next", 3.23f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 9, "When We All Fall Asleep", 2.58f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 10, "Uptown Funk", 4.30f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 11, "Justice", 3.55f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 12, "Blinding Lights", 3.20f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 13, "Shallow", 3.00f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 14, "Humble", 2.57f,hipHop);
        createSongIfAlbumExists(songRepo, albumRepo, 15, "Circles", 3.35f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 16, "Future Nostalgia", 3.06f,rap);
        createSongIfAlbumExists(songRepo, albumRepo, 17, "In My Blood", 3.30f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 18, "Levitating", 3.24f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 19, "Watermelon Sugar", 3.10f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 20, "Lose Yourself", 5.20f,rap);
        createSongIfAlbumExists(songRepo, albumRepo, 21, "Crazy in Love", 5.10f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 21, "Baby Boy", 5.20f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 21, "Me, Myself and I", 5.10f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 21, "Naughty Girl", 5.23f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 21, "Dangerously in Love 2", 5.25f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 21, "Hip Hop Star", 5.27f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 22, "Irreplaceable", 5.28f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 22, "Beautiful Liar", 5.29f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 22, "Green Light", 5.30f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 22, "Upgrade U", 4.27f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 22, "Ring the Alarm", 4.29f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 22, "Deja Vu", 4.25f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 23, "Single Ladies", 4.25f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 23, "Halo", 4.25f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 23, "If I Were a Boy", 4.25f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 23, "Diva", 4.25f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 23, "Sweet Dreams", 4.25f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 23, "Ego", 4.26f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 24, "Run the World", 4.27f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 24, "Love on Top", 4.28f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 24, "Best Thing I Never Had", 4.29f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 24, "Countdown", 3.25f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 24, "I Care", 3.26f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 24, "End of Time", 3.27f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 25, "Drunk in Love", 3.27f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 25, "Partition", 3.28f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 25, "Flawless", 3.29f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 25, "Pretty Hurts", 4.27f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 25, "XO", 4.28f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 25, "Jealous", 4.29f,rock);
        createSongIfAlbumExists(songRepo, albumRepo, 26, "Break My Soul", 3.29f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 26, "Cuff It", 3.22f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 26, "Alien Superstar", 3.21f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 26, "Virgo’s Groove", 4.29f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 26, "Church Girl", 3.29f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 26, "Honey", 2.29f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 27, "APESHT", 2.29f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 27, "Summer", 2.28f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 27, "Friends", 2.27f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 27, "Boss", 3.26f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 27, "Heard About Us", 3.25f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 27, "LoveHappy", 3.26f,electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 28, "Chasing Pavements", 3.26f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 28, "Hometown Glory", 3.25f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 28, "Make You Feel My Love", 3.24f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 28, "Cold Shoulder", 3.23f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 28, "Right as Rain", 3.22f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 29, "Rolling in the Deep", 3.21f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 29, "Someone Like You", 3.22f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 29, "Set Fire to the Rain", 3.23f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 29, "Turning Tables", 3.24f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 29, "Rumour Has It", 3.25f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 30, "When We Were Young", 2.25f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 30, "Send My Love", 4.26f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 30, "Million Years Ago", 5.25f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 30, "Send My Love", 2.25f,pop);
        createSongIfAlbumExists(songRepo, albumRepo, 30, "Water Under the Bridge",3.26f,pop);
        songRepo.getAll().values().forEach(System.out::println);
        return songRepo;
    }
    /**
     * Creates a song if the given album exists.
     *
     * @param songRepo The repository for songs.
     * @param albumRepo The repository for albums.
     * @param albumId The ID of the album to which the song will be added.
     * @param songName The name of the song.
     * @param duration The duration of the song.
     * @param genre The musical genre of the song.
     */
    private static void createSongIfAlbumExists(IRepository<Song> songRepo, IRepository<Album> albumRepo, int albumId, String songName, float duration, Genre genre) {
        Album album = albumRepo.read(albumId);
        if (album != null) {
            Song song = new Song(songName, duration, album);
            if (genre != null) {
                song.setGenre(genre);
            }
            songRepo.create(song);
            album.addSong(song);
        } else {
            System.out.println("Album with id " + albumId + " was not found.");
        }
    }



    /**
     * Creates an in-memory repository for albums and populates it with initial data.
     *
     * @param artistRepo The in-memory repository for artists.
     * @return The in-memory repository for albums.
     */
    private static IRepository<Album> createInMemoryAlbumRepository(IRepository<Artist> artistRepo) {
        IRepository<Album> albumRepo = new InMemoryRepository<>();
        Genre rock = new Genre("Rock");
        Genre pop = new Genre("Pop");
        Genre rap = new Genre("Rap");
        Genre hipHop = new Genre("Hip Hop");
        Genre electronic = new Genre("Electronic");
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "Abbey Road", LocalDate.of(1969, 9, 26),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "Lemonade", LocalDate.of(2016, 4, 23),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 3, "25", LocalDate.of(2015, 11, 20),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 4, "Divide", LocalDate.of(2017, 3, 3),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 5, "Master of Puppets", LocalDate.of(1986, 3, 3),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 6, "1989", LocalDate.of(2014, 10, 27),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 7, "Scorpion", LocalDate.of(2018, 6, 29),hipHop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 8, "Thank U, Next", LocalDate.of(2019, 2, 8),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 9, "When We All Fall Asleep, Where Do We Go?", LocalDate.of(2019, 3, 29),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 10, "Uptown Special", LocalDate.of(2015, 1, 13),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 11, "Justice", LocalDate.of(2021, 3, 19),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 12, "After Hours", LocalDate.of(2020, 3, 20),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 13, "A Star is Born", LocalDate.of(2018, 10, 5),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 14, "DAMN.", LocalDate.of(2017, 4, 14),hipHop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 15, "Hollywood's Bleeding", LocalDate.of(2019, 9, 6),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 16, "Single", LocalDate.of(2020, 8, 7),rap);
        createAlbumIfArtistExists(albumRepo, artistRepo, 17, "Shawn Mendes", LocalDate.of(2018, 5, 25),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 18, "Future Nostalgia", LocalDate.of(2020, 3, 27),electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 19, "Fine Line", LocalDate.of(2019, 12, 13),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "8 Mile Soundtrack", LocalDate.of(2002, 10, 29),rap);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "Dangerously in Love", LocalDate.of(2003, 10, 29),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "B'Day", LocalDate.of(2006, 12, 29),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "I Am Sasha Fierce", LocalDate.of(2008, 12, 30),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "4", LocalDate.of(2011, 9, 12),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "BEYONCE", LocalDate.of(2013, 10, 12),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "Renaissance", LocalDate.of(2022, 5, 16),electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 3, "19", LocalDate.of(2008, 5, 16),electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 3, "21", LocalDate.of(2011, 10, 3),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 3, "25", LocalDate.of(2015, 11, 5),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 3, "30", LocalDate.of(2021, 6, 13),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "Please Please Me", LocalDate.of(1963, 11, 7),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "A Hard Day's Night", LocalDate.of(1964, 2, 8),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "Rubber Soul", LocalDate.of(1965, 11, 28),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "Revolver", LocalDate.of(1966, 10, 29),electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "Pepper's Lonely Hearts Club Band ", LocalDate.of(1967, 12, 27),electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "The White Album", LocalDate.of(1968, 4, 17),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "Let It Be", LocalDate.of(1970, 3, 18),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 7, "Thank Me Later", LocalDate.of(2010, 4, 28),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 7, "Take Care", LocalDate.of(2011, 4, 17),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 7, "Nothing Was the Same", LocalDate.of(2013, 4, 17),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 7, "Certified Lover Boy", LocalDate.of(2021, 5, 15),electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 7, "Her Loss", LocalDate.of(2022, 6, 16),electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "The Slim Shady LP", LocalDate.of(1999, 7, 17),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "The Marshall Mathers LP", LocalDate.of(2000, 8, 18),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "The Eminem Show", LocalDate.of(2002, 9, 19),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "Encore", LocalDate.of(2004, 10, 20),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "Relapse", LocalDate.of(2009, 11, 21),electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "Recovery", LocalDate.of(2010, 12, 22),electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "The Marshall Mathers LP 2", LocalDate.of(2013, 1, 23),electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "Revival", LocalDate.of(2017, 3, 24),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "Kamikaze", LocalDate.of(2018, 6, 25),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "Music to Be Murdered By", LocalDate.of(2020, 9, 26),pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 4, "Plus", LocalDate.of(2011, 12, 7),electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 4, "Multiply", LocalDate.of(2014, 11, 6),electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 4, "Divide", LocalDate.of(2017, 10, 5),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 4, "No.6 Collaborations Project", LocalDate.of(2019, 9, 4),rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 4, "Equals", LocalDate.of(2021, 8, 3),pop);
        albumRepo.getAll().values().forEach(System.out::println);
        return albumRepo;
    }
    private static void createAlbumIfArtistExists(IRepository<Album> albumRepo, IRepository<Artist> artistRepo, int artistId, String albumName, LocalDate releaseDate, Genre genre) {
        Artist artist = artistRepo.get(artistId);
        if (artist != null) {
            Album album = new Album(albumName, releaseDate, artist);
            album.setGenre(genre);
            albumRepo.create(album);
        } else {
            System.out.println("Artist with id " + artistId + "was not found.");
        }
    }

    /**
     * Creates an in-memory repository for listeners and populates it with initial data.
     *
     * @return The in-memory repository for listeners.
     */
    private static IRepository<Listener> createInMemoryListenerRepository() {
        IRepository<Listener> listenerRepo = new InMemoryRepository<>();
        listenerRepo.create(new Listener( "John", "john.doe@email.com"));
        listenerRepo.create(new Listener( "Jane Smith", "jane.smith@email.com"));
        listenerRepo.create(new Listener( "Chris Johnson", "chris.johnson@email.com"));
        listenerRepo.create(new Listener( "Sarah Williams", "sarah.williams@email.com"));
        listenerRepo.create(new Listener( "David Brown", "david.brown@email.com"));
        listenerRepo.create(new Listener("Alice Walker", "alice.walker@example.com"));
        listenerRepo.create(new Listener("Bob Marley", "bob.marley@example.com"));
        listenerRepo.create(new Listener("Charlie Brown", "charlie.brown@example.com"));
        listenerRepo.create(new Listener("Daisy Johnson", "daisy.johnson@example.com"));
        listenerRepo.create(new Listener("Edward Elric", "edward.elric@example.com"));
        listenerRepo.create(new Listener("Fiona Apple", "fiona.apple@example.com"));
        listenerRepo.create(new Listener("George Orwell", "george.orwell@example.com"));
        listenerRepo.create(new Listener("Hannah Montana", "hannah.montana@example.com"));
        listenerRepo.create(new Listener("Isaac Newton", "isaac.newton@example.com"));
        listenerRepo.create(new Listener("Jack Sparrow", "jack.sparrow@example.com"));
        listenerRepo.create(new Listener("Karen Page", "karen.page@example.com"));
        listenerRepo.create(new Listener("Larry Page", "larry.page@example.com"));
        listenerRepo.create(new Listener("Monica Geller", "monica.geller@example.com"));
        listenerRepo.create(new Listener("Nick Fury", "nick.fury@example.com"));
        listenerRepo.create(new Listener("Olivia Pope", "olivia.pope@example.com"));
        listenerRepo.getAll().values().forEach(System.out::println);
        return listenerRepo;
    }

    /**
     * Creates an in-memory repository for subscriptions and populates it with initial data.
     *
     * @param listenerRepo The in-memory repository for listeners.
     * @return The in-memory repository for subscriptions.
     */
    private static IRepository<Subscription> createInMemorySubscriptionRepository(IRepository<Listener> listenerRepo) {
        IRepository<Subscription> subscriptionRepo = new InMemoryRepository<>();
        Subscription sub1=new Subscription("Basic", 9.99f, listenerRepo.get(1));  // John Doe
        Subscription sub2=new Subscription("Premium", 14.99f, listenerRepo.get(2)); // Jane Smith
        Subscription sub3 =new Subscription("Basic", 9.99f, listenerRepo.get(3));  // Chris Johnson
        Subscription sub4=new Subscription("Premium", 14.99f, listenerRepo.get(4)); // Sarah Williams
        Subscription sub5=new Subscription("Basic", 9.99f, listenerRepo.get(5));  // David Brown
        Subscription sub6=new Subscription("Basic", 9.99f, listenerRepo.get(1));  // Alice Walker
        Subscription sub7=new Subscription("Premium", 14.99f, listenerRepo.get(2)); // Bob Marley
        Subscription sub8=new Subscription("Basic", 9.99f, listenerRepo.get(3));  // Charlie Brown
        Subscription sub9=new Subscription("Premium", 14.99f, listenerRepo.get(4)); // Daisy Johnson
        Subscription sub10=new Subscription("Basic", 9.99f, listenerRepo.get(5));    // Edward Elric
        Subscription sub11=new Subscription("Premium", 14.99f, listenerRepo.get(6)); // Fiona Apple
        Subscription sub12=new Subscription("Basic", 9.99f, listenerRepo.get(7));    // George Orwell
        Subscription sub13=new Subscription("Premium", 14.99f, listenerRepo.get(8)); // Hannah Montana
        Subscription sub14=new Subscription("Basic", 9.99f, listenerRepo.get(9));    // Isaac Newton
        Subscription sub15=new Subscription("Premium", 14.99f, listenerRepo.get(10)); // Jack Sparrow
        Subscription sub16=new Subscription("Basic", 9.99f, listenerRepo.get(11));   // Karen Page
        Subscription sub17=new Subscription("Premium", 14.99f, listenerRepo.get(12)); // Larry Page
        Subscription sub18=new Subscription("Basic", 9.99f, listenerRepo.get(13));   // Monica Geller
        Subscription sub19=new Subscription("Premium", 14.99f, listenerRepo.get(14)); // Nick Fury
        Subscription sub20=new Subscription("Basic", 9.99f, listenerRepo.get(15));  // Olivia Pope
        Listener john = listenerRepo.get(1);
        Listener jane = listenerRepo.get(2);
        Listener chris = listenerRepo.get(3);
        Listener sarah =listenerRepo.get(4);
        Listener david = listenerRepo.get(5);
        Listener alice =listenerRepo.get(6);
        Listener bob = listenerRepo.get(7);
        Listener charlie = listenerRepo.get(8);
        Listener daisy = listenerRepo.get(9);
        Listener edward = listenerRepo.get(10);
        Listener fiona = listenerRepo.get(11);
        Listener george = listenerRepo.get(12);
        Listener hannah =listenerRepo.get(13);
        Listener isaac = listenerRepo.get(14);
        Listener jack = listenerRepo.get(15);
        Listener karen = listenerRepo.get(16);
        Listener larry = listenerRepo.get(17);
        Listener monica = listenerRepo.get(18);
        Listener nick = listenerRepo.get(19);
        Listener olivia = listenerRepo.get(20);
        if (john != null) {
            john.setSubscription(sub1);
        }
        if (jane != null) {
            jane.setSubscription(sub2);
        }
        if (chris != null) {
            chris.setSubscription(sub3);
        }
        if (sarah != null) {
            sarah.setSubscription(sub4);
        }
        if (david != null) {
            david.setSubscription(sub5);
        }
        if (alice != null) {
            alice.setSubscription(sub6);
        }
        if (bob!= null) {
            bob.setSubscription(sub7);
        }
        if (charlie != null) {
            charlie.setSubscription(sub8);
        }
        if (daisy != null) {
            daisy.setSubscription(sub9);
        }
        if (edward != null) {
            edward.setSubscription(sub10);
        }
        if (fiona != null) {
            fiona.setSubscription(sub11);
        }
        if (george != null) {
            george.setSubscription(sub12);
        }
        if (hannah != null) {
            hannah.setSubscription(sub13);
        }
        if (isaac != null) {
            isaac.setSubscription(sub14);
        }
        if (jack!= null) {
            jack.setSubscription(sub15);
        }
        if (karen!= null) {
            karen.setSubscription(sub16);
        }
        if (larry != null) {
            larry.setSubscription(sub17);
        }
        if (monica != null) {
            monica.setSubscription(sub18);
        }
        if (nick != null) {
            nick.setSubscription(sub19);
        }
        if (olivia != null) {
            olivia.setSubscription(sub20);
        }
        subscriptionRepo.create(sub1);
        subscriptionRepo.create(sub2);
        subscriptionRepo.create(sub3);
        subscriptionRepo.create(sub4);
        subscriptionRepo.create(sub5);
        subscriptionRepo.create(sub6);
        subscriptionRepo.create(sub7);
        subscriptionRepo.create(sub8);
        subscriptionRepo.create(sub9);
        subscriptionRepo.create(sub10);
        subscriptionRepo.create(sub11);
        subscriptionRepo.create(sub12);
        subscriptionRepo.create(sub13);
        subscriptionRepo.create(sub14);
        subscriptionRepo.create(sub15);
        subscriptionRepo.create(sub16);
        subscriptionRepo.create(sub17);
        subscriptionRepo.create(sub18);
        subscriptionRepo.create(sub19);
        subscriptionRepo.create(sub20);
        subscriptionRepo.getAll().values().forEach(System.out::println);
        return subscriptionRepo;
    }

    /**
     * Creates an in-memory repository for live concerts and populates it with initial data.
     *
     * @param artistRepo The in-memory repository for artists.
     * @return The in-memory repository for live concerts.
     */
    private static IRepository<LiveConcert> createInMemoryLiveConcertRepository( IRepository<Artist> artistRepo) {
        IRepository<LiveConcert> concertRepo = new InMemoryRepository<>();
        concertRepo.create(new LiveConcert("The Beatles Live at Abbey Road", new Date(), artistRepo.get(1), 5000, true, "Live")); //1
        concertRepo.create(new LiveConcert("Beyoncé World Concert", new Date(), artistRepo.get(2), 10000, true,  "Live")); //2
        concertRepo.create(new LiveConcert("Adele Live Concert", new Date(),artistRepo.get(3), 8000, true,  "Live")); //3
        concertRepo.create(new LiveConcert("Ed Sheeran Divide Concert", new Date(), artistRepo.get(4), 12000, true,  "Live")); //4
        concertRepo.create(new LiveConcert("Metallica Concert", new Date(), artistRepo.get(5), 15000, true,  "Live")); //5
        concertRepo.create(new LiveConcert("Taylor Swift Reputation Concert", new Date(), artistRepo.get(6), 14000, true,  "Live")); //6
        concertRepo.create(new LiveConcert("Drake Summer Sixteen", new Date(), artistRepo.get(7), 9000, true,  "Live")); //7
        concertRepo.create(new LiveConcert("Ariana Grande Dangerous Woman Concert", new Date(), artistRepo.get(8), 11000, true,  "Live")); //8
        concertRepo.create(new LiveConcert("Billie Eilish When We All Fall Asleep", new Date(), artistRepo.get(9), 13000, true,  "Live")); //9
        concertRepo.create(new LiveConcert("Bruno Mars 24K Magic Concert", new Date(), artistRepo.get(10), 7000, true,  "Live")); //10
        concertRepo.create(new LiveConcert("Justin Bieber Purpose Concert", new Date(), artistRepo.get(11), 6000, true, "Live")); //11
        concertRepo.create(new LiveConcert("The Weeknd After Hours Concert", new Date(), artistRepo.get(12), 10000, true,  "Live")); //12
        concertRepo.create(new LiveConcert("Lady Gaga Chromatica Ball", new Date(),artistRepo.get(13), 100, true,  "Live")); //13
        concertRepo.create(new LiveConcert("Kendrick Lamar DAMN Concert", new Date(),artistRepo.get(14), 9500, true,  "Live")); //14
        concertRepo.create(new LiveConcert("Post Malone Hollywood's Bleeding Concert", new Date(),artistRepo.get(15), 12000, true,  "Live")); //15
        concertRepo.create(new LiveConcert("Cardi B Invasion of Privacy Tour", new Date(), artistRepo.get(16), 8500, true, "Live")); //16
        concertRepo.create(new LiveConcert("Shawn Mendes Wonder Tour", new Date(), artistRepo.get(17), 9500, true,  "Live")); //17
        concertRepo.create(new LiveConcert("Dua Lipa Future Nostalgia Tour", new Date(), artistRepo.get(18), 11000, true,  "Live")); //18
        concertRepo.create(new LiveConcert("Harry Styles Love On Tour", new Date(), artistRepo.get(19), 12000, true,  "Live")); //19
        concertRepo.create(new LiveConcert("Eminem Revival Tour", new Date(), artistRepo.get(20), 15000, true,  "Live")); //20
        concertRepo.getAll().values().forEach(System.out::println);
        return concertRepo;
    }

    private static IRepository<Artist> createFileBasedArtistRepository() {
        String filePath = "artists.csv"; // Specify the file to store the data.
        IRepository<Artist> artistRepo = new FileRepository<>(filePath);
        artistRepo.create(new Artist("The Beatles", "beatles@gmail.com"));
        artistRepo.create(new Artist("Beyonce", "beyonceles@gmail.com"));
        artistRepo.create(new Artist("Adele", "adele@gmail.com"));
        artistRepo.create(new Artist("Ed Sheeran", "sheeran@gmail.com"));
        artistRepo.create(new Artist("Metallica", "metallica@gmail.com"));
        artistRepo.create(new Artist("Taylor Swift", "taylor.swift@example.com"));
        artistRepo.create(new Artist("Drake", "drake@example.com"));
        artistRepo.create(new Artist("Ariana Grande", "ariana.grande@example.com"));
        artistRepo.create(new Artist("Billie Eilish", "billie.eilish@example.com"));
        artistRepo.create(new Artist("Bruno Mars", "bruno.mars@example.com"));
        artistRepo.create(new Artist("Justin Bieber", "justin.bieber@example.com"));
        artistRepo.create(new Artist("The Weeknd", "the.weeknd@example.com"));
        artistRepo.create(new Artist("Lady Gaga", "lady.gaga@example.com"));
        artistRepo.create(new Artist("Kendrick Lamar", "kendrick.lamar@example.com"));
        artistRepo.create(new Artist("Post Malone", "post.malone@example.com"));
        artistRepo.create(new Artist("Cardi B", "cardi.b@example.com"));
        artistRepo.create(new Artist("Shawn Mendes", "shawn.mendes@example.com"));
        artistRepo.create(new Artist("Dua Lipa", "dua.lipa@example.com"));
        artistRepo.create(new Artist("Harry Styles", "harry.styles@example.com"));
        artistRepo.create(new Artist("Eminem", "eminem@example.com"));
        artistRepo.getAll().values().forEach(System.out::println);
        return artistRepo;
    }
    private static IRepository<Song> createFileBasedSongRepository(IRepository<Album>albumRepo) {
        String filePath = "songs.csv"; // File for storing song data
        IRepository<Song> songRepo = new FileRepository<>(filePath);
        Genre rock = new Genre("Rock");
        Genre pop = new Genre("Pop");
        Genre rap = new Genre("Rap");
        Genre hipHop = new Genre("Hip Hop");
        Genre electronic = new Genre("Electronic");
        createSongIfAlbumExists(songRepo, albumRepo, 1, "Here Comes the Sun", 3.05f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 1, "Come Together", 4.20f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 2, "Formation", 4.15f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 3, "Hello", 4.55f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 4, "Shape of You", 3.54f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 5, "Battery", 5.10f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 6, "Style", 3.50f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 7, "God's Plan", 3.19f, hipHop);
        createSongIfAlbumExists(songRepo, albumRepo, 8, "Thank U, Next", 3.23f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 9, "When We All Fall Asleep", 2.58f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 10, "Uptown Funk", 4.30f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 11, "Justice", 3.55f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 12, "Blinding Lights", 3.20f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 13, "Shallow", 3.00f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 14, "Humble", 2.57f, hipHop);
        createSongIfAlbumExists(songRepo, albumRepo, 15, "Circles", 3.35f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 16, "Future Nostalgia", 3.06f, rap);
        createSongIfAlbumExists(songRepo, albumRepo, 17, "In My Blood", 3.30f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 18, "Levitating", 3.24f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 19, "Watermelon Sugar", 3.10f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 20, "Lose Yourself", 5.20f, rap);
        createSongIfAlbumExists(songRepo, albumRepo, 21, "Crazy in Love", 5.10f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 21, "Baby Boy", 5.20f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 21, "Me, Myself and I", 5.10f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 21, "Naughty Girl", 5.23f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 21, "Dangerously in Love 2", 5.25f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 21, "Hip Hop Star", 5.27f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 22, "Irreplaceable", 5.28f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 22, "Beautiful Liar", 5.29f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 22, "Green Light", 5.30f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 22, "Upgrade U", 4.27f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 22, "Ring the Alarm", 4.29f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 22, "Deja Vu", 4.25f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 23, "Single Ladies", 4.25f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 23, "Halo", 4.25f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 23, "If I Were a Boy", 4.25f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 23, "Diva", 4.25f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 23, "Sweet Dreams", 4.25f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 23, "Ego", 4.26f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 24, "Run the World", 4.27f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 24, "Love on Top", 4.28f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 24, "Best Thing I Never Had", 4.29f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 24, "Countdown", 3.25f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 24, "I Care", 3.26f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 24, "End of Time", 3.27f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 25, "Drunk in Love", 3.27f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 25, "Partition", 3.28f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 25, "Flawless", 3.29f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 25, "Pretty Hurts", 4.27f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 25, "XO", 4.28f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 25, "Jealous", 4.29f, rock);
        createSongIfAlbumExists(songRepo, albumRepo, 26, "Break My Soul", 3.29f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 26, "Cuff It", 3.22f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 26, "Alien Superstar", 3.21f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 26, "Virgo’s Groove", 4.29f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 26, "Church Girl", 3.29f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 26, "Honey", 2.29f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 27, "APESHT", 2.29f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 27, "Summer", 2.28f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 27, "Friends", 2.27f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 27, "Boss", 3.26f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 27, "Heard About Us", 3.25f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 27, "LoveHappy", 3.26f, electronic);
        createSongIfAlbumExists(songRepo, albumRepo, 28, "Chasing Pavements", 3.26f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 28, "Hometown Glory", 3.25f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 28, "Make You Feel My Love", 3.24f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 28, "Cold Shoulder", 3.23f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 28, "Right as Rain", 3.22f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 29, "Rolling in the Deep", 3.21f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 29, "Someone Like You", 3.22f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 29, "Set Fire to the Rain", 3.23f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 29, "Turning Tables", 3.24f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 29, "Rumour Has It", 3.25f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 30, "When We Were Young", 2.25f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 30, "Send My Love", 4.26f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 30, "Million Years Ago", 5.25f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 30, "Send My Love", 2.25f, pop);
        createSongIfAlbumExists(songRepo, albumRepo, 30, "Water Under the Bridge", 3.26f, pop);
        songRepo.getAll().values().forEach(System.out::println);
        return songRepo;
    }

    private static IRepository<Album> createFileBasedAlbumRepository(IRepository<Artist>artistRepo) {
        String filePath = "albums.csv"; // File for storing album data
        IRepository<Album> albumRepo = new FileRepository<>(filePath);
        Genre rock = new Genre("Rock");
        Genre pop = new Genre("Pop");
        Genre rap = new Genre("Rap");
        Genre hipHop = new Genre("Hip Hop");
        Genre electronic = new Genre("Electronic");
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "Abbey Road", LocalDate.of(1969, 9, 26), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "Lemonade", LocalDate.of(2016, 4, 23), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 3, "25", LocalDate.of(2015, 11, 20), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 4, "Divide", LocalDate.of(2017, 3, 3), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 5, "Master of Puppets", LocalDate.of(1986, 3, 3), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 6, "1989", LocalDate.of(2014, 10, 27), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 7, "Scorpion", LocalDate.of(2018, 6, 29), hipHop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 8, "Thank U, Next", LocalDate.of(2019, 2, 8), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 9, "When We All Fall Asleep, Where Do We Go?", LocalDate.of(2019, 3, 29), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 10, "Uptown Special", LocalDate.of(2015, 1, 13), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 11, "Justice", LocalDate.of(2021, 3, 19), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 12, "After Hours", LocalDate.of(2020, 3, 20), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 13, "A Star is Born", LocalDate.of(2018, 10, 5), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 14, "DAMN.", LocalDate.of(2017, 4, 14), hipHop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 15, "Hollywood's Bleeding", LocalDate.of(2019, 9, 6), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 16, "Single", LocalDate.of(2020, 8, 7), rap);
        createAlbumIfArtistExists(albumRepo, artistRepo, 17, "Shawn Mendes", LocalDate.of(2018, 5, 25), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 18, "Future Nostalgia", LocalDate.of(2020, 3, 27), electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 19, "Fine Line", LocalDate.of(2019, 12, 13), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "8 Mile Soundtrack", LocalDate.of(2002, 10, 29), rap);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "Dangerously in Love", LocalDate.of(2003, 10, 29), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "B'Day", LocalDate.of(2006, 12, 29), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "I Am Sasha Fierce", LocalDate.of(2008, 12, 30), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "4", LocalDate.of(2011, 9, 12), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "BEYONCE", LocalDate.of(2013, 10, 12), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 2, "Renaissance", LocalDate.of(2022, 5, 16), electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 3, "19", LocalDate.of(2008, 5, 16), electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 3, "21", LocalDate.of(2011, 10, 3), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 3, "25", LocalDate.of(2015, 11, 5), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 3, "30", LocalDate.of(2021, 6, 13), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "Please Please Me", LocalDate.of(1963, 11, 7), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "A Hard Day's Night", LocalDate.of(1964, 2, 8), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "Rubber Soul", LocalDate.of(1965, 11, 28), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "Revolver", LocalDate.of(1966, 10, 29), electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "Pepper's Lonely Hearts Club Band ", LocalDate.of(1967, 12, 27), electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "The White Album", LocalDate.of(1968, 4, 17), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 1, "Let It Be", LocalDate.of(1970, 3, 18), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 7, "Thank Me Later", LocalDate.of(2010, 4, 28), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 7, "Take Care", LocalDate.of(2011, 4, 17), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 7, "Nothing Was the Same", LocalDate.of(2013, 4, 17), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 7, "Certified Lover Boy", LocalDate.of(2021, 5, 15), electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 7, "Her Loss", LocalDate.of(2022, 6, 16), electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "The Slim Shady LP", LocalDate.of(1999, 7, 17), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "The Marshall Mathers LP", LocalDate.of(2000, 8, 18), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "The Eminem Show", LocalDate.of(2002, 9, 19), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "Encore", LocalDate.of(2004, 10, 20), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "Relapse", LocalDate.of(2009, 11, 21), electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "Recovery", LocalDate.of(2010, 12, 22), electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "The Marshall Mathers LP 2", LocalDate.of(2013, 1, 23), electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "Revival", LocalDate.of(2017, 3, 24), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "Kamikaze", LocalDate.of(2018, 6, 25), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 20, "Music to Be Murdered By", LocalDate.of(2020, 9, 26), pop);
        createAlbumIfArtistExists(albumRepo, artistRepo, 4, "Plus", LocalDate.of(2011, 12, 7), electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 4, "Multiply", LocalDate.of(2014, 11, 6), electronic);
        createAlbumIfArtistExists(albumRepo, artistRepo, 4, "Divide", LocalDate.of(2017, 10, 5), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 4, "No.6 Collaborations Project", LocalDate.of(2019, 9, 4), rock);
        createAlbumIfArtistExists(albumRepo, artistRepo, 4, "Equals", LocalDate.of(2021, 8, 3), pop);
        albumRepo.getAll().values().forEach(System.out::println);
        return albumRepo;
    }
    private static IRepository<Listener> createFileBasedListenerRepository() {
        String filePath = "listeners.csv"; // File for storing listener data
        IRepository<Listener> listenerRepo = new FileRepository<>(filePath);
        listenerRepo.create(new Listener("John", "john.doe@email.com"));
        listenerRepo.create(new Listener("Jane Smith", "jane.smith@email.com"));
        listenerRepo.create(new Listener("Chris Johnson", "chris.johnson@email.com"));
        listenerRepo.create(new Listener("Sarah Williams", "sarah.williams@email.com"));
        listenerRepo.create(new Listener("David Brown", "david.brown@email.com"));
        listenerRepo.create(new Listener("Alice Walker", "alice.walker@example.com"));
        listenerRepo.create(new Listener("Bob Marley", "bob.marley@example.com"));
        listenerRepo.create(new Listener("Charlie Brown", "charlie.brown@example.com"));
        listenerRepo.create(new Listener("Daisy Johnson", "daisy.johnson@example.com"));
        listenerRepo.create(new Listener("Edward Elric", "edward.elric@example.com"));
        listenerRepo.create(new Listener("Fiona Apple", "fiona.apple@example.com"));
        listenerRepo.create(new Listener("George Orwell", "george.orwell@example.com"));
        listenerRepo.create(new Listener("Hannah Montana", "hannah.montana@example.com"));
        listenerRepo.create(new Listener("Isaac Newton", "isaac.newton@example.com"));
        listenerRepo.create(new Listener("Jack Sparrow", "jack.sparrow@example.com"));
        listenerRepo.create(new Listener("Karen Page", "karen.page@example.com"));
        listenerRepo.create(new Listener("Larry Page", "larry.page@example.com"));
        listenerRepo.create(new Listener("Monica Geller", "monica.geller@example.com"));
        listenerRepo.create(new Listener("Nick Fury", "nick.fury@example.com"));
        listenerRepo.create(new Listener("Olivia Pope", "olivia.pope@example.com"));
        listenerRepo.getAll().values().forEach(System.out::println);
        return listenerRepo;
    }

    private static IRepository<LiveConcert> createFileBasedLiveConcertRepository(IRepository<Artist>artistRepo) {
        String filePath = "liveconcerts.csv"; // File for storing live concert data
        IRepository<LiveConcert> concertRepo = new FileRepository<>(filePath);
        concertRepo.create(new LiveConcert("The Beatles Live at Abbey Road", new Date(), artistRepo.get(1), 5000, true, "Live")); //1
        concertRepo.create(new LiveConcert("Beyoncé World Concert", new Date(), artistRepo.get(2), 10000, true, "Live")); //2
        concertRepo.create(new LiveConcert("Adele Live Concert", new Date(), artistRepo.get(3), 8000, true, "Live")); //3
        concertRepo.create(new LiveConcert("Ed Sheeran Divide Concert", new Date(), artistRepo.get(4), 12000, true, "Live")); //4
        concertRepo.create(new LiveConcert("Metallica Concert", new Date(), artistRepo.get(5), 15000, true, "Live")); //5
        concertRepo.create(new LiveConcert("Taylor Swift Reputation Concert", new Date(), artistRepo.get(6), 14000, true, "Live")); //6
        concertRepo.create(new LiveConcert("Drake Summer Sixteen", new Date(), artistRepo.get(7), 9000, true, "Live")); //7
        concertRepo.create(new LiveConcert("Ariana Grande Dangerous Woman Concert", new Date(), artistRepo.get(8), 11000, true, "Live")); //8
        concertRepo.create(new LiveConcert("Billie Eilish When We All Fall Asleep", new Date(), artistRepo.get(9), 13000, true, "Live")); //9
        concertRepo.create(new LiveConcert("Bruno Mars 24K Magic Concert", new Date(), artistRepo.get(10), 7000, true, "Live")); //10
        concertRepo.create(new LiveConcert("Justin Bieber Purpose Concert", new Date(), artistRepo.get(11), 6000, true, "Live")); //11
        concertRepo.create(new LiveConcert("The Weeknd After Hours Concert", new Date(), artistRepo.get(12), 10000, true, "Live")); //12
        concertRepo.create(new LiveConcert("Lady Gaga Chromatica Ball", new Date(), artistRepo.get(13), 100, true, "Live")); //13
        concertRepo.create(new LiveConcert("Kendrick Lamar DAMN Concert", new Date(), artistRepo.get(14), 9500, true, "Live")); //14
        concertRepo.create(new LiveConcert("Post Malone Hollywood's Bleeding Concert", new Date(), artistRepo.get(15), 12000, true, "Live")); //15
        concertRepo.create(new LiveConcert("Cardi B Invasion of Privacy Tour", new Date(), artistRepo.get(16), 8500, true, "Live")); //16
        concertRepo.create(new LiveConcert("Shawn Mendes Wonder Tour", new Date(), artistRepo.get(17), 9500, true, "Live")); //17
        concertRepo.create(new LiveConcert("Dua Lipa Future Nostalgia Tour", new Date(), artistRepo.get(18), 11000, true, "Live")); //18
        concertRepo.create(new LiveConcert("Harry Styles Love On Tour", new Date(), artistRepo.get(19), 12000, true, "Live")); //19
        concertRepo.create(new LiveConcert("Eminem Revival Tour", new Date(), artistRepo.get(20), 15000, true, "Live")); //20
        concertRepo.getAll().values().forEach(System.out::println);
        return concertRepo;
    }

    private static IRepository<Subscription> createFileBasedSubscriptionRepository(IRepository<Listener>listenerRepo) {
        String filePath = "subscriptions.csv"; // File for storing subscription data
        IRepository<Subscription> subscriptionRepo = new FileRepository<>(filePath);
        Subscription sub1 = new Subscription("Basic", 9.99f, listenerRepo.get(1));  // John Doe
        Subscription sub2 = new Subscription("Premium", 14.99f, listenerRepo.get(2)); // Jane Smith
        Subscription sub3 = new Subscription("Basic", 9.99f, listenerRepo.get(3));  // Chris Johnson
        Subscription sub4 = new Subscription("Premium", 14.99f, listenerRepo.get(4)); // Sarah Williams
        Subscription sub5 = new Subscription("Basic", 9.99f, listenerRepo.get(5));  // David Brown
        Subscription sub6 = new Subscription("Basic", 9.99f, listenerRepo.get(1));  // Alice Walker
        Subscription sub7 = new Subscription("Premium", 14.99f, listenerRepo.get(2)); // Bob Marley
        Subscription sub8 = new Subscription("Basic", 9.99f, listenerRepo.get(3));  // Charlie Brown
        Subscription sub9 = new Subscription("Premium", 14.99f, listenerRepo.get(4)); // Daisy Johnson
        Subscription sub10 = new Subscription("Basic", 9.99f, listenerRepo.get(5));    // Edward Elric
        Subscription sub11 = new Subscription("Premium", 14.99f, listenerRepo.get(6)); // Fiona Apple
        Subscription sub12 = new Subscription("Basic", 9.99f, listenerRepo.get(7));    // George Orwell
        Subscription sub13 = new Subscription("Premium", 14.99f, listenerRepo.get(8)); // Hannah Montana
        Subscription sub14 = new Subscription("Basic", 9.99f, listenerRepo.get(9));    // Isaac Newton
        Subscription sub15 = new Subscription("Premium", 14.99f, listenerRepo.get(10)); // Jack Sparrow
        Subscription sub16 = new Subscription("Basic", 9.99f, listenerRepo.get(11));   // Karen Page
        Subscription sub17 = new Subscription("Premium", 14.99f, listenerRepo.get(12)); // Larry Page
        Subscription sub18 = new Subscription("Basic", 9.99f, listenerRepo.get(13));   // Monica Geller
        Subscription sub19 = new Subscription("Premium", 14.99f, listenerRepo.get(14)); // Nick Fury
        Subscription sub20 = new Subscription("Basic", 9.99f, listenerRepo.get(15));  // Olivia Pope
        Listener john = listenerRepo.get(1);
        Listener jane = listenerRepo.get(2);
        Listener chris = listenerRepo.get(3);
        Listener sarah = listenerRepo.get(4);
        Listener david = listenerRepo.get(5);
        Listener alice = listenerRepo.get(6);
        Listener bob = listenerRepo.get(7);
        Listener charlie = listenerRepo.get(8);
        Listener daisy = listenerRepo.get(9);
        Listener edward = listenerRepo.get(10);
        Listener fiona = listenerRepo.get(11);
        Listener george = listenerRepo.get(12);
        Listener hannah = listenerRepo.get(13);
        Listener isaac = listenerRepo.get(14);
        Listener jack = listenerRepo.get(15);
        Listener karen = listenerRepo.get(16);
        Listener larry = listenerRepo.get(17);
        Listener monica = listenerRepo.get(18);
        Listener nick = listenerRepo.get(19);
        Listener olivia = listenerRepo.get(20);
        if (john != null) {
            john.setSubscription(sub1);
        }
        if (jane != null) {
            jane.setSubscription(sub2);
        }
        if (chris != null) {
            chris.setSubscription(sub3);
        }
        if (sarah != null) {
            sarah.setSubscription(sub4);
        }
        if (david != null) {
            david.setSubscription(sub5);
        }
        if (alice != null) {
            alice.setSubscription(sub6);
        }
        if (bob != null) {
            bob.setSubscription(sub7);
        }
        if (charlie != null) {
            charlie.setSubscription(sub8);
        }
        if (daisy != null) {
            daisy.setSubscription(sub9);
        }
        if (edward != null) {
            edward.setSubscription(sub10);
        }
        if (fiona != null) {
            fiona.setSubscription(sub11);
        }
        if (george != null) {
            george.setSubscription(sub12);
        }
        if (hannah != null) {
            hannah.setSubscription(sub13);
        }
        if (isaac != null) {
            isaac.setSubscription(sub14);
        }
        if (jack != null) {
            jack.setSubscription(sub15);
        }
        if (karen != null) {
            karen.setSubscription(sub16);
        }
        if (larry != null) {
            larry.setSubscription(sub17);
        }
        if (monica != null) {
            monica.setSubscription(sub18);
        }
        if (nick != null) {
            nick.setSubscription(sub19);
        }
        if (olivia != null) {
            olivia.setSubscription(sub20);
        }
        subscriptionRepo.create(sub1);
        subscriptionRepo.create(sub2);
        subscriptionRepo.create(sub3);
        subscriptionRepo.create(sub4);
        subscriptionRepo.create(sub5);
        subscriptionRepo.create(sub6);
        subscriptionRepo.create(sub7);
        subscriptionRepo.create(sub8);
        subscriptionRepo.create(sub9);
        subscriptionRepo.create(sub10);
        subscriptionRepo.create(sub11);
        subscriptionRepo.create(sub12);
        subscriptionRepo.create(sub13);
        subscriptionRepo.create(sub14);
        subscriptionRepo.create(sub15);
        subscriptionRepo.create(sub16);
        subscriptionRepo.create(sub17);
        subscriptionRepo.create(sub18);
        subscriptionRepo.create(sub19);
        subscriptionRepo.create(sub20);
        subscriptionRepo.getAll().values().forEach(System.out::println);
        return subscriptionRepo;
    }
}