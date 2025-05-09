package com.example.fpoeuno.models.alerts;

import javafx.scene.control.Alert;

public class AlertInformation implements IAlert {
    @Override
    public void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.getDialogPane().setMinWidth(500);

        alert.showAndWait();
    }
}