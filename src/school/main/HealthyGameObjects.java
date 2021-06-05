package school.main;

import java.awt.*;

// gameObjects with health and damage
public abstract class HealthyGameObjects<PARENT_TYPE extends GameObject> extends GameObject<PARENT_TYPE> {

    public HealthyGameObjects(String id, PARENT_TYPE parent, int initialHealth) {
        super(id, parent);
        this.health = initialHealth;
    }

    public abstract void onDeath();

    private int health;

    // health for game objects
    public int getHeath() {
        return this.health;
    }

    public void setHealth(int h) {
        this.health = h;
        if (health <= 0) {
            this.health = 0;
            this.onDeath();
            this.removeFromParent();
        }
    }

    public void subtractHealth() {
        this.setHealth(this.getHeath() - 10);
    }

    public static double[] verticalDeathBounds = new double[]{75, 75};

    @Override
    public void onUpdate(double deltaMs) {
        if (this.getGlobalX() + this.getWidth() / 2 < 0) {
            this.subtractHealth();
        }
        if (this.getGlobalX() + this.getWidth() > getFrame().getWidth()) {
            this.subtractHealth();
        }
        if (this.getGlobalY() < verticalDeathBounds[0]) {
            this.subtractHealth();
        }
        if (this.getGlobalY() + this.getHeight() > this.getFrame().getHeight() - verticalDeathBounds[1]) {
            this.subtractHealth();
        }
    }
}
