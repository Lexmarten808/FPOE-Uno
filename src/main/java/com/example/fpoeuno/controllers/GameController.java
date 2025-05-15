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
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Objects;

import java.io.IOException;

public class GameController {

    // ===== FXML COMPONENTES =====

    @FXML private AnchorPane colorSelectionBox;
    @FXML private Label labelNickname;
    @FXML private ListView<Card> listViewHumanHand;
    @FXML private ListView<Card> listViewComputerHand;
    @FXML private ImageView imageViewTopCard;
    @FXML private ImageView imageViewHumanFlag;
    @FXML private ImageView imageViewComputerFlag;

    // ===== MODELOS ======

    private Game game = new Game();
    private Deck deck = new Deck();
    private Player human;
    private Player computer;
    private Card topCard;
    private Card pendingWildCard;
    private Thread computerTurnThread;
    private boolean computerCanPlay = true;

    // ===== SETTERS PRINCIPALES =====

    public void setHuman(Player human) {
        this.human = human;
        initializeHumanHandView();
        showLabelNickname();
    }

    public void setComputer(Player computer) {
        this.computer = computer;
        initializeComputerTurnThread();
        initializeComputerHandView();
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setTopCard(Card card) {
        this.topCard = card;
        showImageViewTopCard(topCard);
        establishGameValues(topCard);
    }

    // ===== INICIALIZADORES =====

    private void initializeHumanHandView() {
        listViewHumanHand.setOrientation(Orientation.HORIZONTAL);
        listViewHumanHand.setCellFactory(param -> createHumanCardCell());
        listViewHumanHand.getItems().setAll(human.getHand());
    }

    private void initializeComputerHandView() {
        listViewComputerHand.setOrientation(Orientation.HORIZONTAL);
        listViewComputerHand.setItems(computer.getHand());

        listViewComputerHand.setCellFactory(lv -> new ListCell<Card>() {
            private final ImageView imageView = createCardImageView();

            @Override
            protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                setGraphic(empty || card == null ? null : imageView);
                if (!empty && card != null) {
                    imageView.setImage(loadImage("/com/example/fpoeuno/images/cards/card_uno.png"));
                }
            }
        });
    }

    // ===== EVENTOS PRINCIPALES =====

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
                refreshHumanHand();
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
        System.out.println("***********computer hand");
        computer.printHand();
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
    void onActionButtonSound(ActionEvent event) {
        SoundManager.toggleMusic("music.mp3");
    }

    // ===== SELECCIÓN DE COLOR =====

    @FXML
    void onActionButtonColorYellow(ActionEvent event) {
        applyWildColor("yellow");
    }

    @FXML
    void onActionButtonColorBlue(ActionEvent event) {
        applyWildColor("blue");
    }

    @FXML
    void onActionButtonColorRed(ActionEvent event) {
        applyWildColor("red");
    }

    @FXML
    void onActionButtonColorGreen(ActionEvent event) {
        applyWildColor("green");
    }

    private void applyWildColor(String color) {
        if (pendingWildCard == null) return;

        pendingWildCard.setColor(color);
        setTopCard(pendingWildCard);
        colorSelectionBox.setVisible(false);

        String value = pendingWildCard.getValue();
        Player target = game.getCurrentTurn().equals("Human") ? computer : human;

        if (value.equals("wild_draw_2")) {
            target.addCard(deck.drawCard());
            target.addCard(deck.drawCard());
        } else if (value.equals("wild_draw_4")) {
            for (int i = 0; i < 4; i++) target.addCard(deck.drawCard());
        }

        refreshBothHands();
        pendingWildCard = null;
        changeTurn();
    }

    // ===== LÓGICA DE JUEGO =====

    private void handleHumanCardPlay(Card selectedCard) {
        if (!isHumanTurn()) {
            System.out.println("Es el turno de la máquina, no puedes jugar...");
            return;
        }

        if (selectedCard == null) {
            System.out.println("No es una carta válida.");
            return;
        }

        if (!isPlayable(selectedCard)) {
            System.out.println("Carta no jugable: " + selectedCard);
            return;
        }

        topCard = selectedCard;
        setTopCard(topCard);
        handleWildCardLogic(selectedCard);
        human.removeCard(selectedCard);
        deck.discardCard(selectedCard);
        refreshHumanHand();
        changeTurn();
    }

    private void handleWildCardLogic(Card card) {
        String value = card.getValue();
        String color = card.getColor();
        String currentTurn = game.getCurrentTurn();

        if (value.equals("skip")) {
            changeTurn();
            return;
        }

        if (color.equals("wild")) {
            colorSelectionBox.setVisible(true);
            pendingWildCard = card;
            return;
        }

        if (value.equals("wild_draw_2")) {
            Player target = currentTurn.equals("Human") ? computer : human;
            target.addCard(deck.drawCard());
            target.addCard(deck.drawCard());
        }
    }

    private void machineLogic() {
        if (!game.getCurrentTurn().equals("Computer")) return;

        for (Card card : computer.getHand()) {
            if (isPlayable(card)) {
                topCard = card;
                setTopCard(topCard);
                handleWildCardLogic(card);
                computer.removeCard(card);
                deck.discardCard(card);
                refreshComputerHand();
                changeTurn();
                return;
            }
        }

        computer.addCard(deck.drawCard());
        refreshComputerHand();
        changeTurn();
    }

    // ===== MACHINE THREAD =====

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

    // ===== UTILIDADES =====

    private boolean isPlayable(Card selectedCard) {
        return selectedCard.getColor().equals(game.getEstablishedColor()) ||
                selectedCard.getValue().equals(game.getEstablishedValue()) ||
                selectedCard.getColor().equals("wild");
    }

    private boolean isHumanTurn() {
        return game.getCurrentTurn().equals("Human");
    }

    public void changeTurn() {
        boolean isHumanTurn = game.getCurrentTurn().equals("Human");
        game.setCurrentTurn(isHumanTurn ? "Computer" : "Human");
        imageViewHumanFlag.setVisible(!isHumanTurn);
        imageViewComputerFlag.setVisible(isHumanTurn);
    }

    private void refreshHumanHand() {
        listViewHumanHand.getItems().setAll(human.getHand());
    }

    private void refreshComputerHand() {
        listViewComputerHand.getItems().setAll(computer.getHand());
    }

    private void refreshBothHands() {
        listViewHumanHand.getItems().setAll(human.getHand());
        listViewComputerHand.getItems().setAll(computer.getHand());
    }

    private void showLabelNickname() {
        labelNickname.setText(human.getNickname());
    }

    private void updateTopCardImage(Card card) {
        imageViewTopCard.setImage(loadImage("/com/example/fpoeuno/" + card.getImagePath()));
    }

    private Image loadImage(String path) {
        return new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
    }

    private ImageView createCardImageView() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private ListCell<Card> createHumanCardCell() {
        ImageView imageView = createCardImageView();
        ListCell<Card> cell = new ListCell<>() {
            @Override
            protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                if (empty || card == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(loadImage("/com/example/fpoeuno/" + card.getImagePath()));
                    setGraphic(imageView);
                }
            }
        };

        cell.setOnMouseClicked(mouseEvent -> handleHumanCardPlay(cell.getItem()));
        return cell;
    }

    private void showImageViewTopCard(Card topCard) {
        Image image = new Image(Objects.requireNonNull(getClass().getResource(
                "/com/example/fpoeuno/" + topCard.getImagePath())).toExternalForm());
        imageViewTopCard.setImage(image);
    }

    private void establishGameValues(Card card) {
        game.setEstablishedValue(card.getValue());
        game.setEstablishedColor(card.getColor());
    }

}