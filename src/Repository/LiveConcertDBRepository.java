package Repository;

import Domain.LiveConcert;
import Domain.Listener;
import Domain.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * LiveConcertDBRepository implements IRepository for the LiveConcert entity.
 * This class handles the database operations for live concerts.
 */
public class LiveConcertDBRepository extends DBRepository<LiveConcert> implements IRepository<LiveConcert> {

    private int currentId;

    public LiveConcertDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.currentId = getMaxIdFromDatabase() + 1;
    }

    // Get max ID from the database
    private int getMaxIdFromDatabase() {
        String SQL = "SELECT MAX(id) AS maxId FROM LiveConcert";
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
    public void create(LiveConcert concert) {
        String SQL = "INSERT INTO LiveConcert (id, title, date, artist_id, ticket_count, is_available_post_live, event_type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            concert.setId(currentId);
            statement.setInt(1, currentId++);
            statement.setString(2, concert.getTitle());
            statement.setDate(3, new java.sql.Date(concert.getDate().getTime()));
            statement.setInt(4, concert.getArtist().getId());
            statement.setInt(5, concert.getTicketCount());
            statement.setBoolean(6, concert.replayAvailable());
            statement.setString(7, concert.getEventType());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                // After inserting the concert, add listeners to early and regular access lists
                addListenersToConcert(concert);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Eroare la adăugarea concertului în baza de date", e);
        }
    }

    private void addListenersToConcert(LiveConcert concert) {
        String SQL = "INSERT INTO ConcertListeners (concert_id, listener_id, access_type) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            for (Listener listener : concert.getEarlyAccessList()) {
                statement.setInt(1, concert.getId());
                statement.setInt(2, listener.getId());
                statement.setString(3, "early");
                statement.executeUpdate();
            }

            for (Listener listener : concert.getRegularAccesList()) {
                statement.setInt(1, concert.getId());
                statement.setInt(2, listener.getId());
                statement.setString(3, "regular");
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error add listener to concert", e);
        }
    }

    @Override
    public LiveConcert read(int id) {
        String SQL = "SELECT * FROM LiveConcert WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return readFromResultSet(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error read Concert", e);
        }
    }

    private Artist getArtistById(int artistId) {
        // Implement method to retrieve Artist by ID from the database
        String SQL = "SELECT * FROM Artist WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, artistId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return readArtistFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get Artist by ID", e);
        }
        return null;
    }

    private static Artist readArtistFromResultSet(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String email = rs.getString("email");
        Artist artist = new Artist(name, email);
        artist.setId(rs.getInt("artist_id"));
        return artist;
    }

    private List<Listener> getListenersByConcertId(int concertId, String accessType) {
        String SQL = "SELECT * FROM ConcertListeners WHERE concert_id = ? AND access_type = ?";
        List<Listener> listeners = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, concertId);
            statement.setString(2, accessType);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int listenerId = rs.getInt("listener_id");
                Listener listener = getListenerById(listenerId);
                listeners.add(listener);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get Listeners by Concert ID", e);
        }
        return listeners;
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
                listener.setId(listenerId);
                return listener;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error read listener", e);
        }

        return null;
    }

    @Override
    public void update(LiveConcert concert) {
        String SQL = "UPDATE LiveConcert SET title = ?, date = ?, artist_id = ?, ticket_count = ?, is_available_post_live = ?, event_type = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, concert.getTitle());
            statement.setDate(2, new java.sql.Date(concert.getDate().getTime()));
            statement.setInt(3, concert.getArtist().getId());
            statement.setInt(4, concert.getTicketCount());
            statement.setBoolean(5, concert.replayAvailable());
            statement.setString(6, concert.getEventType());
            statement.setInt(7, concert.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error update Concert", e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM LiveConcert WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error delete Concert", e);
        }
    }

    @Override
    public Map<Integer, LiveConcert> getAll() {
        String SQL = "SELECT * FROM LiveConcert";
        Map<Integer, LiveConcert> concertsMap = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LiveConcert concert = read(id);
                concertsMap.put(id, concert);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get al Concert", e);
        }
        return concertsMap;
    }

    @Override
    public LiveConcert get(int id) {
        return read(id);
    }

    private  LiveConcert readFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        Date date = rs.getDate("date");
        int artistId = rs.getInt("artist_id");
        Artist artist = getArtistById(artistId);
        int ticketCount = rs.getInt("ticket_count");
        boolean isAvailablePostLive = rs.getBoolean("is_available_post_live");
        String eventType = rs.getString("event_type");
        LiveConcert concert = new LiveConcert(title, date, artist, ticketCount, isAvailablePostLive, eventType);
        concert.setId(id);
        List<Listener> earlyAccessListeners = getListenersByConcertId(id, "early");
        List<Listener> regularAccessListeners = getListenersByConcertId(id, "regular");
        concert.getEarlyAccessList().addAll(earlyAccessListeners);
        concert.getRegularAccesList().addAll(regularAccessListeners);
        return concert;
    }

}
