package controller;

import dbAccess.JDBC;
import dbAccess.dbCountries;
import dbAccess.dbCustomers;
import dbAccess.dbFirst_Level_Divisions;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Countries;
import model.Customers;
import model.First_Level_Divisions;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static dbAccess.JDBC.connection;

/** UpdateCustomerFormController controls the updating customer form page which allows the user to update a customer's information. */
public class UpdateCustomerFormController implements Initializable {
    public Label updateCustomerFormLabel;
    public Label updateCustIdLabel;
    public TextField updateCustIdTxtFld;
    public Label updateCustNameLabel;
    public TextField updateCustNameTxtFld;
    public Label updateCustAddressLabel;
    public TextField updateCustAddressTxtFld;
    public Label updateCustPhoneLabel;
    public TextField updateCustPhoneTxtFld;
    public Label updateCustCountryLabel;
    public ComboBox<Countries> updateCustCountryComBx;
    public Label updateCustStateLabel;
    public ComboBox<First_Level_Divisions> updateCustStateComBx;
    public Label updateCustPostalLabel;
    public TextField updateCustPostalTxtFld;
    public Button updateCustSaveBtn;
    public Button updateCustCancelBtn;

    Stage stage;

    Parent scene;

    /** This function is run when the controller is initialized. It gets the database connection.
     * @param url The url of the page
     * @param resourceBundle The bundles with resources */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = JDBC.getConnection();

    }
    /** This function is activated when the customer's country is selected in the combobox. It will give the user different first level divisions
     * depending on the country selected.
     * @param actionEvent the action of the combobox being selected
     * @throws SQLException if an SQL error occurs */
    public void onActionUpdateCustCountryComBx(ActionEvent actionEvent) throws SQLException {

        ObservableList<Countries> fullCountryList = dbCountries.getAllCountries(connection);
        ObservableList<First_Level_Divisions> fullUSStateList = dbFirst_Level_Divisions.getUSStates(connection);
        ObservableList<First_Level_Divisions> fullCanadianProvList = dbFirst_Level_Divisions.getCanadianProv(connection);
        ObservableList<First_Level_Divisions> fullUKRegionList = dbFirst_Level_Divisions.getUKRegions(connection);

        Countries countrySelected = (Countries) updateCustCountryComBx.getSelectionModel().getSelectedItem();

        String s = countrySelected.toString();
        switch (s) {
            case "U.S" -> {
                updateCustStateComBx.setItems(fullUSStateList);


            }
            case "Canada" -> {
                updateCustStateComBx.setItems(fullCanadianProvList);

            }
            case "UK" -> {
                updateCustStateComBx.setItems(fullUKRegionList);

            }
        }
    }

    public void onActionUpdateCustStateComBx(ActionEvent actionEvent) {
    }

    /** This method saves the customer's information (name, address, phone number and postal code) on the database,
     * and verifies that the data has been inserted correctly.
     *
     * @param actionEvent An event to indicate that the "Save" button has been pressed
     * @throws IOException When there is an input/output error in reading/writing from/to a database
     * @throws SQLException When there is an error in executing an SQL database query */
    public void onActionUpdateCustSaveBtn(ActionEvent actionEvent) throws IOException, SQLException {
        try {
            int customerId = Integer.parseInt(updateCustIdTxtFld.getText());
            String customerName = updateCustNameTxtFld.getText();
            String custAddress = updateCustAddressTxtFld.getText();
            String custPhone = updateCustPhoneTxtFld.getText();
            String custPostalCode = updateCustPostalTxtFld.getText();
            First_Level_Divisions divisionSelected = (First_Level_Divisions) updateCustStateComBx.getSelectionModel().getSelectedItem();
            int divisionId = 0;

//            if (divisionSelected == null || divisionSelected.equals("")) {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setContentText("Choose a State / Province / or Region");
//                alert.setTitle("User Error");
//                alert.show();
            if (customerName.equals("") || custAddress.equals("") || custPhone.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Fill in all the data fields");
                alert.setTitle("User Error");
                alert.show();
            } else {

                divisionId = divisionSelected.getDivisionId();

                Customers updateCustomer = new Customers(customerId, customerName, custAddress, custPostalCode, custPhone, divisionId, divisionSelected.getDivisionName());
                dbCustomers.update(updateCustomer);

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/ApptCustOverView.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

        } catch (NullPointerException | NumberFormatException | SQLException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Enter the correct data in each text field");
            alert.setTitle("User Error");
            alert.show();
        }
    }

    /** This method closes the current window and loads the ApptCustOverView fxml.
     * @param actionEvent The action event of the cancel button.
     * @throws IOException If unable to locate or open the ApptCustOverView fxml. */
    public void onActionUpdateCustCancelBtn(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/ApptCustOverView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /** This method gets customer data from a Customer object and populates text fields and combobox selections.
     * @param updateCustomer A Customer object with the customer's data.
     * @throws SQLException if an error accesssing the database occurs. */
    public void receiveCustomerData (Customers updateCustomer) throws SQLException {

        ObservableList<Countries> fullCountryList = dbCountries.getAllCountries(connection);
        ObservableList<First_Level_Divisions> fullUSStateList = dbFirst_Level_Divisions.getUSStates(connection);
        ObservableList<First_Level_Divisions> fullCanadianProvList = dbFirst_Level_Divisions.getCanadianProv(connection);
        ObservableList<First_Level_Divisions> fullUKRegionList = dbFirst_Level_Divisions.getUKRegions(connection);

        updateCustIdTxtFld.setText(String.valueOf(updateCustomer.getCustomerId()));
        updateCustNameTxtFld.setText(updateCustomer.getCustomerName());
        updateCustAddressTxtFld.setText(updateCustomer.getCustAddress());
        updateCustPostalTxtFld.setText(updateCustomer.getCustPostalCode());
        updateCustPhoneTxtFld.setText(updateCustomer.getCustPhone());
        updateCustStateComBx.setValue(new First_Level_Divisions(updateCustomer.getDivisionId()));
        updateCustCountryComBx.setValue(new Countries (updateCustomer.getCountryId()));
        int divisionId = updateCustomer.getDivisionId();


        if (divisionId >= 1 && divisionId <= 54 ) {
            updateCustCountryComBx.setPromptText("U.S");
            updateCustCountryComBx.setItems(fullCountryList);
            updateCustStateComBx.setItems(fullUSStateList);

        }
        if (divisionId >= 60 && divisionId <= 72 ) {
            updateCustCountryComBx.setPromptText("Canada");
            updateCustCountryComBx.setItems(fullCountryList);
            updateCustStateComBx.setItems(fullCanadianProvList);
        }
        if (divisionId >= 101 && divisionId <= 104) {
            updateCustCountryComBx.setPromptText("UK");
            updateCustCountryComBx.setItems(fullCountryList);
            updateCustStateComBx.setItems(fullUKRegionList);
        }

        updateCustCountryComBx.setValue(fullCountryList.stream().filter(c -> c.getCountryId() == updateCustomer.getCountryId()).findFirst().orElse(null));
        updateCustStateComBx.setValue(fullUSStateList.stream().filter(s -> s.getDivisionId() == updateCustomer.getDivisionId()).findFirst().orElse(null));

    }

}
