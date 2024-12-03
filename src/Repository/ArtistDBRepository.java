package Repository;

import Domain.Artist;
import Domain.Album;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistDBRepository extends DBRepository<Artist> {

    private int currentId;

    public ArtistDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.currentId = getMaxIdFromDatabase() + 1;
    }

    private int getMaxIdFromDatabase() {
        String SQL = "SELECT MAX(ID) AS maxId FROM Artist";
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
    public void create(Artist obj) {
        String SQL = "INSERT INTO Artist (ID, name, email) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            obj.setId(currentId);
            statement.setInt(1, currentId++);
            statement.setString(2, obj.getName());
            statement.setString(3, obj.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding Artist", e);
        }
    }

    @Override
    public Artist read(int id) {
        String SQL = "SELECT * FROM Artist WHERE ID = ?";
        Artist artist = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    artist = readFromResultSet(rs);
                    artist.setAlbums(getAlbumsForArtist(id));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading artist", e);
        }
        return artist;
    }

    @Override
    public Artist get(int id) {
        String SQL = "SELECT * FROM Artist WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return readFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get Artist by id", e);
        }
        return null;
    }

    @Override
    public void update(Artist obj) {
        String SQL = "UPDATE Artist SET name = ?, email = ? WHERE ID = ?";
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
        String SQL = "DELETE FROM Artist WHERE ID = ?";
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
                Artist artist = readFromResultSet(resultSet);
                artistMap.put(artist.getId(), artist);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get all Artist", e);
        }
        return artistMap;
    }

    private static Artist readFromResultSet(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String email = rs.getString("email");
        Artist artist = new Artist(name, email);
        artist.setId(rs.getInt("ID"));
        return artist;
    }

    private List<Album> getAlbumsForArtist(int artistId) {
        String SQL = "SELECT * FROM Album WHERE artistId = ?";
        List<Album> albums = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, artistId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
                Artist artist = get(artistId);
                Album album = new Album(title, releaseDate, artist);
                albums.add(album);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error read albums of artist", e);
        }
        return albums;
    }
}
