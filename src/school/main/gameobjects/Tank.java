package school.main.gameobjects;

import school.main.GameObject;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Tank extends GameObject<GameMap> implements KeyListener {

    private final Turret turret;
    //
    boolean isDriving = false;
    boolean isMovingUp = false;
    boolean isMovingDown = false;
    boolean isMovingLeft = false;
    boolean isMovingRight = false;
    private final double SPEED = 1; // pixels per ms;

    public Tank(String id, int x, int y, GameMap parent) {
        super(id, parent);
        this.setLocation(x, y);
        this.setSize(20, 30);
        this.turret = new Turret(this);
        this.addChildObject(turret);
        getFrame().addKeyListener(this);
    }
    //


    @Override
    public void onDraw(Graphics graphics, double deltaMs) { // added KeyEvent e
        graphics.setColor(Color.blue);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (isMovingUp) {
            setY((int) (getY() - deltaMs * SPEED));
        }
        if (isMovingDown) {
            setY((int) (getY() + deltaMs * SPEED));
        }
        if (isMovingLeft) {
            setX((int) (getX() - deltaMs * SPEED));
        }
        if (isMovingRight) {
            setX((int) (getX() + deltaMs * SPEED));
        }

    }


    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // stop the tank
        System.out.println(e.getKeyChar());
        switch (e.getKeyCode()) {
            // up
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                isMovingUp = true;
                break;
            //down
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                isMovingDown = true;
                break;
            //left
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                isMovingLeft = true;
                break;
            //right
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                isMovingRight = true;
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // stop the movement
        switch (e.getKeyCode()) {
            // up
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                isMovingUp = false;
                break;
            //down
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                isMovingDown = false;
                break;
            //left
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                isMovingLeft = false;
                break;
            //right
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                isMovingRight = false;
                break;
        }

    }
}
