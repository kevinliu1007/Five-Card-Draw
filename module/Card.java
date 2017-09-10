package module;

/**
 * Created by kevinliu1007 on 8/31/17.
 */

/**
 * Public class of Card.
 */
public class Card {

    /** Rank of the card. */
    private int rank;

    /** Suit of the card. */
    private String suit;

    /**
     * Constructor of Card class.
     *
     * @param rank  Rank of the card
     * @param suit  Suit of the card
     */
    public Card(int rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Setter for rank.
     *
     * @param rank  Rank of the card
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Setter for suit.
     *
     * @param suit  Suit if the card
     */
    public void setSuit(String suit) {
        this.suit = suit;
    }

    /**
     * Getter for rank.
     *
     * @return  Rank of the card in string format
     */
    public String getRank() {
        if (rank == 1 || rank == 14) {
            return "A";
        } else if (rank == 10) {
            return "T";
        } else if (rank == 11) {
            return "J";
        } else if (rank == 12) {
            return "Q";
        } else if (rank == 13) {
            return "K";
        } else {
            return Integer.toString(rank);
        }
    }

    /**
     * Getter for rank in int format.
     *
     * @return  Ran of the card in int format
     */
    public int getRawRank() {
        return rank;
    }

    /**
     * Getter for suit.
     *
     * @return  Suit of the card
     */
    public String getSuit() {
        return suit;
    }
}
