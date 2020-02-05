import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// All files started from and based on the template provided: memory-game-template
public class MemoryGame extends JFrame implements ActionListener {

    // Core game play objects
    private Board gameBoard;
    private FlippableCard prevCard1, prevCard2;

    // Labels to display game info
    private JLabel errorLabel, timerLabel, guessCountLabel, matchesLabel;

    // layout objects: Views of the board and the label area
    private JPanel boardView, labelView;

    private JFrame mainFrame = new JFrame();

    // Record keeping counts and times
    private int clickCount = 0, gameTime = 0, errorCount = 0, guessesMade = 0, pairsFound = 0;

    // Font for buttons and labels in game
    private Font font = new Font("Arial", Font.PLAIN, 20);

    // Game timer: will be configured to trigger an event when user interacts with game board
    private Timer gameTimer;

    public MemoryGame() {
        // Call the base class constructor
        super("Memory Game");

        // Allocate the interface elements
        JButton restart = new JButton("Start/Restart");
        restart.setFont(font);
        JButton quit = new JButton("Quit");
        quit.setFont(font);
        timerLabel = new JLabel("Timer: " + gameTime);
        timerLabel.setFont(font);
        errorLabel = new JLabel("Errors: " + errorCount);
        errorLabel.setFont(font);
        guessCountLabel = new JLabel("Guesses Made: " + clickCount);
        guessCountLabel.setFont(font);
        matchesLabel = new JLabel("Matches: " + pairsFound);
        matchesLabel.setFont(font);


        // Timer will initiate game play and follow the action listener to flip cards and check for matches
        gameTimer = new Timer(2000, this);
        // Restart button set up to reset the game board and shuffle cards
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == restart) {
                    restartGame();
                }
            }
        });
        // Quit button will exit out of the program and close the window
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == quit) {
                    System.exit(0);
                }
            }


        });


        // Allocate two major panels to hold interface
        labelView = new JPanel();  // used to hold labels
        boardView = new JPanel();  // used to hold game board

        // get the content pane, onto which everything is eventually added
        Container c = getContentPane();

        // Setup the game board with cards
        gameBoard = new Board(25, this);

        // Add the game board to the board layout area
        boardView.setLayout(new GridLayout(5, 5, 2, 0));
        gameBoard.fillBoardView(boardView);

        // Add required interface elements to the "label" JPanel
        labelView.setLayout(new GridLayout(1, 4, 2, 2));
        labelView.add(quit);
        labelView.add(restart);
        labelView.add(matchesLabel);
        labelView.add(guessCountLabel);

        // Both panels should now be individually layed out
        // Add both panels to the container
        c.add(labelView, BorderLayout.NORTH);
        c.add(boardView, BorderLayout.SOUTH);

        setSize(750, 500);
       // setResizable(false);
        setVisible(true);
        gameTimer.start();
        gameTime = (int) System.currentTimeMillis();
    }

    /* Handle anything that gets clicked and that uses MemoryGame as an
     * ActionListener */
    public void actionPerformed(ActionEvent e) {
        // Get the currently clicked card from a click event
        FlippableCard currCard = (FlippableCard) e.getSource();
        if (clickCount != 2) {
            clickCount++;
            currCard.showFront();
            if (currCard.id() == 13) {
                clickCount--;
                return;
            } else if (clickCount == 1) {
                prevCard1 = currCard;
                prevCard1.showFront();
                return;
            } else if (clickCount == 2) {
                if (currCard.id() == 13) {
                    clickCount--;
                    return;
                }
                prevCard2 = currCard;
                prevCard2.showFront();
                if (prevCard1.id() == prevCard2.id()) {
                    pairsFound++;
                    guessesMade++;
                    clickCount = 0;
                } else {
                    Timer wait = new Timer(1000, new ActionListener() {
                        public void actionPerformed(ActionEvent pause) {
                            errorCount++;
                            guessesMade++;
                            prevCard2.hideFront();
                            prevCard1.hideFront();
                            clickCount = 0;
                            ((Timer) pause.getSource()).stop();
                        }
                    });
                    wait.start();
                }
            }
            guessCountLabel.setText("Guesses Made: " + guessesMade);
            matchesLabel.setText("Matches: " + pairsFound);
        }
    }


    private void restartGame() {
        // reset all counts to zero and rest labels to zero
        pairsFound = 0;
        gameTime = 0;
        clickCount = 0;
        errorCount = 0;
        guessesMade = 0;
        timerLabel.setText("Timer: " + gameTime);
        errorLabel.setText("Errors: " + errorCount);
        guessCountLabel.setText("Guesses Made: " + guessesMade + " ");
        matchesLabel.setText("Matches: " + pairsFound);

        // Clear the boardView and have the gameBoard generate a new layout
        boardView.removeAll();
        gameBoard.resetBoard();
        gameBoard.fillBoardView(boardView);
    }

    public static void main(String args[]) {
        MemoryGame M = new MemoryGame();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
