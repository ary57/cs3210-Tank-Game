package school.main.gameobjects;

import school.main.GameObject;

import java.awt.*;

public class GameBox extends GameObject<GameMap> {
    private Color color = Color.GREEN;

    public GameBox(String id, GameMap parent) {
        super(id, parent);
        setSize(20, 20);
    }
    public static void PlaceBox(GameMap map, int x, int y, int size, Color color){
        GameBox b = new GameBox(System.currentTimeMillis() + "", map);
        b.setLocation(x, y);
        b.color = color;
        b.setSize(size, size);
        map.addChildObject(b);
    }

    @Override
    public void onDraw(Graphics graphics, double deltaMs) {
        graphics.setColor(this.color);
        graphics.fillRect(0,0,getWidth(),getHeight());
    }
}
