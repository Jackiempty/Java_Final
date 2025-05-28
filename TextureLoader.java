import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class TextureLoader {
    private final HashMap<Integer, BufferedImage> textures = new HashMap<>();

    public void loadTexture(int id, String path) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(path));
            textures.put(id, img);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getTexture(int id) {
        return textures.get(id);
    }
}
