package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Panel extends JPanel implements ActionListener {

  static final int scrWidth = 1200;
  static final int scrHeight = 600;
  static final int unit = 20;
  static final int gameUnits = ((scrHeight * scrWidth) * (unit * unit));
  static int delay = 60;
  final int x[] = new int[gameUnits];
  final int y[] = new int[gameUnits];
  int bodyParts, appleX, appleY, applesEaten;
  char direction = 'R';
  boolean running = false;
  Timer timer;
  Random random;

  // constructor
  Panel() {
    bodyParts = 10;
    random = new Random();
    this.setPreferredSize(new Dimension(scrWidth, scrHeight));
    this.setBackground(Color.BLACK);
    this.setFocusable(true);
    this.addKeyListener(new MyKeyAdapter());
    startGame();
  }

  public void startGame() {
    newApple();
    running = true;
    timer = new Timer(delay, this);
    timer.start();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  private void draw(Graphics g) {
    if (running) {
      g.setColor(Color.BLUE);
      g.fillOval(appleX, appleY, unit, unit);
      for (int i = 0; i < bodyParts; i++) {
        if (i == 0) {
          g.setColor(Color.red);
          g.fillRect(x[i], y[i], unit, unit);
        } else {
          g.setColor(new Color(80, 0, 180));
          // g.setColor(new Color(random.nextInt(255), random.nextInt(255),
          // random.nextInt(255)));
          g.fillRect(x[i], y[i], unit, unit);
        }
      }
      g.setColor(Color.BLUE);
      g.setFont(new Font("verdana", Font.BOLD, 30));
      FontMetrics metrics = getFontMetrics(g.getFont());
      g.drawString("Score: " + applesEaten, (scrWidth - metrics.stringWidth("Score: " + applesEaten)) / 2,
          g.getFont().getSize());
    } else {
      gameOver(g);
    }
  }

  public void gameOver(Graphics g) {
    // Score
    g.setColor(Color.BLUE);
    g.setFont(new Font("verdana", Font.BOLD, 30));
    FontMetrics metrics1 = getFontMetrics(g.getFont());
    g.drawString("Your score: " + applesEaten, 20, g.getFont().getSize() + 10);
    // Game Over text
    g.setColor(Color.blue);
    g.setFont(new Font("VERDANA", Font.BOLD, 75));
    FontMetrics metrics2 = getFontMetrics(g.getFont());
    g.drawString("Game Over", (scrWidth - metrics2.stringWidth("Game Over")) / 2, scrHeight / 2);
  }

  public void newApple() {
    appleX = random.nextInt((int) scrWidth / unit) * unit;
    appleY = random.nextInt((int) scrHeight / unit) * unit;
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
      delay = delay - 10;
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
    if (x[0] < 0) {
      running = false;
    }
    if (x[0] > scrWidth) {
      running = false;
    }
    if (y[0] < 0) {
      running = false;
    }
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

  // class with specific keys
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
