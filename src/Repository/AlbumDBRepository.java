package Repository;

import Domain.Album;
import Domain.Artist;
import Domain.Genre;
import java.time.LocalDate;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AlbumDBRepository extends DBRepository<Album> {

    private int currentId;

    public AlbumDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.currentId = getMaxIdFromDatabase() + 1;
    }

    private int getMaxIdFromDatabase() {
        String SQL = "SELECT MAX(ID) AS maxId FROM Album";
        try (PreparedStatement statement = connection.prepareStatement(SQL);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("maxId");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading max-ID", e);
        }
        return 0;
    }

    @Override
    public void create(Album album) {
        String SQL = "INSERT INTO Album (ID, title, release_date, artist_id, genre_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            album.setId(currentId);
            statement.setInt(1, currentId++);
            statement.setString(2, album.getTitle());
            statement.setDate(3, Date.valueOf(album.getReleaseDate()));
            statement.setInt(4, album.getArtist().getId());
            Genre genre = album.getGenre();
            int genreId = saveOrGetGenreId(genre);
            statement.setInt(5, genreId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error create album", e);
        }
    }

    @Override
    public Album read(int id) {
        String SQL = "SELECT * FROM Album WHERE ID = ?";
        Album album = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    album = readFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error read Album", e);
        }
        return album;
    }

    @Override
    public Album get(int id) {
        String SQL = "SELECT * FROM Album WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return readFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get Album", e);
        }
        return null;
    }

    @Override
    public void update(Album album) {
        String SQL = "UPDATE Album SET title = ?, release_date = ?, artist_id = ?, genre_id = ? WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, album.getTitle());
            statement.setDate(2, Date.valueOf(album.getReleaseDate()));
            statement.setInt(3, album.getArtist().getId());
            Genre genre = album.getGenre();
            int genreId = saveOrGetGenreId(genre);
            statement.setInt(4, genreId);
            statement.setInt(5, album.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error update Album", e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM Album WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error delete Album", e);
        }
    }

    @Override
    public Map<Integer, Album> getAll() {
        String SQL = "SELECT * FROM Album";
        Map<Integer, Album> albumMap = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Album album = readFromResultSet(resultSet);
                albumMap.put(album.getId(), album);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get all Album", e);
        }
        return albumMap;
    }

    private Album readFromResultSet(ResultSet rs) throws SQLException {
        String title = rs.getString("title");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int artistId = rs.getInt("artist_id");
        int genreId = rs.getInt("genre_id");
        Artist artist = getArtistById(artistId);
        Genre genre = getGenreById(genreId);

        Album album = new Album(title, releaseDate, artist);
        album.setId(rs.getInt("ID"));
        album.setGenre(genre);
        return album;
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
        String SQL = "SELECT * FROM Genre WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, genreId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Genre genre = new Genre(rs.getString("name"));
                    genre.setId(rs.getInt("ID"));
                    return genre;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error read Genre", e);
        }
        return null;
    }

    private int saveOrGetGenreId(Genre genre) {

        String SQL = "SELECT ID FROM Genre WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, genre.getName());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                } else {

                    return createGenre(genre);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error check Genre", e);
        }
    }

    private int createGenre(Genre genre) {
        String SQL = "INSERT INTO Genre (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, genre.getName());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("No id of Genre.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error create Genre", e);
        }
    }
}
