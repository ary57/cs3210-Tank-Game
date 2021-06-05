package school.main.gameobjects;

import school.main.GameObject;
import school.main.HealthyGameObjects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Collision objects placed by player-2 (the dropper)
 */
public class GameBox extends HealthyGameObjects<GameMap> {


    public GameBox(String id, GameMap parent) {
        super(id, parent, 40);
        setSize(20, 20);
        
        File cobbleTexture = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "BoxTexture.png");
        try {
            img  = ImageIO.read(cobbleTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //used to assign unique IDs for each box
    private static int boxCount = 0;

    public static void PlaceBox(GameMap map, int x, int y, int size) {
        GameBox b = new GameBox("Box-" + boxCount++, map);
        b.setLocation(x, y);
        b.setSize(size, size);
        map.addChildObject(b);
    }

    //font stored as field to avoid memory churn
    private Font font = new Font("Ink Free", Font.BOLD, 15);
    //texture to be drawn
    private BufferedImage img;

    @Override
    public void onDraw(Graphics graphics, double deltaMs) {
        graphics.setColor(Color.white);
        graphics.setFont(font);
        graphics.drawImage(img, 0,0,20,20,null);
        //graphics.drawString(this.getId(), (int) (getWidth() / 2), (int) (getHeight() / 2));
    }

    @Override
    public void onDeath(){
        this.removeFromParent();
    }
}
