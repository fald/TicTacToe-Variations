// Do not use!\
// Use Board.java instead.

package nanotictactoe_solo;

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
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Redundant_SingleBoard extends JPanel {

    /*
     * Bookkeeping
     */
    static int board_serial = -1;
    public int board_id;
    // This is static because it probably needs to be? To carry accross the
    // button played, so the right board can be played next, woo.
    public static int button_played_id;

    public boolean isWon = false;
    public int winner;
    public boolean isDraw = false;

    public int[] board = new int[9];
    // This is static so player changes carry accross all boards in play
    public static int currentPlayer = 0;

    ImageIcon xIcon, oIcon, nullIcon;
    Image xImage, oImage, nImage;
    public JButton[] buttons = new JButton[9];



    // probably not a great idea since I don't actually know whats going on.
    // This won't be a child class probably, so it doesn't matter
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Redundant_SingleBoard() {
        Redundant_SingleBoard.board_serial++;
        this.board_id = Redundant_SingleBoard.board_serial % 9;
        this.init_board();
        this.loadImages();
        this.loadGUI();
    }
    
    /*
     * This isn't in the constructor because FUCK Java.
     */
    public void init_icons() {
        this.handleImages(true);
    }
    
    public void init_board() {
        for (int i = 0; i < 9; i++) {
            this.board[i] = -1;
        }
    }

    public void toggle_player() {
        Redundant_SingleBoard.currentPlayer = (Redundant_SingleBoard.currentPlayer == 0? 1 : 0);
    }

    public boolean attempt_change(int id) {
        if (this.board[id] != -1) {
            return false;
        }
        else {
            this.board[id] = currentPlayer;
            Redundant_SingleBoard.button_played_id = id;
            return true;
        }
    }

    public void change_player(int player) {
        Redundant_SingleBoard.currentPlayer = player;
    }

    public void win(int winner_spot) {
        this.isWon = true;
        this.winner = board[winner_spot];
    }

    public void draw() {
        this.isDraw = true;
    }

    public boolean isPlayable() {
        if (isWon == true || isDraw == true) {
            return false;
        }
        return true;
    }

    public boolean play(int id) {
        if (this.attempt_change(id)) {
            this.toggle_player();
            this.checkWin();
            return true;
        }
        return false;
    }

    public void checkWin() {
        // Horizontal
        if (this.board[0] == this.board[1] && this.board[1] == this.board[2] && this.board[0] != -1) {
            this.win(0); return;
        }
        if (this.board[3] == this.board[4] && this.board[4] == this.board[5] && this.board[3] != -1) {
            this.win(3); return;
        }
        if (this.board[6] == this.board[7] && this.board[7] == this.board[8] && this.board[6] != -1) {
            this.win(6); return;
        }

        // Vertical
        if (this.board[0] == this.board[3] && this.board[3] == this.board[6] && this.board[0] != -1) {
            this.win(0); return;
        }
        if (this.board[1] == this.board[4] && this.board[4] == this.board[7] && this.board[1] != -1) {
            this.win(1); return;
        }
        if (this.board[2] == this.board[5] && this.board[5] == this.board[8] && this.board[2] != -1) {
            this.win(2); return;
        }

        // Diagonal
        if (this.board[0] == this.board[4] && this.board[4] == this.board[8] && this.board[0] != -1) {
            this.win(0); return;
        }
        if (this.board[2] == this.board[4] && this.board[4] == this.board[6] && this.board[2] != -1) {
            this.win(2); return;
        }

        // Check draw; if there's at least 1 space available to play, return
        // Otherwise, as long as there isn't a winner, draw.
        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] == -1) {
                return;
            }
        }
        if (!this.isWon) {
            this.draw();
        }
    }

    public int getWinner() {
        return this.winner;
    }

    public int getCurrPlayer() {
        return this.currentPlayer;
    }

    public int getID() {
        return this.board_id;
    }

    public int getButtonID() {
        return Redundant_SingleBoard.button_played_id;
    }

    public void textDisplay() {
        for (int i = 0; i < board.length; i++) {
            System.out.print(
                    ((board[i] == -1)? i :
                        (board[i]==0? "X" : "O")) +
                    (((i+1)%3 == 0) ? "\n" +
                    ((i<6)? "---------\n" : "") 
                    : " | " ));
        }
    }

    public static void playBasic() {
        Redundant_SingleBoard num1 = new Redundant_SingleBoard();
        while (num1.isPlayable()) {
            num1.textDisplay();
            System.out.println("\nWhich square will you play, " +
                    (currentPlayer == 0? "X" : "O") + "?");
            try {
                Scanner input = new Scanner(System.in);
                int attempted_id = input.nextInt();
                num1.play(attempted_id);
            } catch (Exception E) {
                System.out.println(E);
            }
        }
        num1.textDisplay();
        System.out.println("Winner! " + (num1.winner == 0? "X" : "O") );
    }


    /*
     * VISUAL STUFF
     */
    public void loadGUI() {
        this.setLayout( new GridLayout(3, 3, 3, 3));
        this.addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {
                handleImages();
            }
            public void componentMoved(ComponentEvent e) {
            }
            public void componentShown(ComponentEvent e) {
                handleImages(true);
            }
            public void componentHidden(ComponentEvent e) {
            }
        });

        // Button creation and placement
        for (int i = 0; i < this.board.length; i++) {
            buttons[i] = new JButton();
            final int button_id = i;
            buttons[i].addActionListener(new ActionListener() {
                int id = button_id;
                public void actionPerformed(ActionEvent e) {
                    if (play(id)) {
                        Redundant_SingleBoard.button_played_id = this.id;
                        buttons[id].setIcon(
                                board[id] == 0? xIcon : oIcon);
                    }
                }
            });
            buttons[i].setEnabled(true);
            buttons[i].setPreferredSize(new Dimension(16, 16));
            this.add(buttons[i]);
        }
    }

    public void loadImages() {
        try {
            xImage = ImageIO.read(Main.class.getResource("/res/x.png"));
            oImage = ImageIO.read(Main.class.getResource("/res/o.png"));
            nImage = ImageIO.read(Main.class.getResource("/res/n.png"));
        } catch (IOException ex) {
            Logger.getLogger(Redundant_SingleBoard.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("There was a fatal error trying to load the images...");
        }
    }

    // Resize and set images as needed
    public void handleImages(boolean... isNew) {
        boolean cleanBoard = isNew.length > 0? isNew[0] : false;
        nullIcon = new ImageIcon(nImage.getScaledInstance(buttons[0].getWidth(), buttons[0].getHeight(), Image.SCALE_SMOOTH));

        
        if (cleanBoard) {
            // initial, no need to make 2 extra images.
            for (JButton button : buttons) {
                button.setIcon(nullIcon);
            }
        } else {
            xIcon = new ImageIcon(xImage.getScaledInstance(buttons[0].getWidth(), buttons[0].getHeight(), Image.SCALE_SMOOTH));
            oIcon = new ImageIcon(oImage.getScaledInstance(buttons[0].getWidth(), buttons[0].getHeight(), Image.SCALE_SMOOTH));

            for (int i = 0; i < 9; i++) {
                buttons[i].setIcon(
                        board[i] == -1? nullIcon :
                            board[i] == 0? xIcon : oIcon
                        );
            }
        }

    }


    public static void playFull() {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.setResizable(true);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        
        Redundant_SingleBoard s = new Redundant_SingleBoard();
        frame.add(s);
        frame.setVisible(true);
        s.init_icons();
    }

    public static void main(String[] args) {
        
        // playBasic();
        playFull();

        
    }




}
