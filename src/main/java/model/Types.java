package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;

/** The Types class contains methods that allows the database table to be filled
 * with valid types. */
public class Types {

    String type;

    @Override
    public String toString() {
        return type;
    }

    /** This is the constructor for the Types class.
     * @param type the current type */
    public Types(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /** This is a method that returns an ObservableList of Strings which repesents
     * appt types.
     * @param connection the sql connection
     * @return a list containing appt types */
    public static ObservableList<String> fillTypeComBx(Connection connection) {
        ObservableList<String> apptType = FXCollections.observableArrayList();

        String typeExCoach = new String("Executive Coaching");
        apptType.add(typeExCoach);

        String typeKnSh = new String("Knowledge Sharing Session");
        apptType.add(typeKnSh);

        String typePrMan = new String("Project Management");
        apptType.add(typePrMan);

        String typeDeBr = new String("De-Briefing");
        apptType.add(typeDeBr);

        return apptType;
    }
}
