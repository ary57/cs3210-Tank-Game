package school.main.gameobjects;

import school.main.GameObject;

import java.awt.*;

public class Projectile extends GameObject {
    public double speed = 200;

    private static int projCount = 0;
    public Projectile(int x, int y, double rot) {
        super("projectile_" + projCount++, null);
        this.setLocation(x, y);
        this.setRotation(rot);
        this.setSize(3,3);
    }

    @Override
    public void onDraw(Graphics graphics, double deltaMs) {
        graphics.setColor(Color.MAGENTA);
        setX((int)(getX() + (deltaMs/1000.0) * (speed * Math.cos(this.getRotation()))));
        setY((int)(getY() + (deltaMs/1000.0) * speed * Math.sin(this.getRotation())));
        System.out.println("Projectile " + this.getX() + ", " + this.getY());
        graphics.fillOval(0, 0, this.getWidth(), this.getHeight());
    }
}
