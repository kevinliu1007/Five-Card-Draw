package module;

import java.util.ArrayList;

/**
 * Created by kevinliu1007 on 8/31/17.
 */
public class OpponentPlayer extends Player {

    /**
     * Constructor for OpponentPlayer.
     */
    public OpponentPlayer() {
        super();
    }

    /**
     * AI's decision making process.
     *
     * @param cardPile cardPile
     */
    public void makeDecision(CardPile cardPile) {
        int value = evaluateHand();
        int diffSuit = almostSameSuit();
        int diffSeq = almostStraight();

        if (value < 8) {
            if (value == 1) {
                fourOfAKindDiscard(cardPile);
            } else if (value == 5) {
                threeOfAKindDiscard(cardPile);
            } else if (value == 6) {
                twoPairDiscard(cardPile);
            } else if (value == 7) {
                twoOfAKindDiscard(cardPile);
            }
        } else if (diffSuit != -1) {
            singleCardDiscard(diffSuit, cardPile);
        } else if (diffSeq != -1) {
            singleCardDiscard(diffSeq, cardPile);
        } else if (findAce()) {
            noneAceDiscard(cardPile);
        } else {
            lowCardDiscard(cardPile);
        }
    }

    /**
     * Discard a single card given index.
     *
     * @param index     index
     * @param cardPile  cardPile
     */
    private void singleCardDiscard(int index, CardPile cardPile) {
        ArrayList<Integer> cardOption = new ArrayList<>();

        cardOption.add(index);
        discardCards(1, cardOption, cardPile);
    }

    /**
     * Discard the lowest three card in player's hand.
     *
     * @param cardPile  cardPile
     */
    private void lowCardDiscard(CardPile cardPile) {
        ArrayList<Integer> cardOption = new ArrayList<>();

        cardOption.add(2);
        cardOption.add(3);
        cardOption.add(4);

        discardCards(3, cardOption, cardPile);
    }

    /**
     * Discard the card that is not four of a kind.
     *
     * @param cardPile  cardPile
     */
    private void fourOfAKindDiscard(CardPile cardPile) {
        ArrayList<Integer> cardOption = new ArrayList<>();

        if (fourOfAKindIndex() == 0) {
            cardOption.add(4);
            discardCards(1, cardOption, cardPile);
        } else {
            cardOption.add(0);
            discardCards(1, cardOption, cardPile);
        }
    }

    /**
     * Discard the two cards that is not the three of a kind.
     *
     * @param cardPile  cardPile
     */
    private void threeOfAKindDiscard(CardPile cardPile) {
        int index = threeOfAKindIndex();
        ArrayList<Integer> cardOption = new ArrayList<>();

        for (int i = 0; i < 5; ++i) {
            if ((i != index) && (i != index + 1) && (i != index + 2)) {
                cardOption.add(i);
            }
        }

        discardCards(2, cardOption, cardPile);
    }

    /**
     * Discard the card that is not part of two pair.
     *
     * @param cardPile  cardPile
     */
    private void twoPairDiscard(CardPile cardPile) {
        int index = singleIndex();
        ArrayList<Integer> cardOption = new ArrayList<>();

        cardOption.add(index);
        discardCards(1, cardOption, cardPile);
    }

    /**
     * Discard the three cards that is not part of the two of a kind.
     *
     * @param cardPile  cardPile
     */
    private void twoOfAKindDiscard(CardPile cardPile) {
        int index = twoOfAKindIndex();
        ArrayList<Integer> cardOption = new ArrayList<>();

        for (int i = 0; i < 5; ++i) {
            if ((i != index) && (i != index + 1)) {
                cardOption.add(i);
            }
        }

        discardCards(3, cardOption, cardPile);
    }

    /**
     * Discard rest four cards that is not Ace.
     *
     * @param cardPile  cardPile
     */
    private void noneAceDiscard(CardPile cardPile) {
        int count = 0;
        ArrayList<Card> hand = getCards();
        ArrayList<Integer> cardOption = new ArrayList<>();

        for (int i = 0; i < 5; ++i) {
            if (hand.get(i).getRawRank() != 1) {
                cardOption.add(i);
                count++;
            }
        }

        discardCards(count, cardOption, cardPile);
    }

    /**
     * Detect if hand is almost flush.
     *
     * @return  index of the card that has the different suit, -1 if not found
     */
    private int almostSameSuit() {
        int c = 0, d = 0, h = 0, s = 0;
        ArrayList<Card> hand = getCards();

        for (int i = 0; i < 4; ++i) {
            switch (hand.get(i).getSuit()) {
                case "C": c++; break;
                case "D": d++; break;
                case "H": h++; break;
                case "S": s++; break;
                default: break;
            }
        }

        if (c == 4 || d == 4|| h == 4 || s == 4) {
            if (c == 1) {
                return findDifferentSuit(hand, "C");
            } else if (d == 1) {
                return findDifferentSuit(hand, "D");
            } else if (h == 1) {
                return findDifferentSuit(hand, "H");
            } else if (s == 1) {
                return findDifferentSuit(hand, "S");
            }
        }

        return -1;
    }

    /**
     * Detect if hand is almost straight.
     *
     * @return  index of the card that is not in sequence, -1 if not found
     */
    private int almostStraight() {
        ArrayList<Card> hand = getCards();

        if (hand.get(0).getRawRank() - hand.get(3).getRawRank() == 5 ||
                hand.get(0).getRawRank() - hand.get(3).getRawRank() == 4) {
            return 4;
        } else if (hand.get(1).getRawRank() - hand.get(4).getRawRank() == 5 ||
                hand.get(1).getRawRank() - hand.get(4).getRawRank() == 4) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Find the index of the card that has the different suit.
     *
     * @param cards player's hand
     * @param suit  suit that is different
     *
     * @return  index of the card that has the different suit, -1 if not found
     */
    private int findDifferentSuit(ArrayList<Card> cards, String suit) {
        int index = 0;

        for (Card card : cards) {
            if (card.getSuit().equals(suit)) {
                return index;
            }
            index++;
        }

        return -1;
    }
}
