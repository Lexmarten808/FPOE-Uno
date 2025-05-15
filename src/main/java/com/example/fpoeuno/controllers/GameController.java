package com.example.fpoeuno.controllers;

import com.example.fpoeuno.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Objects;

/**
 * Controller for the main game view.
 * Manages user interactions related to game control elements, such as sound.
 */
public class GameController {

    @FXML
    private Label labelNickname;

    @FXML
    private ListView<Card> listViewHumanHand;

    @FXML
    private ListView<Card> listViewComputerHand;

    @FXML
    private ImageView imageViewTopCard;

    Game game = new Game();
    Deck deck = new Deck();
    private Player human;
    private Player computer;
    Card topCard;

    public void setHuman(Player human) {
        this.human = human;
        initializeHumanHandView();
        showLabelNickname();
    }

    public void setTopCard(Card card) {
        this.topCard = card;
        showImageViewTopCard(topCard);
    }

    private void showImageViewTopCard(Card topCard) {
        Image image = new Image(Objects.requireNonNull(getClass().getResource(
                "/org/example/unolast/" + topCard.getImagePath())).toExternalForm());
        imageViewTopCard.setImage(image);
    }

    public void setComputer(Player computer) {
        this.computer = computer;
        initializeComputerHandView();
    }
    private void initializeHumanHandView() {
        listViewHumanHand.setCellFactory(param -> {
            listViewHumanHand.setOrientation(Orientation.HORIZONTAL);

            ImageView imageView = new ImageView();
            imageView.setFitWidth(60);
            imageView.setFitHeight(90);

            ListCell<Card> cell = new ListCell<>() {
                @Override
                protected void updateItem(Card card, boolean empty) {
                    super.updateItem(card, empty);
                    if (empty || card == null) {
                        setGraphic(null);
                    } else {
                        String imagePath = "/org/example/unolast/" + card.getImagePath();
                        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                        imageView.setImage(image);
                        setGraphic(imageView);
                    }
                }
            };

            // 👉 when the mouse is clicked
            cell.setOnMouseClicked(event -> {
                Card selectedCard = cell.getItem();
                if (selectedCard != null && isPlayable(selectedCard)) {
                    //  Actualiza la carta superior
                    topCard = selectedCard;
                    setTopCard(topCard); // actualiza imageViewTopCard

                    //  Elimina la carta del modelo
                    human.removeCard(selectedCard);

                    //  Actualiza la vista del ListView
                    listViewHumanHand.getItems().setAll(human.getHand());
                } else {
                    System.out.println("Carta no jugable: " + selectedCard);
                }
            });

            return cell;
        });

        // Mostrar cartas del jugador al inicializar
        listViewHumanHand.getItems().setAll(human.getHand());
    }

    private void initializeComputerHandView() {
        listViewComputerHand.setOrientation(Orientation.HORIZONTAL);
        listViewComputerHand.setItems(computer.getHand());

        listViewComputerHand.setCellFactory(lv -> new ListCell<Card>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                if (empty || card == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    imageView.setPreserveRatio(true);

                    Image image = new Image(Objects.requireNonNull(getClass().getResource(
                            "/org/example/unolast/images/cards/card_uno.png")).toExternalForm());
                    imageView.setImage(image);

                    setText(null);
                    setGraphic(imageView);
                }
            }
        });
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void showLabelNickname() {
        labelNickname.setText(human.getNickname());
    }

    @FXML
    void onActionButtonPrint(ActionEvent event) throws IOException {
        deck.printDeck();
        System.out.println("***********human hand:");
        human.printHand();
        System.out.println("***********computer hand");
        computer.printHand();
    }

    private boolean isPlayable(Card selectedCard) {
        return selectedCard.getColor().equals(topCard.getColor()) ||
                selectedCard.getValue().equals(topCard.getValue()) ||
                selectedCard.getColor().equals("wild");
    }

}