// Basic Drawing Canvas — abstract Shape, final ids, compile-time scale() overloads.
// Shape is abstract, so `new Shape()` cannot compile: only a specific shape (CircleShape /
// SquareShape) can ever be created. The static counter + final shapeId live once in Shape's
// own constructor (subclasses reach it through super()). Both scale() overloads are concrete
// on Shape and usable by every subclass unchanged; each subclass decides what its own
// dimension field becomes. printArea() takes a Shape reference and never checks the subclass.

abstract class Shape {
    private static int nextShape = 1001;
    private final String shapeId;

    Shape() {
        this.shapeId = "SHP-" + (nextShape++);
    }

    public abstract double calculateArea();

    // Uniform growth; subclasses apply the single factor once to their own dimension.
    void scale(double factor) {
        applyUniformScale(factor);
    }

    // Uneven growth; subclasses apply each factor to their own dimension field.
    void scale(double xFactor, double yFactor) {
        applyUnevenScale(xFactor, yFactor);
    }

    abstract void applyUniformScale(double factor);

    abstract void applyUnevenScale(double xFactor, double yFactor);

    String getShapeId() {
        return shapeId;
    }
}

class CircleShape extends Shape {
    private double radius;

    public CircleShape(double radius) {
        super();
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    @Override
    void applyUniformScale(double factor) {
        radius *= factor;
    }

    @Override
    void applyUnevenScale(double xFactor, double yFactor) {
        radius *= (xFactor + yFactor) / 2.0;
    }
}

class SquareShape extends Shape {
    private double side;

    public SquareShape(double side) {
        super();
        this.side = side;
    }

    @Override
    public double calculateArea() {
        return side * side;
    }

    @Override
    void applyUniformScale(double factor) {
        side *= factor;
    }

    @Override
    void applyUnevenScale(double xFactor, double yFactor) {
        side *= xFactor;
        side *= yFactor;
    }
}

public class DrawingCanvas {
    static void printArea(Shape s) {
        System.out.println(s.calculateArea());
    }

    public static void main(String[] args) {
        CircleShape c = new CircleShape(5.0);
        System.out.println(c.calculateArea());

        SquareShape sq = new SquareShape(4.0);
        System.out.println(sq.calculateArea());

        sq.scale(2.0); // uses the one-argument overload
        System.out.println(sq.calculateArea());

        printArea(c);

        System.out.println(c.getShapeId() + " " + sq.getShapeId());
    }
}
