import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MemoryGame extends JFrame implements ActionListener
{
    // Core game play objects
    private Board gameBoard;
    private FlippableCard prevCard1, prevCard2;

    // Labels to display game info
    private JLabel errorLabel, timerLabel;

    // layout objects: Views of the board and the label area
    private JPanel boardView, labelView;

    // Record keeping counts and times
    private int clickCount = 0, gameTime = 0, errorCount = 0;
    private int pairsFound = 0;

    //array used to check for matching cards
    private FlippableCard[] matchList = new FlippableCard[2];
    private FlippableCard[] prevMatchList = new FlippableCard[2];

    public MemoryGame()
    {
        // Call the base class constructor
        super("Magic the Gathering Memory Game");

        // Allocate the interface elements
        JButton restart = new JButton("Start");
        JButton quit = new JButton("Quit");
        timerLabel = new JLabel("Guesses: 0");
        errorLabel = new JLabel("Matches: 0");

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton restart = (JButton)e.getSource();
                restart.setText("Restart");
                restartGame();
            }
        });

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit( 0 );
            }
        });

        // Allocate two major panels to hold interface
        labelView = new JPanel();  // used to hold labels
        boardView = new JPanel();  // used to hold game board

        // get the content pane, onto which everything is eventually added
        Container c = getContentPane();

        // Setup the game board with cards
        //buttonPress buttonPress = new buttonPress();
        gameBoard = new Board(25, this::actionPerformed);//buttonPress);

        // Add the game board to the board layout area
        boardView.setLayout(new GridLayout(5, 5, 2, 0));
        gameBoard.fillBoardView(boardView);

        // Add required interface elements to the "label" JPanel
        labelView.setLayout(new GridLayout(1, 4, 2, 2));
        labelView.add(quit);
        labelView.add(restart);
        labelView.add(timerLabel);
        labelView.add(errorLabel);

        // Both panels should now be individually layed out
        // Add both panels to the container
        c.add(labelView, BorderLayout.NORTH);
        c.add(boardView, BorderLayout.SOUTH);

        setSize(745, 700);
        setVisible(true);
    }

    // Handle anything that gets clicked and that uses MemoryGame as an ActionListener
    public void actionPerformed(ActionEvent e) {
        // Get the currently clicked card from a click event
        FlippableCard currCard = (FlippableCard) e.getSource();

        //hide the previous guess
        if (prevMatchList[0] != null && prevMatchList[1] != null){
            prevMatchList[0].hideFront();
            prevMatchList[1].hideFront();
        }

        //show the clicked card
        currCard.showFront();

        //get the user input, ignoring the smiley face input
        if (currCard.id() != -1){
            System.out.println(matchList[0]);
            if (matchList[0] == null){
                //get the first choice in pair
                matchList[0] = currCard;
            }
            else if (matchList[1] == null){
                //get the second choice in pair
                matchList[1] = currCard;
            }
        }

        //makes sure you can't match a card with itself
        if (currCard.id() != -1){
            if (matchList[0] == matchList[1]){
                matchList[0].hideFront();
                matchList[0] = null;
                matchList[1] = null;
            }
        }


        //check if there is a match if there are two cards selected
        if (matchList[0] != null && matchList[1] != null){
            if (matchList[0].id() == matchList[1].id()){
                //the pair is a match
                matchList[0].setEnabled(false);
                matchList[1].setEnabled(false);
                pairsFound++;
                clickCount++;
                timerLabel.setText("Matches: " + pairsFound);
            }
            else{
                //the pair is not a match
                //store the incorrect guess to flip back in the next action
                prevMatchList[0] = matchList[0];
                prevMatchList[1] = matchList[1];
                clickCount++;
                errorLabel.setText("Errors: " + clickCount);
            }
            matchList[0] = null;
            matchList[1] = null;
        }

        //check to see if user has won the game
        if (pairsFound == 12){
            //the user has won the game
            JFrame winScreen = new JFrame();
            JTextArea winText = new JTextArea("You win! Thank you for playing!");
            winText.setEditable(false);
            winScreen.add(winText);
            winScreen.setSize(200, 200);
            winScreen.setVisible(true);
        }
    }


    private void restartGame()
    {
        pairsFound = 0;
        clickCount = 0;
        errorCount = 0;
        timerLabel.setText("Matches: 0");
        errorLabel.setText("Guesses: 0");

        // Clear the boardView and have the gameBoard generate a new layout
        boardView.removeAll();
        gameBoard.resetBoard();
        gameBoard.fillBoardView(boardView);
    }

    public static void main(String args[])
    {
        MemoryGame M = new MemoryGame();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }
}
