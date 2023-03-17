package dbAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.First_Level_Divisions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class provides methods to get the data related to first-level divisions from the database. */
public class dbFirst_Level_Divisions {

    /** Gets all the first-level division details from the database.
     * @param connection of type Connection
     * @return a list of first-level division objects */
    public static ObservableList<First_Level_Divisions> getAllFirstLevelDiv(Connection connection) throws SQLException {
        ObservableList<First_Level_Divisions> firstLevelDivs = FXCollections.observableArrayList();
        String sql = "SELECT * FROM first_level_divisions";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            int divisionId = resultSet.getInt("Division_ID");
            String divisionName = resultSet.getString("Division");
            int countryId = resultSet.getInt("Country_ID");
            First_Level_Divisions firstLevelDivision = new First_Level_Divisions(divisionId, divisionName, countryId);
            firstLevelDivs.add(firstLevelDivision);
        }
        return firstLevelDivs;
    }

    /** Gets all the first-level division details for the US from the database.
     * @param connection of type Connection
     * @return a list of first-level division objects for the US */
    public static ObservableList<First_Level_Divisions> getUSStates (Connection connection) throws SQLException {
        ObservableList<First_Level_Divisions> fullUSStateList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = 1";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int divisionId = resultSet.getInt("Division_ID");
            String divisionName = resultSet.getString("Division");
            int countryId = resultSet.getInt("Country_ID");

            fullUSStateList.add(new First_Level_Divisions(divisionId, divisionName, countryId));

        }
       return fullUSStateList;
    }

    /** Gets all the first-level division details for Canada from the database.
     * @param connection of type Connection
     * @return a list of first-level division objects for Canada */
    public static ObservableList<First_Level_Divisions> getCanadianProv (Connection connection) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = 3";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<First_Level_Divisions> fullCanadianProvList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            int divisionId = resultSet.getInt("Division_ID");
            String divisionName = resultSet.getString("Division");
            int countryId = resultSet.getInt("Country_ID");

            fullCanadianProvList.add(new First_Level_Divisions(divisionId, divisionName, countryId));

        }
        return fullCanadianProvList;
    }

    /** Gets all the first-level division details for the UK from the database.
     * @param connection of type Connection
     * @return a list of first-level division objects for the UK */
    public static ObservableList<First_Level_Divisions> getUKRegions (Connection connection) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = 2";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<First_Level_Divisions> fullUKRegionList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            int divisionId = resultSet.getInt("Division_ID");
            String divisionName = resultSet.getString("Division");
            int countryId = resultSet.getInt("Country_ID");

            fullUKRegionList.add(new First_Level_Divisions(divisionId, divisionName, countryId));

        }
        return fullUKRegionList;
    }


}
