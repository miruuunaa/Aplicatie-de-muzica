package Repository;

import Domain.Artist;
import Domain.Album;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository for Artist
 */
public class ArtistDBRepository extends DBRepository<Artist> {

    public ArtistDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);

    }

    @Override
    public void create(Artist obj) {
        String SQL = "INSERT INTO Artist (name, email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding Artist", e);
        }
    }

    @Override
    public Artist read(int id) {
        String SQL = "SELECT * FROM Artist WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Artist artist = extractFromResultSet(rs);
                    List<Album> albums = getAlbumsForArtist(artist.getId());
                    artist.setAlbums(albums);
                    return artist;
                }else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading artist", e);
        }

    }

    @Override
    public Artist get(int id) {
        String SQL = "SELECT * FROM Artist WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return extractFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get Artist by id", e);
        }
        return null;
    }

    @Override
    public void update(Artist obj) {
        String SQL = "UPDATE Artist SET name = ?, email = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getEmail());
            statement.setInt(3, obj.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error update Artist", e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM Artist WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error delete Artist", e);
        }
    }

    @Override
    public Map<Integer, Artist> getAll() {
        String SQL = "SELECT * FROM Artist";
        Map<Integer, Artist> artistMap = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Artist artist = extractFromResultSet(resultSet);
                artist.setAlbums(getAlbumsForArtist(artist.getId()));
                artistMap.put(artist.getId(), artist);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get all Artist", e);
        }
        return artistMap;
    }

    private static Artist extractFromResultSet(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        int id = resultSet.getInt("id");
        Artist artist = new Artist(name, email);
        artist.setId(id);
        return artist;
    }

    private List<Album> getAlbumsForArtist(int artistId) {
        String SQL = "SELECT * FROM Album WHERE artist_id = ?";
        List<Album> albums = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, artistId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                Artist artist = get(artistId);
                if (artist == null) {
                    throw new RuntimeException("Artist not found for ID: " + artistId);
                }
                Album album = new Album(title, releaseDate, artist);
                albums.add(album);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error read albums of artist", e);
        }
        return albums;
    }
}
