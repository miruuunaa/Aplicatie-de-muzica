import Domain.*;
import Service.*;
import Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Flow;

import static org.junit.jupiter.api.Assertions.*;

public class Music_tests {

    private ArtistService artistService;
    private AlbumService albumService;
    private LiveConcertService concertService;
    private ListenerService listenerService;
    private PlaylistService playlistService;
    private SongService songService;
    private SubscriptionService subscriptionService;

    @BeforeEach
    public void setup() {
        IRepository<Artist> artistRepository = new InMemoryRepository<>();
        IRepository<Album> albumRepository = new InMemoryRepository<>();
        IRepository<LiveConcert> concertRepository = new InMemoryRepository<>();
        IRepository<Song> songRepository = new InMemoryRepository<>();
        IRepository<Playlist> playlistRepository = new InMemoryRepository<>();
        IRepository<Subscription> subscriptionRepository = new InMemoryRepository<>();
        IRepository<Listener> listenerRepository = new InMemoryRepository<>();

        artistService = new ArtistService(artistRepository);
        albumService = new AlbumService(albumRepository);
        concertService = new LiveConcertService(concertRepository);
        listenerService = new ListenerService(listenerRepository,subscriptionRepository);
        songService = new SongService(songRepository);
        playlistService = new PlaylistService(playlistRepository);
    }

    @Test
      public void testCalculateConcertVIPScore_Successful() {
        Listener listener = new Listener("John Doe", "john@example.com");
        listener.setSubscription(new Subscription("Premium", 30.0f, listener)); //30
        Artist artist = new Artist("Lady Gaga", "lady@gaga.com");
        artistService.addArtist(artist);
        for (int i = 0; i < 7; i++) {
            Listener follower = new Listener("Follower" + i, "follower" + i + "@example.com");
            artistService.enrollListenerToArtist(follower, artist); //50
        }
        Date concertDate = new Date();
        LiveConcert concert = new LiveConcert("Test Concert", concertDate, artist, 100, true, "Public");
        Listener attendee1 = new Listener("Paul Vere", "paul@example.com");
        Listener attendee2 = new Listener("Raul Bere", "raul@example.com");
        concert.getEarlyAccessList().add(attendee1);
        concert.getRegularAccesList().add(attendee2);
        concertService.addConcert(concert);
        double score = concertService.calculateConcertVIPScore(listener, concert.getId());
        assertEquals(80, score, "VIP score should be calculated correctly.");
    }


