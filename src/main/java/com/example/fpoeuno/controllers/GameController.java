package com.example.fpoeuno.controllers;

import com.example.fpoeuno.models.Card;
import com.example.fpoeuno.models.Game;
import com.example.fpoeuno.models.Player;
import com.example.fpoeuno.models.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Objects;

public class GameController {

    private Game game;
    private Player humanPlayer;

    @FXML
    private ImageView playerTurnFlag;

    @FXML
    private ImageView machineTurnFlag;

    @FXML
    private ImageView topCard;

    @FXML
    private Pane playerHandPane;

    @FXML
    private Pane machineHandPane;

    @FXML
    private Label humanName;

    public void setGame(Game game) {
        this.game = game;
        this.humanPlayer = game.getHuman();

        // Mostrar la carta superior del mazo
        Card firstCard = game.getDeck().getTopDiscard();
        if (firstCard != null && topCard != null) {
            String imagePath = "/com/example/fpoeuno/" + firstCard.getImageUrl();
            Image image = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
            topCard.setImage(image);
            topCard.setFitHeight(90);
            topCard.setFitWidth(58);
            topCard.setPreserveRatio(true);
        }

        // Mostrar manos y nombre
        showPlayerHand(humanPlayer.getHand());
        showMachineHand(game.getMachine().getHand());
        setPlayerName(humanPlayer.getName());

        game.printGameInfo();
    }

    public void setPlayerName(String name) {
        if (humanName != null) {
            humanName.setText(name);
        }
    }

    public void setPlayer(Player humanPlayer) {
        this.humanPlayer = humanPlayer;
        if (humanName != null) {
            humanName.setText(humanPlayer.getName());
        } else {
            humanName.setText("Jugador");
        }
    }

    public void showPlayerHand(List<Card> hand) {
        // No borrar todas las cartas, solo las que deben actualizarse
        double xOffset = 0;

        // Obtener la carta superior del mazo
        Card top = game.getDeck().getTopDiscard();

        // Iterar sobre las cartas de la mano y actualizarlas
        for (Card card : hand) {
            Image image = new Image(Objects.requireNonNull(getClass().getResource(
                    "/com/example/fpoeuno/" + card.getImageUrl())).toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(90);
            imageView.setFitWidth(58);
            imageView.setPreserveRatio(true);
            imageView.setCursor(Cursor.HAND);
            imageView.setLayoutX(xOffset);
            imageView.setLayoutY(0);

            // Evento de clic en la carta
            imageView.setOnMouseClicked(event -> {
                if (game.canPlayCard(card, top)) {
                    game.playCard(humanPlayer, card);  // Jugar la carta

                    // Actualizar la imagen de la carta superior
                    Image newTopImage = new Image(Objects.requireNonNull(getClass().getResource(
                            "/com/example/fpoeuno/" + card.getImageUrl())).toExternalForm());
                    topCard.setImage(newTopImage);

                    // Eliminar la carta jugada de la mano del jugador
                    humanPlayer.getHand().remove(card);

                    // No es necesario redibujar toda la mano, solo actualizar la vista
                    // Actualizamos solo la vista sin borrar todas las cartas
                    updatePlayerHand();
                } else {
                    System.out.println("Can't play card");
                }
            });

            // Añadir la carta a la mano visual
            playerHandPane.getChildren().add(imageView);
            xOffset += 63;  // Mantener el espaciado entre las cartas
        }
    }

    private void updatePlayerHand() {
        // Esta función actualiza la visualización de las cartas en mano
        playerHandPane.getChildren().clear();  // Limpiar la vista anterior

        double xOffset = 0;
        for (Card card : humanPlayer.getHand()) {
            Image image = new Image(Objects.requireNonNull(getClass().getResource(
                    "/com/example/fpoeuno/" + card.getImageUrl())).toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(90);
            imageView.setFitWidth(58);
            imageView.setPreserveRatio(true);
            imageView.setCursor(Cursor.HAND);
            imageView.setLayoutX(xOffset);
            imageView.setLayoutY(0);

            // Añadir el evento de clic
            imageView.setOnMouseClicked(event -> {
                if (game.canPlayCard(card, game.getDeck().getTopDiscard())) {
                    game.playCard(humanPlayer, card);
                    updatePlayerHand();  // Actualiza la mano después de jugar una carta
                }
            });

            // Añadir la carta a la mano visual
            playerHandPane.getChildren().add(imageView);
            xOffset += 63;  // Espacio entre cartas
        }
    }

    public void showMachineHand(List<Card> hand) {
        machineHandPane.getChildren().clear();  // Limpiar la mano de la máquina

        double xOffset = 0;  // Desplazamiento inicial de las cartas

        // Cargar la imagen de la parte trasera de la carta
        Image backImage = new Image(Objects.requireNonNull(getClass().getResource(
                "/com/example/fpoeuno/images/cards-uno/card_uno.png")).toExternalForm());

        for (Card card : hand) {
            ImageView imageView = new ImageView(backImage);  // Usar la imagen de la parte trasera

            imageView.setFitHeight(90);  // Ajustar el tamaño de la carta
            imageView.setFitWidth(58);
            imageView.setPreserveRatio(true);

            // Configurar la posición de la carta
            imageView.setLayoutX(xOffset);
            imageView.setLayoutY(0);  // Mantener en la misma posición vertical

            // Agregar la carta a la mano de la máquina
            machineHandPane.getChildren().add(imageView);

            xOffset += 30;  // Ajustar el desplazamiento para la siguiente carta
        }
    }

    @FXML
    void onActionButtonSound(ActionEvent event) {
        SoundManager.toggleMusic("music.mp3");
    }

}