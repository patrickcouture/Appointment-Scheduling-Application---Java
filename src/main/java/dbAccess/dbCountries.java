package dbAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;

import java.sql.*;

/** This class handles all the database operations related to Countries. */
public class dbCountries {

    /** This method retrieves all the countries present in the database and returns them as an ObservableList.
     * @param connection This is the connection object used to connect to the database.
     * @return ObservableList containing Countries objects that represent all the countries in the database.
     * @throws SQLException if an error is encountered while querying the database. */
    public static ObservableList<Countries> getAllCountries(Connection connection) throws SQLException {

        ObservableList<Countries> fullCountryList = FXCollections.observableArrayList();

            String sql = "SELECT * FROM countries";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Countries countries = new Countries(countryId, countryName);
                fullCountryList.add(countries);
            }
        return fullCountryList;
        }

    }


