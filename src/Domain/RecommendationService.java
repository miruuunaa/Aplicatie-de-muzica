package Domain;

import java.util.*;
import java.util.stream.Collectors;
/**
 * The RecommendationService class provides methods for recommending songs, artists, and genres to a user
 * based on their listening history.
 * It uses the user's listening history to determine the most frequently listened genres and artists.
 */
public class RecommendationService {
    private final History userHistory;
    private List<Song> allSongs;
    /**
     * Constructs a RecommendationService object with the specified user history and list of all songs.
     *
     * @param userHistory The user's listening history
     * @param allSongs    The list of all available songs for recommendation
     */
    public RecommendationService(History userHistory, List<Song> allSongs) {
        this.userHistory = userHistory;
        this.allSongs = allSongs;
    }

    /**
     * Recommends songs to the user based on their listening history.
     * The songs are recommended based on the most frequently listened genre in the user's history.
     *
     * @param userHistory The user's listening history
     * @return A list of recommended songs based on the user's most listened genre
     */
    public List<Song> recommend_songs(History userHistory) {
        Map<Genre, Integer> genreCount = new HashMap<>();

        // Obținem istoricul pieselor ascultate
        for (Song song : userHistory.getSongs()) {
            // Luăm genul piesei din albumul asociat piesei
            Genre genre = song.getAlbum().getGenre();  // presupunând că albumul are un gen
            genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
        }

        // Determinăm genul preferat
        Genre preferredGenre = genreCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (preferredGenre == null) {
            return List.of();  // Dacă nu există un gen preferat, returnăm o listă goală
        }

        // Returnăm piesele care se potrivesc cu genul preferat
        return allSongs.stream()
                .filter(song -> song.getAlbum().getGenre().equals(preferredGenre))  // Verificăm genul albumului
                .collect(Collectors.toList());
    }

    /**
     * Recommends artists to the user based on their listening history.
     * The artists are recommended based on the albums of the songs the user has listened to.
     *
     * @param userHistory The user's listening history
     * @return A list of recommended artists based on the user's listening history
     */
    public List<Artist> recommend_artists(History userHistory) {
        Set<Artist> recommendedArtists = new HashSet<>();

        // Obținem istoricul pieselor ascultate
        for (Song song : userHistory.getSongs()) {
            // Adăugăm artistul asociat albumului piesei
            recommendedArtists.add(song.getAlbum().getArtist());
        }

        return new ArrayList<>(recommendedArtists);  // Returnăm lista de artiști
    }

    /**
     * Retrieves the top genres from a user's listening history.
     * This method analyzes a user's listening history and counts the occurrences of each genre
     * associated with the songs the user has listened to. It then returns a list of the top 5
     * genres by occurrence in descending order.
     * @param userHistory the user's listening history, which contains a list of songs
     * @return a list of the top 5 genres listened to by the user, sorted by frequency in descending order;
     *  if no genres are found, returns an empty list*/

    public List<Genre> get_top_genres(History userHistory) {
        Map<Genre, Integer> genreCount = new HashMap<>();

        // Verificăm istoricul pieselor ascultate
        for (Song song : userHistory.getSongs()) {
            if (song == null) {
                System.out.println("Found a null song in history.");
                continue;
            }

            Album album = song.getAlbum();
            if (album == null) {
                System.out.println("Song " + song.getTitle() + " does not have an associated album.");
                continue;
            }

            Genre genre = album.getGenre();
            if (genre == null) {
                System.out.println("Album " + album.getTitle() + " does not have an associated genre.");
                continue;
            }

            // Incrementăm contorul pentru gen
            genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
        }

        // Verificăm dacă au fost găsite genuri
        if (genreCount.isEmpty()) {
            System.out.println("No genres found.");
            return Collections.emptyList();
        }

        // Returnăm genurile cele mai populare
        return genreCount.entrySet().stream()
                .sorted(Map.Entry.<Genre, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * Returns a string representation of the RecommendationService object.
     *
     * @return A string representing the RecommendationService
     */
    @Override
    public String toString() {
        return "RecommendationService{" +
                "userHistory=" + userHistory +
                ", allSongs=" + allSongs +
                '}';
    }
}
