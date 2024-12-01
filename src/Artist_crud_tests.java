import Domain.Artist;
import Repository.IRepository;
import Repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Artist_crud_tests {

    private IRepository<Artist> artistRepository;

    @BeforeEach
    public void setup() {
        artistRepository = new InMemoryRepository<>();
    }

    @Test
    public void testCRUDOperationsForArtist() {
        // Create
        Artist artist = new Artist("Three Days Grace", "threedays@gracemusic.com");
        artistRepository.create(artist);
        Artist createdArtist = artistRepository.read(artist.getId());
        assertNotNull(createdArtist, "Created artist should not be null.");
        assertEquals("Three Days Grace", createdArtist.getName(), "Artist name should match.");
        assertEquals("threedays@gracemusic.com", createdArtist.getEmail(), "Artist email should match.");

        // Update
        createdArtist.setName("Three Days Grace 2X");
        createdArtist.setEmail("threedays@grace2Xmusic.com");
        artistRepository.update(createdArtist);
        Artist updatedArtist = artistRepository.read(createdArtist.getId());
        assertEquals("Three Days Grace 2X", updatedArtist.getName(), "Updated artist name should match.");
        assertEquals("threedays@grace2Xmusic.com", updatedArtist.getEmail(), "Updated artist email should match.");

        // Delete
        artistRepository.delete(updatedArtist.getId());
        assertNull(artistRepository.read(updatedArtist.getId()), "Artist should no longer exist after deletion.");

    }
}
