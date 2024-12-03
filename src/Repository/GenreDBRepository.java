package Repository;

import Domain.Genre;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository-ul pentru gestionarea genurilor de muzică
 */
public class GenreDBRepository extends DBRepository<Genre> {

    private int currentId;

    public GenreDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.currentId = getMaxIdFromDatabase() + 1;
    }


    private int getMaxIdFromDatabase() {
        String SQL = "SELECT MAX(ID) AS maxId FROM Genre";
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
    public void create(Genre obj) {
        String SQL = "INSERT INTO Genre (ID, name) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            obj.setId(currentId);
            statement.setInt(1, currentId++);
            statement.setString(2, obj.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error create Genre", e);
        }
    }

    @Override
    public Genre read(int id) {
        String SQL = "SELECT * FROM Genre WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return readFromResultSet(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error read Genre", e);
        }
    }

    private Genre readFromResultSet(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        Genre genre = new Genre(name);
        genre.setId(rs.getInt("ID"));
        return genre;
    }

    @Override
    public void update(Genre obj) {
        String SQL = "UPDATE Genre SET name = ? WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, obj.getName());
            statement.setInt(2, obj.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error update Genre", e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM Genre WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error delete Genre", e);
        }
    }


    @Override
    public Map<Integer, Genre> getAll() {
        String SQL = "SELECT * FROM Genre";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            ResultSet resultSet = statement.executeQuery();
            Map<Integer, Genre> genres = new HashMap<>();
            while (resultSet.next()) {
                Genre genre = readFromResultSet(resultSet);
                genres.put(genre.getId(), genre);
            }
            return genres;
        } catch (SQLException e) {
            throw new RuntimeException("Error get all Genre", e);
        }
    }

    @Override
    public Genre get(int id) {
        Map<Integer, Genre> allGenres = getAll();
        return allGenres.get(id);
    }
}
