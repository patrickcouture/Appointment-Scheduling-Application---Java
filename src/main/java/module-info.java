module pcouture.software {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens mainApplication to javafx.fxml, javafx.graphics;
    exports mainApplication;
    exports controller;
    opens controller to javafx.fxml;
    exports model;
    opens model to javafx.fxml;
    exports dbAccess;
    opens dbAccess to javafx.fxml;

}