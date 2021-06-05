package school.main.gameobjects;

import school.main.GameObject;
import school.main.HealthyGameObjects;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Consumer;

public class Projectile extends HealthyGameObjects {
    public double speed = 200;

    private static int projCount = 0;

    public Projectile(double x, double y, Turret turr) {
        super("projectile_" + projCount++, turr.getParent().getParent(), 10);
        this.setLocation(x, y);
        this.setRotation(turr.getRotation());
        this.setSize(5, 5);
        startTime = new Date().getTime();
        this.ignoreObjectIntersectionIds = new String[]{"Map", this.getId(), turr.getId(), turr.getParent().getId()};
    }

    private long startTime;
    public long timeToLiveMs = 1000 * 5;

    @Override
    public void onDraw(Graphics graphics, double deltaMs) {
        graphics.setColor(Color.magenta);
        double ballX = Math.cos(this.getRotation());
        double ballY = Math.sin(this.getRotation());
        setX((getX() + (deltaMs / 100) * (speed * ballX)));
        setY((getY() + (deltaMs / 100) * (speed * ballY)));
        graphics.fillOval(0, 0, (int) this.getWidth(), (int) this.getHeight());
//        System.out.println("X,Y:  ("+ballX + ","+ballY+")");
//        System.out.println("Projectile " + this.getX() + ", " + this.getY());
    }

    @Override
    public void onUpdate(double deltaMs) {
        super.onUpdate(deltaMs);
        if (timeToLiveMs != -1 && new Date().getTime() - startTime >= timeToLiveMs) {
            this.removeFromParent();
        }
        ArrayList<GameObject> collisions = this.getIntersections();
        if (collisions.size() > 0) {
//            System.out.println(collisions.size());
            collisions.forEach((Consumer<GameObject>) (gameObject) -> {
                if (gameObject instanceof HealthyGameObjects) {
//                    System.out.println("Damaging " + gameObject.getId());
                    ((HealthyGameObjects) gameObject).subtractHealth();
                    this.subtractHealth();
                }
            });
        }
    }

    @Override
    public void onDeath() {}
}
