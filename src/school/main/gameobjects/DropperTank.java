package school.main.gameobjects;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * DropperTank is a tank that has the ability to place GameBoxes.
 * Unlike the ShooterTank, it is unable to fire projectiles and thus does not have a turret.
 */
public class DropperTank extends Tank {


    public DropperTank(String id, int x, int y, GameMap parent) {
        super(id, x, y, new int[]{KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L}, parent);
        this.setColor(Color.GREEN);
    }

    @Override
    public void onKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_O:
                placeBlock('o');
                break;
            case KeyEvent.VK_U:
                placeBlock('u');
                break;
        }
    }
    public void placeBlock(char c) {
        int x = (int) this.getX();
        int y = (int) this.getY();
        if (c == 'o') x += (int) this.getWidth() + 5;
        if (c == 'u') x -= (int) this.getWidth() + 5;

        Color color = Color.blue;
        GameBox.PlaceBox(getGameMap(), x, y, 20);
    }
}
