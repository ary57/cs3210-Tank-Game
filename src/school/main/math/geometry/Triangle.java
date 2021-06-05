package school.main.math.geometry;

import school.main.GameObject;
import school.main.math.linear.Vector;
import school.main.math.linear.Vector2;

public class Triangle {
    private final Vector2 vertexA;
    private final Vector2 vertexB;
    private final Vector2 vertexC;
    private final Line edgeAB;
    private final Line edgeBC;
    private final Line edgeCA;

    private GameObject owner = null;

    public Triangle(Vector2 vertexA, Vector2 vertexB, Vector2 vertexC) {
        this.vertexA = vertexA;
        this.vertexB = vertexB;
        this.vertexC = vertexC;
        this.edgeAB = new Line(vertexA, vertexB);
        this.edgeBC = new Line(vertexB, vertexC);
        this.edgeCA = new Line(vertexC, vertexA);
    }

    public Triangle setOwner(GameObject owner) {
        this.owner = owner;
        return this;
    }

    public Vector2 getVertexA() {
        return vertexA;
    }

    public Vector2 getVertexB() {
        return vertexB;
    }

    public Vector2 getVertexC() {
        return vertexC;
    }

    public Line getEdgeAB() {
        return edgeAB;
    }

    public Line getEdgeBC() {
        return edgeBC;
    }

    public Line getEdgeCA() {
        return edgeCA;
    }

    public Vector2[] getVerts() {
        return new Vector2[]{this.getVertexA(), this.getVertexB(), this.getVertexC()};
    }

    public Line[] getEdges() {
        return new Line[]{getEdgeAB(), getEdgeBC(), getEdgeCA()};
    }

    public boolean containsPoint(Vector2 point) {
        double[] uv = this.barycentricUV(point);
        return (uv[0] >= 0) && (uv[1] >= 0) && (uv[0] + uv[1] < 1);
    }

    public boolean isInsideOf(Triangle other) {
        Vector2[] verts = other.getVerts();
        for (Vector2 v : verts) {
            if (this.containsPoint(v)) {
                return true;
            }
        }
        return false;
    }

    public boolean intersects(Triangle other) {
        if (this.isInsideOf(other) ){
//            System.out.println("this " + this.owner.getId() + " is inside of " + other.owner.getId());
            return true;
        }
        if (other.isInsideOf(this)) {
//            System.out.println("other " + other.owner.getId() + " is inside of " + this.owner.getId());
            return true;
        }
        Line[] edges = this.getEdges();
        for (Line edge : edges) {
            if (edge.doesIntersect(other)) {
                return true;
            }
        }
        return false;
    }

    /**
     * algorithm credit: https://observablehq.com/@kelleyvanevert/2d-point-in-triangle-test
     * @param point position to check
     * @return point position relative to triangle's edges
     */
    public double[] barycentricUV(Vector2 point) {
        // Compute vectors        
        Vector2 b = this.getVertexC().subtract(this.getVertexA()).as2D();
        Vector2 c = this.getVertexB().subtract(this.getVertexA()).as2D();
        Vector2 p = point.subtract(this.getVertexA()).as2D();

        // Compute dot products
        double cc = Vector.dotProduct(c, c);
        double bc = Vector.dotProduct(b, c);
        double pc = Vector.dotProduct(c, p);
        double bb = Vector.dotProduct(b, b);
        double pb = Vector.dotProduct(b, p);

        // Compute barycentric coordinates
        double denom = cc * bb - bc * bc;
        double u = (bb * pc - bc * pb) / denom;
        double v = (cc * pb - bc * pc) / denom;

        return new double[]{u, v};
    }


    public String getOwnerId() {
        if(this.owner == null){
            return null;
        }
        return this.owner.getId();
    }
}
