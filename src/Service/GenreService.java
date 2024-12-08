package Service;
import Domain.Genre;
import Repository.IRepository;

import java.util.ArrayList;
import java.util.List;

public class GenreService {
    private final IRepository<Genre> genreRepository;

    public GenreService(IRepository<Genre> genreRepository){
        this.genreRepository=genreRepository;
    }
    public List<Genre> getAllGenres() {
        return new ArrayList<>(genreRepository.getAll().values());
    }
}
