import java.util.*;
import java.util.Stack;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Solitaire class
 * @author Martin Bourdev
 * @version 11-3-18
 */
public class Solitaire  
{
    /**
     * Main method
     * @param args main method
     */
    public static void main(String[] args)
    {
        new Solitaire();
    }
    private static final int PILENUM = 7;
    private static final int FOUNDNUM = 4;
    private static final double FACE_DOWN_LENGTH = 0.05; //mouse distance between cascading 
                                                            //face-down cards
    private static final double FACE_UP_LENGTH = 0.10745; //mouse-distance between cascading
                                                            //face-up cards
    private static final double PILE_START = 1.06; //the distance between the top of  
                                            //the display and the top of the piles
    private Stack<Card> stock;
    private Stack<Card> waste;
    private Stack<Card>[] foundations;
    private Pile[] piles;
    private SolitaireDisplay display;
    private boolean isMusicPlaying;
    private int score;
    private Clip clip;
    private Stack<String> moves;
    /**
     * Sets up solitaire game
     */
    public Solitaire()
    {
        foundations = new Stack[FOUNDNUM];
        score = 0;
        for(int i = 0; i < FOUNDNUM; i++)
        {
            foundations[i] = new Stack();
        }
        piles = new Pile[PILENUM];
        for(int i = 0; i < PILENUM; i++)
        {
            piles[i] = new Pile();
        }
        stock = new Stack<Card>();
        waste = new Stack<Card>();
        moves = new Stack();
        //INSERT CODE HERE
        createStock();
        deal();
        System.out.flush();
        System.out.println("Welcome to solitaire.");
        System.out.println("Press t for the elapsed time.");
        System.out.println("Press r to reset the game.");
        System.out.println("Press p to toggle music.");
        System.out.println("Press z to undo.");
        System.out.println("Press s for your score");
        //System.out.println("Press h to move cards from piles to foundations");
        //System.out.println("FIX HELP AND UNDO");
        
        

        display = new SolitaireDisplay(this);
        //music
        try 
        {
            
            AudioInputStream audioInputStream = 
                AudioSystem.getAudioInputStream(new File("audio/holiday.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        //catch(UnsupportedAudioFileException e)
        //{
            //e.printStackTrace();
            //System.out.println("1");
        //}
        catch(Exception e)
        {
            System.out.println("2");
        }
        finally
        {
            
        }
    }
    /**
     * returns the card on top of the stock,
     * or null if the stock is empty
     * @return top of stock
     */
    public Card getStockCard()
    {
        if(!stock.isEmpty())   
            return stock.peek();
        return null;
    }
    /**
     * Returns score
     * @return score
     */
    public int score()
    {
        return score;
    }
    /**
     * Returns clip
     * @return clip
     */
    public Clip clip()
    {
        return clip;
    }
    /**
     * returns the card on top of the waste,
     * or null if the waste is empty
     * @return top of waste
     */
    public Card getWasteCard()
    {
        if(waste.isEmpty())
            return null;
        return waste.peek();
    }
    /**
     * Changes the number of face up cards of a given pile
     * @param index index of pile
     * @param num number to add 
     */
    public void addFaceUp(int index, int num)
    {
        piles[index].setFaceUpCards(piles[index].getFaceUpCards() + num);
    }
    /**
     * Changes the number of face down cards of a given pile
     * @param index index of pile
     * @param num number to add
     * 
     */
    public void addFaceDown(int index, int num)
    {
        piles[index].setFaceDownCards(piles[index].getFaceDownCards() + num);
    }
    /**
     * returns the top three cards on the waste
     * or null if the waste is empty
     * @return top 3 cards of waste backwards
     */
    public Card[] getWasteArray()
    {
        Card[] cards = new Card[3];
        if(waste.isEmpty())
            return cards;
        
        for(int i = 0; i < 3; i++)
        {
            if(!waste.isEmpty())
            {
                Card topCard = waste.pop();
                Card poppedCard = new Card(topCard.getRank(), topCard.getSuit(), true);
                cards[i] = poppedCard;
            }
            else
                cards[i] = null;
        }
        for(int i = 2; i > -1; i--)
        {
            if(cards[i] != null)
                waste.push(cards[i]);
        }
        return cards;
    }
    
    /**
    precondition:  0 <= index < 4
    postcondition: returns the card on top of the given
    foundation, or null if the foundation
    is empty
    @param index index of foundation to get
    @return card at the top of that foundation
     */
    public Card getFoundationCard(int index)
    {
        if(foundations[index].isEmpty())
            return null;
        else
            return foundations[index].peek();
    }

    /**precondition:  0 <= index < 7
     * postcondition: returns a reference to the given pile
     * @param index index of pile
     * @return pile at said index
     */

    public Pile getPile(int index)
    {
        return piles[index];
    }

    /**
     * Creates randomized pile of cards
     */
    private void createStock()
    {
        ArrayList<Card> deck = new ArrayList<Card>();
        for(int i = 1; i < 14; i++)
        {
            deck.add(new Card(i, "♦", false));
        }
        for(int i = 1; i < 14; i++)
        {
            deck.add(new Card(i, "♥", false));
        }
        for(int i = 1; i < 14; i++)
        {
            deck.add(new Card(i, "♣", false));
        }
        for(int i = 1; i < 14; i++)
        {
            deck.add(new Card(i, "♠", false));
        }
        for(int i = 0; i < 52; i++)
        {
            stock.push(deck.remove((int) (Math.random() * deck.size())));
        }
    }

    /**
     * Deals cards to piles
     */
    public void deal()
    {
        for(int i = 0; i < PILENUM; i++)
        {
            for(int j = 0; j < i+1; j++)
            {
                piles[i].getPile().push(stock.pop());
                addFaceDown(i, 1);
            }
            piles[i].getPile().peek().turnUp();
            addFaceDown(i, -1);
            addFaceUp(i, 1);
        }
    }

    /**
     * Deals three cards to waste, facing up
     */
    public void dealThreeCards()
    {
        
        for(int i = 0; i < 3; i++)
        {
            if(!stock.isEmpty())
            {
                Card card = stock.pop();
                card.turnUp();
                waste.push(card);
                //System.out.println(card.getRank() + card.getSuit());
            }
            display.repaint();
        }
        
    }

    /**
     * Moves all cards from waste to stock, facing down
     */
    public void resetStock()
    {
        while(!waste.isEmpty())
        {
            Card card = waste.pop();
            card.turnDown();
            stock.push(card);
            display.repaint();
        }
        
    }
    /**
     * called when the stock is clicked
     */
    public void stockClicked()
    {

        if(!display.isWasteSelected() && !display.isPileSelected())
        {
            //System.out.println("stock clicked");
            if(stock.isEmpty())
            {
                resetStock();
                moves.push("RS");
                //System.out.println(waste.isEmpty());
            }

            else
            {
                //System.out.println("From back to front, the waste has ");
                dealThreeCards();
                moves.push("DTC");
                //System.out.println("The top of the waste is the " + 
                //waste.peek().getRank() + " of " + waste.peek().getSuit());
                
            }

            //System.out.println(waste.peek().getFileName());
        }
        else
        {
            display.unselect();
        }
        display.repaint();
    }

    /**
     * called when the waste is clicked
     * 
     */
    public void wasteClicked()
    {
        //System.out.println("waste clicked");
        if(display.isWasteSelected())
        {
            for(int i = 0; i < 4; i++)
            {
                if(!waste.isEmpty())
                {
                    if(canAddToFoundation(waste.peek(), i))
                    {
                        foundations[i].push(waste.pop());
                        moves.push("WTF" + i);
                        score += 10;
                    }
                }
            }
            display.unselect();
            
        }
        else
        {
            if (!waste.isEmpty() && !display.isPileSelected())
            {
                display.selectWaste();
                //System.out.println("Waste selected.");
                //System.out.println(display.isWasteSelected());
            }
        }
        display.repaint();
    }

    /**
     * Performs actions when foundation is clicked
     * @param index index of foundation
     */
    public void foundationClicked(int index)
    {
        //IMPLEMENT ME
        
        if(display.isWasteSelected())
        {
            if(canAddToFoundation(waste.peek(), index))
            {    
                foundations[index].push(waste.pop());
                moves.push("WTF" + index);
                display.unselect();
                
                //System.out.println("Moving card to foundation " + index);
                if(checkIfWon())
                    System.out.println("CONGRATS U WON THE GAME!!!");
            }
        }
        else if(display.isPileSelected())
        {
            if(canAddToFoundation(piles[display.selectedPile()].getPile().peek(), index))
            {
                foundations[index].push(piles[display.selectedPile()].getPile().pop());
                moves.push("!" + display.selectedPile() + index); //! = pile to foundation
                //piles[display.selectedPile()].faceUpCards--;
                addFaceUp(display.selectedPile(), -1);
                score += 10;
                //score += foundations[index].peek().getRank();
                display.unselect();
                
                //System.out.println("Moving card to foundation " + index);
                if(checkIfWon())
                    System.out.println("CONGRATS U WON THE GAME!!!");
            }
        }
        else if(display.isFoundationSelected())
        {
            if(display.selectedFoundation() == index) 
            {
                if(!waste.isEmpty() && canAddToFoundation(waste.peek(), index))
                {
                    foundations[index].push(waste.pop());
                    moves.push("WTF");
                    display.unselect();
                    //System.out.println("Moving card to foundation " + index);
                    if(checkIfWon())
                        System.out.println("CONGRATS U WON THE GAME!!!");
                }
                display.unselect();
            }
            
        }
        else
        {
            if(!foundations[index].isEmpty())
            {
                //System.out.println("Foundation selected");
                display.selectFoundation(index);
            }
        }
        display.repaint();
    }

    /**
     * Called when given pile is clicked
     * @param index index of clicked pile
     * @param offset Y value of mouse click
     */
    public void pileClicked(int index, double offset)
    {
        //IMPLEMENT ME
        //System.out.println("Pile #" + index + " clicked");
        //System.out.println(piles[index].getFaceUpCards());
        //System.out.println(piles[index].getFaceDownCards());
        //System.out.println(calcSelectedCards(index, offset));
        if(display.isWasteSelected())
        {
            //System.out.println(waste.peek().getRank());
            if(canAddToPile(waste.peek(), index))
            {
                Card card = waste.pop();
                //System.out.println("Moving " + card.getRank() + 
                 //                   card.getSuit() + " to pile " + index);
                piles[index].getPile().push(card);
                //piles[index].faceUpCards++;
                addFaceUp(index, 1);
                moves.push("WTP" + index);
                score += 20;
                display.unselect();
            }
            else
                display.unselect();
        }
        else
        {
            if(display.isPileSelected())
            {
                if(display.selectedPile() == index)
                {
                    //System.out.println("4");
                    for(int i = 0; i < 4; i++)
                    {
                        if(!piles[display.selectedPile()].getPile().isEmpty())
                        {
                            if(canAddToFoundation(
                                    piles[display.selectedPile()].getPile().peek(), i))
                            {
                                piles[display.selectedPile()].getPile().peek().turnUp();
                                foundations[i].push(piles[display.selectedPile()].getPile().pop());
                                moves.push("!" + index + i);
                                //piles[display.selectedPile()].faceUpCards--;
                                addFaceUp(display.selectedPile(), -1);
                                display.repaint();
                                //System.out.println("Moving card to foundation " + index);
                                if(checkIfWon())
                                    System.out.println("CONGRATS U WON THE GAME!!!");
                                
                            }
                        }
                        //display.unselect();
                    }
                    
                    display.unselect();
                }
                else
                {
                    
                    //System.out.println("1");
                    
                    int selectedCardsFromTop = piles[display.selectedPile()].getFaceUpCards() + 
                            piles[display.selectedPile()].getFaceDownCards() -
                            calcSelectedCards(display.selectedPile(), display.selectedRow());
                    Stack<Card> pileStack = 
                            removeFaceUpCards(display.selectedPile(), selectedCardsFromTop + 1);
                    //pileStack = removeFaceUpCards(display.selectedPile());
                    //System.out.println(selectedCardsFromTop);
                    //System.out.println(pileStack.size());
                    //System.out.println("6");
                    if(canAddToPile(pileStack.peek(), index))
                    {
                        //System.out.println("2");
                        moves.push("@" + display.selectedPile() + 
                                    index + pileStack.peek().getRank());
                        addToPile(pileStack, index);
                        //System.out.println("A" + display.selectedPile());
                        //System.out.println("B" + index);
                        //System.out.println("C" + pileStack.peek().getRank());
                        
                        
                        display.unselect();
                        display.repaint();
                    }
                    else
                    {
                        //System.out.println("3");
                        addToPile(pileStack, display.selectedPile());
                        display.unselect();
                        
                    }
                    //System.out.println("5");
                    
                    
                }
            }
            else if(display.isFoundationSelected())
            {
                //System.out.println("1");
                if(canAddToPile(foundations[display.selectedFoundation()].peek(), index))
                {
                    moves.push("#" + display.selectedFoundation() + index);
                    //piles[index].faceUpCards++;
                    addFaceUp(index, 1);
                    piles[index].getPile().push(foundations[display.selectedFoundation()].pop());
                    display.unselect();
                    score -= 10;
                }
                else
                {
                    display.unselect();
                }
            }
            else
            {
                if(!piles[index].getPile().isEmpty())
                {
                    if(!piles[index].getPile().peek().isFaceUp())
                    {
                        piles[index].getPile().peek().turnUp();
                        score += 50;
                        //System.out.println("weee");
                        //piles[index].faceUpCards++;
                        //piles[index].faceDownCards--;
                        addFaceUp(index, 1);
                        addFaceDown(index, -1);
                        moves.push("TU" + index);
                        //System.out.println("Turning up " + piles[index].peek().getRank() +
                            //piles[index].peek().getSuit() + " on pile " + index);
                    }
                    else
                    {
                        display.selectPile(index, offset);
                        //System.out.println(calcSelectedCards(index, offset));
                        //System.out.println("Pile " + index + " is selected.");
                    }
                }
                
            }
        }
        display.repaint();
        //display.unselect();
    }

    /**
     * Checks if given card can be moved to given pile
     * @param card given card
     * @param index index of pile
     * @return true if card can be moved
     */
    public boolean canAddToPile(Card card, int index)
    {
        if(piles[index].getPile().isEmpty())
            return card.getRank() == 13;
        else
        {
            Card topCard = piles[index].getPile().peek();
            boolean suitMatch;
            if(topCard.getSuit().equals("♣") || topCard.getSuit().equals("♠"))
            {
                if(card.getSuit().equals("♥") || card.getSuit().equals("♦"))
                    suitMatch = true;
                else
                    return false;
            }
            else 
            {
                if(card.getSuit().equals("♣") || card.getSuit().equals("♠"))
                    suitMatch = true;
                else
                    return false;
            }
            if(topCard.getRank() == card.getRank() + 1 && suitMatch)
                return true;
            return false;   
        }

    }

    /**
     * Removes some the face up cards in a given pile
     * @param index index of the pile
     * @param num number of cards to move
     * @return stack of face up cards
     */
    public Stack<Card> removeFaceUpCards(int index, int num)
    {
        Stack<Card> cards = new Stack<Card>();
        
        
        while(piles[index].getPile().peek().isFaceUp() && num > 0)
        {
            cards.push(piles[index].getPile().pop());
            //piles[index].faceUpCards--;
            addFaceUp(index, -1);
            num--;
            if(piles[index].getPile().isEmpty())
                return cards;
        }
        
        return cards;
    }

    /**
     * Adds given stack of cards to given pile
     * @param cards stack of cards to add
     * @param index index of pile
     */
    public void addToPile(Stack<Card> cards, int index)
    {
        while(!cards.isEmpty())
        {
            piles[index].getPile().push(cards.pop());
            //piles[index].faceUpCards++;
            addFaceUp(index, 1);
        }
    }

    /**
     * Tests if given card can be moved to given foundation
     * @param card given card
     * @param index index of foundation
     * @return true if card can be moved
     */
    public boolean canAddToFoundation(Card card, int index)
    {
        if(foundations[index].isEmpty())
            return card.getRank() == 1;
        else
        {
            return(card.getSuit().equals(foundations[index].peek().getSuit()) && 
            card.getRank() == foundations[index].peek().getRank() + 1);
            
        }
    }
    
    /**
     * Checks if player has won
     * @return true if player has won
     */
    public boolean checkIfWon()
    {
        for(int i = 0; i < 4; i++)
        {
            if(!foundations[i].isEmpty())
                if (!(foundations[i].peek().getRank() == 13))
                    return false;
        }
        return true;
    }
    
    /**
     * Automatically makes legal foundation moves
     */
    public void help()
    {
        //System.out.println("helping");
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; i < 7; j++)
            {
                if(canAddToFoundation(piles[j].getPile().peek(), i))
                {
                    //System.out.println("1" + j);
                    foundations[i].push(piles[j].getPile().pop());
                    display.repaint();
                    display.unselect();
                }
            }
            if(canAddToFoundation(waste.peek(), i))
            {
                //System.out.println("2");
                foundations[i].push(waste.pop());
                display.repaint();
                display.unselect();
            }
        }
        display.repaint();
        display.unselect();
    }
    
    /**
     * Undoes a move
     */
    public void undo()
    {
        score -= 50;
        //System.out.println(moves.peek());
        if(!moves.isEmpty())
        {
            String str = moves.pop();
            if(str.equals("RS")) //Reset stock
            {
                //System.out.println("df");
                while(!stock.isEmpty())
                {
                    Card card = stock.pop();
                    card.turnDown();
                    waste.push(card);
                    //System.out.println("df");
                }
                display.repaint();
            }
            else if(str.equals("DTC")) //Deal three cards
            {
                for(int i = 0; i < 3; i++)
                {
                    if(!waste.isEmpty())
                    {
                        Card card = waste.pop();
                        card.turnDown();
                        stock.push(card);
                    }
                }
            }
            else if(str.equals("WTF0") || str.equals("WTF1") 
                        || str.equals("WTF2") || str.equals("WTF3")) //Waste to foundation
            {
                System.out.println(str.substring(3));
                waste.push(foundations[Integer.valueOf(str.substring(3))].pop());
            }
            else if(str.substring(0, 3).equals("WTP"))
            {
                waste.push(piles[Integer.valueOf(str.substring(3))].getPile().pop());
                //piles[Integer.valueOf(str.substring(3))].faceUpCards--;
                addFaceUp(Integer.valueOf(str.substring(3)), -1);
            }
            else if(str.substring(0, 1).equals("!")) //Pile (_x_) to foundation (__y)
            {
                //foundations[Integer.valueOf(str.substring(2))].push
                    //(piles[Integer.valueOf(str.substring(1, 2))].pop());
                piles[Integer.valueOf(str.substring(1, 2))].getPile().
                    push(foundations[Integer.valueOf(str.substring(2))].pop());
                //piles[Integer.valueOf(str.substring(1, 2))].faceUpCards++;
                addFaceUp(Integer.valueOf(str.substring(1,2)), 1);
            }
            else if(str.substring(0, 1).equals("@")) //Pile (_x__) to pile (__y_) up to card (___w)
            {
                int startPile = Integer.valueOf(str.substring(1, 2));
                int endPile = Integer.valueOf(str.substring(2, 3));
                int upTo = Integer.valueOf(str.substring(3));
                Stack<Card> movedPile = new Stack<Card>();
                while(piles[endPile].getPile().peek().getRank() != upTo)
                {
                    movedPile.push(piles[endPile].getPile().pop());
                    //piles[endPile].faceUpCards--;
                    addFaceUp(endPile, -1);
                }
                movedPile.push(piles[endPile].getPile().pop());
                //piles[endPile].faceUpCards--;
                addFaceUp(endPile, -1);
                while(!movedPile.isEmpty())
                {
                    piles[startPile].getPile().push(movedPile.pop());
                    //piles[startPile].faceUpCards++;
                    addFaceUp(startPile, 1);
                }
            }
            else if(str.substring(0, 2).equals("TU")) //Turned up pile (TUx)
            {
                piles[Integer.valueOf(str.substring(2))].getPile().peek().turnDown();
                //piles[Integer.valueOf(str.substring(2))].faceUpCards--;
                addFaceUp(Integer.valueOf(str.substring(2)), -1);
                //piles[Integer.valueOf(str.substring(2))].faceDownCards++;
                addFaceDown(Integer.valueOf(str.substring(2)), 1);
            }
            else if(str.substring(0, 1).equals("#")) //Foundation (#x_) to pile (#_x)
            {
                piles[Integer.valueOf(str.substring(2))].getPile().
                        push(foundations[Integer.valueOf(str.substring(1, 2))].pop());
                //piles[Integer.valueOf(str.substring(2))].faceUpCards++;
                addFaceUp(Integer.valueOf(str.substring(2)), 1);
            }
            
        }
        display.repaint();
    }
    /**
     * Calculates the number of selected cards from the bottom of the stack 
     * only works when clicking on face up cards
     * @param index index of pile
     * @param offset y value of click
     * @return number of selected cards from the bottom of the stack, including down-facing cards
     */
    public int calcSelectedCards(int index, double offset)
    {
        Pile pile = getPile(index);
        double faceDownOffset = pile.getFaceDownCards() * FACE_DOWN_LENGTH;
        int numFaceUpSelected = 0;
        double selection = offset - PILE_START - faceDownOffset;
        while(selection > FACE_UP_LENGTH)
        {
            selection -= FACE_UP_LENGTH;
            numFaceUpSelected++;
        }
        if(numFaceUpSelected == 0 && selection < FACE_UP_LENGTH)
            numFaceUpSelected++;
        int numSelectedCard = numFaceUpSelected + pile.getFaceDownCards();
        if(numSelectedCard > (pile.getFaceDownCards() + pile.getFaceUpCards()))
            return pile.getFaceDownCards() + pile.getFaceUpCards();
        return numSelectedCard;
    }
}