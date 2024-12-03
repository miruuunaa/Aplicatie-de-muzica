package Repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public abstract class  DBRepository<T> implements IRepository<T>, AutoCloseable{
    protected Connection connection;
    DBRepository(String DBUrl, String DBUser, String DBPassword) {
        try
        {
            this.connection = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
