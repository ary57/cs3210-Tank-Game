package school.main.gameobjects;

import school.main.Game;
import school.main.GameObject;

import javax.swing.*;
import java.awt.*;

/**
 * This is the root object for most other in game objects, this creates the appearance of a moving "camera"
 */
public class GameMap extends GameObject {

    public final Game game;
    final JFrame frame;

    // mapSpeed is the speed with which the map scrolls.
    public double mapSpeed = 20;

    public GameMap(JFrame frame, Game mainGame) {
        super("Map", null);
        this.frame = frame;
        this.game = mainGame;
        double middle = frame.getWidth() / 2;
        Tank tonkA = new ShooterTank("Player 1 - Shooter", (int) (middle - middle / 2), 150, this);
        Tank tonkB = new DropperTank("Player 2 - Dropper", (int) (middle + middle / 2), 150, this);
        this.addChildObject(tonkA);
        this.addChildObject(tonkB);
    }

    @Override
    public void onDraw(Graphics graphics, double deltaMs) {
        graphics.setColor(Color.black);
        graphics.drawLine(0, 0, 1000, 0);
        graphics.drawLine(0, 0, 0, 100);
        this.setX(getX() - (deltaMs / 1000d) * mapSpeed);
    }

//    @Override
//    public GameMap r() {
//        return this;
//    }

    @Override
    public GameMap getGameMap() {
        return this;
    }

    public Game getGame() {
        return this.game;
    }

    @Override
    public JFrame getFrame() {
        return this.frame;
    }
}
