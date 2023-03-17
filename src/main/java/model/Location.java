package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;

/** This class contains information about the location of appointments. */
public class Location {
    String location;

    @Override
    public String toString() {
        return location;
    }

    /** Constructs a new Location object.
     * @param location The name of the location. */
    public Location(String location) {
        this.location = location;
    }

    /** Gets the name of the location.
     * @return The name of the location. */
    public String getLocation() {
        return location;
    }

    /** Sets the name of the location.
     * @param location The name of the location. */
    public void setLocation(String location) {
        this.location = location;
    }

    /** Creates a list of location objects representing different locations.
     * @param connection The connection to the database.
     * @return objects populated from the database. */
    public static ObservableList<String> fillLocationComBx(Connection connection) {
        ObservableList<String> apptLocation = FXCollections.observableArrayList();

        String loc1 = new String("Phoenix");
        apptLocation.add(loc1);

        String loc2 = new String("White Plains");
        apptLocation.add(loc2);

        String loc3 = new String("Montreal");
        apptLocation.add(loc3);

        String loc4 = new String("London");
        apptLocation.add(loc4);

        String loc5 = new String("location");
        apptLocation.add(loc5);

        return apptLocation;
    }
}
