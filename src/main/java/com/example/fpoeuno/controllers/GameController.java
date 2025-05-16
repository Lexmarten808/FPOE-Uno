package com.example.fpoeuno.controllers;

import com.example.fpoeuno.models.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.*;

import java.io.IOException;

public class GameController {

    // ===== FXML COMPONENTES =====
    @FXML private VBox unoBox;
    @FXML private AnchorPane colorSelectionBox;
    @FXML private Button buttonGameColor;
    @FXML private Label labelNickname;
    @FXML private ListView<Card> listViewHumanHand;
    @FXML private ListView<Card> listViewComputerHand;
    @FXML private ImageView imageViewTopCard;
    @FXML private ImageView imageViewHumanFlag;
    @FXML private ImageView imageViewComputerFlag;

    // ===== MODELOS ======
    private UnoThreadManager unoThreadManager = new UnoThreadManager();
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
        buttonGameColor.setStyle(currentColor());

        // condicional, SI la top card es la primera carta
        if (deck.getDiscardPile().isEmpty()) { // Si el mazo aún no tiene cartas descartadas, es la inicial
            switch (topCard.getValue()) {
                case "wild_draw_4":
                    System.out.println("La primera carta es +4. El jugador roba 4 cartas.");
                    for (int i = 0; i < 4; i++) human.addCard(deck.drawCard());
                    refreshHumanHand();
                    break;

                case "wild_draw_2":
                    System.out.println("La primera carta es +2. El jugador roba 2 cartas.");
                    for (int i = 0; i < 2; i++) human.addCard(deck.drawCard());
                    refreshHumanHand();
                    break;

                case "skip":
                    System.out.println("La primera carta es bloqueo. Se salta el turno del jugador.");
                    changeTurn(); // Cambia el turno al computador
                    break;

                case "wild":
                    System.out.println("La primera carta es comodín. Se debe elegir un color.");
                    colorSelectionBox.setVisible(true);
                    pendingWildCard = topCard;
                    break;
            }
        }

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
            if (pendingWildCard != null) {
                System.out.println("Primero selecciona un color antes de robar.");
                return;
            }
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

    // ===== LÓGICA DE JUEGO =====

