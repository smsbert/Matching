import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import javax.swing.*;

public class Board {
    // Array to hold board cards
    private FlippableCard cards[];

    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();

    public Board(int size, ActionListener AL) {
        // Allocate and configure the game board: an array of cards
        cards = new FlippableCard[size];

        // Fill the Cards array
        int imageIdx = 1;
        for (int i = 0; i < size; i++) {

            // Load the front image from the resources folder
            String imgPath = "pic/sea" + imageIdx + ".jpg";
            ImageIcon img = new ImageIcon(loader.getResource(imgPath));
            int idCount = imageIdx;

            if (i % 2 != 0) { //We only want two cards to have the same image, so change the index on every odd i
                imageIdx++;  // get ready for the next pair of cards
            }

            // Setup one card at a time
            FlippableCard c = new FlippableCard(img);

            // Add them to the array
            cards[i] = c;
            cards[i].setPreferredSize(new Dimension(40, 70));
            cards[i].hideFront();
            cards[i].setID(idCount);
            cards[i].addActionListener(AL);
        }

        // Randomizes the card position
        cards = shuffle(cards);
    }

    // Shuffles the cards to randomize their positions on the board
    public FlippableCard[] shuffle(FlippableCard shuffleCards[]) {
        for(int k = 0; k < shuffleCards.length; k ++){
            int random = (int)(Math.random() * shuffleCards.length);
            FlippableCard cardHolder = shuffleCards[k];
            shuffleCards[k] = shuffleCards[random];
            shuffleCards[random] = cardHolder;
        }
        return shuffleCards;
    }

    public void fillBoardView(JPanel view) {
        for (FlippableCard c : cards) {
            view.add(c);
            view.setSize(100, 100);
        }
        view.repaint();
    }

    // Reset the flipped cards and randomize the card positions
    public void resetBoard() {
        for(int i = 0; i < cards.length; i++){
            cards[i].hideFront();
        }
        shuffle(cards);
    }
}
