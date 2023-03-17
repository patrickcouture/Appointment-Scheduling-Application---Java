package controller;

import dbAccess.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

import static dbAccess.JDBC.connection;

/** This class is the controller of the add appointment form. It implements the Initializable interface.
 * The class contains the necessary components of a window to add an appointment, including:
 * labels, text fields, combo boxes, date and time pickers, and save and cancel buttons. */
public class AddApptFormController implements Initializable {
    public Label addApptFormLabel;
    public Label apptIdLabel;
    public TextField apptIdTxtFld;
    public Label addApptTitle;
    public TextField addApptTitleTxtFld;
    public Label addApptDescLabel;
    public TextField addApptDescTxtFld;
    public Label addApptLocationLabel;
    public Label addApptContactLabel;
    public ComboBox<Contacts> addApptContactComBx;
    public Label addApptTypeLabel;
    public ComboBox<String> addApptTypeComBx;
    public Label addApptStartDateLabel;
    public DatePicker addApptStartDatePicker;
    public Label addApptStartTimeLabel;
    public ComboBox<LocalTime> addApptStartTimeComBx;
    public Label addApptEndDateLabel;
    public Label addApptEndTimeLabel;
    public ComboBox<LocalTime> addApptEndTimeComBx;
    public Label addApptCustIdLabel;
    public ComboBox<Customers> addApptCustIdComBx;
    public Label addApptUserIdLabel;
    public ComboBox<Users> addApptUserIdComBx;
    public Button addApptSaveBtn;
    public Button addApptCancelBtn;
    public ComboBox<String> addApptLocationComBx;
    public DatePicker addApptEndDatePicker;

    Stage stage;

    Parent scene;

    /** This method is called when a contact is added by the user.
     *  @param actionEvent the action event */
    public void onActionAddApptContactComBx(ActionEvent actionEvent) {
    }

    /** This method is called when a type is added by the user.
     *  @param actionEvent the action event */
    public void onActionAddApptTypeComBx(ActionEvent actionEvent) {
    }

    /** This method is called when a start date is selected by the user.
     *  @param actionEvent the action event */
    public void onActionAddApptStartDatePicker(ActionEvent actionEvent) {
    }

    /** This method is called when a start time is added by the user.
     *  @param actionEvent the action event */
    public void onActionaddApptStartTimeComBx(ActionEvent actionEvent) {
    }

    /** This method is called when an end date is selected by the user.
     *  @param actionEvent the action event */
    public void onActionAddApptEndDatePicker(ActionEvent actionEvent) {
    }

    /** This method is called when an end time is added by the user.
     *  @param actionEvent the action event */
    public void onActionaAddApptEndTimeComBx(ActionEvent actionEvent) {
    }

    /** This method is called when a customer id is added by the user.
     *  @param actionEvent the action event */
    public void onActionAddApptCustIdComBx(ActionEvent actionEvent) {
    }
    /** This method is called when a user id is added by the user.
     *  @param actionEvent the action event */
    public void onActionaddApptUserIdComBx(ActionEvent actionEvent) {
    }

