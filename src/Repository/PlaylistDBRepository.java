package Repository;

import Domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistDBRepository extends DBRepository<Playlist> {

    private int currentId;

    public PlaylistDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.currentId = getMaxIdFromDatabase() + 1;
    }


    private int getMaxIdFromDatabase() {
        String SQL = "SELECT MAX(ID) AS maxId FROM playlists";
        try (PreparedStatement statement = connection.prepareStatement(SQL);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("maxId");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading max ID", e);
        }
        return 0;
    }

    @Override
    public void create(Playlist obj) {
        String SQL = "INSERT INTO playlists (ID, name, user_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            obj.setId(currentId);
            statement.setInt(1, currentId++);
            statement.setString(2, obj.getName());
            statement.setInt(3, obj.getUser().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding Playlist", e);
        }
    }

    @Override
    public Playlist read(int id) {
        String SQL = "SELECT * FROM playlists WHERE ID = ?";
        Playlist playlist = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    playlist = readFromResultSet(rs);
                    playlist.setSongs(getSongsForPlaylist(id));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading playlist", e);
        }
        return playlist;
    }

    @Override
    public Playlist get(int id) {
        String SQL = "SELECT * FROM playlists WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return readFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get Playlist by id", e);
        }
        return null;
    }

    @Override
    public void update(Playlist obj) {
        String SQL = "UPDATE playlists SET name = ?, user_id = ? WHERE ID = ?";
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
        String SQL = "DELETE FROM playlists WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error delete Playlist", e);
        }
    }

    @Override
    public Map<Integer, Playlist> getAll() {
        String SQL = "SELECT * FROM playlists";
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


    private static Playlist readFromResultSet(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        int userId = rs.getInt("user_id");
        String userName = rs.getString("user_name");
        String userEmail = rs.getString("user_email");
        Listener user = new Listener(userName, userEmail);
        user.setId(userId);
        Playlist playlist = new Playlist(name, user);
        playlist.setId(rs.getInt("ID"));
        return playlist;
    }


    private List<Song> getSongsForPlaylist(int playlistId) {
        String SQL = "SELECT * FROM songs WHERE playlist_id = ?";
        List<Song> songs = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, playlistId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                float duration = rs.getFloat("duration");
                int albumId = rs.getInt("album_id");
                Album album = getAlbumById(albumId);
                Song song = new Song(title, duration, album);
                songs.add(song);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading songs for playlist", e);
        }
        return songs;
    }

    private Album getAlbumById(int albumId) {
        String SQL = "SELECT * FROM albums WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, albumId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("title");
                    LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                    int artistId = rs.getInt("artist_id");
                    Artist artist = getArtistById(artistId);
                    Album album = new Album(title, releaseDate, artist);
                    album.setId(rs.getInt("ID"));
                    return album;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting album by ID", e);
        }
        return null;
    }

    private Artist getArtistById(int artistId) {
        String SQL = "SELECT * FROM artists WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, artistId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                return new Artist(name, email);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading artist from database", e);
        }
    }
}
