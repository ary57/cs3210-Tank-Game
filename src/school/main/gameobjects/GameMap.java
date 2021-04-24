package school.main.gameobjects;

import school.main.Game;
import school.main.GameObject;

import javax.swing.*;
import java.awt.*;

public class GameMap extends GameObject {

    final Game game;
    final JFrame frame;

    private int focusObjectPadding = 30;
    private GameObject focusObject = null;

    public GameMap(JFrame frame, Game mainGame) {
        super("Map", null);
        this.frame = frame;
        this.game = mainGame;
        Tank tonk = new Tank("Player 1", 100, 150, this);
        this.focusObject = tonk;
        this.addChildObject(tonk);

    }


    @Override
    public void onDraw(Graphics graphics, double deltaMs) {
        graphics.setColor(Color.black);
        graphics.drawLine(0,0, 1000, 0);
        graphics.drawLine(0,0, 0, 100);
        if (focusObject != null) {
            if (focusObject.getGlobalX() < focusObjectPadding) {
                this.setX((int) (getX() + deltaMs));
            }
            if (focusObject.getGlobalX() + focusObject.getWidth() > game.getWidth() - focusObjectPadding) {
                this.setX((int) (getX() - deltaMs));
            }
            if (focusObject.getGlobalY() < focusObjectPadding) {
                this.setY((int) (getY() + deltaMs));
            }
            if (focusObject.getGlobalY() + focusObject.getHeight() > game.getHeight() - focusObjectPadding) {
                this.setY((int) (getY() - deltaMs));
            }
        }
    }

    @Override
    public GameMap getGameMap() {
        return this;
    }

    @Override
    public JFrame getFrame() {
        return this.frame;
    }
}
