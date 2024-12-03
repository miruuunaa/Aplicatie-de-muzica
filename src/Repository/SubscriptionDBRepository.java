package Repository;

import Domain.Subscription;
import Domain.Listener;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionDBRepository extends DBRepository<Subscription> {
    private int currentId;


    public SubscriptionDBRepository(String DBUrl, String DBUser, String DBPassword) {
        super(DBUrl, DBUser, DBPassword);
        this.currentId = getMaxIdFromDatabase() + 1;
    }


    private int getMaxIdFromDatabase() {
        String SQL = "SELECT MAX(id) AS maxId FROM Subscription";
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
    public void create(Subscription subscription) {
        String SQL = "INSERT INTO Subscription (id, type, price, user_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            subscription.setId(currentId);
            statement.setInt(1, currentId++);
            statement.setString(2, subscription.getType());
            statement.setFloat(3, subscription.getPrice());
            statement.setInt(4, subscription.getUser().getId());


            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating subscription", e);
        }
    }

    @Override
    public Subscription read(int id) {
        String SQL = "SELECT * FROM Subscription WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return readFromResultSet(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading subscription", e);
        }
    }

    private Subscription readFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String type = rs.getString("type");
        float price = rs.getFloat("price");
        int userId = rs.getInt("user_id");
        Listener user = getListenerById(userId);
        Subscription subscription = new Subscription(type, price, user);
        subscription.setId(id);
        return subscription;
    }


    private Listener getListenerById(int userId) {
        String SQL = "SELECT * FROM Listener WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                Listener listener = new Listener(name, email);
                listener.setId(rs.getInt("id"));
                return listener;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting listener by ID", e);
        }
        return null;
    }

    @Override
    public void update(Subscription subscription) {
        String SQL = "UPDATE Subscription SET type = ?, price = ?, user_id = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, subscription.getType());
            statement.setFloat(2, subscription.getPrice());
            statement.setInt(3, subscription.getUser().getId());
            statement.setInt(4, subscription.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating subscription", e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM Subscription WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting subscription", e);
        }
    }

    @Override
    public Map<Integer, Subscription> getAll() {
        String SQL = "SELECT * FROM Subscription";
        Map<Integer, Subscription> subscriptionMap = new HashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Subscription subscription = readFromResultSet(resultSet);
                subscriptionMap.put(subscription.getId(), subscription);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all subscriptions", e);
        }
        return subscriptionMap;
    }

    @Override
    public Subscription get(int id) {
        return read(id);
    }
}
