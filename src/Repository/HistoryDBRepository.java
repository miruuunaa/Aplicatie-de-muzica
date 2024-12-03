package Repository;

import Domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HistoryDBRepository extends DBRepository<History> {
    private int currentId;

    public HistoryDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.currentId = getMaxIdFromDatabase() + 1;

    }

    private int getMaxIdFromDatabase() {
        String SQL = "SELECT MAX(ID) AS maxId FROM History";
        try (PreparedStatement statement = connection.prepareStatement(SQL);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("maxId");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la citirea max-ID din History", e);
        }
        return 0;
    }



    @Override
    public void create(History history) {
        String SQL = "INSERT INTO History (ID, user_id, song_id, play_time) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            for (Map.Entry<Song, LocalDateTime> entry : history.getSongHistory().entrySet()) {
                statement.setInt(1, currentId);
                statement.setInt(2, history.getUser().getId()); // user_id
                statement.setInt(3, entry.getKey().getId()); // song_id
                statement.setTimestamp(4, Timestamp.valueOf(entry.getValue())); // play_time
                statement.executeUpdate();
            }
            currentId++;
        } catch (SQLException e) {
            throw new RuntimeException("Error create History", e);
        }
    }

    @Override
    public History read(int id) {
        String SQL = "SELECT * FROM History WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return readFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la citirea istoricului din baza de date", e);
        }
        return null;
    }

    private Song getSongById(int songId) {
        String SQL = "SELECT * FROM songs WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, songId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("title");
                    float duration = rs.getFloat("duration");
                    int albumId = rs.getInt("albumId");
                    Album album = getAlbumById(albumId);
                    Song song = new Song(title, duration, album);
                    song.setId(songId);
                    return song;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting song by ID", e);
        }
        return null;
    }

    private Listener getListenerById(int listenerId) {
        String SQL = "SELECT * FROM listeners WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, listenerId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    Listener listener = new Listener(name, email);
                    listener.setId(listenerId);
                    return listener;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting listener by ID", e);
        }
        return null;
    }

    @Override
    public void update(History history) {
        String SQL = "UPDATE History SET song_id = ?, play_time = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            for (Map.Entry<Song, LocalDateTime> entry : history.getSongHistory().entrySet()) {
                statement.setInt(1, entry.getKey().getId()); // song_id
                statement.setTimestamp(2, Timestamp.valueOf(entry.getValue())); // play_time
                statement.setInt(3, history.getUser().getId()); // user_id
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error update History", e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM History WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error delete History", e);
        }
    }

    @Override
    public Map<Integer, History> getAll() {
        String SQL = "SELECT * FROM History";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            ResultSet resultSet = statement.executeQuery();
            Map<Integer, History> histories = new HashMap<>();
            while (resultSet.next()) {
                History history = readFromResultSet(resultSet);
                histories.put(history.getUser().getId(), history);
            }
            return histories;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la citirea istoricului din baza de date", e);
        }
    }


    @Override
    public History get(int id) {
        return read(id);
    }

    private Artist getArtistById(int artistId) {
        String SQL = "SELECT * FROM artists WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, artistId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    Artist artist = new Artist(name, email);
                    artist.setId(artistId);
                    return artist;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting artist by ID", e);
        }
        return null;
    }

    private Album getAlbumById(int albumId) {
        String SQL = "SELECT * FROM albums WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, albumId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("title");
                    LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
                    Artist artist = getArtistById(rs.getInt("artistId"));
                    return new Album(title, releaseDate, artist);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting album by ID", e);
        }
        return null;
    }

    private History readFromResultSet(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        Listener user = getListenerById(userId);
        Map<Song, LocalDateTime> songHistory = new HashMap<>();
        do {
            int songId = rs.getInt("song_id");
            LocalDateTime playTime = rs.getTimestamp("play_time").toLocalDateTime();
            Song song = getSongById(songId);
            songHistory.put(song, playTime);
        } while (rs.next());
        History history = new History(user);
        history.setSongHistory(songHistory);
        return history;
    }
}
