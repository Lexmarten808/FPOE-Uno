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

/**
 * Controller for the main game screen.
 * Handles interactions, player and computer turns, card effects, and UI updates.
 * Manages core game flow once the game has started.
 */
public class GameController {

    // ==================== FXML UI COMPONENTS ====================

    @FXML private VBox unoBox;
    @FXML private AnchorPane colorSelectionBox;
    @FXML private Button buttonGameColor;
    @FXML private Label labelNickname;
    @FXML private ListView<Card> listViewHumanHand;
    @FXML private ListView<Card> listViewComputerHand;
    @FXML private ImageView imageViewTopCard;
    @FXML private ImageView imageViewHumanFlag;
    @FXML private ImageView imageViewComputerFlag;

    // ==================== GAME MODELS ====================

    private UnoThreadManager unoThreadManager = new UnoThreadManager(); // Manages timing for UNO button
    private Game game = new Game(); // Core game data model (current turn, top color/value)

    private Deck deck = new Deck(); // Card deck and discard pile
    private Player human;           // Human player
    private Player computer;        // Computer player

    private Card topCard;         // Current card on top of discard pile
    private Card pendingWildCard; // Used when waiting for player to pick a color

    private Thread computerTurnThread;      // Thread handling computer's automatic turn
    private boolean computerCanPlay = true; // Prevents the computer from playing multiple times

    // ==================== SETTERS ====================

    /**
     * Sets the human player and initializes their UI.
     * @param human the human player object.
     */
    public void setHuman(Player human) {
        this.human = human;
        initializeHumanHandView();
        showLabelNickname();
    }

    /**
     * Sets the computer player and starts its turn thread and UI.
     * @param computer the computer player object.
     */
    public void setComputer(Player computer) {
        this.computer = computer;
        initializeComputerTurnThread();
        initializeComputerHandView();
    }

