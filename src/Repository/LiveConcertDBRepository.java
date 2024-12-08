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

    private ListenerDBRepository listenerDBRepository;
    private ArtistDBRepository artistDBRepository;
    public LiveConcertDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.listenerDBRepository = new ListenerDBRepository(DBUrl, DBUser, DBPassword);
        this.artistDBRepository = new ArtistDBRepository(DBUrl, DBUser, DBPassword);

    }
    @Override
    public void create(LiveConcert concert) {
        String SQL = "INSERT INTO LiveConcert (title, date, artist_id, ticket_count, is_available_post_live, event_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, concert.getTitle());
            statement.setDate(2, new java.sql.Date(concert.getDate().getTime()));
            statement.setInt(3, concert.getArtist().getId());
            statement.setInt(4, concert.getTicketCount());
            statement.setBoolean(5, concert.replayAvailable());
            statement.setString(6, concert.getEventType());
            statement.executeUpdate();
            addListenersToConcert(concert);
        } catch (SQLException e) {
            throw new RuntimeException("Error create Concert", e);
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
        Artist artist = artistDBRepository.read(artistId);
        int ticketCount = rs.getInt("ticket_count");
        boolean isAvailablePostLive = rs.getBoolean("is_available_post_live");
        String eventType = rs.getString("event_type");
        LiveConcert concert = new LiveConcert(title, date, artist, ticketCount, isAvailablePostLive, eventType);
        concert.setId(id);
        List<Listener> earlyAccessListeners = listenerDBRepository.getListenersByConcertId(id, "early");
        List<Listener> regularAccessListeners = listenerDBRepository.getListenersByConcertId(id, "regular");
        concert.getEarlyAccessList().addAll(earlyAccessListeners);
        concert.getRegularAccesList().addAll(regularAccessListeners);
        return concert;
    }

}
