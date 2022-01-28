package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.Timer;
import java.util.Timer.*;

public class Panel extends JPanel implements ActionListener {

    static final int scrWidth = 1200;
    static final int scrHeight = 600;
    static final int unit = 20;
    static final int gameUnits = (
            (scrHeight * scrWidth) * (unit * unit)
            );
    static final int delay = 80;
    final int x[] = new int[gameUnits];
    final int y[] = new int[gameUnits];
    int bodyParts;
    int appleX, appleY, applesEaten;
    char direction;
    boolean running = true;
    Timer timer;
    Random random;
    //constructor
    Panel() {
        random = new Random();
        this.setPreferredSize(
                new Dimension(scrWidth, scrHeight)
        );
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame() {
        newApple();
        running = true;
        //timer = new Timer(this, delay);
        //timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (running) {
            g.setColor(Color.BLUE);
            g.fillOval(appleX, appleY, unit, unit);
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, unit, unit);
            for (int i= 0; i<bodyParts; i++) {
                g.getColor(Color.green);
                g.fillOval(x[i], y[i],unit, unit);
            }
            g.setColor(Color.GREEN);
            g.setFont(new Font("verdana", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Your Score: "+applesEaten, 20, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }
    public void gameOver(Graphics g) {
        // Score
        g.setColor(Color.red);
        g.setFont(new Font("verdana", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, 20, g.getFont().getSize());
        // Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("VERDANA", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (scrWidth - metrics2.stringWidth("Game Over")) / 2, scrHeight / 2);
    }

    public void newApple() {
        appleX = random.nextInt((int) scrWidth/unit) * unit;
        appleY = random.nextInt((int) scrHeight/unit) * unit;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - unit;
                break;
            case 'D':
                y[0] = y[0] + unit;
                break;
            case 'L':
                x[0] = x[0] - unit;
                break;
            case 'R':
                x[0] = x[0] + unit;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        // check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        // check if head touches right border
        if (x[0] > scrWidth) {
            running = false;
        }
        // check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        // check if head touches bottom border
        if (y[0] > scrHeight) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    //class with specific keys
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
