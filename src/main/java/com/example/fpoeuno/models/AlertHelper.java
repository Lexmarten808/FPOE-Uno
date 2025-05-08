package com.example.fpoeuno.models;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;

import java.util.Optional;

/**
 * Utility class responsible for displaying different types of alerts
 * (confirmation, information, error, and warning) in the user interface.
 * <p>
 *     This class centralizes alert dialogs for consistent UI messaging.
 * </p>
 *
 * @author Brandon Lasprilla Aristizabal
 * @author Juan Miguel Manjarrez Zuluaga
 */
public class AlertHelper {

    /**
     * Displays a confirmation alert with "Iniciar" and "Cancelar" buttons.
     *
     * @param title   the title of the alert window
     * @param message the message shown in the alert content
     * return {@code true} if the user clicks "Iniciar", {@code false} otherwise
     */
    public static boolean showConfirmationAlert(String title, String message) {
        // Custom buttons
        ButtonType yesButton = new ButtonType("Iniciar");
        ButtonType noButton = new ButtonType("Cancelar");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, yesButton, noButton);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

    /**
     * Displays an informational alert with a custom header and message.
     *
     * @param title   the title of the alert window
     * @param header  the header text of the alert
     * @param message the message shown in the alert content
     */
    public static void showInfoAlert(String title,String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        dialogPane.setMinWidth(520);

        alert.showAndWait();
    }

    /**
     * Displays an error alert with a custom header and message.
     *
     * @param title   the title of the alert window
     * @param header  the header text of the alert
     * @param message the message displayed in the alert content
     */
    public static void showErrorAlert(String title,String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a warning alert with a message and no header.
     *
     * @param title   the title of the alert window
     * @param message the message displayed in the alert content
     */
    public static void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}