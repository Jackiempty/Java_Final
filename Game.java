import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends JPanel implements Runnable, KeyListener {
  private final int WIDTH = 800, HEIGHT = 600;
  private Thread thread;
  private boolean running = false;

  private Player player;
  private Map map;
  private Renderer renderer;

  public Game() {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setFocusable(true);
    addKeyListener(this);
    map = new Map();
    player = new Player(1.5, 1.5, 0);
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
  }

  private void detect_tp() {
    switch (map.getMapNum()) {
      case 0:

        break;
      case 1:
        if (player.x >= 22) {
          map.changeMap(0);
          player.x = 1.5;
          player.y = 1.5;
        }
        break;
      case 2:

        break;
      default:
        break;
    }
  }

  private void restart() {
    player.setParams(1.5, 1.5, 0);
    map.changeMap(0);
  }

  private void showRules() {}

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
    if (e.getKeyCode() == KeyEvent.VK_TAB) // show game rules
      showRules();
  }
  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}
}