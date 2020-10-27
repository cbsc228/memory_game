import java.awt.event.*;
import java.util.Objects;
import java.util.Random;
import javax.swing.*;

public class Board
{
    // Array to hold board cards
    private FlippableCard cards[];

    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();

    public Board(int size, ActionListener AL)
    {
        // Allocate and configure the game board: an array of cards
        // Leave one extra space for the "happy face", added at the end
        cards = new FlippableCard[size];

        // Fill the Cards array
        int imageIdx = 1;
        for (int i = 0; i < (size-1); i += 2) {

            // Load the front image from the resources folder
            String imgPath = "res/mtg" + imageIdx + ".jpg";
            ImageIcon img = new ImageIcon(loader.getResource(imgPath));

            // Setup two cards at a time
            FlippableCard c1 = new FlippableCard(img);
            FlippableCard c2 = new FlippableCard(img);
            c1.addActionListener(AL);
            c2.addActionListener(AL);
            c1.setID(imageIdx);
            c2.setID(imageIdx);

            // Add them to the array
            cards[i] = c1;
            cards[i + 1] = c2;

            // get ready for the next pair of cards
            imageIdx++;
        }
        // Add the "happy face" image
        String imgPath = "res/happy-face.jpg";
        ImageIcon img = new ImageIcon(Objects.requireNonNull(loader.getResource(imgPath)));
        FlippableCard c1 = new FlippableCard(img);
        c1.addActionListener(AL);
        //sets id for the smiley face image so as not to read it as an actual card
        int smileyID = -1;
        c1.setID(smileyID);
        cards[size-1] = c1;

        Random random = new Random();

        for (int i = 0; i < cards.length; i++){
            int randomIndex = random.nextInt(cards.length);
            FlippableCard cardHolder = cards[randomIndex];
            cards[randomIndex] = cards[i];
            cards[i] = cardHolder;
        }

        for (int i = 0; i < cards.length; i++){
            cards[i].setEnabled(false);
        }
    }

    public void fillBoardView(JPanel view)
    {
        for (FlippableCard c : cards) {
            view.add(c);
        }
    }

    public void resetBoard()
    {
        //flip the cards back and re-enable any disabled buttons
        for (int i = 0; i < cards.length; i++){
            cards[i].hideFront();
            cards[i].setEnabled(true);
        }

        //randomize the buttons
        Random random = new Random();

        for (int i = 0; i < cards.length; i++){
            int randomIndex = random.nextInt(cards.length);
            FlippableCard cardHolder = cards[randomIndex];
            cards[randomIndex] = cards[i];
            cards[i] = cardHolder;
        }
    }
}
