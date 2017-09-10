package module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kevinliu1007 on 9/2/17.
 */
public class Player {

    /** Cards play holds. */
    private ArrayList<Card> playerCards;

    /**
     * Constructor for Player class.
     */
    public Player() {
        playerCards = new ArrayList<>();
    }

    /**
     * Add a card into player's hand.
     *
     * @param card  The card added to player's hand
     */
    final public void addCard(Card card) {
        playerCards.add(card);
    }

    /**
     * Get the hand of player.
     *
     * @return  playerCards
     */
    final public ArrayList<Card> getCards() {
        return playerCards;
    }

    /**
     * Display hands of the player.
     */
    final public void displayCards() {
        sortCards();

        for (int i = 0; i < 5; ++i) {
            Card currentCard = playerCards.get(i);
            System.out.print(i + ") " + currentCard.getRank() + currentCard.getSuit() + "  ");
        }
        System.out.println("\n");
    }

    /**
     * Find if player hands contains Ace.
     *
     * @return  True if has Ace, false if not
     */
    public boolean findAce() {
        for (Card cards : playerCards) {
            if (cards.getRawRank() == 1) return true;
        }

        return false;
    }

    /**
     * Discards a certain selected cards from player's hand.
     *
     * @param numberOfCards     Number of cards that is being discarded
     * @param cardsDiscarded    Selections of cards being discarded
     */
    final public void discardCards(final int numberOfCards,
                                   ArrayList<Integer> cardsDiscarded,
                                   CardPile cardPile) {
        /* Sort the order of cards user want to discard */
        Collections.sort(cardsDiscarded, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 < o2) {
                    return -1;
                } else if (o1 == o2) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        /* Discard the cards */
        while (!cardsDiscarded.isEmpty()) {
            cardPile.discardCard(playerCards.get(cardsDiscarded.get(cardsDiscarded.size()-1)));
            int index = cardsDiscarded.get(cardsDiscarded.size()-1);
            playerCards.remove(index);
            cardsDiscarded.remove(cardsDiscarded.size()-1);
        }

        /* Draw new cards */
        for (int i = 0; i < numberOfCards; ++i) {
            playerCards.add(cardPile.dealtCard());
        }

        sortCards();
    }

    /**
     * Evaluate player's hand, smaller the value it return, higher the hand is.
     *
     * @return value of the hand
     */
    public int evaluateHand() {
        if (findStraightFlush()) {
            return 0;
        } else if (findFourOfAKind()) {
            return 1;
        } else if (findFullHouse()) {
            return 2;
        } else if (findFlush()) {
            return 3;
        } else if (findStraight()) {
            return 4;
        } else if (findThreeOfAKind()) {
            return 5;
        } else if (findTwoPair()) {
            return 6;
        } else if (findTwoOfAKind()) {
            return 7;
        } else {
            return 8;
        }
    }

