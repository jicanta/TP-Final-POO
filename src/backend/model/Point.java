package backend.model;

import java.util.Objects;

public class Point {

    private final double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return String.format("{%.2f , %.2f}", this.x, this.y);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Point p && Double.compare(p.x, this.x) == 0 && Double.compare(p.y, this.y) == 0);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.x, this.y);
    }
}
