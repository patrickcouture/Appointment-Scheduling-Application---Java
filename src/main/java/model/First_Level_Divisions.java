package model;


/** This class represents the first level divisions object containing the division id, division name, and country id. */
public class First_Level_Divisions {


    int divisionId;

    String divisionName;

    int countryId;

    public String toString;

    public First_Level_Divisions(int divisionId, String divisionName, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    public First_Level_Divisions(int divisionId) {
        this.divisionId = divisionId;
    }


    public int getDivisionId() {

        return divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }


    @Override
    public String toString() {
        return divisionName;
    }


}
