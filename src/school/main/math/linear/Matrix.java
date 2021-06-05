package school.main.math.linear;

import java.util.function.Supplier;

public class Matrix {
    private boolean isColumns = false;
    private Vector[] vectors;

    private Matrix(boolean isColumns, Vector... vectors) {
        this.vectors = vectors;
        this.isColumns = isColumns;
    }

    public static Matrix FromColumns(Vector... columnVectors) {
        return new Matrix(true, columnVectors);
    }

    public static Matrix FromRows(Vector... rowVectors) {
        return new Matrix(false, rowVectors);
    }

    public static Matrix Scale2DMatrix(Supplier<Double> xScale, Supplier<Double> yScale) {
        return Matrix.FromColumns(new Vector(xScale, () -> 0d), new Vector(() -> 0d, yScale));
    }

    public static Matrix RotationMatrix(Supplier<Double> angle) {
        return Matrix.FromColumns(
                new Vector(() -> (Math.cos(angle.get())), () -> (Math.sin(angle.get()))),
                new Vector(() -> (0 - Math.sin(angle.get())), () -> (Math.cos(angle.get()))));
    }

    public double get(int column, int row) {
        this.checkRange(column, row);
        if (this.isColumns) {
            return vectors[column].getValue(row);
        } else {
            return vectors[row].getValue(column);
        }
    }

    public Vector getColumnAsVector(int column) {
        if (column < 0 || column >= this.getColumnCount()) {
            throw new RuntimeException("Cannot get column " + column + " out of bounds in " + this.toString());
        }
        Supplier<Double> entries[] = new Supplier[this.getRowCount()];
        for (int i = 0; i < this.getRowCount(); i++) {
            int finalI = i;
            entries[i] = () -> (get(column, finalI));
        }
        return new Vector(entries);
    }

    public Vector getRowAsVector(int row) {
        if (row < 0 || row >= this.getRowCount()) {
            throw new RuntimeException("Cannot get row " + row + " out of bounds in " + this.toString());
        }
        Supplier<Double> entries[] = new Supplier[this.getColumnCount()];
        for (int i = 0; i < this.getColumnCount(); i++) {
            int finalI = i;
            entries[i] = () -> (get(finalI, row));
        }
        return new Vector(entries);
    }

    public int getRowCount() {
        if (this.isColumns) {
            return this.vectors[0].getValueCount();
        } else {
            return this.vectors.length;
        }
    }

    public int getColumnCount() {
        if (!this.isColumns) {
            return this.vectors[0].getValueCount();
        } else {
            return this.vectors.length;
        }
    }

    protected void checkRange(int column, int row) {
        if (row < 0) {
            throw new RuntimeException("Invalid matrix index [" + column + ", " + row + "], row < 0");
        }
        if (column < 0) {
            throw new RuntimeException("Invalid matrix index [" + column + ", " + row + "], column < 0");
        }
        if (row >= this.getRowCount()) {
            throw new RuntimeException("Invalid matrix index [" + column + ", " + row + "], row > " + (this.getRowCount() - 1));//(this.getRowCount()-1) because it's an array index
        }
        if (column >= this.getColumnCount()) {
            throw new RuntimeException("Invalid matrix index [" + column + ", " + row + "], column > " + (this.getColumnCount() - 1));//(this.getRowCount()-1) because it's an array index
        }
    }

    public Vector multiplyColumnVector(Vector v) {
        if (v.getValueCount() != this.getColumnCount()) {
            throw new RuntimeException("Cannot multiply vector " + v.toString() + " by matrix " + this.toString());
        }
        Supplier<Double>[] out = new Supplier[this.getRowCount()];
        for (int row = 0; row < this.getRowCount(); row++) {
            final int finalRow = row;
            out[finalRow] = () -> {
                double total = 0;
                for (int col = 0; col < this.getColumnCount(); col++) {
                    total += this.get(col, finalRow) * v.getValue(col);
                }
                return total;
            };

        }
        return new Vector(out);
    }

    public Matrix multiply(Matrix other) {
        if (other.getRowCount() != this.getColumnCount()) {
            throw new RuntimeException("Cannot multiply " + this.toString() + " by " + other.toString());
        }
        double total;
        Vector[] out = new Vector[this.getRowCount()];
        Supplier<Double>[] entries;
        for (int otherColumn = 0; otherColumn < other.getColumnCount(); otherColumn++) {
            entries = new Supplier[other.getColumnCount()];
            Vector currentColumn = other.getColumnAsVector(otherColumn);
            for (int thisRow = 0; thisRow < this.getRowCount(); thisRow++) {
                Vector currentRow = this.getRowAsVector(thisRow);
                entries[thisRow] = () -> (
                        currentColumn.dotProduct(currentRow)
                );
            }
            out[otherColumn] = new Vector(entries);
        }
        return Matrix.FromColumns(out);
    }

    public String toString() {
        return "[" + this.getColumnCount() + "x" + this.getRowCount() + "]";
    }

}
