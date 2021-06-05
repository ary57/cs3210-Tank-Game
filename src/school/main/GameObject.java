package school.main;

import school.main.gameobjects.GameMap;
import school.main.gameobjects.GameOver;
import school.main.math.geometry.Triangle;
import school.main.math.linear.Matrix;
import school.main.math.linear.Vector2;
import school.main.math.linear.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.BiFunction;

public abstract class GameObject<PARENT_TYPE extends GameObject> {
    private String id;

    //position variables
    //this.x and this.y are stored relative to this.parent.x & this.parent.y
    private double x;
    private double y;
    private double width;
    private double height;
    private double rotation = 0;//radians
    private Vector2 globalPosition;

    protected Vector2 upLeftCorner, upRightCorner, downRightCorner, downLeftCorner;

    protected Triangle[] collision;


    public GameObject(String id, PARENT_TYPE parent) {
        this.id = id;
        this.x = 0;
        this.y = 0;
        this.width = 10;
        this.height = 10;
        this.parent = parent;

        this.globalPosition = new Vector2(() -> this.getGlobalX(), () -> this.getGlobalY());

        Vector relativeRotationOffset = new Vector(() -> (this.getRotationOffsetX()), () -> (this.getRotationOffsetY()));

        Vector relativeUpLeft = new Vector(0, 0).subtract(relativeRotationOffset);//same as relativeRotationOffset.multiplyByRef(-1)
        Vector relativeUpRight = new Vector(() -> (this.getWidth()), () -> (0d)).subtract(relativeRotationOffset);
        Vector relativeDownRight = new Vector(() -> (this.getWidth()), () -> this.getHeight()).subtract(relativeRotationOffset);
        Vector relativeDownLeft = new Vector(() -> (0d), () -> (this.getHeight())).subtract(relativeRotationOffset);

        //  | cosθ, -sinθ |
        //  | sinθ,  cosθ |
        Matrix rotationMatrix = Matrix.RotationMatrix(()->this.getRotation());

        this.upLeftCorner = rotationMatrix.multiplyColumnVector(relativeUpLeft).add(relativeRotationOffset).add(this.globalPosition).as2D();
        this.upRightCorner = rotationMatrix.multiplyColumnVector(relativeUpRight).add(relativeRotationOffset).add(this.globalPosition).as2D();
        this.downRightCorner = rotationMatrix.multiplyColumnVector(relativeDownRight).add(relativeRotationOffset).add(this.globalPosition).as2D();
        this.downLeftCorner = rotationMatrix.multiplyColumnVector(relativeDownLeft).add(relativeRotationOffset).add(this.globalPosition).as2D();

        this.collision = new Triangle[]{
                new Triangle(this.upLeftCorner, this.upRightCorner, this.downRightCorner).setOwner(this),
                new Triangle(this.downRightCorner, this.downLeftCorner, this.upLeftCorner).setOwner(this)
        };
    }

    public String getId() {
        return id;
    }


    public void setLocation(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public double getRotation() {
        return rotation;
    }
    public void setRotation(double rot) {
        this.rotation = rot;
    }

    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }

