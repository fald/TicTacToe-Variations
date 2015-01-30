package nanottt;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GamePanel extends JPanel{
    
    // Identification to determine which board played what in the nano-board
    static int _id = -1;
    public int id;
    public int button_id;
    
    public ImageIcon xIcon, oIcon, nIcon;
    public JButton[] grid = new JButton[9];
    public int[] board = new int[9];
    public boolean won = false;
    public int winner = 0;
    public boolean playable = true;
    public int currentPlayer = 1;

    
    public GamePanel() {
        GamePanel._id++;
        this.id = _id % 9;
    }  
    
    public void init_icons() {
        try {
            Image x = ImageIO.read(Main.class.getResource("/res/x.png"));
            Image o = ImageIO.read(Main.class.getResource("/res/o.png"));
            Image n = ImageIO.read(Main.class.getResource("/res/n.png"));
            int newH = grid[0].getWidth(); 
            int newW = grid[0].getHeight();
            xIcon = new ImageIcon(x.getScaledInstance(newW, newH, Image.SCALE_SMOOTH));
            oIcon = new ImageIcon(o.getScaledInstance(newW, newH, Image.SCALE_SMOOTH));
            nIcon = new ImageIcon(n.getScaledInstance(newW, newH, Image.SCALE_SMOOTH));
            for (int i = 0; i < 9; i++) {
                if (grid[i].getIcon() != null) {
                    grid[i].setIcon( board[i] == 1? xIcon : (board[i] == 2? oIcon : nIcon) );
                }
                else {
                    grid[i].setIcon(nIcon);
                }
            
            }
        } catch (IOException ex) { Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex); System.out.println("Resource fail");}
    }

    /* Initialize the panel into which buttons go.
     * Initialize the buttons themselves.
     */
    public void init_components() {
        // Initialize panel
        this.setLayout(new GridLayout(3,3,3,3));
        this.addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {
                init_icons();
            }
            public void componentMoved(ComponentEvent e) {
            }
            public void componentShown(ComponentEvent e) {
                init_icons();
            }
            public void componentHidden(ComponentEvent e) {
            }
        });
        // Create and add buttons
        for (int i = 0; i < 9; i++) {
            final int pos = i;
            grid[i] = new JButton();
            grid[i].addActionListener(new ActionListener() {
                int id=pos;
                public void actionPerformed(ActionEvent e) {
                    play(id);
                    Main.panelPlayed = this.id;
                }
            });
            grid[i].setEnabled(true);
            grid[i].setPreferredSize(new Dimension(50,50));
            this.add(grid[i]);
        }
    }

    public void play(int id) {
        if (attempt_change(id)) {
            currentPlayer = (currentPlayer == 1)? 2:1;
            Main.currentPlayer = this.currentPlayer;
            Main.panelPlayed = this.button_id;
            checkWin();
            // If has become a draw, or a win occurred, flip out (true)
            Main.update(this.playable ? false : true);
        }
    }

    public boolean attempt_change(int id) {
        if (board[id] != 0) {
            return false;
        }
        board[id] = currentPlayer;
        grid[id].setIcon(currentPlayer==1? xIcon:oIcon);
        this.button_id = id;
        return true;
    }

    public void checkWin() {
        // Horizontal
        if (board[0] == board[1] && board[1] == board[2] && board[0] != 0) {
            win(0); return;
        }
        if (board[3] == board[4] && board[4] == board[5] && board[3] != 0) {
            win(3); return;
        }
        if (board[6] == board[7] && board[7] == board[8] && board[6] != 0) {
            win(6); return;
        }

        // Vertical
        if (board[0] == board[3] && board[3] == board[6] && board[0] != 0) {
            win(0); return;
        }
        if (board[1] == board[4] && board[4] == board[7] && board[1] != 0) {
            win(1); return;
        }
        if (board[2] == board[5] && board[5] == board[8] && board[2] != 0) {
            win(2); return;
        }

        // Diagonal
        if (board[0] == board[4] && board[4] == board[8] && board[0] != 0) {
            win(0); return;
        }
        if (board[2] == board[4] && board[4] == board[6] && board[2] != 0) {
            win(2); return;
        }

        // Check draw; if there's at least 1 space available to play, return
        // Otherwise, as long as there isn't a winner, draw.
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                return;
            }
        }
        if (!won) {
            draw();
        }
    }

    public void win(int id) {
        won = true;
        winner = board[id];
        for (JButton j: grid) {
            j.setIcon(winner == 1? xIcon:oIcon);
            j.setEnabled(false);
        }
        this.playable = false;
    }

    // As in, not winning or losing, not as in images
    public void draw() {
        winner = -1;
        for (JButton j : grid) {
            j.setEnabled(false);
        }
        this.playable = false;
    }

    @Override
    public void disable() {
        for (JButton button : this.grid) {
            button.setEnabled(false);
        }
    }

    @Override
    public void enable() {
        for (JButton button : this.grid) {
            button.setEnabled(true);
        }
    }

}
