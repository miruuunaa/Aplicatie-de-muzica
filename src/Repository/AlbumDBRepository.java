package Repository;

import Domain.Album;
import Domain.Artist;
import Domain.Genre;
import Domain.Song;

import java.time.LocalDate;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository for Album
 */
public class AlbumDBRepository extends DBRepository<Album> {
    GenreDBRepository genreDBRepository;
    ArtistDBRepository artistDBRepository;
    public AlbumDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.artistDBRepository=new ArtistDBRepository(DBUrl,DBUser,DBPassword);
        this.genreDBRepository=new GenreDBRepository(DBUrl,DBUser,DBPassword);

    }

    @Override
    public void create(Album album) {
        String SQL = "INSERT INTO Album ( title, release_date, artist_id, genre_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, album.getTitle());
            statement.setDate(2, Date.valueOf(album.getReleaseDate()));
            statement.setInt(3, album.getArtist().getId());
            if (album.getGenre() != null) {
                statement.setInt(4, album.getGenre().getId());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error create album", e);
        }
    }

    @Override
    public Album read(int id) {
        String SQL = "SELECT * FROM Album WHERE id = ?";
        Album album = null;
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    album = extractFromResultSet(rs);

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
                    return extractFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get Album", e);
        }
        return null;
    }

    @Override
    public void update(Album album) {
        String SQL = "UPDATE Album SET title = ?, release_date = ?, artist_id = ?, genre_id = ? WHERE id= ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, album.getTitle());
            statement.setDate(2, Date.valueOf(album.getReleaseDate()));
            statement.setInt(3, album.getArtist().getId());
            statement.setInt(4, album.getGenre().getId());
            statement.setInt(5, album.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error update Album", e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM Album WHERE id = ?";
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
                Album album = extractFromResultSet(resultSet);
                albumMap.put(album.getId(), album);

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get all Album", e);
        }
        return albumMap;
    }

    private Album extractFromResultSet(ResultSet rs) throws  SQLException{
        String title = rs.getString("title");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int artistId = rs.getInt("artist_id");
        int genreId = rs.getInt("genre_id");
        Artist artist = artistDBRepository.get(artistId);
        Genre genre = genreDBRepository.get(genreId);
        Album album = new Album(title, releaseDate, artist);
        album.setId(rs.getInt("id"));
        album.setGenre(genre);
        return album;
    }









}
