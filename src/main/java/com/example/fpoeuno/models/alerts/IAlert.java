package com.example.fpoeuno.models.alerts;

import javafx.scene.control.Alert;

public interface IAlert {
    void showAlert(Alert.AlertType type, String title, String header, String content);
}