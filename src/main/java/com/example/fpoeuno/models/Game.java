package com.example.fpoeuno.models;

import com.example.fpoeuno.models.enums.CardColor;
import com.example.fpoeuno.models.enums.CardType;

import java.util.Scanner;

public class Game {

    private final Deck deck;
    private final Player human;
    private final Player machine;
    private Player currentPlayer;
    private Card topCard;

    public Game() {
        this.deck = new Deck();
        this.human = new Player("Human", true);
        this.machine = new Player("Machine", false);

        dealInitialCards();
        startGame();
    }

    private void dealInitialCards() {
        for (int i = 0; i < 5; i++) {
            human.drawCard(deck.drawCard());
            machine.drawCard(deck.drawCard());
        }
    }

    public void startGame() {
        // Sacamos la primera carta del mazo
        topCard = deck.drawCard();
        deck.discard(topCard);
        System.out.println("First card on the table: " + topCard);

        // El jugador humano empieza
        this.currentPlayer = human;

        if (topCard.getType() == CardType.WILD || topCard.getType() == CardType.WILD_DRAW_FOUR) {
            handleWildCardEffect();
        }
    }

    public boolean canPlayCard(Card cardToPlay, Card topCard) {
        return cardToPlay.getColor() == topCard.getColor() ||
                cardToPlay.getValue() == topCard.getValue() ||
                cardToPlay.getColor() == CardColor.WILD;
    }

    public void playCard(Player player, Card card) {
        if (canPlayCard(card, topCard)) {
            player.getHand().remove(card); // Juega la carta
            topCard = card; // Actualizamos la carta superior
            deck.discard(card); // Descarta la carta del mazo

            // Si la carta es un comodín, ejecutamos su efecto
            if (card.getType() == CardType.WILD || card.getType() == CardType.WILD_DRAW_FOUR) {
                handleWildCardEffect();
            }

            // Cambiar el turno al siguiente jugador
            nextTurn();
            
        } else {
            System.out.println("You can't play that card! Try again.");
        }
    }

    public void nextTurn() {
        // Cambia el turno entre el jugador humano y la máquina
        if (currentPlayer == human) {
            currentPlayer = machine;
        } else {
            currentPlayer = human;
        }
    }

    public void playTurn() {
        System.out.println(currentPlayer.getName() + "'s turn:");
        currentPlayer.printPlayerInfo();

        // El jugador humano juega primero
        if (currentPlayer.isHuman()) {
            playHumanTurn();
        } else {
            playMachineTurn();
        }

        // Cambiar de turno
        currentPlayer = (currentPlayer == human) ? machine : human;
    }



    private void playHumanTurn() {
        Scanner scanner = new Scanner(System.in);

        // Mostrar la carta en la parte superior y las cartas del jugador
        System.out.println("Top card: " + topCard);
        System.out.println("Choose a card to play:");

        // Mostrar las cartas en mano
        for (int i = 0; i < human.getHand().size(); i++) {
            System.out.println("[" + (i + 1) + "] " + human.getHand().get(i));
        }

        // Pedir al jugador que elija una carta
        int choice = scanner.nextInt() - 1;
        Card chosenCard = human.getHand().get(choice);

        if (chosenCard.canPlayOn(topCard)) {
            human.playCard(chosenCard);
            topCard = chosenCard;
            deck.discard(chosenCard);

            // Si es un comodín, el jugador debe elegir un color
            if (chosenCard.getType() == CardType.WILD || chosenCard.getType() == CardType.WILD_DRAW_FOUR) {
                handleWildCardEffect();
            }
        } else {
            System.out.println("You cannot play that card! Try again.");
            playHumanTurn();  // Devolver al mismo jugador si no puede jugar
        }
    }

    private void playMachineTurn() {
        System.out.println("Machine's turn:");

        // Elige una carta aleatoria que pueda jugar
        Card chosenCard = null;
        for (Card card : machine.getHand()) {
            if (card.canPlayOn(topCard)) {
                chosenCard = card;
                break;
            }
        }

        if (chosenCard != null) {
            machine.playCard(chosenCard);
            topCard = chosenCard;
            deck.discard(chosenCard);
            System.out.println("Machine played: " + chosenCard);

            // Si es un comodín, la máquina también elige un color (simulamos elección aleatoria)
            if (chosenCard.getType() == CardType.WILD || chosenCard.getType() == CardType.WILD_DRAW_FOUR) {
                handleWildCardEffect();
            }
        } else {
            // Si la máquina no tiene jugadas posibles, se toma una carta
            Card drawnCard = deck.drawCard();
            machine.drawCard(drawnCard);
            System.out.println("Machine drew a card: " + drawnCard);
        }
    }

    private void handleWildCardEffect() {
        if (currentPlayer.isHuman()) {
            // El jugador humano elige un color si jugó un comodín
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose a color (1: Red, 2: Green, 3: Blue, 4: Yellow):");
            int colorChoice = scanner.nextInt();
            switch (colorChoice) {
                case 1:
                    topCard.setChosenColor(CardColor.RED);
                    break;
                case 2:
                    topCard.setChosenColor(CardColor.GREEN);
                    break;
                case 3:
                    topCard.setChosenColor(CardColor.BLUE);
                    break;
                case 4:
                    topCard.setChosenColor(CardColor.YELLOW);
                    break;
                default:
                    System.out.println("Invalid choice. No color chosen.");
            }
            System.out.println("You chose: " + topCard.getEffectiveColor());
        } else {
            // En este caso, la máquina elige un color aleatorio
            CardColor randomColor = CardColor.values()[(int) (Math.random() * CardColor.values().length)];
            topCard.setChosenColor(randomColor);
            System.out.println("Machine chose: " + randomColor);
        }
    }


    private void chooseColorForWild(Card wildCard) {
        System.out.println("Choose a color to start the game (blue, green, red, yellow):");

        // Simulación temporal por consola (puedes reemplazar esto por un diálogo en JavaFX)
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String chosenColor = scanner.nextLine().trim().toUpperCase();

        try {
            CardColor selectedColor = CardColor.valueOf(chosenColor);
            wildCard.setColor(selectedColor); // actualiza color de la carta
            System.out.println("Color set to: " + selectedColor);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid color. Defaulting to RED.");
            wildCard.setColor(CardColor.RED);
        }

        currentPlayer = human;
    }

    // --- Getters ----
    public Deck getDeck() { return deck; }
    public Player getHuman() { return human; }
    public Player getMachine() { return machine; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public Card getTopCard() { return topCard; }

    public void printGameInfo() {
        deck.printDeckInfo();
        System.out.println("=== Human Player's Hand ===");
        human.printPlayerInfo();
        System.out.println("=== Machine Player's Hand ===");
        machine.printPlayerInfo();
    }

}