package school.main.gameobjects;

import school.main.GameObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Turret extends GameObject<Tank> implements MouseMotionListener, MouseListener {
    public Turret(Tank parent) {
        super(parent.getId() + "-Turret", parent);
        this.setSize(20, 1);
        getFrame().addMouseListener(this);
        getFrame().addMouseMotionListener(this);

    }

    @Override
    public int getX() {
        return this.getParent().getWidth() / 2;
    }

    @Override
    public int getY() {
        return this.getParent().getHeight() / 2;
    }

    @Override
    public int getRotationOffsetX() {
        return 0;
    }

    @Override
    public int getRotationOffsetY() {
        return 0;
    }

    @Override
    public void onDraw(Graphics graphics, double deltaMs) {
        graphics.setColor(Color.YELLOW);
        graphics.fillRect(0, 0, getWidth(), getHeight());

//        this.setRotationDeg(this.getRotationDeg() + 360 * (deltaMs / 1000));

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
fire();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mouse_Location_x = e.getX();// + ;
        int mouse_Location_y = e.getY();// + this.getGameMap().getY();

        double tankLocationX = (this.getParent().getGlobalX() + this.getParent().getWidth() / 2.0);
        double tankLocationY = (this.getParent().getGlobalY() + this.getParent().getHeight()*1.5);

        double turret_mouse_distance_x = mouse_Location_x - tankLocationX;
        double turret_mouse_distance_y = tankLocationY - mouse_Location_y;

        double toRotateRad = Math.atan(turret_mouse_distance_x / turret_mouse_distance_y);
        System.out.println("Parent: " + tankLocationX + " " + tankLocationY);
        System.out.println("Mouse: " + mouse_Location_x + ", " + mouse_Location_y);
        System.out.println("Distance: " + turret_mouse_distance_x + ", " + turret_mouse_distance_y);

        this.setRotation(toRotateRad);

    }

    public void fire() {
        Projectile p = new Projectile(this.getParent().getX(), this.getParent().getY(), 0);
        this.getGameMap().addChildObject(p);
    }
}
