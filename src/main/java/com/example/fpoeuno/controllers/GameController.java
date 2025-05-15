package com.example.fpoeuno.controllers;

import com.example.fpoeuno.models.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Objects;

import java.io.IOException;

public class GameController {

    @FXML
    private Label labelNickname;

    @FXML
    private ListView<Card> listViewHumanHand;

    @FXML
    private ListView<Card> listViewComputerHand;

    @FXML
    private ImageView imageViewTopCard;

    @FXML
    private ImageView imageViewHumanFlag;

    @FXML
    private ImageView imageViewComputerFlag;

    Game game = new Game();
    Deck deck = new Deck();
    private Player human;
    private Player computer;
    Card topCard;
    private Thread computerTurnThread;
    private boolean computerCanPlay = true;

    public void initializeComputerTurnThread() {
        computerTurnThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(5000); // Espera 5 segundos

                    if ("Computer".equals(game.getCurrentTurn()) && computerCanPlay) {
                        computerCanPlay = false;
                        Platform.runLater(() -> {
                            playComputerTurn();
                            changeTurn();
                            computerCanPlay = true;
                        });
                    } else {
                        System.out.println("No es el turno de la computadora.");
                    }
                } catch (InterruptedException ex) {
                    System.out.println("Hilo de la computadora interrumpido.");
                    Thread.currentThread().interrupt();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        computerTurnThread.setDaemon(true); // Se detiene con la aplicación
        computerTurnThread.start();
    }

    private void updateComputerView() {
        listViewComputerHand.getItems().setAll(computer.getHand());
    }

    private void updateHumanView() {
        listViewHumanHand.getItems().setAll(human.getHand());
    }

    private void playComputerTurn() {
        System.out.println("La computadora juega su turno...");

        boolean played = false;

        System.out.println("***********computer hand: antes de jugar");
        computer.printHand();

        for (Card selectedCard : new ArrayList<>(computer.getHand())) {
            if (selectedCard != null && isPlayable(selectedCard)) {
                topCard = selectedCard; //  Actualiza la carta superior
                setTopCard(topCard); // actualiza imageViewTopCard
                computer.removeCard(selectedCard); //  Elimina la carta del modelo
                deck.discardCard(selectedCard); // Agregamos la carta jugada a la pila de descarte
                played = true;
                break;
            }
        }

        if (!played) {
            Card draw = deck.drawCard();
            computer.addCard(draw);
        }

        System.out.println("***********computer hand: después de jugar");
        computer.printHand();
    }

    public void stopComputerTurnThread() {
        if (computerTurnThread != null && computerTurnThread.isAlive()) {
            computerTurnThread.interrupt();
        }
    }

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
                "/com/example/fpoeuno/" + topCard.getImagePath())).toExternalForm());
        imageViewTopCard.setImage(image);
    }

    public void setComputer(Player computer) {
        this.computer = computer;
        initializeComputerTurnThread();
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
                        String imagePath = "/com/example/fpoeuno/" + card.getImagePath();
                        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                        imageView.setImage(image);
                        setGraphic(imageView);
                    }
                }
            };

            // When the mouse is clicked
            cell.setOnMouseClicked(event -> {
                Card selectedCard = cell.getItem();
                if (Objects.equals(game.getCurrentTurn(), "Human")) {
                    if (selectedCard != null && isPlayable(selectedCard)) {
                        topCard = selectedCard; //  Actualiza la carta superior
                        setTopCard(topCard); // actualiza imageViewTopCard
                        human.removeCard(selectedCard); //  Elimina la carta del modelo
                        deck.discardCard(selectedCard); // Agregamos la carta jugada a la pila de descarte
                        listViewHumanHand.getItems().setAll(human.getHand()); //  Actualiza la vista del ListView
                        changeTurn();
                    } else if (selectedCard == null) {
                        System.out.println("No es una carta");
                    } else {
                        System.out.println("Carta no jugable: " + selectedCard);
                    }
                } else {
                    System.out.println("Es el turno de la máquina, no puedes jugar...");
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
                            "/com/example/fpoeuno/images/cards/card_uno.png")).toExternalForm());
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

    public void changeTurn() {
        String currentTurn = game.getCurrentTurn();
        if (Objects.equals(currentTurn, "Human")) {
            game.setCurrentTurn("Computer");
            imageViewHumanFlag.setVisible(false);
            imageViewComputerFlag.setVisible(true);
        } else {
            game.setCurrentTurn("Human");
            imageViewComputerFlag.setVisible(false);
            imageViewHumanFlag.setVisible(true);
        }
    }

    @FXML
    void onActionButtonRobar(ActionEvent event) {
        String text = "";
        if (Objects.equals(game.getCurrentTurn(), "Human")) {
            for (Card card : human.getHand()) {
                if (isPlayable(card)) {
                    text = "Puedes jugar una carta, no puedes robar...";
                }
            }
            System.out.println(text);
            if (text.isEmpty()) { // No tiene cartas para jugar...
                Card draw = deck.drawCard();
                human.addCard(draw);
                changeTurn();
                listViewHumanHand.getItems().setAll(human.getHand()); // Actualiza la vista del ListView
            }
        } else {
            System.out.println("Es el turno de la máquina, no puedes robar...");
        }
    }

    @FXML
    void onActionButtonPrint(ActionEvent event) throws IOException {
        deck.printDeck();
        System.out.println("***********human hand:");
        human.printHand();
        System.out.println("***********computer hand:");
        computer.printHand();
    }

    private boolean isPlayable(Card selectedCard) {
        return selectedCard.getColor().equals(topCard.getColor()) ||
                selectedCard.getValue().equals(topCard.getValue()) ||
                selectedCard.getColor().equals("wild");
    }

    @FXML
    void onActionButtonAyuda(ActionEvent event) {
        AlertHelper.showInfoAlert(
                "Reglas",
                "Reglas de UNO",
                "-1. Cada jugador comienza con 5 cartas.\n" +
                        "-2. La partida inicia con una carta aleatoria en la mesa.\n" +
                        "-3. Juega una carta que coincida en color o número.\n" +
                        "-4. Los comodines pueden jugarse en cualquier momento.\n" +
                        "-5. Presiona el botón UNO antes de jugar tu última carta.\n" +
                        "-6. Si no lo haces, tendrás 2–3 segundos o serás penalizado con una carta."
        );
    }

    @FXML
    void onActionButtonSound(ActionEvent event) { SoundManager.toggleMusic("music.mp3"); }

}