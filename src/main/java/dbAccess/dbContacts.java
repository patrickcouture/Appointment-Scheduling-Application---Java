package dbAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import model.Contacts;

import java.sql.*;
import java.time.LocalDateTime;

/** This class contains the static method getAllContacts */
public class dbContacts {

    /** Retrieves all the contacts from an online database and builds them into an observable list.
     * @param connection The connection to the online database that contains the contacts.
     * @return An observable list of contacts created from the online database.
     * @throws SQLException Thrown when there is an issue with interacting with the online database. */
    public static ObservableList<Contacts> getAllContacts(Connection connection) throws SQLException {

        String sql = "SELECT * FROM contacts";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Contacts> fullContactList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            int contactId = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");
            String contactEmail = resultSet.getString("Email");

            Contacts contacts = new Contacts(contactId, contactName, contactEmail);
            fullContactList.add(contacts);
        }

        return fullContactList;
    }


}
