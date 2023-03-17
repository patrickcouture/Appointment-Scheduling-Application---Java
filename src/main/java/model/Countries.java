package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;

/** This class represents a country object containing the country id and the country name. */
public class Countries {

    private static ObservableList<Countries> allCountries = FXCollections.observableArrayList();
    public int countryId;
    public String countryName;
    public String toString;

    public Countries(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;

    }

    public Countries(int countryId) {
        this.countryId = countryId;
    }

    public int getCountryId() {

        return countryId;
    }

    public String getCountryName() {

        return countryName;
    }


    @Override
    public String toString() {

        return countryName;
    }
}
