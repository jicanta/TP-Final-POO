package backend.model;

public class Ellipse implements Figure {

    protected Point centerPoint;
    protected double sMayorAxis, sMinorAxis;

    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis) {
        this.centerPoint = centerPoint;
        this.sMayorAxis = sMayorAxis;
        this.sMinorAxis = sMinorAxis;
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]", centerPoint, sMayorAxis, sMinorAxis);
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public double getsMayorAxis() {
        return sMayorAxis;
    }

    public double getsMinorAxis() {
        return sMinorAxis;
    }

    @Override
    public void rotateR() {
        double copyMayorAxis = sMayorAxis;
        sMayorAxis = sMinorAxis;
        sMinorAxis = copyMayorAxis;
    }

    public void flipHorizontally(){
        Point newCenterPoint = new Point(centerPoint.getX()+this.getsMayorAxis(), centerPoint.getY());
        centerPoint = newCenterPoint;
    }

    public void flipVertically(){
        Point newCenterPoint = new Point(centerPoint.getX(), centerPoint.getY()+this.getsMinorAxis());
        centerPoint = newCenterPoint;
    }

    public void augment(){
        this.sMayorAxis = this.getsMayorAxis()*1.25;
        this.sMinorAxis = this.getsMinorAxis()*1.25;
    }

    public void reduce() {
        this.sMayorAxis = this.getsMayorAxis()*0.75;
        this.sMinorAxis = this.getsMinorAxis()*0.75;
    }

    @Override
    public boolean figureBelongs(Point point) {
        return ((Math.pow(point.getX() - centerPoint.getX(), 2) / Math.pow(sMayorAxis, 2)) +
                (Math.pow(point.getY() - centerPoint.getY(), 2) / Math.pow(sMinorAxis, 2))) <= 0.30;
    }

    @Override
    public boolean isInside(Rectangle rectangle) {
        return rectangle.figureBelongs(centerPoint);
    }
}
