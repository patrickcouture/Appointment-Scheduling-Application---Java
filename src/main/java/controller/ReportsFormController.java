package controller;

import dbAccess.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import model.Types;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static dbAccess.JDBC.connection;

/** The ReportsFormController class provides the logic for the controls in the ReportsForm. */
public class ReportsFormController implements Initializable {
    public Label reportsFormLabel;
    public Label apptsByTypeMonthReportLabel;
    public Label typeMonthTypeLabel;
    public ComboBox<String> typeMonthTypeComBx;
    public Label typeMonthLabel;
    public ComboBox<String> typeMonthComBx;
    public Label totalAppointmentsLabel;
    public Label customersByStateLabel;
    public Label custByStateCountryLabel;
    public ComboBox<Countries> custByStateCountryComBx;
    public Label custByStateProvLabel;
    public ComboBox custByStateProvComBox;
    public Label totalCustomersLabel;
    public Label contactScheduleReportLabel;
    public Label chooseContactLabel;
    public ComboBox chooseContactComBx;
    public TableView contactScheduleTableView;
    public TableColumn contactScheduleApptIdCol;
    public TableColumn contactScheduleTitleCol;
    public TableColumn contactScheduleTypeCol;
    public TableColumn contactScheduleDescCol;
    public TableColumn contactScheduleStartCol;
    public TableColumn contactScheduleEndCol;
    public TableColumn contactScheduleCustIdCol;
    public Button reportsPreviousBtn;
    public Button reportsLogoutBtn;
    public Label totalCustomersResultTxt;
    public Label totalApptsResultTxt;

    Stage stage;
    Parent scene;

    /** This method handles the initializing of the ReportsForm
     * @param url URL of the resource
     * @param resourceBundle language/country bundle */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Connection connection = JDBC.getConnection();

            ObservableList<String> fillTypeBx = Types.fillTypeComBx(connection);
            typeMonthTypeComBx.setItems(fillTypeBx);
            typeMonthTypeComBx.setPromptText("Select Appt Type");

            ObservableList<String> fillMonthBx = Times.fillMonthsBx(connection);
            typeMonthComBx.setItems(fillMonthBx);
            typeMonthComBx.setPromptText("Select a Month");

            ObservableList<Countries> fillCountryComboBx = dbCountries.getAllCountries(connection);
            custByStateCountryComBx.setItems(fillCountryComboBx);
            custByStateCountryComBx.setPromptText("Select a Country");

