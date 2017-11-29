//import java.util.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Nibbles {

    private Random random;
    private GameManager gm;
    private JFrame frame;
    private JPanel panel;

    private static final int TILE_SIZE = 16;

    public static class GamePanel extends JPanel implements ActionListener {

        private int tileSize;
        private int width;
        private int height;
        private Timer gameTimer;
        private int startingTimer = 200;
        private GameManager gm;
        private JLabel score;

        public GamePanel(GameManager gm, int tileSize) {

            this.width = gm.getWidth();
            this.height = gm.getHeight();
            this.gm = gm;

            this.tileSize = tileSize;

            setPreferredSize(new Dimension(tileSize * width, tileSize * height));
            setBackground(Color.WHITE);

            setFocusable(true);
            requestFocusInWindow();
            addKeyListener(new  KeyAdapter () {

                //TODO: Only accept input once tick has gone

                public  void  keyPressed(KeyEvent  ev) {
                    switch(ev.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            gm.turn(Snake.Dir.LEFT);
                            break;
                        case KeyEvent.VK_UP:
                            gm.turn(Snake.Dir.UP);
                            break;
                        case KeyEvent.VK_DOWN:
                            gm.turn(Snake.Dir.DOWN);
                            break;
                        case KeyEvent.VK_RIGHT:
                            gm.turn(Snake.Dir.RIGHT);
                            break;
                    }
                }
            });

            gameTimer = new Timer(startingTimer, this);
            gameTimer.setInitialDelay(0);
            gameTimer.start();
        }

        public void actionPerformed(ActionEvent ev) {
            gm.update();
            repaint();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            for(int x = 0; x < width; ++x) {
                for(int y = 0; y < height; ++y) {

                    switch(gm.getState(x, y)) {
                        case PLAYER:
                            g.setColor(Color.RED);
                            break;
                        case FOOD:
                            g.setColor(Color.GREEN);
                            break;
                        case WALL:
                            g.setColor(Color.BLACK);
                            break;
                        default:
                            continue;
                    }
                    g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    public Nibbles() {
        random = new Random();
        gm = new GameManager(random);
        score = new JLabel("Score: 0");

        // TODO: Clean this up
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame = new JFrame("Nibbles ~~~:");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                GamePanel panel = new GamePanel(gm, 20);
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(new JButton("Hey"));
                buttonPanel.add(new JLabel("Hello"));

                frame.getContentPane().add(panel, BorderLayout.CENTER);
                frame.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);

                frame.pack();

                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        new Nibbles();
    }
}
