package nanottt;

import javax.swing.*;
import java.awt.*;

/* TODO: Buttons/Score, AI */
/* Lol hardcoding numbers is for nubs */

public class Main {
    
    static int panelPlayed = -1;
    static int currentPlayer = 1;

    static public GamePanel[] grid = new GamePanel[9];
    public static boolean won = false;
    public static String winner;
    
    public void init_components() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setSize(640, 640);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3,3,6,6));
        
        for (int i = 0; i < 9; i++){
            grid[i] = new GamePanel();
            grid[i].init_components();
            mainPanel.add(grid[i]);
        }
        
        // Option panel
        JPanel subPanel = new SidePanel();
        
        // Put everything together
        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new FlowLayout());
        comboPanel.add(mainPanel);
        comboPanel.add(subPanel);
        
        // frame.setContentPane(mainPanel);
        frame.setContentPane(comboPanel);
    }  
    
    // Third time's the fuckin' charm?
    static public void update(boolean enableAll) {
        
        checkWin();
        // If should enable all (last grid played filled up
        if (enableAll) {
            for (GamePanel panel : grid) {
                if (panel.playable) {
                    panel.enable();
                } else {
                    panel.disable();
                }
            }
        } else { // Normal circumstance
            // Played cell didn't fill, target cell not full
            if (grid[Main.panelPlayed].playable) {
                for (GamePanel panel : grid) {
                    if (panel.id != Main.panelPlayed) {
                        panel.disable();
                    } else {
                        panel.enable();
                    }
                }
            } else { // Played cell didn't fill, target cell untargetable 
                for (GamePanel panel : grid) {
                    if (panel.playable) {
                        panel.enable();
                    }
                }
            }
        }
        
        for (GamePanel field : grid) {
            field.currentPlayer = Main.currentPlayer;
        }
    }
    
    static public void checkWin() {
        // Horizontal
        if (
                grid[0].winner == grid[1].winner && 
                grid[1].winner == grid[2].winner &&
                grid[0].winner != 0
                ) {
            win(0); return;
        }
        if (
                grid[3].winner == grid[4].winner && 
                grid[4].winner == grid[5].winner &&
                grid[3].winner != 0
                ) {
            win(3); return;
        }
        if (
                grid[6].winner == grid[7].winner && 
                grid[7].winner == grid[8].winner &&
                grid[6].winner != 0
                ) {
            win(6); return;
        }

        // Vertical
        
        if (
                grid[0].winner == grid[3].winner && 
                grid[3].winner == grid[6].winner &&
                grid[0].winner != 0
                ) {
            win(0); return;
        }
        if (
                grid[1].winner == grid[4].winner && 
                grid[4].winner == grid[7].winner &&
                grid[1].winner != 0
                ) {
            win(1); return;
        }
        if (
                grid[2].winner == grid[5].winner && 
                grid[5].winner == grid[8].winner &&
                grid[2].winner != 0
                ) {
            win(2); return;
        }

        // Diagonal
        if (
                grid[0].winner == grid[4].winner && 
                grid[4].winner == grid[8].winner &&
                grid[0].winner != 0
                ) {
            win(0); return;
        }
        if (
                grid[2].winner == grid[4].winner && 
                grid[4].winner == grid[6].winner &&
                grid[2].winner != 0
                ) {
            win(2); return;
        }

        // Check draw; if there's at least 1 space available to play, return
        // Otherwise, as long as there isn't a winner, draw.
        for (int i = 0; i < grid.length; i++) {
            if (grid[i].playable) {
                return;
            }
        }
        if (!won) {
            draw();
        }
    }
    
    public static void win(int id) {
        won = true;
        winner = (grid[id].winner == 1? "X":"O");
        
        if (JOptionPane.showConfirmDialog(new JFrame("Winner! Congratulations, " + winner), "Player " + winner + " has won! \n Would you like to play again?") == JOptionPane.YES_OPTION) {
            restart();
        } else {
            System.exit(0);
        }
    }
    
    public static void draw() {
        Main.winner = null;
        for (GamePanel panel : Main.grid) {
            panel.disable();
        } 
        Main.won = false;
        
        if (JOptionPane.showConfirmDialog(new JFrame("You're all assholes."), "It's a draw! Play again?") == JOptionPane.YES_OPTION) {
            restart();
        } else {
            System.exit(0);
        }
    }
    
    public static void restart() {
        Main.currentPlayer = 1;
        Main.grid = new GamePanel[9];
        Main.panelPlayed = -1;
        Main.won = false;
        Main m = new Main();
        m.init_components();
    }
    

    public static void main(String[] args) {
        Main m = new Main();
        m.init_components();
        // m.play();
        
    }
}
