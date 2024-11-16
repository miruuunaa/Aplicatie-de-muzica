package Domain;
import Repository.*;
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
     * Recommends other songs from the same artist to the user based on their listening history.
     * The songs are recommended based on the most frequently listened artist in the user's history.
     *
     * @param userHistory The user's listening history
     * @return A list of recommended songs based on the user's most listened genre
     */
    public List<Song> recommend_songs(History userHistory) {
        Set<Artist> favoriteArtists = userHistory.getSongs().stream()
                .map(song -> song.getAlbum().getArtist())
                .collect(Collectors.toSet());

        return allSongs.stream()
                .filter(song -> favoriteArtists.contains(song.getAlbum().getArtist()))
                .filter(song -> !userHistory.getSongs().contains(song))
                .collect(Collectors.toList());
    }

    /**
     * Recommends other artists to the user based on their listening history.
     * The artists are recommended based on the genre the user has listened to.
     *
     * @param userHistory The user's listening history
     * @return A list of recommended artists based on the user's listening history
     */
    public List<Artist> recommend_artists(History userHistory) {
        Map<Genre, Integer> genreCount = new HashMap<>();
        for (Song song : userHistory.getSongs()) {
            Genre genre = song.getAlbum().getGenre();
            genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
        }
        Genre preferredGenre = genreCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        if (preferredGenre == null) {
            return List.of();
        }
        List<Artist> recommendedArtists = new ArrayList<>();
        for (Song song : allSongs) {
            if (song.getAlbum().getGenre().equals(preferredGenre)) {
                Artist artist = song.getAlbum().getArtist();
                if (!recommendedArtists.contains(artist)) {
                    recommendedArtists.add(artist);
                }
            }
        }
        return recommendedArtists;
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
            genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
        }
        if (genreCount.isEmpty()) {
            System.out.println("No genres found.");
            return Collections.emptyList();
        }
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
