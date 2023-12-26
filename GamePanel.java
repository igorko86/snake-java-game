import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private final int SCREEN_WIDTH = 600;
    private final int BODY_LENGTH = 6;


    private final int SCREEN_HEIGHT = 600;
    private final int SIZE = 25;
    private final int DELAY = 75;
    private final int LENGTH = (SCREEN_WIDTH * SCREEN_HEIGHT) / SIZE;
    private final int[] x = new int[LENGTH];
    private final int[] y = new int[LENGTH];
    private int snakeBodyLength = BODY_LENGTH;
    char direction = 'R';
    private boolean running = false;
    Timer timer;
    int appleX = 0;
    int appleY = 0;
    int eatenApples = 0;
    private JButton restartButton;
    Random random;


    GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        restartButton = new JButton("Restart");
        restartButton.addActionListener(this);
        add(restartButton);
        restartButton.setVisible(false); // Initially hide the button

        startGame();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // TODO understand how it works
        draw(g);
    }

    private void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT / SIZE; i++) {
                g.drawLine(SIZE * i, 0, SIZE * i, SCREEN_HEIGHT);
                g.drawLine(0, SIZE * i, SCREEN_WIDTH, SIZE * i);
            }
            drawApple(g);
            drawSnake(g);
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String text = "Game Over";
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics =  getFontMetrics(g.getFont());
        g.drawString(text, (SCREEN_WIDTH - metrics.stringWidth(text)) / 2, SCREEN_HEIGHT / 2);
        // Score
        String scoreTxt = "Score: ";
        g.setColor(Color.BLUE);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metricsScore =  getFontMetrics(g.getFont());
        g.drawString(scoreTxt + eatenApples, (SCREEN_WIDTH - metricsScore.stringWidth(text)) / 2, g.getFont().getSize());

        restartButton.setVisible(true);
        restartButton.setBounds((SCREEN_WIDTH - 100) / 2, (SCREEN_HEIGHT - 30) / 2, 100, 30);
    }


    private void resetData() {
        snakeBodyLength = BODY_LENGTH;
        eatenApples = 0;
        direction = 'R';
        for (int i = 0; i < BODY_LENGTH; i++) {
            x[i] = 0;
            y[i] = 0;
        }
    }

    private void drawSnake(Graphics g) {
        for (int i = 0; i < snakeBodyLength; i++) {
            if(i == 0) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(new Color(45,180,0));
            }
            g.fillRect(x[i], y[i], SIZE,SIZE);
        }
    }

    private void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void newApple() {
        if (random == null) {
            random = new Random();
        }
        appleX = random.nextInt(SCREEN_WIDTH/SIZE) * SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/SIZE) * SIZE;
    }

    public void move() {
        for (int i = snakeBodyLength; i > 0 ; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'R':
                x[0] = x[0] + SIZE;
                break;
            case 'D':
                y[0] = y[0] + SIZE;
                break;
            case 'L':
                x[0] = x[0] - SIZE;
                break;
            case 'U':
                y[0] = y[0] - SIZE;
                break;
        }
    }

    public void drawApple(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(appleX,appleY,SIZE,SIZE);
    }

    private void checkCollisions() {
                System.out.println("AGGAGAGA");
        for(int i = snakeBodyLength; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        if(x[0] < 0) {
            running = false;
        }
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if(y[0] < 0) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    private void checkApple() {
        if(x[0] == appleX && y[0] == appleY) {
            snakeBodyLength++;
            eatenApples++;
            newApple();
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollisions();
            checkApple();
        } else {
            if(e.getSource() == restartButton) {
                restartButton.setVisible(false);
                resetData();
                running = true;
            }
        }
        repaint();
    }


    class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case 40:
                    if (direction == 'U') return;
                    direction = 'D';
                    break;
                case 39:
                    if (direction == 'L') return;
                    direction = 'R';
                    break;
                case 38:
                    if (direction == 'D') return;
                    direction = 'U';
                    break;
                case 37:
                    if (direction == 'R') return;
                    direction = 'L';
                    break;
            }
        }
    }
}
