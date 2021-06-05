package school.main.math.linear;

import java.util.function.Supplier;

/**
 * Subclass of Vector, giving direct access to getX() and getY() without having to use getValue(0) and getValue(1)
 */
public class Vector2 extends Vector {

    public Vector2(Supplier<Double> x, Supplier<Double> y) {
        super(x, y);
    }

    public static Vector2 fromAngle(Supplier<Double> theta) {
        return new Vector2(() -> Math.cos(theta.get()), () -> Math.sin(theta.get()));
    }

    public double getX() {
        return this.getValue(0);
    }

    public double getY() {
        return this.getValue(1);
    }


    public double crossProduct2D(Vector2 B) {
        return this.getX() * B.getY() - this.getY() * B.getX();
    }

    public static double crossProduct2D(Vector2 A, Vector2 B) {
        return A.getX() * B.getY() - A.getY() * B.getX();
    }

}
