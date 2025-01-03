package Service;
import Domain.Genre;
import Exceptions.DatabaseException;
import Repository.IRepository;

import java.util.ArrayList;
import java.util.List;

import Exceptions.EntityNotFoundException;
import Exceptions.ValidationException;

public class GenreService {
    private final IRepository<Genre> genreRepository;

    public GenreService(IRepository<Genre> genreRepository) {
        if (genreRepository == null) {
            throw new ValidationException("Genre repository cannot be null.");
        }
        this.genreRepository = genreRepository;
    }

    /**
     * Retrieves all genres from the repository.
     *
     * @return A list of all genres available in the repository.
     * @throws EntityNotFoundException if no genres are found in the repository.
     * @throws DatabaseException if there is an error accessing the repository.
     */
    public List<Genre> getAllGenres() {
        try {
            List<Genre> genres = new ArrayList<>(genreRepository.getAll().values());

            if (genres.isEmpty()) {
                throw new EntityNotFoundException("No genres found in the repository.");
            }

            return genres;
        }catch (DatabaseException e){
            throw new DatabaseException("Error while retrieving genres from the repository: " + e.getMessage());
        }
    }
}