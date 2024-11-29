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
        //Create
        Artist artist = new Artist("Test Artist", "test@example.com");
       // Artist createdArtist = artistRepository.create(artist);
        //assertNotNull(createdArtist, "Created artist should not be null.");
        //assertEquals("Test Artist", createdArtist.getName(), "Artist name should match.");
        //assertEquals("test@example.com", createdArtist.getEmail(), "Artist email should match.");

        //Read
        //Artist fetchedArtist = artistRepository.get(createdArtist.getId());
        //assertNotNull(fetchedArtist, "Fetched artist should not be null.");
        //assertEquals("Test Artist", fetchedArtist.getName(), "Fetched artist name should match.");
        //assertEquals("test@example.com", fetchedArtist.getEmail(), "Fetched artist email should match.");

        //Update
        //fetchedArtist.setName("Updated Artist");
        //fetchedArtist.setEmail("updated@example.com");
        //Artist updatedArtist = artistRepository.update(fetchedArtist);
       // assertEquals("Updated Artist", updatedArtist.getName(), "Updated artist name should match.");
       // assertEquals("updated@example.com", updatedArtist.getEmail(), "Updated artist email should match.");

        //Delete
        //artistRepository.delete(updatedArtist.getId());
        //assertNull(artistRepository.get(updatedArtist.getId()), "Artist should no longer exist after deletion.");
    }
}
