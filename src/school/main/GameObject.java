package school.main;

import school.main.gameobjects.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

public abstract class GameObject<PARENT_TYPE extends GameObject> {
    private String id;
    private int x;
    private int y;
    private int width;
    private int height;
    private double rotation = 0;

    public GameObject(String id, PARENT_TYPE parent) {
        this.id = id;
        this.x = 0;
        this.y = 0;
        this.width = 10;
        this.height = 10;
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocation(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addY(int ay){
        System.out.println("Was " + this.y);
        this.y += ay;
        System.out.println("Is " + this.y);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void addChildObject(GameObject child) {
        if (this.childObjects == null) {
            this.childObjects = new HashMap();
        }
        this.childObjects.put(child.getId(), child);
    }

    public PARENT_TYPE getParent() {
        return parent;
    }

    public int getGlobalX(){
        if(this.parent != null){
            return this.getX() + this.parent.getGlobalX();
        }else{
            return this.getX();
        }
    }
    public int getGlobalY(){
        if(this.parent != null){
            return this.getY() + this.parent.getGlobalY();
        }else{
            return this.getY();
        }
    }

    private PARENT_TYPE parent;
    private HashMap<String, GameObject> childObjects = null;
    private AffineTransform oldTransform;
    private GameObject currentGameObject;

    public int getRotationOffsetX(){
        return this.getWidth()/2;
    }
    public int getRotationOffsetY(){
        return this.getHeight()/2;
    }
    public void draw(Graphics2D graphics, double deltaMs) {
        this.oldTransform = graphics.getTransform();
        this.onDraw(graphics, deltaMs);

        if (this.childObjects != null) {
            for (String id : this.childObjects.keySet()) {
                currentGameObject = this.childObjects.get(id);
                graphics.translate(currentGameObject.getX(), currentGameObject.getY());
                graphics.rotate(currentGameObject.getRotation(), currentGameObject.getRotationOffsetX(), currentGameObject.getRotationOffsetY());
                currentGameObject.draw(graphics, deltaMs);
                graphics.setTransform(oldTransform);
                //graphics.translate(0-currentGameObject.getX(), 0-currentGameObject.getY());

            }
        }
    }

    public abstract void onDraw(Graphics graphics, double deltaMs);
    public JFrame getFrame(){
        return this.getGameMap().getFrame();
    }
    public GameMap getGameMap(){
        if(this.getParent() != null){
            return this.getParent().getGameMap();
        }
        return null;
    }

    public double getRotationDeg() {
        return Math.toDegrees(this.rotation);
    }

    public void setRotationDeg(double rotDeg) {
        this.setRotation(Math.toRadians(rotDeg));
    }

    //radians
    public double getRotation() {
        return rotation;
    }

    //radians
    public void setRotation(double rot) {
        this.rotation = rot;
    }

    public void setSize(int w, int h) {
        this.setWidth(w);
        this.setHeight(h);
    }

}
