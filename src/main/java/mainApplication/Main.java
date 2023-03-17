package mainApplication;

import dbAccess.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;


/** The Main class supplies the launch of the application. The main method opens a connection to the database, then
 * utilizes the launch method from Application to display the Log In Form using the FXMLLoader.
 *
 * @package mainApplication */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/LogInForm.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setTitle("Scheduler Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();

        launch(args);

    }
}