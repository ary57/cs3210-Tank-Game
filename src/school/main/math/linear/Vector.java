package school.main.math.linear;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class Vector {
    //values of vectors are stored as higher order functions, which are called each time the vector is evaluated
    //This avoids updating vectors manually
    private LinkedList<Supplier<Double>> values;

    public Vector(LinkedList<Supplier<Double>> values) {
        this.values = values;
    }

    public Vector(double... values) {
        this.values = new LinkedList();
        for (Double val : values) {
            this.values.addLast(() -> val);
        }
    }

    public Vector(Supplier<Double>... values) {
        this.values = new LinkedList(Arrays.asList(values));
    }

    public double getValue(int index) {
        return this.values.get(index).get();
    }

    public int getValueCount() {
        return this.values.size();
    }

    public Vector add(Vector b) {
        return this.transform((Integer index, Double value) -> (value + b.getValue(index)));
    }

    public Vector subtract(Vector b) {
        return this.transform((Integer index, Double value) -> (value - b.getValue(index)));
    }

    public Vector multiply(double b) {
        return this.transform((Integer index, Double value) -> (value * b));
    }

    public Vector multiply(Supplier<Double> b) {
        return this.transform((Integer index, Double value) -> (value * b.get()));
    }

    public Vector multiply(Vector b) {
        return this.transform((Integer index, Double value) -> (value * b.getValue(index)));
    }

    public Vector divide(Vector b) {
        return this.transform((Integer index, Double value) -> (value / b.getValue(index)));
    }

    public Vector divide(double b) {
        return this.transform((Integer index, Double value) -> (value / b));
    }

    public double dotProduct(Vector b) {
        return Vector.dotProduct(this, b);
    }

    public static double dotProduct(Vector A, Vector B) {
        double total = 0;
        for (int i = 0; i < A.getValueCount(); i++) {
            total += A.getValue(i) * B.getValue(i);
        }
        return total;
    }

    public Vector2 as2D() {
        if (this.getValueCount() == 2) {
            if (this instanceof Vector2) {
                return (Vector2) this;
            }
            return new Vector2(() -> this.getValue(0), () -> this.getValue(1));
        }
        throw new RuntimeException("Vector is too long");
    }

    public String toString() {
        String out = null;
        Supplier<Double> val;
        for (int i = 0; i < this.getValueCount(); i++) {
            val = this.values.get(i);
            if (out == null) {
                out = "(" + val.get();
            } else {
                out += ", " + val.get();
            }
        }
        return out.substring(0, out.length() - 2) + ")";
    }

    /**
     * @param func (index, value)->(new Value)
     * @return new vector containing product of func run on each value from this vector
     */
    public Vector transform(BiFunction<Integer, Double, Double> func) {
        LinkedList<Supplier<Double>> out = new LinkedList();

        for (int i = 0; i < this.getValueCount(); i++) {
            //TransformEncapsulator ensures that i will be correctly associated
            out.addLast(new TransformEncapsulator(i, func, this));
        }
        return new Vector(out);
    }

    /**
     * Used to avoid problems with closure capture of index
     */
    private static class TransformEncapsulator implements Supplier<Double> {
        private final int index;
        private final BiFunction<Integer, Double, Double> func;
        private final Vector source;

        public TransformEncapsulator(int index, BiFunction<Integer, Double, Double> func, Vector source) {
            this.index = index;
            this.func = func;
            this.source = source;
        }

        @Override
        public Double get() {
            return this.func.apply(this.index, source.getValue(this.index));
        }
    }


//    public static void main(String[] args) {
//        Vector a = new Vector(1, 2);
//        Vector b = new Vector(2, 2);
//        Vector result = a.add(b);
//        System.out.println(result.toString());
//    }
}