            ObservableList<Contacts> fillContactComboBx = dbContacts.getAllContacts(connection);
            chooseContactComBx.setItems(fillContactComboBx);
            chooseContactComBx.setPromptText("Select a Contact");

        } catch (Exception exception){
            exception.printStackTrace();
        }
    }

    /** Handler for the actionTypeMonthTypeComBx.
     * @param actionEvent - The ActionEvent object. */
    public void onActionTypeMonthTypeComBx(ActionEvent actionEvent) {
    }

    /** Handler for the actionTypeMonthComBx.
     * @param actionEvent - The ActionEvent object. */
    public void onActionTypeMonthComBx(ActionEvent actionEvent) {

    }

    /** Handler for the actionCustByStateCountryComBx.
     * Populates the custByStateProvComBox based on the Country selection.
     * @param actionEvent - The ActionEvent object. */
    public void onActionCustByStateCountryComBx(ActionEvent actionEvent) {
        try {
            Countries countrySelected = custByStateCountryComBx.getSelectionModel().getSelectedItem();

            ObservableList<Countries> fullCountryList = dbCountries.getAllCountries(connection);
            ObservableList<First_Level_Divisions> fullUSStateList = dbFirst_Level_Divisions.getUSStates(connection);
            ObservableList<First_Level_Divisions> fullCanadianProvList = dbFirst_Level_Divisions.getCanadianProv(connection);
            ObservableList<First_Level_Divisions> fullUKRegionList = dbFirst_Level_Divisions.getUKRegions(connection);


            String s = countrySelected.toString();
            switch (s) {
                case "U.S" -> {
                    custByStateProvComBox.setItems(fullUSStateList);


                }
                case "Canada" -> {
                    custByStateProvComBox.setItems(fullCanadianProvList);

                }
                case "UK" -> {
                    custByStateProvComBox.setItems(fullUKRegionList);

                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Handler for the actionCustByStateProvComBox.
     * @param actionEvent - The ActionEvent object. */
    public void onActionCustByStateProvComBox(ActionEvent actionEvent) {
    }

    /** Handler for the actionchooseContactComBx.
     * @param actionEvent - The ActionEvent object. */
    public void onActionchooseContactComBx(ActionEvent actionEvent) {
    }

    /** Handler for the actionReportsPreviousBtn.
     * @param actionEvent - The ActionEvent object.
     * @throws IOException - This exception is thrown when an IO error occurs. */
    public void onActionReportsPreviousBtn(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/ApptCustOverView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Handler for the actionReportsLogoutBtn.
     * Closes the current window and the connection to the Database.
     * @param actionEvent - The ActionEvent object. */
    public void onActionReportsLogoutBtn(ActionEvent actionEvent) {
        JDBC.closeConnection();
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }


    /** This method onActionCreateApptTypeMonth() creates an action listener associated with the first comboBox (typeMonthTypeComBx), and an associated second comboBox (typeMonthComBx).
     * If both these comboBoxes return valid data, the method executes a query string which counts all appointments with the type and month provided. The resulting value is
     * set as text in a text field - totalApptsResultTxt.
     * The purpose of the Lambda expression in the code above is to determine if the ResultSet rs contains any records and, if so,
     * assign the value of the record to resultText; if not, assign the value "No results found" to resultText.
     * If either comboBox returns null, an alert is shown and resultText is set to ""
     * @param actionEvent
     * @throws SQLException */
   public void onActionCreateApptTypeMonth(ActionEvent actionEvent) throws SQLException {
       String typeChosen = typeMonthTypeComBx.getValue();
       String monthChosen = typeMonthComBx.getValue();
       String query = "SELECT Count(*) AS TOTAL FROM appointments WHERE type = ? and monthname(Start) = ?";
       String resultText;

       if (typeChosen != null && monthChosen != null && !typeChosen.isEmpty() && !monthChosen.isEmpty()) {
           PreparedStatement ps = JDBC.connection.prepareStatement(query);
           ps.setString(1, typeChosen);
           ps.setString(2, monthChosen);
           ResultSet rs = ps.executeQuery();
           //Lambda expression
           resultText = rs.next() ? String.valueOf(rs.getInt("TOTAL")) : "No results found";
       }
       else {
           Alert comboalert = new Alert(Alert.AlertType.WARNING);
           comboalert.setTitle("ERROR");
           comboalert.setContentText("Please select a type and month");
           comboalert.show();
           resultText = "";
       }

       totalApptsResultTxt.setText(resultText);

   }

    /** This method onActionCustByState() creates an action listener associated with a comboBox - custByStateProvComBox.
     * If a valid item is selected, this will return the total number of customers based on the first level division. This value is displayed as a label.
     * @param actionEvent */
    public void onActionCustByState(ActionEvent actionEvent) {
        try {

            First_Level_Divisions divisionSelected = (First_Level_Divisions) custByStateProvComBox.getSelectionModel().getSelectedItem();
            int divisionId = 0;
            divisionId = divisionSelected.getDivisionId();


            String query = "SELECT COUNT(Customer_ID) AS TOTAL FROM customers WHERE Division_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(query);

            ps.setInt(1, divisionId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            totalCustomersResultTxt.setText(String.valueOf(count));


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /** This method getApptsByContact() takes a Connection object and creates an action listener associated with a comboBox - chooseContactComBx.
     * When a valid contact is selected, this query returns all appointments associated with that contact.
     * @param  connection connects to the database
     * @return ObservableList of Appointments associated with the selected Contact
     * @throws SQLException
     */
    public ObservableList<Appointments> getApptsByContact(Connection connection) throws SQLException {
        Contacts chosenContact = (Contacts) chooseContactComBx.getSelectionModel().getSelectedItem();

        if (chosenContact == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Contact Selected");
            alert.setHeaderText("No Contact Selected");
            alert.setContentText("Please select a contact from the list to view their schedule.");
            alert.showAndWait();


        }else {
            String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
            preparedStatement.setInt(1, chosenContact.getContactId());
            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<Appointments> apptByContact = FXCollections.observableArrayList();

            while (resultSet.next()) {

                int apptId = resultSet.getInt("Appointment_ID");
                String apptTitle = resultSet.getString("Title");
                String apptDesc = resultSet.getString("Description");
                String apptLocation = resultSet.getString("Location");
                int apptContactId = resultSet.getInt("Contact_ID");
                String apptType = resultSet.getString("Type");
//                    Timestamp tsStart = resultSet.getTimestamp("Start");
//                    LocalDateTime apptStart = tsStart.toLocalDateTime();
                LocalDateTime apptStart = Timestamp.valueOf(resultSet.getTimestamp("Start").toLocalDateTime()).toLocalDateTime();
                LocalDateTime apptEnd = Timestamp.valueOf(resultSet.getTimestamp("End").toLocalDateTime()).toLocalDateTime();
                int apptCustomerId = resultSet.getInt("Customer_ID");
                int apptUserId = resultSet.getInt("User_ID");

                Appointments contactAppt = new Appointments(apptId, apptTitle, apptDesc, apptLocation,
                        apptType, apptStart, apptEnd, apptCustomerId, apptUserId, apptContactId);
                apptByContact.add(contactAppt);

            }
            return apptByContact;
        }

        return null;
    }


    /** This method onActionViewContactSchedule() takes the results from method getApptsByContact(), and sets them as the items in a TableView - contactScheduleTableView.
     * The items are set to corresponding TableColumns to populate the TableView.
     * @param actionEvent
     * @throws SQLException */
    public void onActionViewContactSchedule(ActionEvent actionEvent) throws SQLException {



        ObservableList<Appointments> apptByContact = getApptsByContact(connection);

        contactScheduleApptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        contactScheduleTitleCol.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        contactScheduleTypeCol.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        contactScheduleDescCol.setCellValueFactory(new PropertyValueFactory<>("apptDesc"));
        contactScheduleStartCol.setCellValueFactory(new PropertyValueFactory<>("apptStart"));
        contactScheduleEndCol.setCellValueFactory(new PropertyValueFactory<>("apptEnd"));
        contactScheduleCustIdCol.setCellValueFactory(new PropertyValueFactory<>("apptCustomerId"));
        contactScheduleTableView.setItems(apptByContact);

    }
}
