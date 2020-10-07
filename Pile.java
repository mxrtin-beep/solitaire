import java.util.*;
import java.util.Stack;
/**
 * Pile class
 * 
 * @author Martin Bourdev
 * @version 11-22-18
 */
public class Pile
{
    // instance variables - replace the example below with your own
    private int faceUpCards;
    private int faceDownCards;
    private Stack<Card> pile;

    /**
     * Constructor for objects of class Pile
     */
    public Pile()
    {
        faceUpCards = 0;
        faceDownCards = 0;
        pile = new Stack<Card>();

    }
    /**
     * Returns the pile
     * @return pile
     */
    public Stack<Card> getPile()
    {
        return pile;
    }
    /**
     * Sets the number of face up cards
     * @param num number to set it to
     */
    public void setFaceUpCards(int num)
    {
        faceUpCards = num;
    }
    /**
     * Sets the number of face down cards
     * @param num the number of face down cards
     */
    public void setFaceDownCards(int num)
    {
        faceDownCards = num;
    }
    /**
     * @return number of face up cards
     */
    public int getFaceUpCards()
    {
        return faceUpCards;
    }
    /**
     * @return number of face down cards
     */
    public int getFaceDownCards()
    {
        return faceDownCards;
    }
    
    
}
