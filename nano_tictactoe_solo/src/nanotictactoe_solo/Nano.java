package nanotictactoe_solo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/*
 * TODO
 * Take each component to be its own class.
 * Check win
 * Reset
 */

public class Nano extends JFrame {

    // For 9 boards, have an 'enabled' field to say which board is available; -1 => All
    // Also a hasEnded.
    // After each play, change the enabled to be either Board.lastPlayed (or whatever I called it)
    // or, if that board hasEnded, to -1.
    // CheckWin has 3 rules:
    //      Type 1: first to win a board
    //      Type 2: first to win 3 boards in a row (spatially, not chronologically)
    //      Type 3: person who wins the most boards when the game ends

    // For right panel, have a score, an AI/Human chooser, a reset, current turn and rule changer (What else am I missing...)

    public JPanel nanoPanel;
    public Board[] boards;
    public JPanel options;
    public boolean hasEnded;
    public int[] scores;

    public String[] rules;
    public String currRule, nextRule;
    
    public JPanel liveInfo, gameOptions, ruleSet;
    public JLabel currPlayerLabel, currPlayer, xScore, xScoreLabel, oScore, oScoreLabel, ruleLabel;
    public JButton reset, newGame, help;
    public JRadioButton firstBoard, threeBoard, maximumBoards;
    public ButtonGroup ruleGroup;
    public JLabel[] labels;
    public JButton[] buttons;
    public JRadioButton[] rbuttons;
    public Font labelFont;

    public Nano() {
        this.labelFont = new Font("cf crack and bold", Font.PLAIN, 24);

        this.rules = new String[]{"Classic", "Speed", "Endurance"};
        this.currRule = rules[0];
        this.nextRule = rules[0];

        this.setDefaultCloseOperation(3);
        this.hasEnded   = false;

        this.scores     = new int[]{0, 0};
        
        this.init_components();

        this.setVisible(true);
        this.setResizable(true);
        this.setSize(880, 660);
        this.setLocationRelativeTo(null);
    }

    private void init_components() {

        this.fillNano();
        this.fillOptions();
        this.setListeners();

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
        this.add(nanoPanel);
        this.add(options);

        this.enable(-1);
        
    }

