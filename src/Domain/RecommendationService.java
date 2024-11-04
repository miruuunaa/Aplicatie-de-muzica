package Domain;

import java.util.*;

public class RecommendationService {
    private History userHistory;
    private Map<Genre, Integer> userGenres;

    public RecommendationService(History userHistory) {
        this.userHistory = userHistory;
        this.userGenres = new HashMap<>();
        analyzeUserHistory();
    }

    private void analyzeUserHistory() {
        for (Song song : userHistory.getSongs()) {
            Genre genre = song.getGenre();
            userGenres.put(genre, userGenres.getOrDefault(genre, 0) + 1);
        }
    }

    public List<Genre> getTopGenres() {
        List<Map.Entry<Genre, Integer>> genreList = new ArrayList<>(userGenres.entrySet());
        genreList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        List<Genre> topGenres = new ArrayList<>();
        for (Map.Entry<Genre, Integer> entry : genreList) {
            topGenres.add(entry.getKey());
        }
        return topGenres;
    }

    public List<Song> recommendSongs() {
        List<Genre> topGenres = getTopGenres();
        Set<Song> recommendedSongs = new HashSet<>();

        for (Genre genre : topGenres) {
            recommendedSongs.addAll(genre.getSongs());
        }

        return new ArrayList<>(recommendedSongs);
    }

    public History getUserHistory() {
        return userHistory;
    }

    public void setUserHistory(History userHistory) {
        this.userHistory = userHistory;
        userGenres.clear();
        analyzeUserHistory();
    }

    public Map<Genre, Integer> getUserGenres() {
        return new HashMap<>(userGenres);
    }
}
