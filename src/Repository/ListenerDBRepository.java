package Repository;

import Domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository for Listener
 */
public class ListenerDBRepository extends DBRepository<Listener> {
    SubscriptionDBRepository subscriptionDBRepository;
    public ListenerDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.subscriptionDBRepository=new SubscriptionDBRepository( DBUrl, DBUser,DBPassword);

    }
    @Override
    public void create(Listener listener) {
        String SQL = "INSERT INTO Listener (name, email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, listener.getName());
            statement.setString(2, listener.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error create Listener", e);
        }
    }

    @Override
    public Listener read(int id) {
        String SQL = "SELECT * FROM Listener WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error read Listener", e);
        }
        return null;
    }

    @Override
    public void update(Listener listener) {
        String SQL = "UPDATE Listener SET name = ?, email = ?, WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, listener.getName());
            statement.setString(2, listener.getEmail());
            statement.setInt(3, listener.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error update Listener", e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM Listener WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error delete Listener", e);
        }
    }

    @Override
    public Map<Integer, Listener> getAll() {
        String SQL = "SELECT * FROM Listener";
        Map<Integer, Listener> listeners = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Listener listener = extractFromResultSet(resultSet);
                listeners.put(listener.getId(), listener);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get all Listener", e);
        }
        return listeners;
    }

    @Override
    public Listener get(int id) {
        return read(id);
    }

    private Listener extractFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        Listener listener = new Listener(name, email);
        listener.setId(id);
        Subscription subscription = subscriptionDBRepository.read(id);
        if (subscription != null) {
            listener.setSubscription(subscription);
        }
        return listener;
    }

    public List<Listener> getListenersByConcertId(int concertId, String accessType) {
        String SQL = "SELECT * FROM ConcertListeners WHERE concert_id = ? AND access_type = ?";
        List<Listener> listeners = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, concertId);
            statement.setString(2, accessType);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int listenerId = rs.getInt("id");
                Listener listener = read(listenerId);
                if (listener != null) {
                    listeners.add(listener);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving listeners for concert", e);
        }
        return listeners;
    }





}
