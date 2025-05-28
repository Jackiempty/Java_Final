
import java.awt.*;
import java.awt.image.BufferedImage;


public class Renderer {
    private int width, height;
    private Map map;
    private Player player;


    public Renderer(int width, int height, Map map, Player player) {
        this.width = width;
        this.height = height;
        this.map = map;
        this.player = player;
        
    }

    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY); // sky
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
                if (map.isWall(mapX, mapY)) hit = 1;
            }

            double perpWallDist;
            if (side == 0) perpWallDist = (sideDistX - deltaDistX);
            else perpWallDist = (sideDistY - deltaDistY);

            int lineHeight = (int) (height / perpWallDist);
            int drawStart = -lineHeight / 2 + height / 2;
            int drawEnd = lineHeight / 2 + height / 2;

            int shade = Math.min(255, (int) (255 / perpWallDist));
            if (side == 1) shade = (int) (shade * 0.7); // darker shade for y-sides
            g.setColor(new Color(shade, shade, shade));
            g.drawLine(x, drawStart, x, drawEnd);

            
//            int texNum = map.getCell(mapX, mapY) - 1; // map值1 → 紋理0
//            BufferedImage texture = textureLoader.getTexture(texNum);
//
//            double wallX;
//            if (side == 0) wallX = player.y + perpWallDist * rayDirY;
//            else           wallX = player.x + perpWallDist * rayDirX;
//            wallX -= Math.floor(wallX);
//
//            int texX = (int)(wallX * texture.getWidth());
//            if (side == 0 && rayDirX > 0) texX = texture.getWidth() - texX - 1;
//            if (side == 1 && rayDirY < 0) texX = texture.getWidth() - texX - 1;
//
//            for (int y = drawStart; y < drawEnd; y++) {
//                int d = y * 256 - height * 128 + lineHeight * 128;
//                int texY = ((d * texture.getHeight()) / lineHeight) / 256;
//
//                if (texX >= 0 && texY >= 0 && texX < texture.getWidth() && texY < texture.getHeight()) {
//                    int color = texture.getRGB(texX, texY);
//                    if (side == 1) {
//                        Color c = new Color(color);
//                        color = new Color(
//                            (int)(c.getRed() * 0.7),
//                            (int)(c.getGreen() * 0.7),
//                            (int)(c.getBlue() * 0.7)
//                        ).getRGB();
//                    }
//                    g.setColor(new Color(color));
//                    g.drawLine(x, y, x, y); // 畫1像素的垂直線
//                }
//            }
        }
    }
}
