package Repository;

import Domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenerDBRepository extends DBRepository<Listener> {
    private int currentId;

    public ListenerDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.currentId = getMaxIdFromDatabase() + 1;
    }
    private int getMaxIdFromDatabase() {
        String SQL = "SELECT MAX(id) AS maxId FROM Listener";
        try (PreparedStatement statement = connection.prepareStatement(SQL);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("maxId");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading max-ID from Listener table", e);
        }
        return 0;
    }



    @Override
    public void create(Listener listener) {
        String SQL = "INSERT INTO Listener (id, name, email, subscription_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            listener.setId(currentId);
            statement.setInt(1, currentId++);
            statement.setString(2, listener.getName());
            statement.setString(3, listener.getEmail());
            statement.setInt(4, listener.getSubscription() != null ? listener.getSubscription().getId() : 0);
            statement.executeUpdate();


            createPlaylists(listener);
            createHistory(listener);

        } catch (SQLException e) {
            throw new RuntimeException("Error create Listener", e);
        }
    }

    private void createPlaylists(Listener listener) {
        String SQL = "INSERT INTO Playlist (listener_id, playlist_name) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            for (Playlist playlist : listener.getPlaylists()) {
                statement.setInt(1, listener.getId());
                statement.setString(2, playlist.getName());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error create Playlist", e);
        }
    }

    private void createHistory(Listener listener) {
        String SQL = "INSERT INTO History (user_id) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, listener.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error create History", e);
        }
    }

    @Override
    public Listener read(int id) {
        String SQL = "SELECT * FROM Listener WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return readFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error read Listener", e);
        }
        return null;
    }

    private Subscription getSubscriptionById(int subscriptionId) {
        String SQL = "SELECT * FROM Subscription WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, subscriptionId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("type");
                    float price = rs.getFloat("price");
                    int listenerId = rs.getInt("listener_id");
                    Listener listener = getListenerById(listenerId);
                    Subscription subscription = new Subscription(type, price, listener);
                    subscription.setId(subscriptionId);
                    return subscription;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get subscription by id", e);
        }
        return null;
    }

    private List<Playlist> getPlaylistsByListenerId(int listenerId) {
        String SQL = "SELECT * FROM Playlist WHERE listener_id = ?";
        List<Playlist> playlists = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, listenerId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String playlistName = rs.getString("playlist_name");
                Listener listener = getListenerById(listenerId);
                Playlist playlist = new Playlist(playlistName, listener);
                playlists.add(playlist);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get playlist by listener id", e);
        }
        return playlists;
    }

    private Listener getListenerById(int listenerId) {
        String SQL = "SELECT * FROM Listener WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, listenerId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                Listener listener = new Listener(name, email);
                listener.setId(rs.getInt("id"));
                return listener;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting listener by ID", e);
        }
        return null;
    }

    private History getHistoryByListenerId(int listenerId) {
        String SQL = "SELECT * FROM History WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, listenerId);
            try (ResultSet rs = statement.executeQuery()) {
                Map<Song, LocalDateTime> songHistory = new HashMap<>();

                while (rs.next()) {
                    int songId = rs.getInt("song_id");
                    LocalDateTime playTime = rs.getTimestamp("play_time").toLocalDateTime();
                    Song song = getSongById(songId);
                    if (song != null) {
                        songHistory.put(song, playTime);
                    }
                }
                Listener listener = getListenerById(listenerId);
                History history = new History(listener);
                history.setSongHistory(songHistory);
                return history;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get History by Listener ID", e);
        }
    }

    @Override
    public void update(Listener listener) {
        String SQL = "UPDATE Listener SET name = ?, email = ?, subscription_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, listener.getName());
            statement.setString(2, listener.getEmail());
            statement.setInt(3, listener.getSubscription() != null ? listener.getSubscription().getId() : 0);
            statement.setInt(4, listener.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error update Listener", e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM Listener WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error delete Listener", e);
        }
    }

    @Override
    public Map<Integer, Listener> getAll() {
        String SQL = "SELECT * FROM Listener";
        Map<Integer, Listener> listeners = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Listener listener = readFromResultSet(resultSet);
                listeners.put(listener.getId(), listener);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get all Listener", e);
        }
        return listeners;
    }

    @Override
    public Listener get(int id) {
        return read(id);
    }

    private Listener readFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        int subscriptionId = rs.getInt("subscription_id");
        Listener listener = new Listener(name, email);
        listener.setId(id);
        Subscription subscription = getSubscriptionById(subscriptionId);
        listener.setSubscription(subscription);
        List<Playlist> playlists = getPlaylistsByListenerId(id);
        listener.getPlaylists().addAll(playlists);
        History history = getHistoryByListenerId(id);
        listener.getHistory().setSongHistory(history.getSongHistory());
        return listener;
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
            throw new RuntimeException("Error get Song by ID", e);
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
                    int artistId = rs.getInt("artistId");
                    Artist artist = getArtistById(artistId);
                    return new Album(title, releaseDate, artist);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get Album by ID", e);
        }
        return null;
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
            throw new RuntimeException("Error get Artist by ID", e);
        }
        return null;
    }


}
