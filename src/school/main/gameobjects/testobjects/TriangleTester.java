package school.main.gameobjects.testobjects;

import school.main.GameObject;
import school.main.gameobjects.GameMap;
import school.main.math.geometry.Line;
import school.main.math.linear.Vector2;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * This class is not used in the game, but was used to debug collision math. Please ignore this file
 */
public class TriangleTester extends GameObject<GameMap> implements MouseMotionListener, MouseWheelListener {
    protected Line testLine;
    protected Vector2 lineEnd;
    protected Vector2 mouseLocation;
    protected double mouseX = 0, mouseY = 0;
    private Color color = Color.GREEN;
    protected Double testAngle = 0d;

    public TriangleTester(String id, GameMap parent) {
        super(id, parent);
        this.mouseLocation = new Vector2(() -> this.mouseX, () -> this.mouseY);
        this.lineEnd = mouseLocation.add(Vector2.fromAngle(() -> this.testAngle).multiply(50)).as2D();
        this.testLine = new Line(mouseLocation, lineEnd);
        setSize(20, 20);
    }

    public static void PlaceTester(GameMap map, int x, int y, int size, Color color) {
        TriangleTester b = new TriangleTester("Tester-" + System.currentTimeMillis(), map);
        b.setLocation(x, y);
        b.color = color;
        b.setSize(size, size);
        map.addChildObject(b);
        map.game.addMouseMotionListener(b);
        map.game.addMouseWheelListener(b);
    }

    private Font font = new Font("Ink Free", Font.PLAIN, 18);

    @Override
    public void onDraw(Graphics graphics, double deltaMs) {
        // double[] uv = this.collision[1].barycentricUV(this.mouseLocation);
        graphics.setColor(this.testLine.doesIntersect(this.collision[0]) ? Color.BLUE : Color.orange);
        graphics.fillRect(0, 0, (int) getWidth(), (int) getHeight());
        // graphics.drawString(uv[0] + ", " + uv[1], 0, 0);

        double[] uv = this.collision[0].barycentricUV(this.mouseLocation);

//        graphics.setFont(this.font);
//        double[] debug = testLine.getIntersection(this.collision[0].getEdgeAB());
//        graphics.drawString(debug[0] + ", " + debug[1], 0, 0);
//        debug = testLine.getIntersection(this.collision[0].getEdgeBC());
//        graphics.drawString(debug[0] + ", " + debug[1], 0, -this.font.getSize());
//        debug = testLine.getIntersection(this.collision[0].getEdgeCA());
//        graphics.drawString(debug[0] + ", " + debug[1], 0, -this.font.getSize()*2);
//    }


        graphics.drawString(testLine.getIntersectionAlphas(this.collision[0].getEdgeAB()) + ", " + testLine.getIntersectionAlphas(this.collision[0].getEdgeBC()) + ", " + testLine.getIntersectionAlphas(this.collision[0].getEdgeCA()), 0, 0);

        ;

    }

    //
    public void onPostDrawCollisions(Graphics2D graphics, double deltaMs) {
        graphics.setColor(Color.MAGENTA);
        this.testLine.drawLine(graphics);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.testAngle += (Math.PI / 16) * (e.getScrollAmount() / 3d) * e.getWheelRotation();
    }
}
