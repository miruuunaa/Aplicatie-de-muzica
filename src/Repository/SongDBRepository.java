package Repository;
import Domain.Artist;
import Domain.Song;
import Domain.Album;
import Domain.Genre;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

public class SongDBRepository extends DBRepository<Song> {

    private int currentId;

    public SongDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.currentId = getMaxIdFromDatabase() + 1;
    }


    private int getMaxIdFromDatabase() {
        String SQL = "SELECT MAX(ID) AS maxId FROM songs";
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
    public void create(Song obj) {
        String SQL = "INSERT INTO songs (ID, title, duration, album_id, genre_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            obj.setId(currentId);
            statement.setInt(1, currentId++);
            statement.setString(2, obj.getTitle());
            statement.setFloat(3, obj.getDuration());
            statement.setInt(4, obj.getAlbum().getId());
            statement.setInt(5, obj.getGenre().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding Song", e);
        }
    }

    @Override
    public Song read(int id) {
        String SQL = "SELECT * FROM songs WHERE ID = ?";
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
        String SQL = "SELECT * FROM songs WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return readFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get Song by id", e);
        }
        return null;
    }

    @Override
    public void update(Song obj) {
        String SQL = "UPDATE songs SET title = ?, duration = ?, album_id = ?, genre_id = ? WHERE ID = ?";
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
        String SQL = "DELETE FROM songs WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Song", e);
        }
    }

    @Override
    public Map<Integer, Song> getAll() {
        String SQL = "SELECT * FROM songs";
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
        return songMap;
    }


    private  Song readFromResultSet(ResultSet rs) throws SQLException {
        String title = rs.getString("title");
        float duration = rs.getFloat("duration");
        int albumId = rs.getInt("album_id");
        int genreId = rs.getInt("genre_id");
        Album album = getAlbumById(albumId);
        Genre genre = getGenreById(genreId);
        Song song = new Song(title, duration, album);
        song.setId(rs.getInt("ID"));
        song.setGenre(genre);
        return song;
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
        String SQL = "SELECT * FROM artists WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, artistId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    Artist artist = new Artist(name, email);
                    artist.setId(rs.getInt("ID"));
                    return artist;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting artist by ID", e);
        }
        return null;
    }


    private Genre getGenreById(int genreId) {
        String SQL = "SELECT * FROM genre WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, genreId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    Genre genre = new Genre(name);
                    genre.setId(rs.getInt("ID"));
                    return genre;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting genre by ID", e);
        }
        return null;
    }
}
