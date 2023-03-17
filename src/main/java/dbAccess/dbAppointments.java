package dbAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import model.Contacts;

import java.sql.*;
import java.time.LocalDateTime;

/** This abstract class contains methods for managing the Appointments in the database. */
public abstract class dbAppointments{

    /** Gets the list of appointments from the database
     * @param connection
     * @return a list of all appointments */
    public static ObservableList<Appointments> getAllAppointments(Connection connection) throws SQLException {
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, appointments.Contact_ID, contacts.Contact_Name\n" +
                "FROM appointments\n" +
                "INNER JOIN contacts\n" +
                "ON appointments.Contact_ID = contacts.Contact_ID;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<Appointments> fullAppointmentList = FXCollections.observableArrayList();

        while (rs.next()) {
            int apptId = rs.getInt("Appointment_ID");
            String apptTitle = rs.getString("Title");
            String apptDesc = rs.getString("Description");
            String apptLocation = rs.getString("Location");
            String apptContact = rs.getString("Contact_Name");
            int apptContactId = rs.getInt("Contact_ID");
            String apptType = rs.getString("Type");
            Timestamp tsStart = rs.getTimestamp("Start");
            LocalDateTime apptStart = tsStart.toLocalDateTime();
            Timestamp tsEnd = rs.getTimestamp("End");
            LocalDateTime apptEnd = tsEnd.toLocalDateTime();
            int apptCustId = rs.getInt("Customer_ID");
            int apptUserId = rs.getInt("User_ID");

            Appointments appointments = new Appointments(apptId, apptTitle, apptDesc, apptLocation, apptContact, apptContactId, apptType, apptStart, apptEnd, apptCustId, apptUserId);
            fullAppointmentList.add(appointments);

        }

        return fullAppointmentList;
    }

    /** Inserts a new Appointment into the database
     * @param newAppointment the Appointment to be inserted
     * @return an integer representing the number of rows affected */
    public static int insert(Appointments newAppointment) throws SQLException {
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, newAppointment.getApptTitle());
        ps.setString(2, newAppointment.getApptDesc());
        ps.setString(3, newAppointment.getApptLocation());
        ps.setString(4, newAppointment.getApptType());
        ps.setTimestamp(5, Timestamp.valueOf(newAppointment.getApptStart()));
        ps.setTimestamp(6, Timestamp.valueOf(newAppointment.getApptEnd()));
        ps.setInt(7, newAppointment.getApptCustomerId());
        ps.setInt(8, newAppointment.getApptUserId());
        ps.setInt(9, newAppointment.getApptContactId());
        return ps.executeUpdate();

    }

    public static int update(Appointments updateAppointment) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?  WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, updateAppointment.getApptTitle());
        ps.setString(2, updateAppointment.getApptDesc());
        ps.setString(3, updateAppointment.getApptLocation());
        ps.setString(4, updateAppointment.getApptType());
        ps.setTimestamp(5, Timestamp.valueOf(updateAppointment.getApptStart()));
        ps.setTimestamp(6, Timestamp.valueOf(updateAppointment.getApptEnd()));
        ps.setInt(7, updateAppointment.getApptCustomerId());
        ps.setInt(8, updateAppointment.getApptUserId());
        ps.setInt(9, updateAppointment.getApptContactId());
        ps.setInt(10, updateAppointment.getApptId());
        return ps.executeUpdate();

    }



    /** Deletes an Appointment from the database
     * @param apptId the id of the Appointment to be deleted
     * @return an integer representing the number of rows affected */
    public static int delete(int apptId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, apptId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

    /** Deletes all Appointments associated with a customer from the database
     * @param customerId the id of the customer associated with appointments to delete
     * @return an integer representing the number of rows affected */
    public static int deleteAssocAppts(int customerId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        preparedStatement.setInt(1, customerId);
        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected;
    }

}


