module com.example.fpoeuno {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fpoeuno to javafx.fxml;
    exports com.example.fpoeuno;
    exports com.example.fpoeuno.controllers;
    opens com.example.fpoeuno.controllers to javafx.fxml;
}