    @Test
    public void testCalculateConcertVIPScore_ConcertNotFound() {
        Listener listener = new Listener("John Doe", "john@example.com");
        listener.setSubscription(new Subscription("Premium", 30.0f, listener));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> concertService.calculateConcertVIPScore(listener, 999),
                "Exception should be thrown when concert is not found.");
        assertEquals("Concert not found.", exception.getMessage());
    }

    @Test
    public void testFilterAlbumsByGenre_Successful() {
        Artist artist = new Artist("Taylor Swift", "taylor@swiftmusic.com");
        artistService.addArtist(artist);
        Genre popGenre = new Genre("Pop");
        Genre countryGenre = new Genre("Country");
        Album album1 = new Album("1989", LocalDate.of(2014, 10, 27), artist);
        album1.setGenre(popGenre);
        Album album2 = new Album("Fearless", LocalDate.of(2008, 11, 11), artist);
        album2.setGenre(countryGenre);
        Album album3=new Album("Midnights", LocalDate.of(2022, 10, 21), artist);
        album3.setGenre(popGenre);
        albumService.addAlbum(album1);
        albumService.addAlbum(album2);
        albumService.addAlbum(album3);
        List<Album> filteredAlbums = artistService.filterAlbumsByGenre(artist.getId(), "Pop");
        assertEquals(2, filteredAlbums.size());
        assertTrue(filteredAlbums.stream().anyMatch(album -> album.getTitle().equals("1989")));
        assertTrue(filteredAlbums.stream().anyMatch(album -> album.getTitle().equals("Midnights")));
    }

    @Test
    public void testFilterAlbumsByGenre_ArtistNotFound() {
        List<Album> filteredAlbums = artistService.filterAlbumsByGenre(999, "Pop");
        assertTrue(filteredAlbums.isEmpty(), "No albums should be returned if artist is not found.");
    }

    @Test
    public void testFilterSongsByMinimumDuration_Successful() {
        Artist artist = new Artist("Ed Sheeran", "ed@sheeranmusic.com");
        artistService.addArtist(artist);
        Album album1 = new Album("Divide", LocalDate.of(2017, 3, 3), artist);
        Album album2 = new Album("No.6 Collaborations Project", LocalDate.of(2019, 7, 12), artist);
        Song song1 = new Song("Shape of You", 3.5f, album1);
        Song song2 = new Song("Perfect", 4.2f, album1);
        Song song3 = new Song("I Don't Care", 2.8f, album2);
        Song song4 = new Song("Beautiful People", 3.6f, album2);
        album1.addSong(song1);
        album1.addSong(song2);
        album2.addSong(song3);
        album2.addSong(song4);
        albumService.addAlbum(album1);
        albumService.addAlbum(album2);
        List<Song> filteredSongs = artistService.filterSongsByMinimumDuration(artist.getId(), 3.0f);
        assertEquals(3, filteredSongs.size(), "3 songs with duration >= 3.0");
        assertTrue(filteredSongs.stream().anyMatch(song -> song.getTitle().equals("Shape of You")));
        assertTrue(filteredSongs.stream().anyMatch(song -> song.getTitle().equals("Perfect")));
        assertTrue(filteredSongs.stream().anyMatch(song -> song.getTitle().equals("Beautiful People")));
    }

    @Test
    public void testFilterSongsByMinimumDuration_ArtistNotFound() {
        List<Song> filteredSongs = artistService.filterSongsByMinimumDuration(999, 3.0f);
        assertTrue(filteredSongs.isEmpty(), "No songs should be returned if artist is not found.");
    }

    @Test
    public void testSortAlbumsByReleaseDate_Successful() {
        Artist artist = new Artist("Taylor Swift", "taylor@swiftmusic.com");
        artistService.addArtist(artist);
        Album album1 = new Album("Fearless", LocalDate.of(2008, 11, 11), artist);
        Album album2 = new Album("1989", LocalDate.of(2014, 10, 27), artist);
        Album album3 = new Album("Lover", LocalDate.of(2019, 8, 23), artist);
        albumService.addAlbum(album1);
        albumService.addAlbum(album2);
        albumService.addAlbum(album3);
        List<Album> sortedAlbums = artistService.sortAlbumsByReleaseDate(artist.getId());
        assertEquals("Fearless", sortedAlbums.get(0).getTitle(), "First album must be 'Fearless'.");
        assertEquals("1989", sortedAlbums.get(1).getTitle(), "Second album must be  '1989'.");
        assertEquals("Lover", sortedAlbums.get(2).getTitle(), "Third  album must be  'Lover'.");
    }

    @Test
    public void testSortAlbumsByReleaseDate_ArtistNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> artistService.sortAlbumsByReleaseDate(999),
                "Exception should be thrown when artist is not found.");
        assertEquals("Artist not found.", exception.getMessage());
    }

    @Test
    public void testSortSongsByTitle_Successful() {
        Artist artist = new Artist("Ed Sheeran", "ed@musicmail.com");
        Album album = new Album("Divide", LocalDate.of(2017, 3, 3), artist);
        Song song1 = new Song("Castle on the Hill", 4.21f, album);
        Song song2 = new Song("Perfect", 4.40f, album);
        Song song3 = new Song("Shape of You", 3.53f, album);
        album.addSong(song1);
        album.addSong(song2);
        album.addSong(song3);
        albumService.addAlbum(album);
        List<Song> sortedSongs = albumService.sortSongsByTitle(album.getId());
        assertEquals("Castle on the Hill", sortedSongs.get(0).getTitle(), "First song must be 'Castle on the Hill'.");
        assertEquals("Perfect", sortedSongs.get(1).getTitle(), "Second song must be 'Perfect'.");
        assertEquals("Shape of You", sortedSongs.get(2).getTitle(), "Third song must be 'Shape of You'.");
    }

    @Test
    public void testSortSongsByTitle_AlbumNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> albumService.sortSongsByTitle(999),
                "Exception should be thrown when album is not found.");
        assertEquals("Album not found.", exception.getMessage());
    }
}
