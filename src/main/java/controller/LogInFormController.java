package controller;

import dbAccess.JDBC;
import dbAccess.dbUsers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Users;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

/** LogInFormController is  a class that creates the LogInForm view controller to allow users to log into their accounts. */
public class LogInFormController implements Initializable {

    public Label logInLabel;
    public Label usernameLabel;
    public TextField usernameTxtFld;
    public Label passwordLabel;
    public PasswordField passwordTxtFld;
    public Button loginBtn;
    public Button cancelBtn;
    public Label locationLabel;
    @FXML
    Stage stage;
    Parent scene;


    ResourceBundle rb = ResourceBundle.getBundle("Bundle/Nat");


    /** This method is used to initialize the elements in the scene graph for internationalization.
     *  @param url url
     *  @param resourceBundle resource bundle */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logInLabel.setText(rb.getString("logInLabel"));
        usernameLabel.setText(rb.getString("usernameLabel"));
        passwordLabel.setText(rb.getString("passwordLabel"));
        loginBtn.setText(rb.getString("loginBtn"));
        cancelBtn.setText(rb.getString("cancelBtn"));
        locationLabel.setText(rb.getString("locationLabel") + " " + ZoneId.systemDefault());
    }

    /** This method attempts to log the user in with the username and password, and records the timestamp of the attempt. The database is
     * queried to check the user's login information, and if correct, an appointment alert is displayed if there
     * is a valid appointment in the next 15 minutes, otherwise is taken to the appointment customer overview page. If the login
     * is unsuccessful, an error alert is displayed.
     * The login attempt as well as the user login activity is written to an output file.
     *
     * @param actionEvent The action event triggering the login attempt.
     * @throws IOException If there is an issues writing to the file. */
    public void onActionLoginBtn(ActionEvent actionEvent) throws IOException{
        try {
            String username = usernameTxtFld.getText();
            String password = passwordTxtFld.getText();
            ZonedDateTime attemptTimeStamp = ZonedDateTime.now();

            Connection connection = JDBC.getConnection();

            Statement userQuery = connection.createStatement();

            FileWriter loginAttempt = new FileWriter("user_login_activity.txt", true);
            PrintWriter toFile = new PrintWriter(new FileOutputStream(new File("user_login_activity.txt"), true));

            ResultSet resultSet = userQuery.executeQuery("SELECT * FROM users WHERE User_Name = '" + username + "' AND Password = '" + password + "'");

            if(resultSet.next()) {

                for (Users user : dbUsers.getAllUsers(connection)) {
                    if (user.getUsername().equals(username)) {
                        int userId = user.getUserId();

                        String apptFifteen = "SELECT * FROM appointments WHERE User_ID = ? AND Start BETWEEN CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP + INTERVAL 15 MINUTE";
                        PreparedStatement pState = JDBC.connection.prepareStatement(apptFifteen);
                        pState.setInt(1, userId);
                        ResultSet result = pState.executeQuery();

                        if (result.next()) {

                            int appointmentId = result.getInt("Appointment_ID");
                            Timestamp timeStart = result.getTimestamp("Start");
                            Alert apptComingUp = new Alert(Alert.AlertType.INFORMATION);
                            String apptComingUpTxt = rb.getString("apptComingUpTxt");
                            String apptComingUpTitle = rb.getString("apptComingUpTitle");
                            apptComingUp.setTitle(apptComingUpTitle);
                            apptComingUp.setContentText(apptComingUpTxt + " #" + appointmentId + " at" + timeStart);
                            apptComingUp.showAndWait();
                        } else {

                            String nullApptsTxt = rb.getString("nullApptsTxt");
                            String nullApptsTitle = rb.getString("nullApptsTitle");
                            Alert nullAppts = new Alert(Alert.AlertType.CONFIRMATION);
                            nullAppts.setTitle(nullApptsTitle);
                            nullAppts.setContentText(nullApptsTxt);
                            nullAppts.showAndWait();

                            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                            stage.hide();
                        }

                        toFile.append(username + " made a successful login at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
                        toFile.close();

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/ApptCustOverView.fxml"));
                        loader.load();
                        ApptCustOverViewController apptCustOverViewController = loader.getController();
                        apptCustOverViewController.sendUser(username);

                        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                        Parent scene = loader.getRoot();
                        stage.setScene(new Scene(scene));
                        stage.show();

                    }
                }
            } else {

                String loginFailed = rb.getString("loginFailed");

                toFile.append(username + "made a failed login attempt at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
                toFile.close();
                Alert badLogin = new Alert(Alert.AlertType.ERROR);
                badLogin.setTitle("Error");
                badLogin.setContentText(loginFailed);
                badLogin.show();
                usernameTxtFld.setText("");
                passwordTxtFld.setText("");

            }
        } catch (SQLException | NullPointerException | IOException e) {
            e.printStackTrace();
        }

    }

    /** This method is activated when the user clicks the "Cancel" button.
     * It closes the current connection to the JDBC, and closes the current window.
     *
     * @param actionEvent action of clicking the cancel button */
    public void onActionCancelBtn(ActionEvent actionEvent) {
        JDBC.closeConnection();
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

}