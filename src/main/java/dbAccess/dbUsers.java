package dbAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/** This class includes methods for accessing users from the database. */
public class dbUsers {

    /** This method is used to get a list of all users from the database.
     * @param connection JDBC connection
     * @return All users in the database */
    public static ObservableList<Users> getAllUsers(Connection connection) throws SQLException {

        String query = "SELECT User_ID, User_Name, Password FROM users";

        PreparedStatement ps = JDBC.connection.prepareStatement(query);
        ResultSet resultSet = ps.executeQuery();

        ObservableList<Users> fullList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            int userId = resultSet.getInt("User_ID");
            String username = resultSet.getString("User_Name");
            String password = resultSet.getString("Password");

            Users newUser = new Users(userId, username, password);
            fullList.add(newUser);
        }
        return fullList;
    }
}