    /**
     * Sets the current deck used in the game.
     * @param deck the deck of cards.
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    /**
     * Sets the top card on the discard pile and applies its effect.
     * @param card the card to place on top of the pile.
     */
    public void setTopCard(Card card) {
        this.topCard = card;

        showImageViewTopCard(topCard);
        establishGameValues(topCard);
        buttonGameColor.setStyle(currentColor());

        // Only apply effects if it's the first card played
        if (deck.getDiscardPile().isEmpty()) {
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

    // ==================== INITIALIZERS ====================

    /**
     * Initializes the human player's hand view in the ListView.
     * Configures the ListView to display cards horizontally and
     * sets a custom cell factory to render the human cards with images.
     */
    private void initializeHumanHandView() {
        unoBox.setVisible(false); // Initially hides the UNO button
        listViewHumanHand.setOrientation(Orientation.HORIZONTAL);
        listViewHumanHand.setCellFactory(param -> createHumanCardCell());
        listViewHumanHand.getItems().setAll(human.getHand());
    }

    /**
     * Initializes the computers hand view in the ListView.
     * Displays the computer's hand horizontally, but only shows the back of each card
     * using a default UNO card image to keep them hidden from the player.
     */
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

    // ==================== MAIN GAME LOGIC ====================

    /**
     * Handles the logic for then the human player attempts to play a card.
     * @param selectedCard the card the player is trying to play.
     */
    private void handleHumanCardPlay(Card selectedCard) {
        try {
            if (!isHumanTurn()) {
                throw new GameExceptions.InvalidTurnException("Es el turno de la máquina. No puedes jugar.");
            }

            if (pendingWildCard != null) {
                throw new GameExceptions.InvalidCardPlayException("Primero selecciona un color antes de seguir jugando.");
            }

            if (selectedCard == null) {
                throw new GameExceptions.InvalidCardPlayException("No es una carta válida.");
            }

            if (!isPlayable(selectedCard)) {
                throw new GameExceptions.InvalidCardPlayException("Carta no jugable: " + selectedCard);
            }

        } catch (GameExceptions.InvalidCardPlayException | GameExceptions.InvalidTurnException e) {
            System.out.println("Error: " + e.getMessage());
            AlertHelper.showErrorAlert("Movimiento inválido", "No puedes realizar esta acción", e.getMessage());
            return;
        }

        // If the player has only 2 cards and is about to play one -> trigger UNO timer
        if (human.getHand().size() == 2) {
            unoBox.setVisible(true);
            computerCanPlay = false;
            System.out.println("Presiona UNO");

            // Starts the UNO timer thread
            unoThreadManager.start(human, player -> {
                System.out.println("No presionó UNO a tiempo. Se penaliza a " + player.getNickname());
                player.addCard(deck.drawCard());
                unoBox.setVisible(false);
                computerCanPlay = true;
                refreshHumanHand();

                if (game.getCurrentTurn().equals("Human")) {
                    changeTurn();
                }
            });
        }

        topCard = selectedCard;
        setTopCard(topCard);

        // If this is the last card, player wins
        if (human.getHand().size() == 1){
            human.removeCard(selectedCard);
            refreshHumanHand();
            System.out.println("Juego terminado, " + human.getNickname() + " gana!");
            AlertHelper.showInfoAlert(
                    "GANADOR: " + human.getNickname(),
                    "JUEGO TERMINADO",
                    "Juego terminado, " + human.getNickname() + " gana!"
            );
            System.exit(0);
        }

        // Handle wild card color selection if applicable
        handleWildCardLogic(selectedCard);
        human.removeCard(selectedCard);
        deck.discardCard(selectedCard);
        refreshHumanHand();

        // Change turn only if no wild color is pending
        if (pendingWildCard == null) {
            changeTurn();
        }
    }

    /**
     * Handles the special logic for wild cards and ohter special cards.
     * This includes skip cards, wild cards (color selection), and wild draw two card.
     *
     * @param card the card played that may have special effects.
     */
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
            // Show color selection UI for wild cards
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

    /**
     * Applies the chosen color to the pending wild card and continues game logic.
     * Throws exceptions if no wild card is pending or if the color is invalid.
     *
     * @param color the color selected by the player.
     * @throws IllegalStateException if there is no pending wild card.
     * @throws IllegalArgumentException if the color is null or blank.
     */
    private void applyWildColor(String color) {
        if (pendingWildCard == null) {
            throw new IllegalStateException("No hay una carta comodín pendiente para aplicar color");
        }
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("El color no puede estar vacío");
        }

        System.out.println("Color elegido por el usuario: " + color);

        setTopCard(pendingWildCard);
        colorSelectionBox.setVisible(false);

        String value = pendingWildCard.getValue();
        game.setEstablishedColor(color);
        game.setEstablishedValue(pendingWildCard.getValue());
        buttonGameColor.setStyle(currentColor());

        // Determine the target player who receives penalties (cards to draw)
        Player target = game.getCurrentTurn().equals("Human") ? computer : human;

        if (value.equals("wild_draw_4")) {
            for (int i = 0; i < 4; i++) target.addCard(deck.drawCard());
            System.out.println("Efecto +4 aplicado al jugador: " + target.getNickname());
        }

        pendingWildCard = null;
        changeTurn();
    }

    // ==================== WILD COLOR SELECTION ====================

    /** Sets the selected wild card color to yellow. */
    @FXML
    void onActionButtonColorYellow(ActionEvent event) { applyWildColor("yellow"); }

    /** Sets the selected wild card color to blue. */
    @FXML
    void onActionButtonColorBlue(ActionEvent event) { applyWildColor("blue"); }

    /** Sets the selected wild card color to red. */
    @FXML
    void onActionButtonColorRed(ActionEvent event) { applyWildColor("red"); }

    /** Sets the selected wild card color to green. */
    @FXML
    void onActionButtonColorGreen(ActionEvent event) { applyWildColor("green"); }

    // ==================== EVENT HANDLERS ====================

    /**
     * Event handler for the "Robar" button.
     * Validates that it's the human player's turn, that no wild card color
     * is pending selection, and that the player has no playable cards.
     * If all conditions are met, a card is drawn and the turn changes.
     * @param event the action event triggered by clickin the draw button.
     */
    @FXML
    void onActionButtonRobar(ActionEvent event) {
        try {
            if (!Objects.equals(game.getCurrentTurn(), "Human")) {
                throw new GameExceptions.NotYourTurnException("Es el turno de la máquina, no puedes robar.");
            }

            if (pendingWildCard != null) {
                throw new GameExceptions.WildColorPendingException("Primero selecciona un color antes de robar.");
            }

            // Check if the human has any playable card
            for (Card card : human.getHand()) {
                if (isPlayable(card)) {
                    throw new GameExceptions.CannotDrawCardException("Tienes una carta jugable, no puedes robar.");
                }
            }

            // No playable cards, player can draw one
            Card draw = deck.drawCard();
            human.addCard(draw);
            changeTurn();
            refreshHumanHand();

        } catch (GameExceptions.NotYourTurnException | GameExceptions.WildColorPendingException |
                 GameExceptions.CannotDrawCardException e) {
            AlertHelper.showErrorAlert("Movimiento inválido", "No puedes robar una carta", e.getMessage());
        }
    }

    /**
     * Handles the action when the UNO button is pressed.
     * Pressing UNO is valid only when the player has exactly one card and the UNO state is active.
     * Applies penalties or allows play accordingly.
     * @param event the action event triggered by clicking the UNO button.
     */
    @FXML
    void onActionButtonUno(ActionEvent event) {
        if (human.getHand().size() == 1 && unoThreadManager.isUNOActive()) {
            unoThreadManager.pressUNO();
            System.out.println("¡UNO presionado a tiempo!");
            computerCanPlay = true;
            unoBox.setVisible(false);
        } else if (computer.getHand().size() == 1 && unoThreadManager.isUNOActive()) {
            unoThreadManager.pressUNO();
            System.out.println("¡UNO presionado a tiempo, penalización de 1 carta para la máquina!");
            Card draw = deck.drawCard();
            computer.addCard(draw);
            unoBox.setVisible(false);
        } else {
            System.out.println("No puedes presionar UNO ahora.");
        }
    }

    // ==================== MACHINE THREAD ====================

    /**
     * Initializes and starts the thread responsible for handling the computer player's turn.
     * The thread runs continuously, checking every 5 seconds if it is the computer's turn,
     * and if so, executes the computer's play on the JavaFX Application Thread.
     */
    public void initializeComputerTurnThread() {
        computerTurnThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(5000); // Wait for 5 seconds

                    if ("Computer".equals(game.getCurrentTurn()) && computerCanPlay) {
                        computerCanPlay = false;
                        Platform.runLater(() -> {
                            if (pendingWildCard != null) {
                                System.out.println("Esperando selección de color. La Máquina no puede jugar.");
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

        computerTurnThread.setDaemon(true); // Stops when the application closes
        computerTurnThread.start();
    }

    /**
     * Executes the computer player's turn logic.
     * The computer attempts to play the first valid card in its hand.
     * Handles special cards (wilds, skips, draws) and updates the game state accordingly.
     * If no valid cards are available, the computer draws a card.
     */
    private void playComputerTurn() {
        if (pendingWildCard != null) {
            System.out.println("Comodín sin resolver, la máquina espera...");
            return;
        }

        System.out.println("=== TURNO DE LA COMPUTADORA ===");
        System.out.println("Carta superior actual: " + topCard);

        boolean played = false;

        List<Card> hand = new ArrayList<>(computer.getHand());

        for (Card selectedCard : hand) {
            if (isPlayable(selectedCard)) {
                if (computer.getHand().size() == 2) {
                    unoBox.setVisible(true);
                    computerCanPlay = false;
                    System.out.println("Presiona UNO");
                    unoThreadManager.start(computer, player -> {
                        System.out.println("No presionó UNO a tiempo. No hay penalización para la máquina.");
                        unoBox.setVisible(false);
                        computerCanPlay = true;
                    });
                }
                System.out.println("La computadora juega: " + selectedCard);

                topCard = selectedCard; //
                setTopCard(topCard); // Display as top card

                if (computer.getHand().size() == 1){
                    computer.removeCard(selectedCard);
                    refreshComputerHand();
                    System.out.println("Juego terminado, " + computer.getNickname() + " gana!");
                    AlertHelper.showInfoAlert(
                            "GANADOR: " + computer.getNickname(),
                            "JUEGO TERMINADO",
                            "Juego terminado, " + computer.getNickname() + " gana!"
                    );
                    System.exit(0);
                }

                computer.removeCard(selectedCard); //  Remove from hand
                deck.discardCard(selectedCard); // Add to discard pile


                establishGameValues(topCard); // Update game state
                played = true;

                // Handle wild and special cards effects
                String value = selectedCard.getValue();

                if (selectedCard.getColor().equals("wild")) {
                    String chosenColor = chooseComputerColor(computer.getHand());
                    game.setEstablishedColor(chosenColor);
                    game.setEstablishedValue("wild");
                    buttonGameColor.setStyle(currentColor());
                    System.out.println("La computadora elige el color: " + chosenColor);

                    if (value.equals("wild_draw_4")) {
                        for (int i = 0; i < 4; i++) human.addCard(deck.drawCard());
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

        changeTurn();
    }

    // ==================== UTILITIES ====================

    /**
     * Event handler for the "Print" button.
     * @param event the action event triggered by clicking the print button.
     * @throws IOException if the deck printing fails.
     */
    @FXML
    void onActionButtonPrint(ActionEvent event)throws IOException  {
        deck.printDeck();
        System.out.println("********** Human Hand **********");
        human.printHand();
        System.out.println("********** Computer Hand **********");
        computer.printHand();
        System.out.println("********** Game Info **********");
        System.out.println(game.getCurrentTurn());
        System.out.println(game.getEstablishedColor());
        System.out.println(game.getEstablishedValue());
    }

    /**
     * Checks if a selected card can be played on the current established card.
     * A card is playable if its color matches the established color,
     * or its value matches the established value,
     * or if it is a wild card.
     * @param selectedCard the card to check for playability.
     * @return true if the card is playable; false otherwise.
     */
    private boolean isPlayable(Card selectedCard) {
        return selectedCard.getColor().equals(game.getEstablishedColor()) ||
                selectedCard.getValue().equals(game.getEstablishedValue()) ||
                selectedCard.getColor().equals("wild");
    }

    /**
     * Determines if it is currently the human player's turn.
     * @return true if the current turn belongs to the human player; false otherwise.
     */
    private boolean isHumanTurn() {
        return game.getCurrentTurn().equals("Human");
    }

    /**
     * Switches the turn from human to computer or vice versa,
     * and updates the UI flags visibility accordingly.
     */
    public void changeTurn() {
        boolean isHumanTurn = game.getCurrentTurn().equals("Human");
        game.setCurrentTurn(isHumanTurn ? "Computer" : "Human");
        imageViewHumanFlag.setVisible(!isHumanTurn);
        imageViewComputerFlag.setVisible(isHumanTurn);
    }

    /**
     * Refreshes the UI list showing the human player's hand.
     */
    private void refreshHumanHand() {
        listViewHumanHand.getItems().setAll(human.getHand());
    }

    /**
     * Refreshes the UI list showing the computer player's hand.
     */
    private void refreshComputerHand() {
        listViewComputerHand.getItems().setAll(computer.getHand());
    }

    /**
     * Updates the label displaying the human player's nickname.
     */
    private void showLabelNickname() {
        labelNickname.setText(human.getNickname());
    }

    /**
     * Loads an image from the given resource path.
     * @param path the path to the image resource
     * @return the loaded Image object
     * @throws NullPointerException if the resource is not found.
     */
    private Image loadImage(String path) {
        return new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
    }

    /**
     * Creates an ImageView configured to display a card image with fixed size and preserved ratio.
     * @return a configured ImageView instance.
     */
    private ImageView createCardImageView() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    /**
     * Creates a ListCell for displaying human player's cards with their images,
     * and attaches a mouse click event handler for playing the card.
     * @return a ListCell configured for Card objects with click handling.
     */
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

        cell.setOnMouseClicked(mouseEvent -> handleHumanCardPlay(cell.getItem()));{}
        return cell;
    }

    /**
     * Shows the top card image on the UI
     * @param topCard the card to show as the top card.
     */
    private void showImageViewTopCard(Card topCard) {
        Image image = new Image(Objects.requireNonNull(getClass().getResource(
                "/com/example/fpoeuno/" + topCard.getImagePath())).toExternalForm());
        imageViewTopCard.setImage(image);
    }

    /**
     * Sets the game's established color and value based on the given card
     * and updates the color button style.
     * @param card the card used to establish game state.
     */
    private void establishGameValues(Card card) {
        game.setEstablishedValue(card.getValue());
        game.setEstablishedColor(card.getColor());
        buttonGameColor.setStyle(currentColor());
    }

    /**
     * Returns the CSS style string corresponding to the current established color.
     * @return CSS background-color style string for the established color.
     */
    private String currentColor() {
        return switch (game.getEstablishedColor()) {
            case "red" -> "-fx-background-color: #C62828";
            case "blue" -> "-fx-background-color: #282dc6";
            case "green" -> "-fx-background-color: #309e11";
            case "yellow" -> "-fx-background-color: #e0ca22";
            default -> "-fx-background-color: gray";
        };
    }

    /**
     * Chooses a color for the computer player to set when playing a wild card.
     * The chosen color is the first non-wild color found in the hand.
     * Defaults to "red" if no colored cards are present.
     * @param hand the list of cards in the computer's hand.
     * @return the chosen color as a String.
     */
    private String chooseComputerColor(List<Card> hand) {
        for (Card card : hand) {
            String color = card.getColor();
            if (!color.equals("wild")) {
                return color;
            }
        }
        return "red"; // default color if no colored cards found
    }

    /**
     * Applies the initial game logic based on the first top card when starting the game.
     * Handles skip, wild draw 2, and wild cards accordingly.
     * @param card the first top card to apply logic on.
     */
    public void firstCardLogic(Card card) {
        System.out.println("APLICANDO LA LÓGICA DE LA TOP CARD");
        String value = card.getValue();
        String color = card.getColor();

        if (value.equals("skip")) {
            establishGameValues(card);
            changeTurn();
            System.out.println("APLICANDO LA LÓGICA DE SKIP CARD");
            return;
        }

        if (value.equals("wild_draw_2")) {
            for (int i = 0; i < 2; i++) human.addCard(deck.drawCard());
            refreshHumanHand();
            System.out.println("Efecto +2 aplicado al jugador: " + human.getNickname());
            System.out.println("APLICANDO LA LÓGICA DE LA WILD+2 CARD");
        }

        if (color.equals("wild")) {
            String chosenColor = chooseComputerColor(computer.getHand());

            topCard.setColor(chosenColor);
            game.setEstablishedColor(chosenColor);
            game.setEstablishedValue("wild");
            buttonGameColor.setStyle(currentColor());
            System.out.println("COLOR ESTABLECIDO: " + chosenColor);

            if (value.equals("wild_draw_4")) {
                for (int i = 0; i < 4; i++) human.addCard(deck.drawCard());
                System.out.println("Efecto +4 aplicado al humano.");
                refreshHumanHand();
                changeTurn();
            }
        }
    }

    // ==================== SUPPORT & SOUND ====================

    /**
     * Event handler for the "Ayuda" button.
     * @param event the action event triggered by the Ayuda button.
     */
    @FXML
    void onActionButtonAyuda(ActionEvent event) {
        AlertHelper.showInfoAlert(
                "Reglas",
                "Reglas de UNO",
                "-1. Cada jugador comienza con 5 cartas.\n" +
                        "-2. La partida inicia con una carta aleatoria en la mesa.\n" +
                        "-3. Juega una carta que coincida en color o número.\n" +
                        "-4. Los comodines pueden jugarse en cualquier momento.\n" +
                        "-5. Presiona el botón UNO para jugar tu última carta, tendrás 2-4 segundos.\n" +
                        "-6. Si no lo haces, serás penalizado con una carta."
        );
    }

    /**
     * Event handler for the sound button.
     * @param event the action event triggered by the sound button.
     */
    @FXML
    void onActionButtonSound(ActionEvent event) {
        SoundManager.toggleMusic("music.mp3");
    }

}