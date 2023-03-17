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

import static dbAccess.JDBC.*;
import static dbAccess.dbAppointments.update;

/** Class that creates the form that updates an appointment */
public class UpdateApptFormController implements Initializable {
    public Label updateApptFormLabel;
    public Label updateApptIdLabel;
    public TextField updateApptIDTxtFld;
    public Label updateApptTitleLabel;
    public TextField updateApptTitleTxtFld;
    public Label updateApptDescLabel;
    public Label updateApptLocationLabel;
    public Label updateApptContactLabel;
    public ComboBox<Contacts> updateApptContactComBx;
    public Label updateApptTypeLabel;
    public ComboBox<String> updateApptTypeComBx;
    public Label updateApptStartDateLabel;
    public DatePicker updateApptStartDatePkr;
    public Label updateApptStartTimeLabel;
    public ComboBox<LocalTime> updateApptStartTimeComBx;
    public Label updateApptEndDateLabel;
    public DatePicker updateApptEndDatePkr;
    public Label updateApptEndTimeLabel;
    public ComboBox<LocalTime> updateApptEndTimeComBx;
    public Label updateApptCustIdLabel;
    public Label updateApptUserIdLabel;
    public Button updateApptSaveBtn;
    public Button updateApptCancelBtn;
    public ComboBox<String> updateApptLocationComBx;
    public TextField updateApptDescTxtFld;

    public ComboBox<Customers> updateApptCustIdComBx;
    public ComboBox<Users> updateApptUserIdComBx;

    Stage stage;
    Parent scene;

    /** This method initializes the ComboBoxes on the Update Appointment window with the different options from the database.
     * @param url The URL to the fxml file.
     * @param resourceBundle The resources supplied for the UpdateApptController. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = JDBC.getConnection();

        try {
            ObservableList<String> fillTypeBx = Types.fillTypeComBx(connection);
            updateApptTypeComBx.setItems(fillTypeBx);

            ObservableList<String> fillLocBx = Location.fillLocationComBx(connection);
            updateApptLocationComBx.setItems(fillLocBx);

            ObservableList<Contacts> fillContactBx = dbContacts.getAllContacts(connection);
            updateApptContactComBx.setItems(fillContactBx);

            ObservableList<LocalTime> fillStartTimeBx = Times.fillStartTimeBx(connection);
            updateApptStartTimeComBx.setItems(fillStartTimeBx);

            ObservableList<LocalTime> fillEndTimeBx = Times.fillEndTimeBx(connection);
            updateApptEndTimeComBx.setItems(fillEndTimeBx);

            ObservableList<Customers> fillCustIdBx = dbCustomers.getAllCustomers(connection);
            updateApptCustIdComBx.setItems(fillCustIdBx);

            ObservableList<Users> fillUserIdBx = dbUsers.getAllUsers(connection);
            updateApptUserIdComBx.setItems(fillUserIdBx);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void onActionUpdateApptContactComBx(ActionEvent actionEvent) {
    }

    public void onActionUpdateApptTypeComBx(ActionEvent actionEvent) {
    }

    public void onActionUpdateApptStartDatePkr(ActionEvent actionEvent) {
    }

    public void onActionUpdateApptStartTimeComBx(ActionEvent actionEvent) {
    }

    public void onActionUpdateApptEndDatePkr(ActionEvent actionEvent) {
    }

    public void onActionUpdateApptEndTimeComBx(ActionEvent actionEvent) {
    }

    /** A method that handles an update appointment save button. Checks to make sure the appointment start time is before
     * the end time, and checks for any appointment overlap before saving the appointment.
     * @param actionEvent The specified ActionEvent.
     * @throws SQLException Throws SQL exception if data could not be saved into the database.
     * @throws IOException Throws IO exception if there is an issue loading the specified FXML file. */
    public void onActionupdateApptSaveBtn(ActionEvent actionEvent) throws SQLException, IOException {

        try {
            int apptId = Integer.parseInt(updateApptIDTxtFld.getText());
            String apptTitle = updateApptTitleTxtFld.getText();
            String apptDesc = updateApptDescTxtFld.getText();
            String apptLocation = updateApptLocationComBx.getSelectionModel().getSelectedItem();
            String apptType = updateApptTypeComBx.getSelectionModel().getSelectedItem();
            Contacts apptContact = updateApptContactComBx.getSelectionModel().getSelectedItem();
            int apptContactId = apptContact.getContactId();
            LocalDate startDate = updateApptStartDatePkr.getValue();
            LocalDate endDate = updateApptEndDatePkr.getValue();
            LocalTime startTime = updateApptStartTimeComBx.getSelectionModel().getSelectedItem();
            LocalTime endTime = updateApptEndTimeComBx.getSelectionModel().getSelectedItem();
            LocalDateTime apptStart = LocalDateTime.of(startDate, startTime);
            LocalDateTime apptEnd = LocalDateTime.of(endDate, endTime);
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zdt = ZonedDateTime.of(apptStart, zoneId);
            Customers apptCustomerId = (Customers) updateApptCustIdComBx.getSelectionModel().getSelectedItem();
            Users apptUserId = (Users) updateApptUserIdComBx.getSelectionModel().getSelectedItem();


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

            Appointments newAppointment = new Appointments(apptId, apptTitle, apptDesc, apptLocation, apptContact, apptContactId, apptType, apptStart, apptEnd, apptCustomerId.getCustomerId(), apptUserId.getUserId());
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
                        overlap.setContentText("This appointment conflicts with another appointment");
                        overlap.showAndWait();
                    }
                }

            }

            Boolean comboInfoMissing = false;