    /** This method handles what happens when the "Save" button is clicked on the "Add Appointment" screen. It starts by creating a new Appointment instance
     * and retrieving data from the fields entered in the GUI. It then validates that the start time is before the end time and that the appointment does not
     * overlap with another appointment for the same customer. It then uses the data to add the appointment to the database, as long as all fields have been
     * filled out correctly. If any of these validations are not met, it will display an error message.
     * @param actionEvent the ActionEvent
     * @throws SQLException
     * @throws IOException */
    public void onActionaddApptSaveBtn(ActionEvent actionEvent) throws SQLException, IOException {

        try {
            int apptId = 0;
            String apptTitle = addApptTitle.getText();
            String apptDesc = addApptDescTxtFld.getText();
            String apptLocation = addApptLocationComBx.getSelectionModel().getSelectedItem();
            String apptType = addApptTypeComBx.getSelectionModel().getSelectedItem();
            Contacts apptContact = addApptContactComBx.getSelectionModel().getSelectedItem();
            int apptContactId = apptContact.getContactId();
            LocalDate startDate = addApptStartDatePicker.getValue();
            LocalDate endDate = addApptEndDatePicker.getValue();
            LocalTime startTime = addApptStartTimeComBx.getSelectionModel().getSelectedItem();
            LocalTime endTime = addApptEndTimeComBx.getSelectionModel().getSelectedItem();
            LocalDateTime apptStart = LocalDateTime.of(startDate, startTime);
            LocalDateTime apptEnd = LocalDateTime.of(endDate, endTime);
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zdt = ZonedDateTime.of(apptStart, zoneId);
            int apptCustomerId = addApptCustIdComBx.getSelectionModel().getSelectedItem().getCustomerId();
            int apptUserId = addApptUserIdComBx.getSelectionModel().getSelectedItem().getUserId();


            if (apptStart.isAfter(apptEnd) || apptStart.equals(apptEnd)) {
                Alert timing = new Alert(Alert.AlertType.ERROR);
                timing.setTitle("Error");
                timing.setContentText("Appointment start time must be before end time.");
                timing.showAndWait();
            }


            LocalTime meetingStart = apptStart.toLocalTime();
            LocalTime meetingEnd = apptEnd.toLocalTime();

            LocalTime businessHoursStart = LocalTime.of(8, 0);
            LocalTime businessHoursEnd = LocalTime.of(22, 0);

            ObservableList<Appointments> appointments = dbAppointments.getAllAppointments(connection);

            Appointments newAppointment = new Appointments(apptId, apptTitle, apptDesc, apptLocation, apptContact, apptContactId, apptType, apptStart, apptEnd, apptCustomerId, apptUserId);
            int searchCustId = newAppointment.getApptCustomerId();
            boolean apptOverlap = false;

            for (Appointments appointment : appointments) {
                if (appointment.getApptCustomerId() == searchCustId) {

                    if ((newAppointment.getStart().isAfter(appointment.getStart()) || (newAppointment.getStart()).equals(appointment.getStart()))
                            && ((newAppointment.getStart().isBefore(appointment.getEnd())) || (newAppointment.getStart().equals(appointment.getEnd())))
                            || (newAppointment.getEnd().isAfter(appointment.getStart()) && ((newAppointment.getEnd().isBefore(appointment.getEnd()))
                            || (newAppointment.getEnd().equals(appointment.getEnd()))))) {
                        apptOverlap = true;
                    }

                    if (apptOverlap) {

                        Alert overlap = new Alert(Alert.AlertType.ERROR);
                        overlap.setContentText("Appointment overlaps with another appointment.");
                        overlap.showAndWait();
                    }
                }


            }

            if ((addApptStartTimeComBx.getSelectionModel().isEmpty() || addApptEndTimeComBx.getSelectionModel().isEmpty() ||
                    addApptUserIdComBx.getSelectionModel().isEmpty() || addApptCustIdComBx.getSelectionModel().isEmpty() ||
                    addApptLocationComBx.getSelectionModel().isEmpty() || addApptTypeComBx.getSelectionModel().isEmpty() ||
                    addApptContactComBx.getSelectionModel().isEmpty() || addApptTitleTxtFld.getText().isEmpty() ||
                    addApptDescTxtFld.getText().isEmpty() || apptStart.isAfter(apptEnd)) || apptOverlap) {
                Alert infoMissing = new Alert(Alert.AlertType.ERROR);
                infoMissing.setContentText("Please make a selection in all fields before saving.");
                infoMissing.showAndWait();
            } else {
                Appointments appt = new Appointments(apptId, apptTitle, apptDesc, apptLocation, apptContact, apptContactId, apptType, apptStart, apptEnd, apptCustomerId, apptUserId);
                dbAppointments.insert(appt);
                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/ApptCustOverView.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

            }
        }catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fields or Combo Boxes are Null");
            alert.setContentText("Please fill in all fields or select an item in the combo boxes.");
            alert.showAndWait();

        }
            
}

    /** onActionAddApptCancelBtn is a method that sends the user to the Appointment Customer Overview View.
     * @param actionEvent Cancel Button is clicked
     * @throws IOException When the Appointment Customer Overview View cannot be loaded */
    public void onActionAddApptCancelBtn(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/ApptCustOverView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    public void onActionAddApptLocationComBx(ActionEvent actionEvent) {
    }

    /** Initialize is a method used to fill the combo boxes in the Add Appointment View with data from the database.
     * @param url URL of the current scene
     * @param resourceBundle The ResourceBundle of the current scene */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Connection connection = JDBC.getConnection();

            ObservableList<String> fillTypeBx = Types.fillTypeComBx(connection);
            addApptTypeComBx.setItems(fillTypeBx);
            addApptTypeComBx.setPromptText("Select Appt Type");

            ObservableList<String> fillLocBx = Location.fillLocationComBx(connection);
            addApptLocationComBx.setItems(fillLocBx);
            addApptLocationComBx.setPromptText("Select a Location");

            ObservableList<Contacts> fillContactBx = dbContacts.getAllContacts(connection);
            addApptContactComBx.setItems(fillContactBx);
            addApptContactComBx.setPromptText("Select an Appt Contact");

            ObservableList<LocalTime> fillStartTimeBx = Times.fillStartTimeBx(connection);
            addApptStartTimeComBx.setItems(fillStartTimeBx);
            addApptStartTimeComBx.setPromptText("Select an Appt Start Time");

            ObservableList<LocalTime> fillEndTimeBx = Times.fillEndTimeBx(connection);
            addApptEndTimeComBx.setItems(fillEndTimeBx);
            addApptEndTimeComBx.setPromptText("Select an Appt End Time");

            ObservableList<Customers> fillCustIdBx = dbCustomers.getAllCustomers(connection);
            addApptCustIdComBx.setItems(fillCustIdBx);
            addApptCustIdComBx.setPromptText("Select a Customer");

            ObservableList<Users> fillUserIdBx = dbUsers.getAllUsers(connection);
            addApptUserIdComBx.setItems(fillUserIdBx);
            addApptUserIdComBx.setPromptText("Select a User");




        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
