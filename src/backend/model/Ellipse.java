package backend.model;

public class Ellipse implements Figure {

    private Point centerPoint;
    private double sMayorAxis, sMinorAxis;

    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis) {
        if(sMayorAxis <= 0 || sMinorAxis <= 0){
            throw new FigureException();
        }
        setCenterPoint(centerPoint.getX(), centerPoint.getY());
        this.sMayorAxis = sMayorAxis;
        this.sMinorAxis = sMinorAxis;
    }

    public Point getCenterPoint() {
        return this.centerPoint;
    }

    private void setCenterPoint(double x, double y){
        this.centerPoint = new Point(x, y);
    }

    public double getsMayorAxis() {
        return this.sMayorAxis;
    }

    public double getsMinorAxis() {
        return this.sMinorAxis;
    }

    public double getRadius() {
        return Math.max(this.sMayorAxis / 2, this.sMinorAxis / 2);
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]", this.centerPoint, this.sMayorAxis, this.sMinorAxis);
    }

    @Override
    public void moveFigure(Double diffX, Double diffY){
        setCenterPoint(this.centerPoint.getX() + diffX, this.centerPoint.getY() + diffY);
    }

    @Override
    public Double[] getDrawParameters(){
       return new Double[]{ this.centerPoint.getX() - (this.sMayorAxis / 2), this.centerPoint.getY() - (this.sMinorAxis / 2), this.sMayorAxis, this.sMinorAxis };
    }

    @Override
    public void rotateR() {
        double copyMayorAxis = this.sMayorAxis;
        this.sMayorAxis = this.sMinorAxis;
        this.sMinorAxis = copyMayorAxis;
    }

    public void flipHorizontally(){
        setCenterPoint(this.centerPoint.getX() + this.getsMayorAxis(), this.centerPoint.getY());
    }

    public void flipVertically(){
        setCenterPoint(this.centerPoint.getX(), this.centerPoint.getY() + this.getsMinorAxis());
    }

    public void augment(){
        this.sMayorAxis = this.getsMayorAxis() * 1.25;
        this.sMinorAxis = this.getsMinorAxis() * 1.25;
    }

    public void reduce() {
        this.sMayorAxis = this.getsMayorAxis() * 0.75;
        this.sMinorAxis = this.getsMinorAxis() * 0.75;
    }

    @Override
    public boolean figureBelongs(Point point) {
        return ((Math.pow(point.getX() - this.centerPoint.getX(), 2) / Math.pow(this.sMayorAxis, 2)) +
                (Math.pow(point.getY() - this.centerPoint.getY(), 2) / Math.pow(this.sMinorAxis, 2))) <= 0.30;
    }

    @Override
    public boolean isInside(Rectangle rectangle) {
        return rectangle.figureBelongs(new Point(this.centerPoint.getX() + this.sMayorAxis / 2, this.centerPoint.getY())) &&
                rectangle.figureBelongs(new Point(this.centerPoint.getX() - this.sMayorAxis / 2, this.centerPoint.getY())) &&
                rectangle.figureBelongs(new Point(this.centerPoint.getX() , this.centerPoint.getY() + this.sMinorAxis / 2.0)) &&
                rectangle.figureBelongs(new Point(this.centerPoint.getX() , this.centerPoint.getY() - this.sMinorAxis / 2.0));
    }
}
