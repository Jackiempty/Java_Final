import java.awt.*;

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
        // sky and floor
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, width, height / 2);
        g.setColor(Color.GRAY);
        g.fillRect(0, height / 2, width, height / 2);

        for (int x = 0; x < width; x++) {
            double cameraX = 2 * x / (double) width - 1;
            double rayDirX = Math.cos(player.angle) + cameraX * Math.sin(player.angle);
            double rayDirY = Math.sin(player.angle) - cameraX * Math.cos(player.angle);

            double rayX = player.x;
            double rayY = player.y;

            double deltaDist = 0.01;
            double distance = 0;

            while (true) {
                rayX += rayDirX * deltaDist;
                rayY += rayDirY * deltaDist;
                distance += deltaDist;

                if (map.isWall(rayX, rayY)) break;
            }

            int lineHeight = (int) (height / distance);
            int drawStart = height / 2 - lineHeight / 2;
            int drawEnd = height / 2 + lineHeight / 2;

            int shade = Math.min(255, (int) (255 / distance));
            g.setColor(new Color(shade, shade, shade));
            g.drawLine(x, drawStart, x, drawEnd);
        }
    }
}
