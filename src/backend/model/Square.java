package backend.model;

public class Square implements Figure {

    private final Point topLeft, bottomRight;

    public Square(Point topLeft, double size) {
        this.topLeft = topLeft;
        this.bottomRight = new Point(topLeft.x + size, topLeft.y + size);
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public String toString() {
        return String.format("Cuadrado [ %s , %s ]", topLeft, bottomRight);
    }

}
