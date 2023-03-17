package controller;

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
import dbAccess.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static dbAccess.JDBC.connection;

/** AddCustomerFormController is a class that implements the Initializable interface. */
public class AddCustomerFormController implements Initializable {

    public Label addCustomerFormLabel;
    public Label addCustIdLabel;
    public TextField addCustIdTxtFld;
    public Label addCustNameLabel;
    public TextField addCustNameTxtFld;
    public Label addCustAddressLabel;
    public Label addCustPhoneLabel;
    public TextField addCustPhoneTxtFld;
    public Label addCustCountryLabel;
    public ComboBox<Countries> addCustCountryComBx;
    public Label addCustStateLabel;
    public ComboBox<First_Level_Divisions> addCustStateComBx;
    public Label addCustPostalLabel;
    public TextField addCustPostalTxtFld;
    public Button addCustSaveBtn;
    public Button addCustCancelBtn;
    public TextField addCustAddressTxtFld;
    Stage stage;
    
    Parent scene;

    /** Initialize the methods by connecting to the database and filling the country combo box.
     * @param url The URL associated with the combo box
     * @param resourceBundle The resource bundle for the combo box
     * @throws Exception Any exceptions thrown by the code */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Connection connection = JDBC.getConnection();
            ObservableList<Countries> fillCountryComboBx = dbCountries.getAllCountries(connection);
            ObservableList<First_Level_Divisions> allDivs = dbFirst_Level_Divisions.getAllFirstLevelDiv(connection);

            addCustCountryComBx.setItems(fillCountryComboBx);
            addCustCountryComBx.setPromptText("Select a Country");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /** This method sets the correct list of items in the addCustStateComBx when the Country is selected.
     * @param actionEvent Event raised when selecting a country in the combo box
     * @throws SQLException Any SQL Exceptions thrown by the code */
    public void onActionAddCustCountryComBx(ActionEvent actionEvent) throws SQLException {

        try {
            Countries countrySelected = addCustCountryComBx.getSelectionModel().getSelectedItem();

                ObservableList<Countries> fullCountryList = dbCountries.getAllCountries(connection);
                ObservableList<First_Level_Divisions> fullUSStateList = dbFirst_Level_Divisions.getUSStates(connection);
                ObservableList<First_Level_Divisions> fullCanadianProvList = dbFirst_Level_Divisions.getCanadianProv(connection);
                ObservableList<First_Level_Divisions> fullUKRegionList = dbFirst_Level_Divisions.getUKRegions(connection);

                String s = countrySelected.toString();
                switch (s) {
                    case "U.S" -> {
                        addCustStateComBx.setItems(fullUSStateList);
                    }
                    case "Canada" -> {
                        addCustStateComBx.setItems(fullCanadianProvList);
                    }
                    case "UK" -> {
                        addCustStateComBx.setItems(fullUKRegionList);
                    }
                }
            }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onActionAddCustStateComBx(ActionEvent actionEvent) {
    }

    /** This method handles the logic for adding a customer to the applicaiton. It inserts the data into the database.
     * If there is an error in one of the fields, it will show an error dialog. If the insertion is successful, the scene
     * is changed to ApptCustOverview.fxml
     *
     * @param actionEvent The event triggered by clicking the save button in the application
     * @throws IOException If the file the code is referencing cannot be found
     * @throws SQLException If there is an error with the SQL query */
    public void onActionAddCustSaveBtn(ActionEvent actionEvent) throws IOException, SQLException {

        try {
           int customerId = 0;
           String customerName = addCustNameTxtFld.getText();
           String custAddress = addCustAddressTxtFld.getText();
           String custPhone = addCustPhoneTxtFld.getText();
           String custPostalCode = addCustPostalTxtFld.getText();
           First_Level_Divisions divisionSelected = addCustStateComBx.getSelectionModel().getSelectedItem();
           int divisionId = 0;

            if (divisionSelected == null || divisionSelected.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Choose a State / Province / or Region");
                alert.setTitle("User Error");
                alert.show();
            } else if (customerName.equals("") || custAddress.equals("") || custPhone.equals("") || custPostalCode.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Fill in all the data fields");
                alert.setTitle("User Error");
                alert.show();
            } else {

                divisionId = divisionSelected.getDivisionId();

                Customers newCustomer = new Customers(customerId, customerName, custAddress, custPostalCode, custPhone, divisionId, divisionSelected.getDivisionName());
                dbCustomers.insert(newCustomer);

                stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/ApptCustOverView.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

        } catch (NullPointerException | NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Enter the correct data in each text field");
            alert.setTitle("User Error");
            alert.show();
        }

    }

    /** This method handles the canceling of the add customer action. When it is clicked it changes the scene to ApptCustOverview.fxml
     * @param actionEvent The event triggered by clicking the cancel button in the application
     * @throws IOException If the file the code is referencing cannot be found
     */
    public void onActionAddCustCancelBtn(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/ApptCustOverView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
