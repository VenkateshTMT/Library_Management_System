package db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetConnection {

    private static Connection connection = null;

    public static Connection makeConnection() {
        String url = "jdbc:mysql://localhost:3306/library_management";
        String username = "root";
        String password = "root";
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
