package dbAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class provides methods for performing query operations on the customers table in the database. */
public class dbCustomers {

    /** Retrieve all customer information from the database.
     * @param connection The connection with the database.
     * @return An observable list of customers populated with customer information. */
    public static ObservableList<Customers> getAllCustomers(Connection connection) throws SQLException {

        String sql = "SELECT Customer_ID, Customer_Name, Address, Phone, Postal_Code, customers.Division_ID, first_level_divisions.Division\n" +
                "FROM customers\n" +
                "INNER JOIN first_level_divisions\n" +
                "ON customers.Division_ID = first_level_divisions.Division_ID;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ObservableList<Customers> fullCustomerList = FXCollections.observableArrayList();

        while (rs.next()) {
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String custAddress = rs.getString("Address");
            String custPostalCode = rs.getString("Postal_Code");
            String custPhone = rs.getString("Phone");
            int divisionId = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");

            Customers customer = new Customers(customerId, customerName, custAddress, custPostalCode, custPhone, divisionId, divisionName);
            fullCustomerList.add(customer);

        }

        return fullCustomerList;
    }

    /** Insert a new customer row into the database.
     * @param newCustomer A Customers object containing the customer details.
     * @return The number of affected rows. */
    public static int insert(Customers newCustomer) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, newCustomer.customerName);
        ps.setString(2, newCustomer.custAddress);
        ps.setString(3, newCustomer.custPostalCode);
        ps.setString(4, newCustomer.custPhone);
        ps.setInt(5, newCustomer.divisionId);
        return ps.executeUpdate();

    }

    /** Update an existing customer row in the database.
     * @param updateCustomer A Customers object containing the customer details.
     * @return The number of affected rows. */
    public static int update(Customers updateCustomer) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        preparedStatement.setString(1, updateCustomer.customerName);
        preparedStatement.setString(2, updateCustomer.custAddress);
        preparedStatement.setString(3, updateCustomer.custPostalCode);
        preparedStatement.setString(4, updateCustomer.custPhone);
        preparedStatement.setInt(5, updateCustomer.divisionId);
        preparedStatement.setInt(6, updateCustomer.customerId);
        return preparedStatement.executeUpdate();

    }

    /** Delete an existing customer row from the database.
     * @param customerId The ID of the customer to be deleted.
     * @return The number of affected rows. */
    public static int delete(int customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

    /** Select customer rows for a given division from the database.
     * @param divisionId The ID of the division for which the customers should be returned. */
    public static void select(int divisionId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String custAddress = rs.getString("Address");
            String custPostalCode = rs.getString("Postal_Code");
            String custPhone = rs.getString("Phone");
            int divisionIdFk = rs.getInt("Division_ID");

        }

    }

}
