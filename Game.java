import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends JPanel implements Runnable, KeyListener {
  private final int WIDTH = 800, HEIGHT = 600;
  private Thread thread;
  private boolean running = false;
  private int Rule = -1;
  private int counter = 1;
  private int pass = 0;

  private Player player;
  private Map map;
  private Renderer renderer;

  public Game() {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setFocusable(true);
    addKeyListener(this);
    map = new Map();
    player = new Player(39.5, 22, 1.57);
    renderer = new Renderer(WIDTH, HEIGHT, map, player);
  }

  public void start() {
    running = true;
    thread = new Thread(this);
    thread.start();
  }

  public void run() {
    while (running) {
      detect_tp();
      if (counter == 0)
        reset_counter();
      repaint();
      try {
        Thread.sleep(16);
      } catch (InterruptedException e) {
      }
    }
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    renderer.render(g);
    if (Rule == 1) {
      renderer.renderImage(g, 9);
    }

    if (pass == 1) {
      renderer.renderImage(g, 10);
      // System.out.println("pass!");
    }
  }

  private void detect_tp() {
    switch (map.getMapNum()) {
      case 0:
        if (player.x >= 31 && player.x <= 32 && player.y >= 18 && player.y <= 19) {
          restart();
        }

        if (player.x >= 1 && player.x <= 2 && player.y >= 16 && player.y <= 17) {
          restart();
        }

        if (player.x >= 19 && player.x <= 20 && player.y >= 15 && player.y <= 16 && counter == 1) {
          map.changeMap(1);
          player.setParams(1.5, 1.5, 0);
          counter = 0;
        }

        if (player.x >= 39 && player.x <= 40 && player.y >= 23 && player.y <= 24 && counter == 1) {
          map.changeMap(1);
          player.setParams(8.5, 2.5, 4.71);
          counter = 0;
        }

        if (player.x >= 41 && player.x <= 42 && player.y >= 10 && player.y <= 11) {
          pass = 1;
        }
        break;
      case 1:
        if (player.x >= 1 && player.x <= 2 && player.y >= 1 && player.y <= 2 && counter == 1) {
          map.changeMap(0);
          player.setParams(19.5, 15.5, player.angle);
          counter = 0;
        }

        if (player.x >= 8 && player.x <= 9 && player.y >= 2 && player.y <= 3 && counter == 1) {
          map.changeMap(0);
          player.setParams(39.5, 23.5, player.angle);
          counter = 0;
        }
        break;
      case 2:

        break;
      default:
        break;
    }
  }

  private void restart() {
    player.setParams(1.5, 4.5, 1.57);
    map.changeMap(0);
  }

  private void reset_counter() {
    switch (map.getMapNum()) {
      case 0:
        if (!(player.x >= 19 && player.x <= 20 && player.y >= 15 && player.y <= 16)
            && !(player.x >= 39 && player.x <= 40 && player.y >= 23 && player.y <= 24)) {
          counter = 1;
        }
        break;
      case 1:
        if (!(player.x >= 1 && player.x <= 2 && player.y >= 1 && player.y <= 2)
            && !(player.x >= 8 && player.x <= 9 && player.y >= 2 && player.y <= 3)) {
          counter = 1;
        }
        break;
      default:
        break;
    }
  }

  // keyboard input
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_LEFT)
      player.turn(-0.1);
    if (e.getKeyCode() == KeyEvent.VK_RIGHT)
      player.turn(0.1);
    if (e.getKeyCode() == KeyEvent.VK_W)
      player.move_FB(0.5, map);
    if (e.getKeyCode() == KeyEvent.VK_A)
      player.move_LR(-0.5, map);
    if (e.getKeyCode() == KeyEvent.VK_S)
      player.move_FB(-0.5, map);
    if (e.getKeyCode() == KeyEvent.VK_D)
      player.move_LR(0.5, map);
    if (e.getKeyCode() == KeyEvent.VK_P) // restart buttom
      restart();
    if (e.getKeyCode() == KeyEvent.VK_O) // show game rules
      Rule *= -1;
    // System.out.println("Rule = " + Rule);
  }
  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}
}