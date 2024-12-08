package Repository;
import Domain.Artist;
import Domain.Song;
import Domain.Album;
import Domain.Genre;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

/**
 * Repository for Song
 */
public class SongDBRepository extends DBRepository<Song> {
    private AlbumDBRepository albumDBRepository;
    public SongDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.albumDBRepository = new AlbumDBRepository(DBUrl, DBUser, DBPassword);


    }
    @Override
    public void create(Song obj) {
        String SQL = "INSERT INTO Song (title, duration, album_id, genre_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, obj.getTitle());
            statement.setFloat(2, obj.getDuration());
            statement.setInt(3, obj.getAlbum().getId());
            statement.setInt(4, obj.getGenre().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding Song", e);
        }
    }

    @Override
    public Song read(int id) {
        String SQL = "SELECT * FROM Song WHERE id = ?";
        Song song = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    song = readFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading song", e);
        }
        return song;
    }

    @Override
    public Song get(int id) {
        return read(id);
    }

    @Override
    public void update(Song obj) {
        String SQL = "UPDATE Song SET title = ?, duration = ?, album_id = ?, genre_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, obj.getTitle());
            statement.setFloat(2, obj.getDuration());
            statement.setInt(3, obj.getAlbum().getId());
            statement.setInt(4, obj.getGenre().getId());
            statement.setInt(5, obj.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Song", e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM Song WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Song", e);
        }
    }

    @Override
    public Map<Integer, Song> getAll() {
        String SQL = "SELECT * FROM Song";
        Map<Integer, Song> songMap = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Song song = readFromResultSet(resultSet);
                songMap.put(song.getId(), song);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all songs", e);
        }
        System.out.println("Songs retrieved: " + songMap.size());
        return songMap;
    }

    public List<Song> getSongsForPlaylist(int playlistId) {
        String SQL = "SELECT s.* FROM Song s " +
                "JOIN PlaylistSongs ps ON s.ID = ps.song_id " +
                "WHERE ps.playlist_id = ?";
        List<Song> songs = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, playlistId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                songs.add(readFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading songs for playlist", e);
        }
        return songs;
    }


    private  Song readFromResultSet(ResultSet rs) throws SQLException {
        String title = rs.getString("title");
        float duration = rs.getFloat("duration");
        int albumId = rs.getInt("album_id");
        Album album = albumDBRepository.read(albumId);
        Song song = new Song(title, duration, album);
        song.setId(rs.getInt("id"));
        return song;
    }












}
