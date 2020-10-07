
/**
 * Card class
 * 
 * @author Martin Bourdev 
 * @version 10-31-18
 */
public class Card
{
    // instance variables - replace the example below with your own
    private int rank;
    private String suit;
    private boolean isFaceUp;

    /**
     * Constructor for objects of class Card
     * @param r rank
     * @param s suit
     * @param ifu isFaceUp
     */
    public Card(int r, String s, boolean ifu)
    {
        // initialise instance variables
        rank = r;
        suit = s;
        isFaceUp = ifu;
    }

    /**
     * Returns the rank of the card
     * @return int rank
     */
    public int getRank()
    {
        return rank;
    }
    
    /**
     * Returns the suit of the card
     * @return String suit
     */
    public String getSuit()
    {
        return suit;
    }
    
    /**
     * Returns true if the card is red
     * @return true if card is red
     */
    public boolean isRed()
    {
        return suit.equals("♦") || suit.equals("♥");
    }
    
    /**
     * Return true if the card is facing up
     * @return isFaceUp
     */
    public boolean isFaceUp()
    {
        return isFaceUp;
    }
    
    /**
     * Turns the card up
     */
    public void turnUp()
    {
        isFaceUp = true;
    }
    
    /**
     * Turns the card down
     */
    public void turnDown()
    {
        isFaceUp = false;
    }
    
    /**
     * Returns the file name of a card based on its properties
     * @return file name of card image
     */
    public String getFileName()
    {
        if(!isFaceUp())
            return "cards/back.gif";
        String rankString = "";
        if(rank == 1)
            rankString = "a";
        else if(rank == 10)
            rankString = "t";
        else if(rank == 11)
            rankString = "j";
        else if(rank == 12)
            rankString = "q";
        else if(rank == 13)
            rankString = "k";
        else
            rankString = Integer.toString(rank);
        
        String suitString;
        if(suit.equals("♣"))
            suitString = "c";
        else if(suit.equals("♦"))
            suitString = "d";
        else if(suit.equals("♥"))
            suitString = "h";
        else
            suitString = "s";
        return "cards/" + rankString + suitString + ".gif";
    }
}
