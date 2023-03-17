package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/** This class serves to represent times and months. */
public class Times {

    LocalTime apptStartTime;

    LocalTime apptEndTime;

    /** This is the constructor for the Times class.
     * @param apptStartTime The start time for an appointment
     * @param apptEndTime The end time for an appointment */
    public Times(LocalTime apptStartTime, LocalTime apptEndTime) {
        this.apptStartTime = apptStartTime;
        this.apptEndTime = apptEndTime;
    }

    /** This method takes a local Database connection as a parameter, and returns an observable list
     * of all the start times on the hour between 8AM and 10PM, in the local timezone of the user
     * @param connection The local Database connection
     * @return an observable list of all possible start times on the hour between 8AM and 10PM */
    public static ObservableList<LocalTime> fillStartTimeBx(Connection connection) {
        ObservableList<LocalTime> apptStartTime = FXCollections.observableArrayList();

        for(int i = 8; i <= 22; i++) {
            apptStartTime.add(LocalTime.of(i,0));
        }

        ZoneId localTimeZone = ZoneId.systemDefault();

        for (int i = 0; i < apptStartTime.size(); i++) {
            LocalTime localTime = apptStartTime.get(i);
            ZonedDateTime timeEST = ZonedDateTime.of(LocalDate.now(), localTime, ZoneId.of("America/New_York"));
            ZonedDateTime localTimeZ = timeEST.withZoneSameInstant(localTimeZone);
            LocalTime localTime1 = localTimeZ.toLocalTime();
            apptStartTime.set(i, localTime1);
        }
        return apptStartTime;
    }

    /** This method takes a local Database connection as a parameter, and returns an observable list
     * of all the end times on the hour between 8AM and 10PM, in the local timezone of the user
     * @param connection The local Database connection
     * @return an list of all possible end times on the hour between 8AM and 10PM */
    public static ObservableList<LocalTime> fillEndTimeBx(Connection connection) {
        ObservableList<LocalTime> apptEndTime = FXCollections.observableArrayList();

        for(int i = 8; i <= 22; i++) {
            apptEndTime.add(LocalTime.of(i,0));
        }

        ZoneId localTimeZone = ZoneId.systemDefault();

        for (int i = 0; i < apptEndTime.size(); i++) {
            LocalTime localTime = apptEndTime.get(i);
            ZonedDateTime timeEST = ZonedDateTime.of(LocalDate.now(), localTime, ZoneId.of("America/New_York"));
            ZonedDateTime localTimeZ = timeEST.withZoneSameInstant(localTimeZone);
            LocalTime localTime1 = localTimeZ.toLocalTime();
            apptEndTime.set(i, localTime1);
        }
        return apptEndTime;
    }

    /** This method returns an observable list of all 12 months
     * @return an observable list of all 12 months */
    public static ObservableList<String> fillMonthsBx(Connection connection) {
        ObservableList<String> reportMonth = FXCollections.observableArrayList();

        String Jan = new String("January");
        reportMonth.add(Jan);

        String Feb = new String("February");
        reportMonth.add(Feb);

        String Mar = new String("March");
        reportMonth.add(Mar);

        String Apr = new String("April");
        reportMonth.add(Apr);

        String May = new String("May");
        reportMonth.add(May);

        String Jun = new String("June");
        reportMonth.add(Jun);

        String Jul = new String("July");
        reportMonth.add(Jul);

        String Aug = new String("August");
        reportMonth.add(Aug);

        String Sept = new String("September");
        reportMonth.add(Sept);

        String Oct = new String("October");
        reportMonth.add(Oct);

        String Nov = new String("November");
        reportMonth.add(Nov);

        String Dec = new String("December");
        reportMonth.add(Dec);

        return reportMonth;
    }
}
