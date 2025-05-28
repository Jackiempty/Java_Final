public class Map {
    private final int[][] map = {
        {1,1,1,1,1},
        {1,0,0,0,1},
        {1,0,1,0,1},
        {1,0,0,0,1},
        {1,1,1,1,1}
    };

    public boolean isWall(double x, double y) {
        int mapX = (int)x;
        int mapY = (int)y;
        if (mapX < 0 || mapX >= map[0].length || mapY < 0 || mapY >= map.length) return true;
        return map[mapY][mapX] > 0;
    }
}
