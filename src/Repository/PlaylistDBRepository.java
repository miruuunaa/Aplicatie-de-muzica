package Repository;

import Domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository for Playlist
 */
public class PlaylistDBRepository extends DBRepository<Playlist> {
    private SongDBRepository songDBRepository;
    private ListenerDBRepository listenerDBRepository;
    public PlaylistDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.songDBRepository = new SongDBRepository(DBUrl, DBUser, DBPassword);
        this.listenerDBRepository = new ListenerDBRepository(DBUrl, DBUser, DBPassword);

    }
    @Override
    public void create(Playlist obj) {
        String SQL = "INSERT INTO Playlist (name, user_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, obj.getName());
            statement.setInt(2, obj.getUser().getId());
            statement.executeUpdate();
            addSongsToPlaylist(obj);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding Playlist", e);
        }
    }
    private void addSongsToPlaylist(Playlist playlist) {
        String SQL = "INSERT INTO PlaylistSongs (playlist_id, song_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            for (Song song : playlist.getSongs()) {
                statement.setInt(1, playlist.getId());
                statement.setInt(2, song.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding songs to playlist", e);
        }
    }

    @Override
    public Playlist read(int id) {
        String SQL = "SELECT * FROM Playlist WHERE id = ?";
        Playlist playlist = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    playlist = readFromResultSet(rs);
                    playlist.setSongs(songDBRepository.getSongsForPlaylist(id));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading playlist", e);
        }
        return playlist;
    }

    @Override
    public Playlist get(int id) {
        return read(id);
    }

    @Override
    public void update(Playlist obj) {
        String SQL = "UPDATE Playlist SET name = ?, user_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, obj.getName());
            statement.setInt(2, obj.getUser().getId());
            statement.setInt(3, obj.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error update Playlist", e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM Playlist WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error delete Playlist", e);
        }
    }

    @Override
    public Map<Integer, Playlist> getAll() {
        String SQL = "SELECT * FROM Playlist";
        Map<Integer, Playlist> playlistMap = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Playlist playlist = readFromResultSet(resultSet);
                playlistMap.put(playlist.getId(), playlist);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get all Playlist", e);
        }
        return playlistMap;
    }


    private Playlist readFromResultSet(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        int userId = rs.getInt("user_id");
        Listener user=listenerDBRepository.read(userId);
        Playlist playlist = new Playlist(name, user);
        playlist.setId(rs.getInt("id"));
        return playlist;
    }







}
