import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Controller.MusicController;
import Domain.*;
import Konsole.MusicKonsole;
import Repository.IRepository;
import Repository.InMemoryRepository;
import Service.*;


/**
 * The main application class that manages a music recommendation system.
 * It creates various service components, repositories, and controls the application through the music console.
 */
public class MusicApp {
    public static void main(String[] args) {
        //Repo
        IRepository<Artist> artistRepository = createInMemoryArtistRepository();
        IRepository<Album> albumRepository =  createInMemoryAlbumRepository(artistRepository);
        IRepository<Song> songRepository = createInMemorySongRepository(albumRepository);
        IRepository<LiveConcert> concertRepository = createInMemoryLiveConcertRepository(artistRepository);
        IRepository<Playlist> playlistRepository = new InMemoryRepository<>();
        IRepository<Listener> listenerRepository = createInMemoryListenerRepository();
        IRepository<Subscription> subscriptionRepository = createInMemorySubscriptionRepository(listenerRepository);
        //Service
        ArtistService artistService=new ArtistService(artistRepository);
        AlbumService albumService=new AlbumService(albumRepository);
        SongService songService=new SongService(songRepository);
        LiveConcertService liveConcertService=new LiveConcertService(concertRepository);
        PlaylistService playlistService=new PlaylistService(playlistRepository);
        ListenerService listenerService=new ListenerService(listenerRepository,subscriptionRepository);
        SubscriptionService subscriptionService=new SubscriptionService(subscriptionRepository);

        Listener listener = listenerRepository.get(0);
        History userHistory = new History(listener);
        userHistory.addSongToHistory(songRepository.get(0));
        userHistory.addSongToHistory(songRepository.get(1));
        List<Song> allSongs = new ArrayList<>(songRepository.getAll().values());
        RecommendationService recommendationService = new RecommendationService(userHistory, allSongs);

        //Controller
        MusicController musicController=new MusicController(artistService,albumService,songService,playlistService,listenerService,
                liveConcertService,recommendationService,subscriptionService);
        //Konsole
        MusicKonsole musicKonsole=new MusicKonsole(musicController);
        musicKonsole.showMainMenu();
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
                song.setGenre(genre); // Setăm genul piesei doar dacă nu este null
            }
            songRepo.create(song);
            album.addSong(song);
        } else {
            System.out.println("Albumul cu ID-ul " + albumId + " nu a fost găsit. Melodia '" + songName + "' nu a fost adăugată.");
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

        // Afișăm toate albumele
        albumRepo.getAll().values().forEach(System.out::println);

        return albumRepo;
    }

    private static void createAlbumIfArtistExists(IRepository<Album> albumRepo, IRepository<Artist> artistRepo, int artistId, String albumName, LocalDate releaseDate, Genre genre) {
        Artist artist = artistRepo.get(artistId);
        if (artist != null) {
            Album album = new Album(albumName, releaseDate, artist); // Creăm albumul fără gen
            album.setGenre(genre); // Setăm genul folosind setter-ul
            albumRepo.create(album);
        } else {
            System.out.println("Artistul cu ID-ul " + artistId + " nu a fost găsit. Albumul '" + albumName + "' nu a fost adăugat.");
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


        // Afișăm toți utilizatorii
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

        // Asociază abonamentele la ascultători
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
        concertRepo.create(new LiveConcert("The Beatles Live at Abbey Road", new Date(), artistRepo.get(1), 5000, true, true, true, "Live"));
        concertRepo.create(new LiveConcert("Beyoncé World Concert", new Date(), artistRepo.get(2), 10000, true, true, true, "Live"));
        concertRepo.create(new LiveConcert("Adele Live Concert", new Date(),artistRepo.get(3), 8000, true, false, true, "Live"));
        concertRepo.create(new LiveConcert("Ed Sheeran Divide Concert", new Date(), artistRepo.get(4), 12000, true, true, false, "Live"));
        concertRepo.create(new LiveConcert("Metallica Concert", new Date(), artistRepo.get(5), 15000, true, false, false, "Live"));
        concertRepo.create(new LiveConcert("Taylor Swift Reputation Concert", new Date(), artistRepo.get(6), 14000, true, true, true, "Live"));
        concertRepo.create(new LiveConcert("Drake Summer Sixteen", new Date(), artistRepo.get(7), 9000, true, true, false, "Live"));
        concertRepo.create(new LiveConcert("Ariana Grande Dangerous Woman Concert", new Date(), artistRepo.get(8), 11000, true, false, true, "Live"));
        concertRepo.create(new LiveConcert("Billie Eilish When We All Fall Asleep", new Date(), artistRepo.get(9), 13000, true, true, false, "Live"));
        concertRepo.create(new LiveConcert("Bruno Mars 24K Magic Concert", new Date(), artistRepo.get(10), 7000, true, true, true, "Live"));
        concertRepo.create(new LiveConcert("Justin Bieber Purpose Concert", new Date(), artistRepo.get(11), 6000, true, false, true, "Live"));
        concertRepo.create(new LiveConcert("The Weeknd After Hours Concert", new Date(), artistRepo.get(12), 10000, true, true, true, "Live"));
        concertRepo.create(new LiveConcert("Lady Gaga Chromatica Ball", new Date(),artistRepo.get(13), 8000, true, false, false, "Live"));
        concertRepo.create(new LiveConcert("Kendrick Lamar DAMN Concert", new Date(),artistRepo.get(14), 9500, true, true, false, "Live"));
        concertRepo.create(new LiveConcert("Post Malone Hollywood's Bleeding Concert", new Date(),artistRepo.get(15), 12000, true, false, true, "Live"));
        concertRepo.create(new LiveConcert("Cardi B Invasion of Privacy Tour", new Date(), artistRepo.get(16), 8500, true, true, false, "Live"));
        concertRepo.create(new LiveConcert("Shawn Mendes Wonder Tour", new Date(), artistRepo.get(17), 9500, true, false, true, "Live"));
        concertRepo.create(new LiveConcert("Dua Lipa Future Nostalgia Tour", new Date(), artistRepo.get(18), 11000, true, true, true, "Live"));
        concertRepo.create(new LiveConcert("Harry Styles Love On Tour", new Date(), artistRepo.get(19), 12000, true, true, false, "Live"));
        concertRepo.create(new LiveConcert("Eminem Revival Tour", new Date(), artistRepo.get(20), 15000, true, false, true, "Live"));



        // Afișează toate concertele din repository
        concertRepo.getAll().values().forEach(System.out::println);

        return concertRepo;
    }

    // Creează o listă de genuri
    private static List<Genre> createGenres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("Rock"));
        genres.add(new Genre("Pop"));
        genres.add(new Genre("Hip Hop"));
        genres.add(new Genre("Jazz"));
        genres.add(new Genre("Classical"));
        genres.add(new Genre("Electronic"));

        return genres;
    }

}