package school.main.gameobjects;

import school.main.gameobjects.GameMap;
import school.main.gameobjects.Tank;
import school.main.gameobjects.Turret;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * ShooterTank is a tank that has the ability to aim and shoot projectiles.
 * Unlike the DropperTank, it is not capable of placing GameBoxes
 */
public class ShooterTank extends Tank {
    private final Turret turret;
    public ShooterTank(String id, int x, int y, GameMap parent) {
        super(id, x, y, new int[]{KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D}, parent);
        this.setColor(Color.red);
        this.turret = new Turret(this);
        this.addChildObject(turret);
        this.ignoreObjectIntersectionIds = new String[]{"Map", id, turret.getId()};
        this.edgeIgnoreList = new String[]{turret.getId()};

    }

    @Override
    public void onKeyReleased(KeyEvent e) { }
}
