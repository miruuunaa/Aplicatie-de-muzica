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

    public Genre getGenreByName(String genreName) {
        try {
            return genreRepository.getAll().values().stream()
                    .filter(genre -> genre.getName().equalsIgnoreCase(genreName))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve genres: " + e.getMessage());
        }
    }

    public void addGenre(Genre genre) {
        if (genre == null || genre.getName() == null || genre.getName().isEmpty()) {
            throw new ValidationException("Genre name cannot be null or empty.");
        }
        if (genreRepository.getAll().values().stream().anyMatch(existingGenre -> existingGenre.getName().equalsIgnoreCase(genre.getName()))) {
            throw new ValidationException("Genre '" + genre.getName() + "' already exists.");
        }
        try {
            genreRepository.create(genre);
            System.out.println("Genre '" + genre.getName() + "' added successfully.");
        } catch (DatabaseException e) {
            throw new DatabaseException("Error while adding genre to the repository: " + e.getMessage());
        }
    }


}