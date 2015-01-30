package tictactoe;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    public JButton[] grid = new JButton[9];
    public ImageIcon xicon, yicon;
    public int[] board = new int[9];
    public boolean won = false;
    public int currentPlayer = 1;
    ImageIcon xIcon;
    ImageIcon oIcon;

    public void play(int id) {
        if (attempt_change(id)) {
            // move successful; toggle player
            currentPlayer = (currentPlayer == 1)? 2:1;
            checkWin();
        }
    }

    /* Has a move been successful? */
    public boolean attempt_change(int id) {
        // if square is not empty before click
        if (board[id] != 0) {
            return false;
        }
        board[id] = currentPlayer;
        grid[id].setIcon(currentPlayer==1? xIcon:oIcon);
        return true;
    }

    /* Check winning conditions...in a poor, but functional way */
    public void checkWin() {
        int[] horiz_starts = {0,3,6};
        int[] vert_starts = {0,1,2};
        // diag starts are kind of a pain
        for (int i : horiz_starts) {
            if (board[i] == board[i + 1] && board[i+1] == board[i+2] && board[i] != 0) {
                win(i);
            }
        }
        for (int i : vert_starts) {
            if (board[i] == board[i + 3] && board[i+3] == board[i+6] && board[i] != 0) {
                win(i);
            }
        }
        // manual diags :(
        if (board[0] == board[4] && board[4] == board[8] && board[8] != 0) {
            win(0);
        }
        if (board[2] == board[4] && board[4] == board[6] && board[6] != 0) {
            win(2);
        }

        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                return;
            }
        }

        if (!won) {
            draw();
        }

    }

    public void draw() {
        if (JOptionPane.showConfirmDialog(new JFrame("It was a draw!"), "Nobody wins! \n Would you like to play again?") == JOptionPane.YES_OPTION) {
            restart();
        } else {
            System.exit(0);
        }
    }

    /* do stuff on win */
    public void win(int id) {
        won = true;
        // determine winner
        String winner = board[id] == 1? "x":"o";

        if (JOptionPane.showConfirmDialog(new JFrame("Winner! Congratulations, " + winner), "Player " + winner + " has won! \n Would you like to play again?") == JOptionPane.YES_OPTION) {
            restart();
        } else {
            System.exit(0);
        }
    };

    public void restart() {
        // reset!
        won = false;
        currentPlayer = 1;
        for (int i = 0; i < 9; i++) {
            board[i] = 0;
            grid[i].setIcon(null);
        }
    }

    /* Initialize board buttons; resizable, listener for presses */
    public void init_components() {
        // Main thing
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setSize(320,320);
        // null makes it default to center
        frame.setLocationRelativeTo(null);

        // containter to put in frame after drawing on
        JPanel panel = new JPanel();
        // Panel on frame
        //frame.setContentPane(panel);
        // Default layout, rows, cols; horiz gap, vert gap
        panel.setLayout(new GridLayout(3,3,3,3));
        // Add listener, to do stuff when things happen to the panel.
        panel.addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {
                // Whenever window is resized, reinitialize the icons
                init_icons();
            }
            // remaining listeners unused
            public void componentMoved(ComponentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            public void componentShown(ComponentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            public void componentHidden(ComponentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        // Create buttons for the 9 positions, add listener, add to panel
        for (int i = 0; i < 9; i++){
            final int pos = i;
            grid[i] = new JButton();
            grid[i].addActionListener(new ActionListener() {
                int id = pos;
                public void actionPerformed(ActionEvent e) {
                    play(id);
                }
            });
            //grid[i].setEnabled(false);

            panel.add(grid[i]);

        }
        frame.setContentPane(panel);

    }

    /* Initialize icons, including resizes */
    public void init_icons() {

        try {
            // Grab images, took forever to get the right path, wotta pain
            Image x = ImageIO.read(Main.class.getResource("/res/x.png"));
            Image o = ImageIO.read(Main.class.getResource("/res/o.png"));
            

            // Make icons from them; scaled to size of grid
            xIcon = new ImageIcon(x.getScaledInstance(grid[0].getWidth(), grid[0].getHeight(), Image.SCALE_SMOOTH));
            oIcon = new ImageIcon(o.getScaledInstance(grid[0].getWidth(), grid[0].getHeight(), Image.SCALE_SMOOTH));

            // Draw 'em on
            for (int i = 0; i < 9; i++) {
                if (grid[i].getIcon() != null) {
                    grid[i].setIcon( board[i] == 1? xIcon:oIcon);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.init_components();
        m.init_icons();
    }

}
