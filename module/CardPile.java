package module;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kevinliu1007 on 8/31/17.
 */
public class CardPile {

    /** Card pile. */
    private ArrayList<Card> cardPile;

    /** Discard pile. */
    private ArrayList<Card> discardPile;

    /**
     * Constructor of CardPile class.
     */
    public CardPile() {
        cardPile = randomizeCards();
        discardPile = new ArrayList<>();
    }

    /**
     * Deal a card out of card pile, random a new card if card pile become empty.
     *
     * @return  card
     */
    public Card dealtCard() {
        Card card = cardPile.get(0);
        if (card.getRawRank() == 1) {
            card.setRank(14);
        }
        cardPile.remove(0);

        if (cardPile.isEmpty()) {
            cardPile = randomizeCards();
        }

        return card;
    }

    /**
     * Put the discarded card into discarded pile.
     *
     * @param card  Card to be discarded
     */
    public void discardCard(Card card) {
        discardPile.add(card);
    }

    /**
     * Create a sorted card pile and randomize it.
     *
     * @return  randomedCardPile
     */
    private ArrayList<Card> randomizeCards() {
        ArrayList<Card> sortedCardPile = new ArrayList<>();
        ArrayList<Card> randomedCardPile = new ArrayList<>();

        /* Initialize sorted pile of cards */
        for (int i = 0; i < 4; ++i) {
            for (int j = 1; j <= 13; ++j) {
                String suit;
                switch (i) {
                    case 0: suit = "C"; break;
                    case 1: suit = "D"; break;
                    case 2: suit = "H"; break;
                    case 3: suit = "S"; break;
                    default: suit = String.valueOf(i);
                    break;
                }
                sortedCardPile.add(new Card(j, suit));
            }
        }

        int leftOverCards = 52;
        Random rn = new Random();

        /* Randomize card pile */
        while (leftOverCards > 0) {
            int index = rn.nextInt(leftOverCards );
            randomedCardPile.add(sortedCardPile.get(index));
            sortedCardPile.remove(index);
            leftOverCards--;
        }

        return randomedCardPile;
    }
}
