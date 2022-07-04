import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

    boolean running = false;

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;

    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyLength = 6;

    char direction = 'R';

    int score;
    int foodX;
    int foodY;

    static final int DELAY = 75;
    Timer timer;

    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new CustomKeyAdapter());
        startGame();
    }

    public void startGame() {
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {// Draw background grid
            for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            //Draw the food
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
            // Draw the player
            for (int i = 0; i < bodyLength; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont( new Font("Ink Free",Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newFood() {
        // Creates a new food in a random location
        foodX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        // Move player
        for (int i = bodyLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        // Change direction of player movement
        switch (direction) {
            case 'R' -> x[0] += UNIT_SIZE;
            case 'L' -> x[0] -= UNIT_SIZE;
            case 'U' -> y[0] -= UNIT_SIZE;
            case 'D' -> y[0] += UNIT_SIZE;
        }
    }

    public void checkFood() {
        // Check if player has eaten food
        if (x[0] == foodX && y[0] == foodY) {
            bodyLength++;
            score++;
            newFood();
        }
    }

    public void checkCollision() {
        // Check collision with self
        for (int i = bodyLength; i < 0 ; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        // Check collision with borders
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE || y[0] < 0 || x[0] < 0 || y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
            running = false;
        }
    }

    public void gameOver(Graphics g) {
        // Draw game over screen
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 30));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + score, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + score)) / 2, g.getFont().getSize());
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }
    // Custom key adapter to handle key presses and set direction
    public class CustomKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP && direction != 'D') {
                direction = 'U';
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN && direction != 'U') {
                direction = 'D';
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT && direction != 'R') {
                direction = 'L';
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction != 'L') {
                direction = 'R';
            }
        }
    }
}
