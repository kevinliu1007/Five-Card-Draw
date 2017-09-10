package Game;

import module.CardPile;
import module.OpponentPlayer;
import module.UserPlayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Game Class
 *
 * Created by kevinliu1007 on 8/31/17.
 */
public class Game {

    /** Pile of cards. */
    private CardPile cardPile;

    /** Human player. */
    private UserPlayer userPlayer;

    /** AI player. */
    private ArrayList<OpponentPlayer> opponentPlayers;

    /** Number of opponents */
    private int numberOfOpponents;

    public Game() {
        cardPile = new CardPile();
        userPlayer = new UserPlayer();
        opponentPlayers = new ArrayList<>();
    }

    /**
     * Select numbers of opponents player want to play against.
     */
    public void opponentSelection() {
        int numOpponents = 0;
        boolean doneReading = false;

        /* Reading user input */
        while(!doneReading) {
            System.out.print("Please enter number of opponents you want to play against: ");

            /* Find number of opponent user want */
            try {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
                String line = buffer.readLine();
                Scanner scanner = new Scanner(line);

                if (!scanner.hasNext()) {
                    System.out.println("Please enter a number");
                } else {
                    numOpponents = scanner.nextInt();

                    System.out.println();

                    if (scanner.hasNext()) {
                        System.out.println("Please only enter 1 number");
                        numOpponents = 0;
                    } else if (numOpponents < 1 || numOpponents > 3) {
                        System.out.println("Please enter number between 1 - 3");
                    } else {
                        doneReading = true;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        /* Create opponent players */
        for (int i = 0; i < numOpponents; ++i) {
            opponentPlayers.add(new OpponentPlayer());
        }
        numberOfOpponents = numOpponents;
    }

    /**
     * Deal cards to players.
     */
    public void dealingPhase() {
        System.out.println("Shuffling Cards...");
        System.out.println("Dealing Cards...");
        for(int i = 0; i < 5; ++i) {
            userPlayer.addCard(cardPile.dealtCard());
            for (OpponentPlayer player : opponentPlayers) {
                player.addCard(cardPile.dealtCard());
            }
        }
        System.out.println("Cards has been dealt to " + Integer.toString(numberOfOpponents) + " players\n");
    }

    /**
     * Discard phase of the game.
     */
    public void discardPhase() {
        ArrayList<Integer> discardOptions = new ArrayList<>();
        boolean hasAce = false, discardSuccess = false;

        /* Display hands of the players */
        System.out.print("The cards in your hands are: ");
        userPlayer.displayCards();

        /* Find if hand contains Ace */
        if (userPlayer.findAce()) {
            hasAce = true;
            System.out.println("Since you have an Ace you can keep the Ace and discard the other four cards.");
        }

        /* Discard cards from hand */
        while (!discardSuccess) {
            System.out.print("List the cards numbers you wish to discard. > ");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
            int discardCounter = 0;

            /* Read user input */
            try {
                String line = buffer.readLine();
                Scanner scanner = new Scanner(line);

                if (!scanner.hasNext()) {
                    discardCounter = -1;
                } else {
                    while (scanner.hasNextInt()) {
                        discardCounter++;
                        discardOptions.add(scanner.nextInt());
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            /* Check for input error */
            if (discardCounter > 4) {
                System.out.println("Cannot select more than 3 cards to discard!");
            } else if (!hasAce && discardCounter > 3) {
                System.out.println("Cannot select more than 3 cards to discard!");
            } else if (hasAce && discardCounter > 4) {
                System.out.println("Cannot select more than 4 cards to discard!");
            } else {
                if(!lastAce(discardOptions, userPlayer) && discardCounter == 4) {
                    System.out.println("Cannot discard Ace");
                } else {
                    userPlayer.discardCards(discardCounter, discardOptions, cardPile);
                    discardSuccess = true;
                }
            }
        }

        for (OpponentPlayer op : opponentPlayers) {
            op.makeDecision(cardPile);
        }
    }

    /**
     * Compare phase of the game.
     */
    public void comparePhase() {
        int bestScore, winner = -1, AI = 1, player = 1, tiedPlayer = -1, userScore = userPlayer.evaluateHand();
        ArrayList<Integer> computerPlayers = new ArrayList<>();
        boolean tie = false;

        /* Display user's hand */
        System.out.print("\nYour hand: ");
        userPlayer.displayCards();

        /* Evaluate AIs' hands and display their hands*/
        for (OpponentPlayer op : opponentPlayers) {
            computerPlayers.add(op.evaluateHand());
            System.out.print("AI Player " + player + ": ");
            op.displayCards();
            player++;
        }

        /* Check for the winner */
        bestScore = userScore;
        for (int s : computerPlayers) {
            if (s < bestScore) {
                winner = AI;
                bestScore = s;
                tie = false;
            } else if (s == bestScore) {
                if (tieBreaker(userPlayer, opponentPlayers.get(AI-1), bestScore, tie)) {
                    winner = AI;
                    if (tie) {
                        tiedPlayer = AI;
                    }
                }
            }
            AI++;
        }

        /* Display the winner */
        if (winner == -1) {
            if (tie) {
                System.out.println("You tied with Player " + tiedPlayer + " with " + winningHand(bestScore));
            }
            System.out.println("You Win with " + winningHand(bestScore) + "!!!");
            userPlayer.displayCards();
        } else {
            System.out.println("AI player " + winner + " win with " + winningHand(bestScore));
            opponentPlayers.get(winner-1).displayCards();
        }
    }

    /**
     * Find if the remaining card contains Ace or not.
     *
     * @param discardOption discarOption
     * @param userPlayer    userPlayer
     *
     * @return  return true if remaining card contains Ace, false else wise
     */
    private boolean lastAce(ArrayList<Integer> discardOption, UserPlayer userPlayer) {
        int[] indices = new int[5];
        Arrays.fill(indices, 1);

        for (int i : discardOption) {
            indices[i] = 0;
        }

        for (int i = 0; i < 5; ++i) {
            if (indices[i] == 1 && userPlayer.getCards().get(i).getRawRank() == 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determine the winning hand.
     *
     * @param value value of player's hand
     *
     * @return  the winning hand
     */
    private String winningHand(int value) {
        switch (value) {
            case 0: return "Straight Flush";
            case 1: return "Four of a Kind";
            case 2: return "Full House";
            case 3: return "Flush";
            case 4: return "Straight";
            case 5: return "Three of a Kind";
            case 6: return "Two Pair";
            case 7: return "Two of a Kind";
            case 8: return "High Card";
            default: return "Error";
        }
    }

    /**
     * Determine the winner if two players are tied.
     *
     * @param userPlayer        userPlayer
     * @param opponentPlayer    opponentPlayer
     *
     * @return  true if AI Player win, false else wise
     */
    private boolean tieBreaker(UserPlayer userPlayer, OpponentPlayer opponentPlayer, int score, boolean tie) {
        switch (score) {
            case 0: return straightDecider(userPlayer, opponentPlayer, tie);
            case 1: return fourOfAKindDecider(userPlayer, opponentPlayer, tie);
            case 2: return fullHouseDecider(userPlayer, opponentPlayer);
            case 3: return flushDecider(userPlayer, opponentPlayer, tie);
            case 4: return straightDecider(userPlayer, opponentPlayer, tie);
            case 5: return threeOfAKindDecider(userPlayer, opponentPlayer);
            case 6: return twoPairDecider(userPlayer, opponentPlayer, tie);
            case 7: return pairDecider(userPlayer, opponentPlayer);
            case 8: return highCardDecider(userPlayer, opponentPlayer);
            default: return true;
        }
    }

    /**
     * Decider for winner between two straight flush.
     *
     * @param userPlayer        userPlayer
     * @param opponentPlayer    opponentPlayer
     * @param tie               tie
     *
     * @return  true if opponentPlayer win, false else wise
     */
    private boolean straightDecider(UserPlayer userPlayer, OpponentPlayer opponentPlayer, boolean tie) {
        if (userPlayer.getCards().get(0).getRawRank() == 1) {
            return false;
        } else if (userPlayer.getCards().get(0).getRawRank() < opponentPlayer.getCards().get(0).getRawRank()) {
            return true;
        } else {
            tie = true;
            return false;
        }
    }

    /**
     * Decider for winner between two four of a kind.
     *
     * @param userPlayer        userPlayer
     * @param opponentPlayer    opponentPlayer
     * @param tie               tie
     *
     * @return  true if opponentPlayer win, false else wise
     */
    private boolean fourOfAKindDecider(UserPlayer userPlayer, OpponentPlayer opponentPlayer, boolean tie) {
        int userIndex = userPlayer.fourOfAKindIndex();
        int opponentIndex = opponentPlayer.fourOfAKindIndex();

        if (userPlayer.getCards().get(userIndex).getRawRank() == 1 ) {
            return false;
        } else if (opponentPlayer.getCards().get(opponentIndex).getRawRank() == 1 ) {
            return true;
        } else if (userPlayer.getCards().get(userIndex).getRawRank()
                < opponentPlayer.getCards().get(opponentIndex).getRawRank()) {
            return true;
        } else {
            if (userPlayer.getCards().get(userIndex).getRawRank()
                    == opponentPlayer.getCards().get(opponentIndex).getRawRank()) {
                tie = true;
            }
            return false;
        }
    }

    /**
     * Decider for winner between two full house.
     *
     * @param userPlayer        userPlayer
     * @param opponentPlayer    opponentPlayer
     *
     * @return  true if opponentPlayer win, false else wise
     */
    private boolean fullHouseDecider(UserPlayer userPlayer, OpponentPlayer opponentPlayer) {
        int userThreeIndex = userPlayer.threeOfAKindIndex();
        int userTwoIndex = userPlayer.twoOfAKindIndex();
        int opponentThreeIndex = opponentPlayer.threeOfAKindIndex();
        int opponentTwoIndex = opponentPlayer.twoOfAKindIndex();

        if (userPlayer.getCards().get(userThreeIndex).getRawRank() == 1 ) {
            return false;
        } else if (opponentPlayer.getCards().get(opponentThreeIndex).getRawRank() == 1 ) {
            return true;
        } else if (userPlayer.getCards().get(userTwoIndex).getRawRank() == 1 ) {
            return false;
        } else if (opponentPlayer.getCards().get(opponentTwoIndex).getRawRank() == 1 ) {
            return true;
        } else if (userPlayer.getCards().get(userThreeIndex).getRawRank()
                < opponentPlayer.getCards().get(opponentThreeIndex).getRawRank()) {
            return true;
        } else if (userPlayer.getCards().get(userTwoIndex).getRawRank()
                < opponentPlayer.getCards().get(opponentTwoIndex).getRawRank()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Decider for winner between two flush.
     *
     * @param userPlayer        userPlayer
     * @param opponentPlayer    opponentPlayer
     * @param tie               tie
     *
     * @return  true if opponentPlayer win, false else wise
     */
    private boolean flushDecider(UserPlayer userPlayer, OpponentPlayer opponentPlayer, boolean tie) {
        if (userPlayer.getCards().get(0).getRawRank() == 1) {
            return false;
        } else if (opponentPlayer.getCards().get(0).getRawRank() == 1) {
            return true;
        } else {
            int i = 0;

            while (i < 5) {
                if (userPlayer.getCards().get(i).getRawRank() != opponentPlayer.getCards().get(i).getRawRank()) {
                    return userPlayer.getCards().get(i).getRawRank() < opponentPlayer.getCards().get(i).getRawRank();
                }
                i++;
            }
        }

        tie = true;
        return false;
    }

    /**
     * Decider for winner between two flush.
     *
     * @param userPlayer        userPlayer
     * @param opponentPlayer    opponentPlayer
     *
     * @return  true if opponentPlayer win, false else wise
     */
    private boolean highCardDecider(UserPlayer userPlayer, OpponentPlayer opponentPlayer) {
        if (userPlayer.getCards().get(0).getRawRank() == 1) {
            return false;
        } else if (opponentPlayer.getCards().get(0).getRawRank() == 1) {
            return true;
        } else {
            return userPlayer.getCards().get(0).getRawRank() < opponentPlayer.getCards().get(0).getRawRank();
        }
    }

    /**
     * Decider for winner between two three of a kind.
     *
     * @param userPlayer        userPlayer
     * @param opponentPlayer    opponentPlayer
     *
     * @return  true if opponentPlayer win, false else wise
     */
    private boolean threeOfAKindDecider(UserPlayer userPlayer, OpponentPlayer opponentPlayer) {
        int userIndex = userPlayer.threeOfAKindIndex();
        int opponentIndex = opponentPlayer.threeOfAKindIndex();

        if (userPlayer.getCards().get(userIndex).getRawRank() == 1 ) {
            return false;
        } else if (opponentPlayer.getCards().get(opponentIndex).getRawRank() == 1 ) {
            return true;
        } else if (userPlayer.getCards().get(userIndex).getRawRank()
                < opponentPlayer.getCards().get(opponentIndex).getRawRank()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Decider for winner between two two pair.
     *
     * @param userPlayer        userPlayer
     * @param opponentPlayer    opponentPlayer
     * @param tie               tie
     *
     * @return  true if opponentPlayer win, false else wise
     */
    private boolean twoPairDecider(UserPlayer userPlayer, OpponentPlayer opponentPlayer, boolean tie) {
        int userIndex = userPlayer.twoOfAKindIndex();
        int opponentIndex = opponentPlayer.twoOfAKindIndex();

        if (userPlayer.getCards().get(userIndex).getRawRank() == 1 ) {
            return false;
        } else if (opponentPlayer.getCards().get(opponentIndex).getRawRank() == 1 ) {
            return true;
        } else if (userPlayer.getCards().get(userIndex).getRawRank()
                != opponentPlayer.getCards().get(opponentIndex).getRawRank()) {
            return userPlayer.getCards().get(userIndex).getRawRank()
                    < opponentPlayer.getCards().get(opponentIndex).getRawRank();
        } else if (userPlayer.getCards().get(userPlayer.secondPairIndex()).getRawRank()
                != opponentPlayer.getCards().get(opponentPlayer.secondPairIndex()).getRawRank()) {
            return userPlayer.getCards().get(userIndex).getRawRank()
                    < opponentPlayer.getCards().get(opponentIndex).getRawRank();
        } else {
            int userSingle = userPlayer.singleIndex();
            int opponentSingle = opponentPlayer.singleIndex();

            if (userPlayer.getCards().get(userSingle).getRawRank()
                    != opponentPlayer.getCards().get(opponentSingle).getRawRank()) {
                return userPlayer.getCards().get(userSingle).getRawRank()
                        < opponentPlayer.getCards().get(opponentSingle).getRawRank();
            } else {
                tie = true;
                return false;
            }
        }
    }

    /**
     * Decider for winner between two two pair.
     *
     * @param userPlayer        userPlayer
     * @param opponentPlayer    opponentPlayer
     *
     * @return  true if opponentPlayer win, false else wise
     */
    private boolean pairDecider(UserPlayer userPlayer, OpponentPlayer opponentPlayer) {
        int userIndex = userPlayer.twoOfAKindIndex();
        int opponentIndex = opponentPlayer.twoOfAKindIndex();

        if (userPlayer.getCards().get(userIndex).getRawRank() == 1 ) {
            return false;
        } else if (opponentPlayer.getCards().get(opponentIndex).getRawRank() == 1 ) {
            return true;
        } else if (userPlayer.getCards().get(userIndex).getRawRank()
                != opponentPlayer.getCards().get(opponentIndex).getRawRank()) {
            return userPlayer.getCards().get(userIndex).getRawRank()
                    < opponentPlayer.getCards().get(opponentIndex).getRawRank();
        } else {
            int userStarting = 0, opponentStarting = 0;
            if (userIndex == 0) {
                userStarting = 2;
            }
            if (opponentIndex == 0) {
                opponentStarting = 2;
            }

            return userPlayer.getCards().get(userStarting).getRawRank()
                    < opponentPlayer.getCards().get(opponentStarting).getRawRank();
        }
    }
}