    private void handleHumanCardPlay(Card selectedCard) {

        unoBox.setVisible(false);
        if (!isHumanTurn()) {
            System.out.println("Es el turno de la máquina, no puedes jugar...");
            return;
        }

        if (pendingWildCard != null) {
            System.out.println("Primero selecciona un color antes de seguir jugando.");
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
        if (human.getHand().size() == 2) {
            unoBox.setVisible(true);
            System.out.println("presiona UNO");
            unoThreadManager.start(human, player -> {
                System.out.println("No presionó UNO a tiempo. Se penaliza a " + player.getNickname());
                player.addCard(deck.drawCard());
                refreshHumanHand();
                changeTurn();
            });
        }

        topCard = selectedCard;
        setTopCard(topCard);
        handleWildCardLogic(selectedCard);
        human.removeCard(selectedCard);
        deck.discardCard(selectedCard);
        refreshHumanHand();


        // Solo cambiar turno si no es un comodín
        if (pendingWildCard == null) {
            changeTurn();
        }
    }

    private void handleWildCardLogic(Card card) {
        String value = card.getValue();
        String color = card.getColor();
        String currentTurn = game.getCurrentTurn();

        if (value.equals("skip")) {
            establishGameValues(card);
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
            for (int i = 0; i < 2; i++) target.addCard(deck.drawCard());
            System.out.println("Efecto +2 aplicado al jugador: " + target.getNickname());
        }
    }

    private void applyWildColor(String color) {
        if (pendingWildCard == null) return;

        System.out.println("Color elegido por el usuario: " + color);

        pendingWildCard.setColor(color);
        setTopCard(pendingWildCard);
        colorSelectionBox.setVisible(false);

        String value = pendingWildCard.getValue();
        establishGameValues(pendingWildCard);

        Player target = game.getCurrentTurn().equals("Human") ? computer : human;

        if (value.equals("wild_draw_4")) {
            for (int i = 0; i < 4; i++) target.addCard(deck.drawCard());
            System.out.println("Efecto +4 aplicado al jugador: " + target.getNickname());
        }

        pendingWildCard = null;
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
                            if (pendingWildCard != null) {
                                System.out.println("Esperando selección de color. Máquina no puede jugar.");
                                return;
                            }

                            playComputerTurn();
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
        if (pendingWildCard != null) {
            System.out.println("Comodín sin resolver, la máquina espera...");
            return;
        }

        System.out.println("=== TURNO DE LA COMPUTADORA ===");
        computer.printHand();
        System.out.println("Carta superior actual: " + topCard);

        boolean played = false;

        List<Card> hand = new ArrayList<>(computer.getHand());

        for (Card selectedCard : hand) {
            if (isPlayable(selectedCard)) {
                System.out.println("La computadora juega: " + selectedCard);

                computer.removeCard(selectedCard); //  Tomar la carta de la mano
                deck.discardCard(selectedCard); // Ponerla en la pila de descarte
                topCard = selectedCard; //
                setTopCard(topCard); // Mostrarla como carta superior
                establishGameValues(topCard); // Establecer el estado del juego
                played = true;

                // === comodines ===
                String value = selectedCard.getValue();
                String color = selectedCard.getColor();

                if (selectedCard.getColor().equals("wild")) {
                    String chosenColor = chooseComputerColor(computer.getHand());
                    selectedCard.setColor(chosenColor);
                    game.setEstablishedColor(chosenColor);
                    game.setEstablishedValue("wild");
                    buttonGameColor.setStyle(currentColor());
                    System.out.println("La computadora elige el color: " + chosenColor);

                    if (value.equals("wild_draw_4")) {
                        for (int i = 0; i < 2; i++) human.addCard(deck.drawCard());
                        System.out.println("Efecto +4 aplicado al humano.");
                        refreshHumanHand();
                        changeTurn();
                        return;
                    }
                } else if (value.equals("wild_draw_2")) {
                    for (int i = 0; i < 2; i++) human.addCard(deck.drawCard());
                    System.out.println("Efecto +2 aplicado al humano.");
                    refreshHumanHand();
                    changeTurn();
                    return;
                } else if (value.equals("skip")) {
                    System.out.println("Turno del jugador saltado.");
                    refreshHumanHand();
                    return;
                }
                break;
            }
        }

        if (!played) {
            System.out.println("La computadora no puede jugar. Roba una carta.");
            Card draw = deck.drawCard();
            computer.addCard(draw);
        }

        System.out.println("***********computer hand: después de jugar");
        computer.printHand();
        // refreshBothHands();
        changeTurn();
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
        buttonGameColor.setStyle(currentColor());
    }

    private String currentColor() {
        return switch (game.getEstablishedColor()) {
            case "red" -> "-fx-background-color: #C62828";
            case "blue" -> "-fx-background-color: #282dc6";
            case "green" -> "-fx-background-color: #309e11";
            case "yellow" -> "-fx-background-color: #e0ca22";
            default -> "-fx-background-color: gray";
        };
    }

    private String chooseComputerColor(List<Card> hand) {
        for (Card card : hand) {
            String color = card.getColor();
            if (!color.equals("wild")) {
                return color;
            }
        }
        return "red"; // color por defecto si no hay cartas de color
    }

    @FXML
    void onActionButtonUno(ActionEvent event) {
        if (human.getHand().size() == 1 && unoThreadManager.isUNOActive()) {
            unoThreadManager.pressUNO();
            System.out.println("¡UNO presionado a tiempo!");
        } else {
            System.out.println("No puedes presionar UNO ahora.");
        }
    }




}