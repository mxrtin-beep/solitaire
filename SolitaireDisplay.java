import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
/**
 * Solitaire display
 * @author Anu Datar
 * @version 11-4-18
 */
public class SolitaireDisplay extends JComponent implements MouseListener, KeyListener
{
    private static final int CARD_WIDTH = 73;
    private static final int CARD_HEIGHT = 97;
    private static final int SPACING = 5;  //distance between cards
    private static final int FACE_UP_OFFSET = 15;  //distance for cascading face-up cards
    private static final int FACE_DOWN_OFFSET = 5;  //distance for cascading face-down cards
    private static final int WASTE_SPACING = 13; //distance between waste cards
    private static final double FACE_DOWN_LENGTH = 0.05; //mouse distance between cascading 
                                                            //face-down cards
    private static final double FACE_UP_LENGTH = 0.10745; //mouse-distance between cascading
                                                            //face-up cards
    private static final double PILE_START = 1.06; //the distance between the top of  
                                            //the display and the top of the piles
    private static final double CONVERSION =  //Conversion between mouse coords and distance
                                104.144;                                                        
    private JFrame frame;
    private double selectedRow = -1.0;
    private int selectedCol = -1;
    private Solitaire game;
    private double startTime = System.currentTimeMillis();
    private double endTime = System.currentTimeMillis();
    
    
    /**
     * Solitaire display
     * @param game game of solitaire
     */
    public SolitaireDisplay(Solitaire game)
    {
        this.game = game;

        frame = new JFrame("Solitaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);

        this.setPreferredSize(new Dimension((int) CARD_WIDTH * 7 + (int) SPACING * 8, 
                               (int) CARD_HEIGHT * 2 + (int) SPACING * 3 + 
                               (int) FACE_DOWN_OFFSET * 7 + 
                                13 * (int) FACE_UP_OFFSET));
        this.addMouseListener(this);
        frame.addKeyListener(this);
        frame.pack();
        frame.setVisible(true);
    }
    /**
     * Paint component
     * @param g graphic
     */
    public void paintComponent(Graphics g)
    {
        //background
        g.setColor(Color.PINK);
        g.fillRect(0, 0, getWidth(), getHeight());

        //face down
        drawCard(g, game.getStockCard(), SPACING, SPACING);

        //stock
        
        
        for(int i = 2; i > -1; i--)
        {
            drawCard(g, game.getWasteArray()[i], SPACING * 2 + (int) CARD_WIDTH 
                        + (2-i)*WASTE_SPACING, SPACING);
        }
        
        //drawCard(g, game.getWasteCard(), SPACING * 2 + CARD_WIDTH, SPACING);
        if (selectedRow >= 0 && selectedRow <= 1 && selectedCol == 1)
            drawBorder(g, SPACING * 2 + CARD_WIDTH + WASTE_SPACING*2, SPACING);

        //aces
        for (int i = 0; i < 4; i++)
            drawCard(g, game.getFoundationCard(i), SPACING * (4 + i) + 
                (int) CARD_WIDTH * (3 + i), SPACING);

        if(selectedRow == 0 && selectedCol > 2)
            drawBorder(g, selectedCol*(SPACING + CARD_WIDTH) + SPACING, SPACING);
        //piles
        
        for (int i = 0; i < 7; i++)
        {
            Pile pile = game.getPile(i);
            int offset = 0;
            
            //Calculates number of cards from the bottom of the stack are selected based on
            //The selectedRow
            int numSelectedCards = game.calcSelectedCards(i, selectedRow);
            int numSelectedUpCards = numSelectedCards - pile.getFaceDownCards();
            int faceDownOffset = pile.getFaceDownCards()*FACE_DOWN_OFFSET;
            int faceUpOffset = (numSelectedUpCards - 1)*FACE_UP_OFFSET;
            for (int j = 0; j < pile.getPile().size(); j++)
            {
                drawCard(g, pile.getPile().get(j), SPACING + ((int) CARD_WIDTH + SPACING) * i, 
                    (int) CARD_HEIGHT + 2 * SPACING + offset);
                if (selectedRow >= (PILE_START + pile.getFaceDownCards()*FACE_DOWN_LENGTH) 
                                        && selectedCol == i)
                    drawBorder(g, SPACING + (CARD_WIDTH + SPACING) * i, 
                        CARD_HEIGHT + 2 * SPACING + faceDownOffset + faceUpOffset);

                if (pile.getPile().get(j).isFaceUp())
                    offset += FACE_UP_OFFSET;
                else
                    offset += FACE_DOWN_OFFSET;
            }
        }
    }

    /**
     * Draws cards
     * @param g graphic
     * @param card card
     * @param x x coordinate
     * @param y y coordinate
     */
    private void drawCard(Graphics g, Card card, int x, int y)
    {
        if (card == null)
        {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        }
        else
        {
            String fileName = card.getFileName();
            if (!new File(fileName).exists())
                throw new IllegalArgumentException("bad file name:  " + fileName);
            Image image = new ImageIcon(fileName).getImage();
            g.drawImage(image, x, y, CARD_WIDTH, CARD_HEIGHT, null);
        }
    }

    /**
     * Called when key is typed
     * @param e key
     */
    public void keyTyped(KeyEvent e)
    {
        //System.out.println("KEY pressed");
        if(e.getKeyChar() == 't' || e.getKeyChar() == 'T')
        {
            endTime = System.currentTimeMillis();
            System.out.println("The elapsed time is " + Double.toString((endTime - startTime)/1000)
                                 + " seconds.");
        }
        if(e.getKeyChar() == 'r' || e.getKeyChar() == 'R')
        {
            game.clip().stop();
            game = new Solitaire();
            game.clip().stop();
        }
        if(e.getKeyChar() == 'h' || e.getKeyChar() == 'H')
            game.help();
        if(e.getKeyChar() == 'p' || e.getKeyChar() == 'P')
        {    
            if(game.clip().isRunning())
                game.clip().stop();
            else
                game.clip().start();
        }
        if(e.getKeyChar() == 'z' || e.getKeyChar() == 'Z')
        {
            game.undo();
            repaint();
        }
        if(e.getKeyChar() == 's' || e.getKeyChar() == 'S')
        {
            endTime = System.currentTimeMillis();
            double timeDiff = Double.valueOf((endTime - startTime)/1000);
            int intTimeDiff = (int) timeDiff;
            System.out.println(game.score() - intTimeDiff);
        }
    }
    
    /**
     * Called when key is pressed
     * @param e key
     */
    public void keyPressed(KeyEvent e)
    {
    }
    
    /**
     * Called when key is released
     * @param e key
     */
    public void keyReleased(KeyEvent e)
    {
    }
    
    /**
     * Called when mouse leaves display
     * @param e mouse 
     */
    public void mouseExited(MouseEvent e)
    {
        //endTime = System.currentTimeMillis();
        //System.out.println("The current time is " + Double.toString((endTime - startTime)/1000));
        
    }

    /**
     * Called when mouse enters display
     * @param e mouse 
     */
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * Called when mouse is released
     * @param e mouse
     */
    public void mouseReleased(MouseEvent e)
    {
    }

    /**
     * Called when mouse is pressed 
     * @param e mouse
     */
    public void mousePressed(MouseEvent e)
    {
    }

    /**
     * Called when mouse is clicked 
     * @param e mouse
     */
    public void mouseClicked(MouseEvent e)
    {

        //none selected previously
        
        int col = e.getX() / (SPACING + CARD_WIDTH);
        double row = e.getY() / (double) (SPACING + CARD_HEIGHT);
        //System.out.println("X: " + col);
        //System.out.println("Y: " + row);
        //if (row > 1)
        //    row = 1;
        if (col > 6)
            col = 6;

        if (row < 1 && col == 0)
            game.stockClicked();
        else if (row < 1 && (col == 1 || col == 2))
            game.wasteClicked();
        else if (row > 0.0 && row < 1.0 && col >= 3)
            game.foundationClicked(col - 3);
        else if (row >= 1)
            game.pileClicked(col, row);
        repaint();
    }

    /**
     * Draws border
     * @param g graphic
     * @param x x coordinate
     * @param y y coordinate
     */
    private void drawBorder(Graphics g, int x, int y)
    {
        g.setColor(Color.YELLOW);
        g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        g.drawRect(x + 1, y + 1, CARD_WIDTH - 2, CARD_HEIGHT - 2);
        g.drawRect(x + 2, y + 2, CARD_WIDTH - 4, CARD_HEIGHT - 4);
    }

    /**
     * Unselects everything
     */
    public void unselect()
    {
        selectedRow = -1;
        selectedCol = -1;
    }
    
    /**
     * @return true if any foundation is selected
     */
    public boolean isFoundationSelected()
    {
        return selectedRow == 0 && selectedCol > 2;
    }
    
    /**
     * Called when a foundation is selected
     * @param index index of foundation 
     */
    public void selectFoundation(int index)
    {
        selectedRow = 0;
        selectedCol = index + 3; //Foundations are (0, 3), (0, 4), (0, 5), and (0, 6)
    }
    
    /**
     * Returns index of selected foundation
     * @return index of selected foundation if foundation is selected
     *      -1 if no foundation selected
     */
    public int selectedFoundation()
    {
        if(selectedRow == 0 && selectedCol > 2)
            return selectedCol - 3;
        else
            return -1;
    }
    
    /**
     * @return true if waste is selected
     */
    public boolean isWasteSelected()
    {
        return selectedRow >= 0 && selectedRow < 1 && (selectedCol == 1 || selectedCol == 2);
    }

    /**
     * Selects waste
     */
    public void selectWaste()
    {
        selectedRow = 0;
        selectedCol = 1;
    }

    /**
     * @return true if any pile is selected
     */
    public boolean isPileSelected()
    {
        if(selectedCol == -1) 
        {
            return false;
        }
        Pile pile = game.getPile(selectedCol);
        return selectedRow >= (1.11 + pile.getFaceDownCards()*FACE_DOWN_LENGTH);
    }
    
    /**
     * @return index of selected pile
     *      -1 if no pile selected
     */
    public int selectedPile()
    {
        if (selectedRow >= 1)
            return selectedCol;
        else
            return -1;
    }
 
    /**
     * Selects pile given index
     * @param index given index
     * @param offset of the card selected from the bottom of the pile
     */
    public void selectPile(int index, double offset)
    {
        selectedRow = offset;
        selectedCol = index;
    }
    
    /**
     * @return selected row
     */
    public double selectedRow()
    {
        return selectedRow;
    }
}