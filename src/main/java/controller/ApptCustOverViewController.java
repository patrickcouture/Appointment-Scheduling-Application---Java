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
import model.Appointments;
import model.Countries;
import model.Customers;
import model.First_Level_Divisions;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.ResourceBundle;

import static dbAccess.JDBC.connection;


/** ApptCustOverViewController constructor sets up the form elements and their attributes from the Appointment and Customer database tables.
 * Configures the database connection and populates the table views with the data from the database tables.
 * Also contains the event handlers for the button actions. */
public class ApptCustOverViewController implements Initializable {
    public Label appointmentsFormLabel;
    public RadioButton viewAllAppt;
    public ToggleGroup appointmentViews;
    public RadioButton viewCurrentWeekAppt;
    public RadioButton viewCurrentMonthAppt;
    public TableView appointmentTableView;
    public TableColumn apptTableApptIdCol;
    public TableColumn apptTableTitleCol;
    public TableColumn apptTableDescCol;
    public TableColumn apptTableLocationCol;
    public TableColumn apptTableContactCol;
    public TableColumn apptTableTypeCol;
    public TableColumn apptTableStartCol;
    public TableColumn apptTableEndCol;
    public TableColumn apptTableUserIdCol;
    public Button addApptBtn;
    public Button updateApptBtn;
    public Button deleteApptBtn;
    public Label customersLabel;
    public TextField searchCustomerTxtFld;
    public Button searchCustomerBtn;
    public TableView customerTableView;
    public TableColumn custTableCustIdCol;
    public TableColumn custTableNameCol;
    public TableColumn custTableAddressCol;
    public TableColumn custTablePhoneCol;
    public TableColumn custTableStateCol;
    public TableColumn custTablePostalCol;
    public Button addCustomerBtn;
    public Button updateCustomerBtn;
    public Button deleteCustomerBtn;
    public Button reportsBtn;
    public Button logoutBtn;
    public TableColumn apptTableCustIdCol;

    Stage stage;

    Parent scene;


    /** inititalize(URL url, ResourceBundle resourceBundle) fetches Customer and Appointment data from the database connection and
     *  sets up the TableViews and the TableColumns to display the fetched data. It also sorts the Appointment TableView by start date.
     * @param url
     * @param resourceBundle */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = JDBC.getConnection();
            ObservableList fillCustTable = dbCustomers.getAllCustomers(connection);


            custTableCustIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            custTableNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            custTableAddressCol.setCellValueFactory(new PropertyValueFactory<>("custAddress"));
            custTablePhoneCol.setCellValueFactory(new PropertyValueFactory<>("custPhone"));
            custTablePostalCol.setCellValueFactory(new PropertyValueFactory<>("custPostalCode"));
            custTableStateCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
            customerTableView.setItems(fillCustTable);

            ObservableList fillAllApptTable = dbAppointments.getAllAppointments(connection);

            apptTableApptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            apptTableTitleCol.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
            apptTableDescCol.setCellValueFactory(new PropertyValueFactory<>("apptDesc"));
            apptTableLocationCol.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
            apptTableContactCol.setCellValueFactory(new PropertyValueFactory<>("apptContact"));
            apptTableTypeCol.setCellValueFactory(new PropertyValueFactory<>("apptType"));
            apptTableStartCol.setCellValueFactory(new PropertyValueFactory<>("apptStart"));
            apptTableEndCol.setCellValueFactory(new PropertyValueFactory<>("apptEnd"));
            apptTableCustIdCol.setCellValueFactory(new PropertyValueFactory<>("apptCustomerId"));
            apptTableUserIdCol.setCellValueFactory(new PropertyValueFactory<>("apptUserId"));
            appointmentTableView.setItems(fillAllApptTable);

            // Sort table by start date/time
            appointmentTableView.getSortOrder().add(apptTableStartCol);
            apptTableStartCol.setSortType(TableColumn.SortType.ASCENDING);
            appointmentTableView.sort();





        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /** onActionViewAllAppt creates a tableview of all appointments from the database and displays it.
     * @param actionEvent */
    public void onActionViewAllAppt(ActionEvent actionEvent) throws SQLException {

        ObservableList<Appointments> allAppts = dbAppointments.getAllAppointments(connection);

        appointmentTableView.setItems(allAppts);
    }

    /** onActionViewCurrentWeekAppt creates a tableview of all appointments for the current week from the database and displays it.
     * @param actionEvent */
    public void onActionViewCurrentWeekAppt(ActionEvent actionEvent) throws SQLException {

        ObservableList <Appointments> currentWeekAppts = dbAppointments.getAllAppointments(connection).filtered(appointments ->
                appointments.getEnd().isAfter(LocalDateTime.now().minusWeeks(1)) &&
                        appointments.getEnd().isBefore(LocalDateTime.now().plusWeeks(2))
        );
        appointmentTableView.setItems(currentWeekAppts);
    }

    /** onActionViewCurrentMonthAppt creates a tableview of all appointments for the current month from the database and displays it.
     * @param actionEvent */
    public void onActionViewCurrentMonthAppt(ActionEvent actionEvent) throws SQLException {

        ObservableList <Appointments> currentMonthAppts = dbAppointments.getAllAppointments(connection).filtered(appointments ->
                appointments.getEnd().isAfter(LocalDateTime.now().minusMonths(1)) &&
                appointments.getEnd().isBefore(LocalDateTime.now().plusMonths(1)));
        appointmentTableView.setItems(currentMonthAppts);
    }

