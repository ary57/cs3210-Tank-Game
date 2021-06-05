package school.main.math.geometry;

import school.main.GameObject;
import school.main.math.linear.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.BiFunction;

public class Line {
    private final Vector2 a;
    private final Vector2 b;

    public Line(Vector2 A, Vector2 B) {
        this.a = A;
        this.b = B;
    }

    public Vector2 getA() {
        return a;
    }

    public Vector2 getB() {
        return b;
    }

    public boolean doesIntersect(Line other) {
        Vector2 p = this.getA();
        Vector2 r = this.getB().subtract(p).as2D();

        Vector2 q = other.getA();
        Vector2 s = other.getB().subtract(q).as2D();
        double t = q.subtract(p).as2D().crossProduct2D(s.divide(r.crossProduct2D(s)).as2D());
        double u = q.subtract(p).as2D().crossProduct2D(r.multiply(1 / r.crossProduct2D(s)).as2D());
        if (r.crossProduct2D(s) == 0) {
            if (q.subtract(p).as2D().crossProduct2D(r) == 0) {
                //lines are colinear
                double T0 = q.subtract(p).dotProduct(r.divide(r.dotProduct(r)));
                double T1 = (T0 + s.dotProduct(r.divide(r.dotProduct(r))));
                if (s.dotProduct(r) < 0) {
                    //pointing in opposite directions
                }
                return false;//todo fix this for colinear intersections
            } else {
                //lines are parallel and non-intersecting
                return false;
            }
        } else {
            return t >= 0 && t <= 1;
        }

    }


    //

    /**
     * Calculates intersection alphas (t & u) between two line segments (A-B) and (C-D).
     * algorithm credit: https://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
     * @param other line to check intersection with
     * @return [t, u]
     */
    public double[] getIntersectionAlphas(Line other) {
        Vector2 p = this.getA();
        Vector2 r = this.getB().subtract(p).as2D();

        Vector2 q = other.getA();
        Vector2 s = other.getB().subtract(q).as2D();
        return new double[]{q.subtract(p).as2D().crossProduct2D(s.divide(r.crossProduct2D(s)).as2D()), q.subtract(p).as2D().crossProduct2D(r.multiply(1 / r.crossProduct2D(s)).as2D())};
    }

    /**
     * Checks if this line intersects any of given triangle's edges,
     * then checks if this line is inside of the triangle
     * @param triangle geometry to test intersection against
     * @return
     */
    public boolean doesIntersect(Triangle triangle) {
        for (Line edge : triangle.getEdges()) {
            double[] intersection = this.getIntersectionAlphas(edge);
            if (!Double.isNaN(intersection[0]) && !Double.isNaN(intersection[0])
                    && intersection[0] > 0 && intersection[0] <= 1
                    && intersection[1] > 0 && intersection[1] <= 1) {
                return true;
            }
        }
        return triangle.containsPoint(this.getA());
    }


    /**
     * @param exitOnFirstCollision If you are only interested in 1 collision, avoid unnecessary checks
     * @param rootObject Will recursively check each child object of rootObject
     * @param ignoreIds List of IDs to ignore. This is used by ShooterTank to avoid getting stuck on it's own cannon
     * @return
     */
    public ArrayList<GameObject> getIntersections(boolean exitOnFirstCollision, GameObject rootObject, String[] ignoreIds) {
        ArrayList<GameObject> out = new ArrayList();
        rootObject.forEachCollisionTriangle((BiFunction<GameObject, Triangle, Boolean>) (gameObject, triangle) -> {
            if (this.doesIntersect(triangle)) {
                for(String ignore : ignoreIds){
                    if(triangle.getOwnerId().equals(ignore)){
                        return false;
                    }
                }
                out.add(gameObject);
                if (exitOnFirstCollision) {
                    return true;
                }
            }
            return false;
        }, true);

        return out;
    }

    /**
     * Helper method for debug graphics
     * @param graphics AWT graphics to draw to
     */
    public void drawLine(Graphics graphics) {
        graphics.drawLine((int) this.a.getX(), (int) this.a.getY(), (int) this.b.getX(), (int) this.b.getY());
    }
}