    public void setWidth(double width) {
        this.width = width;
    }
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @return x rotation pivot position (relative to object)
     */
    public double getRotationOffsetX() {
        return this.getWidth() / 2;
    }
    /**
     * @return y rotation pivot position (relative to object)
     */
    public double getRotationOffsetY() {
        return this.getHeight() / 2;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return x position relative to screen space
     */
    public double getGlobalX() {
        if (this.parent != null) {
            return this.getX() + this.parent.getGlobalX();
        } else {
            return this.getX();
        }
    }

    /**
     * @return y position relative to screen space
     */
    public double getGlobalY() {
        if (this.parent != null) {
            return this.getY() + this.parent.getGlobalY();
        } else {
            return this.getY();
        }
    }

    //reference to parent object
    private PARENT_TYPE parent;

    public PARENT_TYPE getParent() {
        return parent;
    }

    /**
     * Used for deleting objects from game
     */
    public void removeFromParent() {
        if (this.getParent() == null) {
            System.err.println("Cannot remove " + this.getId() + " because it has no parent");
        } else {
            for (int i = 0; i < this.getParent().childObjects.size(); i++) {
                if (((GameObject) this.getParent().childObjects.get(i)).getId().equals(this.getId())) {
                    this.getParent().childObjects.remove(i);
                    return;
                }
            }

        }
    }

    //list of child objects to be drawn relative to this object
    protected LinkedList<GameObject> childObjects = null;

    /**
     * @param child new object to be added to this object
     */
    public void addChildObject(GameObject child) {
        if (this.childObjects == null) {
            this.childObjects = new LinkedList();
        }
        this.childObjects.addLast(child);
    }

    /**
     * draws collision triangles for debug
     *
     * @param graphics
     * @param deltaMs
     */
    public void drawCollisions(Graphics2D graphics, double deltaMs) {
        //this.oldTransform = graphics.getTransform();
        //this.onDraw(graphics, deltaMs);

        for (Triangle t : this.collision) {
            graphics.setColor(Color.green);
            t.getEdgeAB().drawLine(graphics);
            graphics.setColor(Color.MAGENTA);
            t.getEdgeBC().drawLine(graphics);
            graphics.setColor(Color.blue);
            t.getEdgeCA().drawLine(graphics);
        }
        this.onPostDrawCollisions(graphics, deltaMs);
        if (this.childObjects != null) {
            this.childObjects.forEach((GameObject currentGameObject) -> {
                currentGameObject.drawCollisions(graphics, deltaMs);
            });
        }
    }

    /**
     * @param graphics
     * @param deltaMs  overridden for drawing debug graphics without offset transforms
     */
    public void onPostDrawCollisions(Graphics2D graphics, double deltaMs) {

    }


    //oldTransform is declared as a field instead of inside of draw() to avoid memory churn
    private AffineTransform oldTransform;

    /**
     * This function drives the overridable onDraw function,
     * taking care of relative coordinate translation & rotation
     *
     * @param graphics awt graphics object to draw to
     * @param deltaMs  time since last update in ms
     */
    public void draw(Graphics2D graphics, double deltaMs) {
        this.oldTransform = graphics.getTransform();
        this.onUpdate(deltaMs);
        this.onDraw(graphics, deltaMs);

        if (this.childObjects != null) {
            try {
                this.childObjects.forEach((GameObject currentGameObject) -> {
                    graphics.translate(currentGameObject.getX(), currentGameObject.getY());
                    graphics.rotate(currentGameObject.getRotation(), currentGameObject.getRotationOffsetX(), currentGameObject.getRotationOffsetY());
                    currentGameObject.draw(graphics, deltaMs);
                    graphics.setTransform(oldTransform);
                });
            } catch (ConcurrentModificationException cme) {
                //this happens when you add or remove an item during a draw procedure.
            }
        }
    }

    /**
     * Overriding onDraw is the heart of the graphics structure.
     * Before this function is called by the graphics update loop, the canvas is translated to this.getGlobalX() and this.getGlobalY().
     * Therefore draw commands are relative. Eg. drawRect(0, 0, width, height) will place a rectangle covering this object.
     *
     * @param graphics awt graphics object to draw to
     * @param deltaMs  time since last update in ms
     */
    public abstract void onDraw(Graphics graphics, double deltaMs);

    /**
     * used for updating every frame
     *
     * @param deltaMs time since last update in ms
     */
    public void onUpdate(double deltaMs) {
    }

    /**
     * This allows game objects easy access to the frame instance.
     * used mostly for accessing screen width & height
     *
     * @return jframe which the game is running in
     */
    public JFrame getFrame() {
        return this.getRoot().getFrame();
    }

    public GameMap getGameMap() {
        if (this.getParent() != null) {
            return this.getParent().getGameMap();
        }

        return null;
    }

    public void setSize(double w, double h) {
        this.setWidth(w);
        this.setHeight(h);
    }

    /**
     * @return Recursivly find root of parent/child object tree
     */
    public GameObject getRoot() {
        if (this.getParent() == null) {
            return this;
        }
        return this.getParent().getRoot();
    }

    /**
     * @param consumer          function to be run on each triangle: return true to keep iterating, return false to quit loop
     * @param recurseOnChildren
     */
    public void forEachCollisionTriangle(BiFunction<GameObject, Triangle, Boolean> consumer, boolean recurseOnChildren) {
        for (int i = 0; i < this.collision.length; i++) {
            if (consumer.apply(this, this.collision[i])) {
                return;
            }
        }
        if (recurseOnChildren && this.childObjects != null) {
            this.childObjects.forEach((GameObject object) -> {
                object.forEachCollisionTriangle(consumer, true);
            });
        }
    }

    protected String[] ignoreObjectIntersectionIds = new String[]{"", this.getId()};

    /**
     * Overloaded for ease of use
     * @return List of objects which geometrically overlap with this object
     */
    public ArrayList<GameObject> getIntersections() {
        return this.getIntersections(false, this.getRoot());
    }

    /**
     * @param exitOnFirstCollision If you're only interested in 1 intersection, set exitOnFirstCollision to true avoid processing the rest of this list
     * @param rootObject object to recursively check (will collision check children of rootObject)
     * @return List of objects which geometrically overlap with this object
     */
    public ArrayList<GameObject> getIntersections(boolean exitOnFirstCollision, GameObject rootObject) {
        ArrayList<GameObject> out = new ArrayList();
        GameObject ths = this;
        rootObject.forEachCollisionTriangle((BiFunction<GameObject, Triangle, Boolean>) (gameObject, triangle) -> {
            for (String ignoreId : ignoreObjectIntersectionIds) {
                if (gameObject.getId().equals(ignoreId)) {
                    return false;
                }
            }

            for (int i = 0; i < ths.collision.length; i++) {
                if (ths.collision[i].intersects(triangle)) {

                    out.add(gameObject);
                    if (exitOnFirstCollision) {
                        return false;
                    }
                }
            }
            return true;
        }, true);

        return out;
    }

}
