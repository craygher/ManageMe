module com.example.manageme {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires jbcrypt;
    requires javafx.graphics;
    requires javafx.base;

    opens com.example.manageme to javafx.fxml;
    exports com.example.manageme;
}