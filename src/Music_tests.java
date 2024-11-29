import Domain.*;
import Service.*;
import Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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

//    @Test
//    public void testCalculateConcertVIPScore_Successful() {
//        Listener listener = new Listener("John Doe", "john@example.com");
//        listener.setSubscription(new Subscription("Premium", 30.0f, listener));
//
//        Artist artist = new Artist("Famous Artist", "artist@example.com");
//        artistService.addArtist(artist);
//
//        LiveConcert concert = new LiveConcert("Test Concert", LocalDate.now().atStartOfDay(), artist, 30, true, "Public");
//        concert. setTicketSold(10);
//        concertService.create(concert);
//
//        double score = concertService.calculateConcertVIPScore(listener, concert.getId());
//        assertEquals(65, score, "VIP score should be calculated correctly.");
//    }

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
        Artist artist = new Artist("Test Artist", "test@example.com");
        artistService.addArtist(artist);

        Album album1 = new Album("Pop Album", LocalDate.now(), artist);
        Album album2 = new Album("Rock Album", LocalDate.now(), artist);
        album1.setArtist(artist);
        album2.setArtist(artist);

        albumService.addAlbum(album1);
        albumService.addAlbum(album2);

        List<Album> filteredAlbums = artistService.filterAlbumsByGenre(artist.getId(), "Pop");
        assertEquals(1, filteredAlbums.size());
        assertEquals("Pop Album", filteredAlbums.get(0).getTitle());
    }

    @Test
    public void testFilterAlbumsByGenre_ArtistNotFound() {
        List<Album> filteredAlbums = artistService.filterAlbumsByGenre(999, "Pop");
        assertTrue(filteredAlbums.isEmpty(), "No albums should be returned if artist is not found.");
    }

    @Test
    public void testFilterSongsByMinimumDuration_Successful() {
        Artist artist = new Artist("Test Artist", "test@example.com");
        artistService.addArtist(artist);

        Album album1 = new Album("Pop Album", LocalDate.now(), artist);
        Album album2 = new Album("Rock Album", LocalDate.now(),artist);
        album1.setArtist(artist);
        album2.setArtist(artist);

        Song song1 = new Song("Song 1", 3.5f, album1);
        Song song2 = new Song("Song 2", 2.0f, album1);
        Song song3 = new Song("Song 3", 4.0f, album2);

        album1.setSongs(List.of(song1, song2));
        album2.setSongs(List.of(song3));

        albumService.addAlbum(album1);
        albumService.addAlbum(album2);

        List<Song> filteredSongs = artistService.filterSongsByMinimumDuration(artist.getId(), 3.0f);
        assertEquals(2, filteredSongs.size());
    }

    @Test
    public void testFilterSongsByMinimumDuration_ArtistNotFound() {
        List<Song> filteredSongs = artistService.filterSongsByMinimumDuration(999, 3.0f);
        assertTrue(filteredSongs.isEmpty(), "No songs should be returned if artist is not found.");
    }

    @Test
    public void testSortAlbumsByReleaseDate_Successful() {
        Artist artist = new Artist("Test Artist", "test@example.com");
        artistService.addArtist(artist);

        Album album1 = new Album("Old Album", LocalDate.of(2019, 1, 1), artist);
        Album album2 = new Album("New Album", LocalDate.of(2021, 1, 1), artist);
        album1.setArtist(artist);
        album2.setArtist(artist);

        albumService.addAlbum(album1);
        albumService.addAlbum(album2);

        List<Album> sortedAlbums = artistService.sortAlbumsByReleaseDate(artist.getId());
        assertEquals("Old Album", sortedAlbums.get(0).getTitle());
        assertEquals("New Album", sortedAlbums.get(1).getTitle());
    }

    @Test
    public void testSortAlbumsByReleaseDate_ArtistNotFound() {
        List<Album> sortedAlbums = artistService.sortAlbumsByReleaseDate(999);
        assertTrue(sortedAlbums.isEmpty(), "No albums should be returned if artist is not found.");
    }

    @Test
    public void testSortSongsByTitle_Successful() {
        Artist artist = new Artist("Test Artist", "test@example.com");
        Album album = new Album("Test Album", LocalDate.now(), artist);

        Song song1 = new Song("B Song", 3.5f, album);
        Song song2 = new Song("A Song", 2.0f, album);
        Song song3 = new Song("C Song", 4.0f, album);

        album.setSongs(List.of(song1, song2, song3));
        albumService.addAlbum(album);

        List<Song> sortedSongs = albumService.sortSongsByTitle(album.getId());
        assertEquals("A Song", sortedSongs.get(0).getTitle());
        assertEquals("B Song", sortedSongs.get(1).getTitle());
        assertEquals("C Song", sortedSongs.get(2).getTitle());
    }

    @Test
    public void testSortSongsByTitle_AlbumNotFound() {
        List<Song> sortedSongs = albumService.sortSongsByTitle(999);
        assertTrue(sortedSongs.isEmpty(), "No songs should be returned if album is not found.");
    }
}
