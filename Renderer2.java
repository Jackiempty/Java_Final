
import java.awt.*;
import java.awt.image.BufferedImage;


public class Renderer2 {
    private int width, height;
    private Map map;
    private Player player;
    private TextureLoader textureLoader;
    private BufferedImage screenImage;



    public Renderer2(int width, int height, Map map, Player player) {
        this.width = width;
        this.height = height;
        this.map = map;
        this.player = player;
        this.textureLoader = new TextureLoader();
        screenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    }

    public void render(Graphics g) {
    	Graphics2D g2d = screenImage.createGraphics();
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, width, height / 2);
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, height / 2, width, height / 2);
        g2d.dispose();

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
            
            //新增檢查
            if (drawStart < 0) drawStart = 0;
            if (drawEnd >= height) drawEnd = height - 1;
            
            int shade = Math.min(255, (int) (255 / perpWallDist));
            if (side == 1) shade = (int) (shade * 0.7); // darker shade for y-sides
            g.setColor(new Color(shade, shade, shade));

            int texNum = map.getCell(mapX, mapY);
            BufferedImage texture = textureLoader.getTexture(texNum);

            double wallX;
            if (side == 0) wallX = player.y + perpWallDist * rayDirY;
            else           wallX = player.x + perpWallDist * rayDirX;
            wallX -= Math.floor(wallX);

            int texX = (int)(wallX * texture.getWidth());
            if (side == 0 && rayDirX > 0) texX = texture.getWidth() - texX - 1;
            if (side == 1 && rayDirY < 0) texX = texture.getWidth() - texX - 1;

            for (int y = drawStart; y <= drawEnd; y++) {
                int d = y * 256 - height * 128 + lineHeight * 128;
                int texY = ((d * texture.getHeight()) / lineHeight) / 256;

                // 加入邊界檢查
                if (texY < 0) texY = 0;
                if (texY >= texture.getHeight()) texY = texture.getHeight() - 1;

                int color = texture.getRGB(texX, texY);

                if (side == 1) {
                    Color c = new Color(color);
                    color = new Color(
                        (int)(c.getRed() * 0.7),
                        (int)(c.getGreen() * 0.7),
                        (int)(c.getBlue() * 0.7)
                    ).getRGB();
                }

                screenImage.setRGB(x, y, color);
            }

            g.drawImage(screenImage, 0, 0, null);

        }
    }
}
