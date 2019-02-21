import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Statkevich Nataliya.
 */

public class Main {

    private static final long FRAME_TIME = 1000L / 50L;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris from Nataliya  =)");
        //frame.setLayout(new GridLayout(1, 4));
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        Tetris player1 = new Tetris();
        Tetris player2 = new Tetris();

        frame.add(player2.getBoard());
        frame.add(player2.getSide());
        frame.add(player1.getSide());
        frame.add(player1.getBoard());


        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {

                    //Drop
                    case KeyEvent.VK_DOWN:
                        player1.onKeyDownPressed();
                        break;
                    case KeyEvent.VK_S:
                        player2.onKeyDownPressed();
                        break;

                    //Move Left
                    case KeyEvent.VK_LEFT:
                        player1.onLeft();
                        break;
                    case KeyEvent.VK_A:
                        player2.onLeft();
                        break;

                    //Move Right
                    case KeyEvent.VK_RIGHT:
                        player1.onRight();
                        break;
                    case KeyEvent.VK_D:
                        player2.onRight();
                        break;

                    //Rotate Clockwise
                    case KeyEvent.VK_UP:
                        player1.onRotate();
                        break;
                    case KeyEvent.VK_W:
                        player2.onRotate();
                        break;

                    //Pause Game
                    case KeyEvent.VK_P:
                        player1.onPause();
                        player2.onPause();
                        break;

                    //Start Game
                    case KeyEvent.VK_ENTER:
                        player1.onStart();
                        player2.onStart();
                        break;
                }
            }

            //Drop - When you let go, the speed of the logical timer is set back to the current game speed.
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        player1.onKeyDownReleased();
                        break;
                    case KeyEvent.VK_S:
                        player2.onKeyDownReleased();
                        break;
                }
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        player1.startGame();
        player2.startGame();
        mainLoop(player1, player2);
    }

    private static void mainLoop(Tetris player1, Tetris player2) {
        while (true) {
            //Get the time that the frame started.
            long start = System.nanoTime();

            player1.updateTimer();
            player2.updateTimer();

            player1.decrementDelayFall();
            player2.decrementDelayFall();

            player1.renderGame();
            player2.renderGame();

            // Sleep to cap the frame rate.
            long delta = (System.nanoTime() - start) / 1000000L;
            if (delta < FRAME_TIME) {
                try {
                    Thread.sleep(FRAME_TIME - delta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
