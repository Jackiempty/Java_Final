import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer {
  private int width, height;
  private Map map;
  private Player player;
  private TextureLoader textureLoader;

  public Renderer(int width, int height, Map map, Player player) {
    this.width = width;
    this.height = height;
    this.map = map;
    this.player = player;
    this.textureLoader = new TextureLoader();
  }

  public void render(Graphics g) {
    g.setColor(Color.GRAY); // sky
    g.fillRect(0, 0, width, height / 2);
    g.setColor(Color.GRAY); // floor
    g.fillRect(0, height / 2, width, height / 2);

    for (int x = 0; x < width; x++) {
      double cameraX = 2 * x / (double) width - 1;
      double rayDirX = Math.cos(player.angle) + cameraX * -Math.sin(player.angle);
      double rayDirY = Math.sin(player.angle) + cameraX * Math.cos(player.angle);

      int mapX = (int) player.x;
      int mapY = (int) player.y;

      double deltaDistX = (rayDirX == 0) ? 1e30 : Math.abs(1 / rayDirX);
      double deltaDistY = (rayDirY == 0) ? 1e30 : Math.abs(1 / rayDirY);

      double sideDistX;
      double sideDistY;

      int stepX;
      int stepY;

      if (rayDirX < 0) {
        stepX = -1;
        sideDistX = (player.x - mapX) * deltaDistX;
      } else {
        stepX = 1;
        sideDistX = (mapX + 1.0 - player.x) * deltaDistX;
      }

      if (rayDirY < 0) {
        stepY = -1;
        sideDistY = (player.y - mapY) * deltaDistY;
      } else {
        stepY = 1;
        sideDistY = (mapY + 1.0 - player.y) * deltaDistY;
      }

      int hit = 0;
      int side = 0;

      while (hit == 0) {
        if (sideDistX < sideDistY) {
          sideDistX += deltaDistX;
          mapX += stepX;
          side = 0;
        } else {
          sideDistY += deltaDistY;
          mapY += stepY;
          side = 1;
        }
        if (map.isWall(mapX, mapY))
          hit = 1;
      }

      double perpWallDist;
      if (side == 0)
        perpWallDist = (sideDistX - deltaDistX);
      else
        perpWallDist = (sideDistY - deltaDistY);

      int lineHeight = (int) (height / perpWallDist);
      int drawStart = -lineHeight / 2 + height / 2;
      int drawEnd = lineHeight / 2 + height / 2;

      // add check
      if (drawStart < 0)
        drawStart = 0;
      if (drawEnd > height)
        drawEnd = height - 1;

      int shade = Math.min(255, (int) (255 / perpWallDist));
      if (side == 1)
        shade = (int) (shade * 0.7); // darker shade for y-sides
      g.setColor(new Color(shade, shade, shade));

      int texNum = map.getCell(mapX, mapY);
      BufferedImage texture = textureLoader.getTexture(texNum);

      double wallX;
      if (side == 0)
        wallX = player.y + perpWallDist * rayDirY;
      else
        wallX = player.x + perpWallDist * rayDirX;
      wallX -= Math.floor(wallX);

      int texX = (int) (wallX * texture.getWidth());
      if (side == 0 && rayDirX > 0)
        texX = texture.getWidth() - texX - 1;
      if (side == 1 && rayDirY < 0)
        texX = texture.getWidth() - texX - 1;

      for (int y = drawStart; y <= drawEnd; y++) {
        int d = y * 256 - height * 128 + lineHeight * 128;
        int texY = ((d * texture.getHeight()) / lineHeight) / 256;

        // border check
        if (texY < 0)
          texY = 0;
        if (texY >= texture.getHeight())
          texY = texture.getHeight() - 1;

        int color = texture.getRGB(texX, texY);

        if (side == 1) {
          Color c = new Color(color);
          color = new Color(
              (int) (c.getRed() * 0.7), (int) (c.getGreen() * 0.7), (int) (c.getBlue() * 0.7))
                      .getRGB();
        }

        g.setColor(new Color(color));
        g.drawLine(x, y, x, y);
      }
    }

    // renderMiniMap(g);
  }

  public void renderMiniMap(Graphics g) {
    int tileSize = 8; // display size for each block
    int offsetX = 10; // starting x position in mini map
    int offsetY = 10; // starting y position in mini map

    // draw mini map block
    for (int y = 0; y < map.getHeight(); y++) {
      for (int x = 0; x < map.getWidth(); x++) {
        if (map.isWall(x, y)) {
          g.setColor(Color.DARK_GRAY);
        } else {
          g.setColor(Color.LIGHT_GRAY);
        }
        g.fillRect(offsetX + x * tileSize, offsetY + y * tileSize, tileSize, tileSize);
      }
    }

    // draw player position
    int playerX = (int) (offsetX + player.x * tileSize);
    int playerY = (int) (offsetY + player.y * tileSize);
    g.setColor(Color.RED);
    g.fillOval(playerX - 3, playerY - 3, 6, 6);

    // show player's direction
    int lineLength = 10;
    int dirX = (int) (Math.cos(player.angle) * lineLength);
    int dirY = (int) (Math.sin(player.angle) * lineLength);
    g.drawLine(playerX, playerY, playerX + dirX, playerY + dirY);
  }

  public void renderRules(Graphics g) {
    int tileSize = 8; // display size for each block
    int offsetX = 100; // starting x position in mini map
    int offsetY = 20; // starting y position in mini map

    BufferedImage texture = textureLoader.getTexture(1);
    // g.drawImage(texture, offsetX, offsetY, null);
    for (int x = 0; x < texture.getWidth(); x++) {
      for (int y = 0; y < texture.getHeight(); y++) {
        int color = texture.getRGB(x, y);
        g.setColor(new Color(color));
        g.fillRect(offsetX + x * tileSize, offsetY + y * tileSize, tileSize, tileSize);
      }
    }
  }
}