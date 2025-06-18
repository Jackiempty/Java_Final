import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class TextureLoader {
  private HashMap<Integer, BufferedImage> textures = new HashMap<>();

  public TextureLoader() {
    try {
      textures.put(1, ImageIO.read(getClass().getResource("/resources/tex1.png")));
      textures.put(2, ImageIO.read(getClass().getResource("/resources/tex2.png")));
      textures.put(3, ImageIO.read(getClass().getResource("/resources/tex3.png")));
      textures.put(4, ImageIO.read(getClass().getResource("/resources/tex4.png")));
      textures.put(5, ImageIO.read(getClass().getResource("/resources/tex5.png")));
      textures.put(6, ImageIO.read(getClass().getResource("/resources/tex6.png")));
      textures.put(7, ImageIO.read(getClass().getResource("/resources/tex7.png")));
      textures.put(8, ImageIO.read(getClass().getResource("/resources/tex8.png")));
      textures.put(9, ImageIO.read(getClass().getResource("/resources/Rule.jpeg")));
      textures.put(10, ImageIO.read(getClass().getResource("/resources/End.jpeg")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public BufferedImage getTexture(int index) {
    return textures.get(index);
  }
}
