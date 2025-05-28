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
}