    private void fillOptions() {
        this.options    = new JPanel();
        this.options.setLayout(new GridLayout(3, 1, 3, 3));

        // Live info; current player, total score
        this.liveInfo = new JPanel();
        this.liveInfo.setPreferredSize(new Dimension(200, 200));
        this.liveInfo.setLayout(new GridLayout(3, 2, 2, 2));
        this.currPlayer = new JLabel();
        this.currPlayerLabel = new JLabel();
        this.currPlayerLabel.setText("Player:");
        this.xScore = new JLabel();
        this.xScoreLabel = new JLabel();
        this.xScoreLabel.setText("X: ");
        this.oScore = new JLabel();
        this.oScoreLabel = new JLabel();
        this.oScoreLabel.setText("O: ");
        this.setInfo();

        this.liveInfo.add(this.currPlayerLabel);
        this.liveInfo.add(this.currPlayer);
        this.liveInfo.add(this.xScoreLabel);
        this.liveInfo.add(this.xScore);
        this.liveInfo.add(this.oScoreLabel);
        this.liveInfo.add(this.oScore);

        // Game options; new game, reset, help
        this.gameOptions = new JPanel();
        this.gameOptions.setLayout(new GridLayout(3, 1, 3, 3));
        this.newGame = new JButton();
        this.newGame.setText("New Game");
        this.newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });
        this.reset = new JButton();
        this.reset.setText("Reset");
        this.reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        this.help = new JButton();
        this.help.setText("Help");
        this.help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                help();
            }
        });
        this.gameOptions.add(newGame);
        this.gameOptions.add(help);
        this.gameOptions.add(reset);

        // Rules to win!
        this.ruleLabel = new JLabel();
        this.ruleLabel.setText("Rules to win:");
        this.firstBoard = new JRadioButton();
        this.firstBoard.setText("First");
        this.firstBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextRule = rules[1];
            }
        });
        this.threeBoard = new JRadioButton();
        this.threeBoard.setText("Classic");
        this.threeBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextRule = rules[0];
            }
        });
        this.maximumBoards = new JRadioButton();
        this.maximumBoards.setText("Endurance");
        this.maximumBoards.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextRule = rules[2];
            }
        });

        this.ruleGroup = new ButtonGroup();
        this.ruleGroup.add(this.firstBoard);
        this.ruleGroup.add(this.threeBoard);
        this.ruleGroup.add(this.maximumBoards);
        this.ruleGroup.setSelected(this.threeBoard.getModel(), true);

        this.ruleSet = new JPanel();
        this.ruleSet.setLayout(new GridLayout(4, 1, 2, 2));
        this.ruleSet.add(this.ruleLabel);
        this.ruleSet.add(this.threeBoard);
        this.ruleSet.add(this.firstBoard);
        this.ruleSet.add(this.maximumBoards);

        // Fonts and styles
        this.labels = new JLabel[]{
            this.currPlayerLabel, this.currPlayer, this.xScore,
            this.xScoreLabel, this.oScore, this.oScoreLabel, this.ruleLabel};
        this.buttons = new JButton[]{
            this.reset, this.newGame, this.help};
        this.rbuttons = new JRadioButton[]{
            this.firstBoard, this.threeBoard, this.maximumBoards};
        for (JLabel label : this.labels) {
            label.setFont(this.labelFont);
        }
        for (JButton button : this.buttons) {
            button.setFont(this.labelFont);
        }
        for (JRadioButton rbutton : this.rbuttons) {
            rbutton.setFont(this.labelFont);
        }


        this.options.add(this.liveInfo);
        this.options.add(this.gameOptions);
        this.options.add(this.ruleSet);
    }

    private void fillNano() {
        /* Create 9 boards and place them into the correct panel. */
        this.nanoPanel  = new JPanel();
        this.nanoPanel.setBackground(Color.red);
        this.boards     = new Board[9];
        this.nanoPanel.setLayout(new GridLayout(3, 3, 6, 6));
        for (int i = 0; i < 9; i++) {
            this.boards[i] = new Board();
            this.nanoPanel.add(boards[i]);
        }
    }

    private void setInfo() {
        // Too much in one function.
        this.currPlayer.setText((Board.currentPlayer == 0? "X" : "O"));
        this.xScore.setText("" + scores[0]);
        this.oScore.setText("" + scores[1]);
    }

    private void enable(int board_number) {
        // If board number is -1, enable all (that haven't ended)

        if (board_number != -1 && this.boards[board_number].hasEnded) {
            this.enable(-1); return;
        }
        for (int i = 0; i < 9; i++) {
            if (!this.boards[i].hasEnded) {
                for (Component button : this.boards[i].getComponents()) {
                    if (i == board_number || board_number == -1) {
                        button.setEnabled(true);
                    } else {
                        button.setEnabled(false);
                    }
                }
            }
        }
    }

    private void setListeners() {
        // Just override the old listener, this shit is stupid
        // this.setBoardListeners();

        JButton button;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                final int button_id = j;
                final int board_id = i;
                button = (JButton) boards[i].getComponent(j);
                button.removeActionListener(button.getActionListeners()[0]);
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Overridden - these next 2 lines mimic original functionality
                        boards[board_id].play(button_id);
                        boards[board_id].handleImages();
                        // Nano-specific stuff!
                        // Probably shouldn't have named it enable, but there you go
                        // What am I going to do, refactor trivially? Psh.
                        enable(button_id);
                        updatePlayer();
                        //setInfo();
                        if (boards[board_id].hasEnded) {
                            checkWin();
                        }
                    }
                });
            }
        }

        /*
         for (int i = 0; i < 9; i++) {
            for (Component comp : boards[i].getComponents()) {
                final int j = i;
                button = (JButton)comp;
                /* button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        enable(Board.last_played);
                        System.out.println("Second action performed");
                    }
                });
                // Event listeners fire off in reverse order of when they were added...
                ActionListener temp = button.getActionListeners()[1];
                button.getActionListeners()[1] = button.getActionListeners()[0];
                button.getActionListeners()[0] = temp;
                // AND YET IT DOESN'T FUCKING WORK. GET YOUR SHIT TOGETHER, TIRED-ME.
                *

                // Just override the fucking thing...stupid Swing.
                button.removeActionListener(button.getActionListeners()[0]);
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        boards[j].play(button.);
                        System.out.println("Override!");
                    }
                });
            }
        }
        // this.setOptionListeners();
         * And of course I put these listeners into the fillOptions.
         * God damn.
         */
    }

    private void checkWin() {
        // Depends on rule
        // Remember to add score
        // Auto new game, Y/N?


        //...

        this.setScore();
    }

    private void reset() {
        this.newGame();
        this.scores = new int[]{0, 0};
    }

    private void help() {

    }
    
    private void newGame() {
        this.currRule = this.nextRule;
        this.init_components();
        /*
        for (Board board : this.boards) {
            board.hard_reset();
        }
        this.setListeners();

         */
    }

    private void updatePlayer() {
        this.currPlayer.setText(Board.currentPlayer == 0? "X" : "O");
    }

    private void setScore() {
        this.oScore.setText("" + scores[1]);
        this.xScore.setText("" + scores[0]);
    }
    public static void main(String[] args) {
        Nano n = new Nano();
    }
}
