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
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

import java.io.IOException;

public class GameController {
    @FXML
    AnchorPane colorSelectionBox;
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

    public void setHuman(Player human) {
        this.human = human;
        initializeHumanHandView();
        showLabelNickname();
    }

    public void setTopCard(Card card) {
        String currentTurn = game.getCurrentTurn();
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
                        wildLogic(selectedCard);// agrega la logica para las cartas especiales
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
            // machineLogic(); incorporacion con errores
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
        System.out.println("***********computer hand");
        computer.printHand();
    }

    private boolean isPlayable(Card selectedCard) {
        return selectedCard.getColor().equals(topCard.getColor()) ||
                selectedCard.getValue().equals(topCard.getValue()) ||
                selectedCard.getColor().equals("wild");
    }

    private void wildLogic(Card selectedCard) {
        String currentTurn = game.getCurrentTurn();
        if(selectedCard.getValue().equals("skip")) {changeTurn();}
        // Mostrar selector de color si es comodín
        if (selectedCard.getColor().equals("wild")) {
            colorSelectionBox.setVisible(true);
            changeTurn();
        }
        if (selectedCard.getValue().equals("wild_draw_2") && currentTurn.equals("Human")) {
            computer.addCard(deck.drawCard());
            computer.addCard(deck.drawCard());
            changeTurn();
        }
        if (selectedCard.getValue().equals("wild_draw_2") && currentTurn.equals("Computer")) {
            human.addCard(deck.drawCard());
            human.addCard(deck.drawCard());
            changeTurn();
        }
        if (selectedCard.getValue().equals("wild_draw_4") && currentTurn.equals("Human")) {
            computer.addCard(deck.drawCard());
            computer.addCard(deck.drawCard());
            computer.addCard(deck.drawCard());
            computer.addCard(deck.drawCard());


        }
        if (selectedCard.getValue().equals("wild_draw_4") && currentTurn.equals("Computer")) {
            human.addCard(deck.drawCard());
            human.addCard(deck.drawCard());
            human.addCard(deck.drawCard());
            human.addCard(deck.drawCard());


        }
    }

    private void machineLogic() {
        if (!game.getCurrentTurn().equals("Computer")) return;

        for (Card card : computer.getHand()) {

            if (isPlayable(card)) {
                topCard = card;
                setTopCard(topCard);
                wildLogic(card);
                computer.removeCard(card);
                deck.discardCard(card);
                listViewComputerHand.getItems().setAll(computer.getHand());
                changeTurn(); // vuelve al humano
                return;
            }
        }

        // Si ninguna carta es jugable, robar
        Card draw = deck.drawCard();
        computer.addCard(draw);
        listViewComputerHand.getItems().setAll(computer.getHand());


        changeTurn(); // vuelva al humano
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


    @FXML
    void onColorSelectedRed(ActionEvent event) {
        applyWildColor("red");
    }

    @FXML
    void onColorSelectedYellow(ActionEvent event) {
        applyWildColor("yellow");
    }

    @FXML
    void onColorSelectedGreen(ActionEvent event) {
        applyWildColor("green");
    }

    @FXML
    void onColorSelectedBlue(ActionEvent event) {
        applyWildColor("blue");
    }

    private void applyWildColor(String color) {
       this.topCard.setColor(color);
        setTopCard(topCard); // actualiza la imagen
        colorSelectionBox.setVisible(false); // oculta los botones
        if (topCard.getValue().equals("wild")){changeTurn();} // continúa el turno
    }
}