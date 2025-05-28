
public class Player {
    public double x, y;
    public double angle;

    public Player(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public void turn(double delta) {
        angle += delta;
    }
    
    public void move(double distance, Map map) {
        double newX = x + Math.cos(angle) * distance;
        double newY = y + Math.sin(angle) * distance;

        // Debug: 印出位置
        System.out.println("Move attempt to: " + newX + ", " + newY);

        if (!map.isWall(newX, y)) x = newX;
        if (!map.isWall(x, newY)) y = newY;
    }
    
}
