package school.main.gameobjects;

import school.main.GameObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Turret extends GameObject<ShooterTank> implements MouseMotionListener, MouseListener {
    public Turret(ShooterTank parent) {
        super(parent.getId() + "-Turret", parent);
        this.setSize(20, 2);
        getFrame().addMouseListener(this);
        getFrame().addMouseMotionListener(this);
    }

    @Override
    public double getX() {
        return this.getParent().getWidth() / 2;
    }
    @Override
    public double getY() {
        return this.getParent().getHeight() / 2;
    }

    @Override
    public double getRotationOffsetX() {
        return 0;
    }
    @Override
    public double getRotationOffsetY() {
        return 0;
    }

    @Override
    public void onDraw(Graphics graphics, double deltaMs) {
        graphics.setColor(Color.YELLOW);
        graphics.fillRect(0, 0, (int)getWidth(), (int)getHeight());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {
        fire();
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
        double tankLocationY = (this.getParent().getGlobalY() + this.getParent().getHeight()* 1.5);

        // distance from the mouse to the center of the tank (or the turret)
        double turret_mouse_distance_x = mouse_Location_x - tankLocationX;
        double turret_mouse_distance_y = tankLocationY - mouse_Location_y;

        // calculate the degree to which the turret needs to be rotated; degree theta = tan^-1(x/y);
        double toRotateRad = Math.atan(turret_mouse_distance_x / turret_mouse_distance_y);

//        System.out.println("Parent: " + tankLocationX + " " + tankLocationY);
//        System.out.println("Mouse: " + mouse_Location_x + ", " + mouse_Location_y);
//        System.out.println("Distance: " + turret_mouse_distance_x + ", " + turret_mouse_distance_y);

        toRotateRad+=Math.PI/2;
        if(turret_mouse_distance_y > 0) toRotateRad+=Math.PI;
        this.setRotation(toRotateRad);
    }

    /**
     * fires a projectile in the direction pointed by the mouse
     */
    public void fire() {
        double projectileX = this.getParent().getX()+this.getParent().getWidth()/2;
        double projectileY = this.getParent().getY() + this.getParent().getHeight()/2;
        Projectile p = new Projectile(projectileX, projectileY, this);
        this.getRoot().addChildObject(p);
    }
}
