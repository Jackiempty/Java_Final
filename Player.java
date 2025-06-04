
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
    
    public void move_FB(double distance, Map map) { // move forward and backward
        double newX = x + Math.cos(angle) * distance;
        double newY = y + Math.sin(angle) * distance;

        // Debug: print coordinate
        System.out.println("Move attempt to: " + newX + ", " + newY);

        if (!map.isWall(newX, y)) x = newX;
        if (!map.isWall(x, newY)) y = newY;
    }

    public void move_LR(double distance, Map map) { // move left and right
        double newX = x + Math.cos(angle + Math.PI / 2) * distance;
        double newY = y + Math.sin(angle + Math.PI / 2) * distance;

        // Debug: print coordinate
        System.out.println("Strafe attempt to: " + newX + ", " + newY);

        if (!map.isWall(newX, y)) x = newX;
        if (!map.isWall(x, newY)) y = newY;
    }
}
