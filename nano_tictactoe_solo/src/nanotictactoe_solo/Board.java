package nanotictactoe_solo;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Board extends JPanel{

    // The 3x3s
    public int board[] = new int[9];
    public JButton buttons[] = new JButton[9];

    // Identifiers
    static int board_serial = -1;
    public int board_id = -1;
    public static int last_played = -1;
    public static int currentPlayer = -1;

    // Winning variables
    public boolean hasEnded = false;
    public int winner = -1;

    // Graphical
    Image xImage, oImage, nImage;
    ImageIcon xIcon, oIcon, nIcon;
    
    public void hard_reset() {
        this.board = new int[9];
        this.buttons = new JButton[9];
        Board.board_serial = -1;
        board_id = -1;
        Board.last_played = -1;
        Board.currentPlayer = -1;
        this.hasEnded = false;
        this.winner = -1;
    }

    public void resetBoard() {
        /* ?? */
    }

    public Board() {
        Board.board_serial++;
        this.board_id = Board.board_serial % 9;
        this.init_board();
        Board.currentPlayer = 0;
        this.loadImages();
        this.loadGUI();
    }

    // Must be called after constructor - not 100% on why,
    // but it seems to be a timing thing, as even if this is
    // placed after the GUI portion, the error says the buttons
    // are 0 dimensional.
    private void init_icons() {
        this.handleImages(true);
    }

    private void init_board() {
        for (int i = 0; i < 9; i++) {
            this.board[i] = -1;
        }
    }

    private void loadImages() {
        try {
            xImage = ImageIO.read(Main.class.getResource("/res/x.png"));
            oImage = ImageIO.read(Main.class.getResource("/res/o.png"));
            nImage = ImageIO.read(Main.class.getResource("/res/n.png"));
        } catch (Exception e) {
            Logger.getLogger(Redundant_SingleBoard.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("There was a fatal error trying to load the images...");
        }
    }

    private void loadGUI() {
        this.setLayout(new GridLayout(3, 3, 3, 3));
        this.setPreferredSize(new Dimension(200, 200));
        this.addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {
                handleImages();
            }

            public void componentMoved(ComponentEvent e) {
                // Nothin'
            }

            public void componentShown(ComponentEvent e) {
                handleImages();
            }

            public void componentHidden(ComponentEvent e) {
                // Nothin'
            }


        });

        // Button creation and placement
        for (int i = 0; i < 9; i++) {
            this.buttons[i] = new JButton();
            final int button_id = i;
            this.buttons[i].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    play(button_id);
                    handleImages();
                }

            });
            this.buttons[i].setEnabled(true);
            this.add(buttons[i]);
        }
    }

    public void redraw() {
        // basically taken care of in handleImages, which takes into account resizes
        for (int i = 0; i < 9; i++) {
            this.buttons[i].setIcon(board[i] == 0? xIcon: oIcon);
        }
    }

    public void handleImages(boolean... b) {
        // Are we cleaning the board?
        // Why did I do it this way, what the fuck, past-Me?
        boolean cleanBoard = b.length > 0? b[0] : false;
        this.nIcon = new ImageIcon(nImage.getScaledInstance(
                buttons[0].getWidth(),
                buttons[0].getHeight(),
                Image.SCALE_SMOOTH));

        if (cleanBoard) {
            // Don't need any images but null
            for (JButton button : buttons) {
                button.setIcon(nIcon);
            }
        } else {
            // now we need other icons
            this.xIcon = new ImageIcon(xImage.getScaledInstance(
                buttons[0].getWidth(),
                buttons[0].getHeight(),
                Image.SCALE_SMOOTH));
            this.oIcon = new ImageIcon(oImage.getScaledInstance(
                buttons[0].getWidth(),
                buttons[0].getHeight(),
                Image.SCALE_SMOOTH));

            for (int i = 0; i < 9; i++) {
                if (board[i] == -1) {
                    buttons[i].setIcon(nIcon);
                } else {
                    buttons[i].setIcon(board[i] == 0? xIcon : oIcon);
                }
            }
        }

    }

    public static void togglePlayer() {
        Board.currentPlayer = (Board.currentPlayer == 0? 1 : 0);
    }

    public static void changePlayer(int player) {
        Board.currentPlayer = player;
    }

    public boolean play(int id) {
        /* Attempt to play on a given spot */
        if (this.board[id] == -1) {
            this.board[id] = Board.currentPlayer;
            Board.last_played = id;
            this.checkWin();
            Board.togglePlayer();
            return true;
        } else {
            return false;
        }
    }

    public void checkWin() {
        // Horizontal
        if (this.board[0] == this.board[1] && this.board[1] == this.board[2] && this.board[0] != -1) {
            this.end(0); return;
        }
        if (this.board[3] == this.board[4] && this.board[4] == this.board[5] && this.board[3] != -1) {
            this.end(3); return;
        }
        if (this.board[6] == this.board[7] && this.board[7] == this.board[8] && this.board[6] != -1) {
            this.end(6); return;
        }

        // Vertical
        if (this.board[0] == this.board[3] && this.board[3] == this.board[6] && this.board[0] != -1) {
            this.end(0); return;
        }
        if (this.board[1] == this.board[4] && this.board[4] == this.board[7] && this.board[1] != -1) {
            this.end(1); return;
        }
        if (this.board[2] == this.board[5] && this.board[5] == this.board[8] && this.board[2] != -1) {
            this.end(2); return;
        }

        // Diagonal
        if (this.board[0] == this.board[4] && this.board[4] == this.board[8] && this.board[0] != -1) {
            this.end(0); return;
        }
        if (this.board[2] == this.board[4] && this.board[4] == this.board[6] && this.board[2] != -1) {
            this.end(2); return;
        }

        // Check draw; if there's at least 1 space available to play, return
        // Otherwise, as long as there isn't a winner, draw.
        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] == -1) {
                return;
            }
        }
        if (!this.hasEnded) {
            this.end(-1);
        }
    }
    
    public void end(int winner_pos) {
        // -1 => draw
        this.hasEnded = true;
        this.winner = board[winner_pos];

        if (this.winner > -1) {
            // To give some sort of (crappy) visual effect to it
            // Should probably change the drawn boards to not be blank,
            // could get confusing with the 3x3 version
            for (int i = 0; i < 9; i++) {
                this.board[i] = this.winner;
                this.handleImages();
                this.buttons[i].setEnabled(false);
            }
        } else {
            for (JButton button : this.buttons) {
                button.setEnabled(false);
            }
        }
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
        Board num1 = new Board();
        while (!num1.hasEnded) {
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

    public static void playFull() {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.setResizable(true);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        Board s = new Board();
        frame.add(s);
        frame.setVisible(true);
        s.init_icons();
    }

    public static void main(String[] args) {
        // playBasic();
        playFull();
    }
}
