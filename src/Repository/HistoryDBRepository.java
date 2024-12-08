package Repository;

import Domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository for History
 */
public class HistoryDBRepository extends DBRepository<History> {
    private SongDBRepository songDBRepository;
    private ListenerDBRepository listenerDBRepository;
    public HistoryDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.songDBRepository = new SongDBRepository(DBUrl, DBUser, DBPassword);
        this.listenerDBRepository = new ListenerDBRepository(DBUrl, DBUser, DBPassword);


    }
    @Override
    public void create(History history) {
        String SQL = "INSERT INTO History (user_id, song_id, play_time) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            for (Map.Entry<Song, LocalDateTime> entry : history.getSongHistory().entrySet()) {
                statement.setInt(1, history.getUser().getId()); // user_id
                statement.setInt(2, entry.getKey().getId()); // song_id
                statement.setTimestamp(3, Timestamp.valueOf(entry.getValue())); // play_time
                statement.executeUpdate();
            }

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
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading history", e);
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
                History history = extractFromResultSet(resultSet);
                histories.put(history.getUser().getId(), history);
            }
            return histories;
        } catch (SQLException e) {
            throw new RuntimeException("Error get_all History", e);
        }
    }


    @Override
    public History get(int id) {
        return read(id);
    }

    private History extractFromResultSet(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        Listener user = listenerDBRepository.read(userId);
        Map<Song, LocalDateTime> songHistory = new HashMap<>();
        do {
            int songId = rs.getInt("song_id");
            LocalDateTime playTime = rs.getTimestamp("play_time").toLocalDateTime();
            Song song = songDBRepository.read(songId);
            songHistory.put(song, playTime);
        } while (rs.next());
        History history = new History(user);
        history.setSongHistory(songHistory);
        return history;
    }
}
