package school.main.gameobjects;

import school.main.GameObject;

import school.main.HealthyGameObjects;
import school.main.math.geometry.Line;
import school.main.math.linear.Vector2;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public abstract class Tank extends HealthyGameObjects<GameMap> implements KeyListener {

    private final Line frontEdge;
    private final Line rightEdge;
    private final Line backEdge;
    private final Line leftEdge;
    protected String[] edgeIgnoreList = new String[0];
    //
    private Color color;
    //
    boolean isMovingUp() {
        return this.isMoving[0];
    }
    boolean isMovingDown() {
        return this.isMoving[1];
    }
    boolean isMovingLeft() {
        return this.isMoving[2];
    }
    boolean isMovingRight() {
        return this.isMoving[3];
    }

    // SPEED in pixels per s;
    private final double SPEED = 80;

    private boolean[] isMoving = new boolean[]{false, false, false, false};
    //Controls to move up, down, left, right
    private final int[] movementControls;

    public Tank(String id, int x, int y, int[] movementControls, GameMap parent) {
        super(id, parent, 100);
        this.movementControls = movementControls;
        this.setLocation(x, y);
        this.setSize(30, 20);
        getFrame().addKeyListener(this);
        double edgeDistance = 1;
        //Supplier<Double> edgeDistance = () -> ((1 + Math.cos(new Date().getTime() / 1000.0)) * 10);

        Vector2 frontOffset = Vector2.fromAngle(() -> getRotation() + Math.PI / 2).multiply(edgeDistance).multiply(-1).as2D();
        this.frontEdge = new Line(this.upLeftCorner.add(frontOffset).as2D(), this.upRightCorner.add(frontOffset).as2D());

        Vector2 rightOffset = Vector2.fromAngle(() -> getRotation()).multiply(edgeDistance).as2D();
        this.rightEdge = new Line(this.upRightCorner.add(rightOffset).as2D(), this.downRightCorner.add(rightOffset).as2D());

        Vector2 backOffset = Vector2.fromAngle(() -> getRotation() - Math.PI / 2).multiply(edgeDistance).multiply(-1).as2D();
        this.backEdge = new Line(this.downLeftCorner.add(backOffset).as2D(), this.downRightCorner.add(backOffset).as2D());

        Vector2 leftOffset = Vector2.fromAngle(() -> getRotation() + Math.PI).multiply(edgeDistance).as2D();
        this.leftEdge = new Line(this.downLeftCorner.add(leftOffset).as2D(), this.upLeftCorner.add(leftOffset).as2D());
    }
    //

    @Override
    public void onPostDrawCollisions(Graphics2D graphics, double deltaMs) {
        graphics.setColor(Color.cyan);
        frontEdge.drawLine(graphics);
        graphics.setColor(Color.YELLOW);
        rightEdge.drawLine(graphics);
        graphics.setColor(Color.green);
        backEdge.drawLine(graphics);
        graphics.setColor(Color.blue);
        leftEdge.drawLine(graphics);
    }
    @Override
    public void onDraw(Graphics graphics, double deltaMs) { // added KeyEvent e
        graphics.setColor(color);
        graphics.fillRect(0, 0, (int) this.getWidth(), (int) this.getHeight());

        if (isMovingUp()) {
            if (this.frontEdge.getIntersections(true, this.getRoot(), edgeIgnoreList).size() == 0) {
                setY((getY() - deltaMs / 1000d * SPEED));
            }
        }
        if (isMovingDown()) {
            if (this.backEdge.getIntersections(true, this.getRoot(), edgeIgnoreList).size() == 0) {
                setY((getY() + deltaMs / 1000d * SPEED));
            }
        }
        if (isMovingLeft()) {
            if (this.leftEdge.getIntersections(true, this.getRoot(), edgeIgnoreList).size() == 0) {
                setX((getX() - deltaMs / 1000d * SPEED));
            }
        }
        if (isMovingRight()) {
            ArrayList<GameObject> intersections = this.rightEdge.getIntersections(true, this.getRoot(), edgeIgnoreList);
            if (intersections.size() == 0) {
                setX((getX() + deltaMs / 1000d * SPEED));
            } else {
                //System.out.println(intersections.toString());
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        // stop the tank
        // System.out.println(e.getKeyChar());
        for (int i = 0; i < movementControls.length; i++) {
            if (e.getKeyCode() == movementControls[i]) {
                this.isMoving[i] = true;
                break;
            }
        }
    }
    public abstract void onKeyReleased(KeyEvent e);
    @Override
    public void keyReleased(KeyEvent e) {
        // stop the movement
        for (int i = 0; i < movementControls.length; i++) {
            if (e.getKeyCode() == movementControls[i]) {
                this.isMoving[i] = false;
                break;
            }
        }
        this.onKeyReleased(e);
    }

    /**
     * if a tank dies it returns its id so the end screen can specify the type of message to print in screen.
     */
    @Override
    public void onDeath() {
        this.getGameMap().getGame().gameOver(this.getId());
    }

    public void setColor(Color c){
        this.color = c;
    }


}