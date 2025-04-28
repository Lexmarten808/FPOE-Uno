module com.example.fpoeuno {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens com.example.fpoeuno to javafx.fxml;

    exports com.example.fpoeuno.controllers;
    opens com.example.fpoeuno.controllers to javafx.fxml;
    exports com.example.fpoeuno.views;
    opens com.example.fpoeuno.views to javafx.fxml;
}