    /**
     * Find the starting index of the four of a kind.
     *
     * @return  index of the four of a kind
     */
    public int fourOfAKindIndex() {
        if (playerCards.get(0).getRawRank() != playerCards.get(1).getRawRank()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Find the starting index of the two of a kind in full house.
     *
     * @return  index of the two of a kind in full house
     */
    public int twoOfAKindIndex() {
        int count = 1, two = -1;

        for (int i = 0; i < 4; ++i) {
            if (playerCards.get(i).getRawRank() == playerCards.get(i+1).getRawRank()) {
                count++;
            } else {
                if (count == 3) {
                    count = 1;
                } else if (count == 2) {
                    two = i - 1;
                    return two;
                }
            }
        }

        return two;
    }

    /**
     * Find the starting index of the second two of a kind in two pair.
     *
     * @return  index of the two of a kind in full house
     */
    public int secondPairIndex() {
        int count = 1, two = -1;

        for (int i = 0; i < 4; ++i) {
            if (playerCards.get(i).getRawRank() == playerCards.get(i+1).getRawRank()) {
                count++;
            } else {
                if (count == 2) {
                    two = i - 1;
                    count = 1;
                }
            }
        }

        return two;
    }

    /**
     * Find the starting index of the three of a kind.
     *
     * @return  index of the three of a kind
     */
    public int threeOfAKindIndex() {
        int count = 1, three = -1;

        for (int i = 0; i < 4; ++i) {
            if (playerCards.get(i).getRawRank() == playerCards.get(i+1).getRawRank()) {
                count++;
            } else {
                if (count == 3) {
                    three = i - 2;
                    return three;
                }
            }
        }

        return three;
    }

    /**
     * Find the index of the single card out of two pair.
     *
     * @return  index of the single card out of two pair
     */
    public int singleIndex() {
        int firstPair = twoOfAKindIndex();
        int secondPair = secondPairIndex();
        int[] indices = new int[5];
        Arrays.fill(indices, 1);

        indices[firstPair] = 0;
        indices[firstPair+1] = 0;
        indices[secondPair] = 0;
        indices[secondPair+1] = 0;

        for (int i = 0; i < 5; ++i) {
            if (indices[i] == 1) {
                return i;
            }
        }

        return -1;
    }


    /**
     * Sort player hands base on rank of the cards.
     */
    private void sortCards() {
        Collections.sort(playerCards, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                if (o1.getRawRank() == 1) {
                    return -1;
                } else if (o2.getRawRank() == 1) {
                    return -1;
                } else if (o1.getRawRank() > o2.getRawRank()) {
                    return -1;
                } else if (o1.getRawRank() == o2.getRawRank()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
    }

    /**
     * Find if player hand contains flush.
     *
     * @return  true if player has flush, false else wise
     */
    private boolean findFlush() {
        String suit = playerCards.get(0).getSuit();

        for (int i = 1; i < 5; ++i) {
            if (!playerCards.get(i).getSuit().equals(suit)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Find if player hand contains straight.
     *
     * @return  true if player has straight, false else wise
     */
    private boolean findStraight() {
        int rank = playerCards.get(0).getRawRank();

        for (int i = 1; i < 5; ++i) {
            if (playerCards.get(i).getRawRank() - rank != 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Find if player hand contains straight flush.
     *
     * @return  true if player has straight flush, false else wise
     */
    private boolean findStraightFlush() {
        if (findFlush() && findStraight()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Find if player hand contains four of a kind.
     *
     * @return  true if player has four of a kind, false else wise
     */
    private boolean findFourOfAKind() {
        int count = 1;

        for (int i = 0; i < 4; ++i) {
            if (playerCards.get(i).getRawRank() == playerCards.get(i+1).getRawRank()) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 1;
            }
        }

        return false;
    }

    /**
     * Find if player hand contains full house.
     *
     * @return  true if player has full house, false else wise
     */
    private boolean findFullHouse() {
        if (playerCards.get(0).getRawRank() == playerCards.get(1).getRawRank()
                && playerCards.get(2).getRawRank() == playerCards.get(3).getRawRank()
                && playerCards.get(3).getRawRank() == playerCards.get(4).getRawRank()) {
            return true;
        } else if (playerCards.get(3).getRawRank() == playerCards.get(4).getRawRank()
                && playerCards.get(0).getRawRank() == playerCards.get(1).getRawRank()
                && playerCards.get(1).getRawRank() == playerCards.get(2).getRawRank()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Find if player hand contains three of a kind.
     *
     * @return  true if player has three of a kind, false else wise
     */
    private boolean findThreeOfAKind() {
        int count = 1;

        for (int i = 0; i < 4; ++i) {
            if (playerCards.get(i).getRawRank() == playerCards.get(i+1).getRawRank()) {
                count++;
                if (count == 3) {
                    return true;
                }
            } else {
                count = 1;
            }
        }

        return false;
    }

    /**
     * Find if player hand contains two pair.
     *
     * @return  true if player has two pair, false else wise
     */
    private boolean findTwoPair() {
        int count = 1, pair = 0;

        for (int i = 0; i < 4; ++i) {
            if (playerCards.get(i).getRawRank() == playerCards.get(i+1).getRawRank()) {
                count++;
                if (count == 2) {
                    pair++;
                }
            } else {
                count = 1;
            }
        }

        if (pair == 2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Find if player hand contains two of a kind.
     *
     * @return  true if player has two of a kind, false else wise
     */
    private boolean findTwoOfAKind() {
        int count = 1;

        for (int i = 0; i < 4; ++i) {
            if (playerCards.get(i).getRawRank() == playerCards.get(i+1).getRawRank()) {
                count++;
                if (count == 2) {
                    return true;
                }
            } else {
                count = 1;
            }
        }

        return false;
    }
}