//            if(updateApptStartTimeComBx.getValue().== || updateApptEndTimeComBx.getSelectionModel().isEmpty() ||
//                    updateApptLocationComBx.getSelectionModel().isEmpty() || updateApptTypeComBx.getSelectionModel().isEmpty() ||
//            updateApptContactComBx.getSelectionModel().isEmpty() || updateApptCustIdComBx.getSelectionModel().isEmpty() || updateApptUserIdComBx.getSelectionModel().isEmpty()){
//                comboInfoMissing = true;
//                Alert missingCombo = new Alert(Alert.AlertType.ERROR);
//                missingCombo.setContentText("You need to make a selection in all fields.");
//                missingCombo.showAndWait();
//            }
            Boolean textMissing = false;
            if((apptTitle.isEmpty()) || (apptDesc.isEmpty())) {
                textMissing = true;
                Alert missingText = new Alert(Alert.AlertType.ERROR);
                missingText.setContentText("You need to enter text in the text fields");
                missingText.show();
            }

            else {

                Appointments updateAppt = new Appointments(apptId, apptTitle, apptDesc, apptLocation, apptType, apptStart, apptEnd, apptCustomerId.getCustomerId(), apptUserId.getUserId(), apptContactId);
                dbAppointments.update(updateAppt);
                stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/ApptCustOverView.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }catch (NullPointerException | NumberFormatException exception) {
            exception.printStackTrace();
        }
    }

    /** This method closes the current window and loads the ApptCustOverView fxml.
     * @param actionEvent The action event of the cancel button.
     * @throws IOException If unable to locate or open the ApptCustOverView fxml. */
    public void onActionUpdateApptCancelBtn(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/ApptCustOverView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onActionUpdateApptLocationComBx(ActionEvent actionEvent) {
    }

    /** This method is used to set the appointment fields of the appointment object from the database to the corresponding fields of the update appointment window.
     * @param updateAppt The current appointment object pulled from the database.
     * @param selectedIndex The index of the appointment.
     * @throws SQLException If unable to select the data from the database. */
    public void receiveApptData (Appointments updateAppt, int selectedIndex) throws SQLException {

        updateApptIDTxtFld.setText(String.valueOf(updateAppt.getApptId()));
        updateApptTitleTxtFld.setText(updateAppt.getApptTitle());
        updateApptDescTxtFld.setText(updateAppt.getApptDesc());
        updateApptLocationComBx.setValue(updateAppt.getApptLocation());

        for (Contacts contacts : updateApptContactComBx.getItems()) {
            if (contacts.getContactId() == updateAppt.getContactId()) {
                updateApptContactComBx.getSelectionModel().select(contacts);
            }
        }

        updateApptStartDatePkr.setValue(LocalDate.from(updateAppt.getStart()));
        updateApptEndDatePkr.setValue(LocalDate.from(updateAppt.getEnd()));
        updateApptStartTimeComBx.setValue(LocalTime.from(updateAppt.getStart()));
        updateApptEndTimeComBx.setValue(LocalTime.from(updateAppt.getEnd()));
        updateApptTypeComBx.setValue(updateAppt.getApptType());
        updateApptCustIdComBx.setValue(new Customers(updateAppt.getApptCustomerId()));
        updateApptUserIdComBx.setValue(new Users (updateAppt.getApptUserId()));

    }

    public void onActionUpdateApptCustIdComBx(ActionEvent actionEvent) {
    }

    public void onActionupdateUserIdComBx(ActionEvent actionEvent) {
    }
}
