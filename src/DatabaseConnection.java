import java.sql.*;

public class DatabaseConnection {
    private static Connection connection;
    public static Connection getConnection() {
        if (connection == null) {
            try {

                String url = "jdbc:postgresql://localhost:5432/MTifyDatabase";
                String user ="postgres";
                String password = "1111";
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                throw new RuntimeException("Error connecting to the database", e);
            }
        }
        return connection;
    }

    //String sql="select name from Artist where id=3";
    //String URL = "jdbc:postgresql://localhost:5432/MTifyDatabase";
    //String USER = "postgres";
    //String PASSWORD = "1111";
    //Connection con=DriverManager.getConnection(URL,USER,PASSWORD);
    //Statement st= con.createStatement();
    //ResultSet rs= st.executeQuery(sql);
    //rs.next();
    //String name=rs.getString(1);
    //System.out.println(name);


}

