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
        Artist artist = new Artist("Test Artist", "test@example.com");
        artistRepository.create(artist);
        Artist createdArtist = artistRepository.read(artist.getId());
        assertNotNull(createdArtist, "Created artist should not be null.");
        assertEquals("Test Artist", createdArtist.getName(), "Artist name should match.");
        assertEquals("test@example.com", createdArtist.getEmail(), "Artist email should match.");

        // Update
        createdArtist.setName("Updated Artist");
        createdArtist.setEmail("updated@example.com");
        artistRepository.update(createdArtist);
        Artist updatedArtist = artistRepository.read(createdArtist.getId());
        assertEquals("Updated Artist", updatedArtist.getName(), "Updated artist name should match.");
        assertEquals("updated@example.com", updatedArtist.getEmail(), "Updated artist email should match.");

        // Delete
        artistRepository.delete(updatedArtist.getId());
        assertNull(artistRepository.read(updatedArtist.getId()), "Artist should no longer exist after deletion.");

    }
}