    /** onActionAddApptBtn loads the FXML loader and the AddApptForm.FXML to create a new appointment form.
     * @param actionEvent */
    public void onActionAddApptBtn(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/AddApptForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** onActionUpdateApptBtn receives the selected appointment from the current TableView and its index, fetches the UpdateApptForm.FXML
     *      and loads it to the scene for a user to update the appointment.
     * @param actionEvent */
    public void onActionUpdateApptBtn(ActionEvent actionEvent) throws IOException {

        try {
            if (appointmentTableView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No appointment selected");
                alert.setHeaderText("No appointment selected");
                alert.setContentText("Please select an appointment to update and try again.");
                alert.showAndWait();
            }

            if (appointmentTableView.getSelectionModel().getSelectedItem() != null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/UpdateApptForm.fxml"));
                loader.load();

                UpdateApptFormController UAFController = loader.getController();
                UAFController.receiveApptData((Appointments) appointmentTableView.getSelectionModel().getSelectedItem(), appointmentTableView.getSelectionModel().getSelectedIndex());


                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (NullPointerException | SQLException e) {
            e.printStackTrace();

        }
    }

    /** onActionDeleteApptBtn checks to see if an appointment is selected. If it is, it creates an alert for the user to confirm
     *      deleting the appointment selected. If the user confirms it, the appointment is deleted from the database.
     *      The lambda expression checks to see if the user pressed the "OK" ButtonType in the alert. If so, it deletes
     *      the appointment selected and confirms the delete.
     *
     *  @param actionEvent */
    public void onActionDeleteApptBtn(ActionEvent actionEvent) throws SQLException {
        Appointments apptSelected = (Appointments) appointmentTableView.getSelectionModel().getSelectedItem();

        if (apptSelected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("USER ERROR");
            alert.setContentText("You must select an Appointment to delete first.");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Is it your desire to delete this appointment");
            alert.setTitle("Delete Appointment?");
            Optional<ButtonType> userResult = alert.showAndWait();

            //Lambda expression
            userResult.ifPresent((ButtonType type) -> {
                if(type == ButtonType.OK) {
                    try{
                        dbAppointments.delete(apptSelected.getApptId());
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Appointment Deleted!");
                        confirm.setContentText("Appointment with ID of: " + apptSelected.getApptId() + " and of Type: " + apptSelected.getApptType() + " has been deleted.");
                        confirm.show();
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }
            });
            appointmentTableView.setItems(dbAppointments.getAllAppointments(connection));
        }
    }


    /** onActionAddCustomerBtn loads the AddCustomerForm.FXML. This is the form a user can use to add customers.
     * @param actionEvent */
    public void onActionAddCustomerBtn(ActionEvent actionEvent) throws IOException {

        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/AddCustomerForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Allows the user to update customer information by loading the corresponding form when the
     * update customer button is clicked
     * @param actionEvent a reference to a specific ActionEvent instance
     * @throws IOException if an I/O exception occurred */
    public void onActionUpdateCustomerBtn(ActionEvent actionEvent) throws IOException {

        try {
            if (customerTableView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No customer selected");
                alert.setHeaderText("No customer selected");
                alert.setContentText("Please select a customer to update and try again.");
                alert.showAndWait();
            }

            if (customerTableView.getSelectionModel().getSelectedItem() != null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/UpdateCustomerForm.fxml"));
                loader.load();

                UpdateCustomerFormController UCFController = loader.getController();
                UCFController.receiveCustomerData((Customers) customerTableView.getSelectionModel().getSelectedItem());

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (NullPointerException | SQLException e) {
            e.printStackTrace();

        }
    }

    /** Deletes a customer when the delete customer button is clicked
     * @param actionEvent ActionEvent
     * @throws SQLException an exception that provides information on a database access error or other errors */
    public void onActionDeleteCustomerBtn(ActionEvent actionEvent) throws SQLException {
        Customers customerSelect = (Customers) customerTableView.getSelectionModel().getSelectedItem();

        if (customerSelect != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Is it your desire to delete this Customer?");
            alert.setTitle("Delete Customer");
            Optional<ButtonType> userResult = alert.showAndWait();

            if (userResult.isPresent() && userResult.get() == ButtonType.OK) {
                int customerId = customerSelect.getCustomerId();
                dbAppointments.deleteAssocAppts(customerSelect.getCustomerId());
                dbCustomers.delete(customerSelect.getCustomerId());
                Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Customer: " + customerId + " has been deleted.");
                deleteAlert.setTitle("Customer Deleted");
                deleteAlert.showAndWait();

                customerTableView.setItems(dbCustomers.getAllCustomers(connection));
                appointmentTableView.setItems(dbAppointments.getAllAppointments(connection));
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("USER ERROR");
            alert.setContentText("You must select a customer to delete.");
            alert.show();
        }
    }

    /** Loads the reports form when the reports button is clicked
     * @param actionEvent
     * @throws IOException if an I/O exception occurred */
    public void onActionReportsBtn(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/ReportsForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Logs out of the user account when the logout button is clicked
     * @param actionEvent */
    public void onActionLogoutBtn(ActionEvent actionEvent) {
        JDBC.closeConnection();
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    /** Sends the user data to another form
     * @param username a reference to the user's username */
    public void sendUser(String username) {
    }
}
