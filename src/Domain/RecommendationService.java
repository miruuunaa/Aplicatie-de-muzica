package Domain;

import java.util.HashMap;
import java.util.List;

public class RecommendationService {
    private History user_history;
    private HashMap<Genre, Integer> user_genres;

    public RecommendationService(History user_history) {
        this.user_history = user_history;
        this.user_genres = new HashMap<>();
    }

    public History getUser_history() {
        return user_history;
    }

    public void setUser_history(History user_history) {
        this.user_history = user_history;
    }

    public HashMap<Genre, Integer> getUser_genres() {
        return user_genres;
    }

    public void setUser_genres(HashMap<Genre, Integer> user_genres) {
        this.user_genres = user_genres;
    }

    public List<Genre> get_top_genres() {

        return null;
    }

    public List<Artist> recommend_artists() {

        return null;
    }

    public List<Song> recommend_songs() {

        return null;
    }
}
