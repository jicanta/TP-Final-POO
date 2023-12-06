package backend.model;

import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

public class Ellipse implements Figure {

    protected Point centerPoint;
    protected double sMayorAxis, sMinorAxis;

    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis) {
        setCenterPoint(centerPoint.getX(), centerPoint.getY());
        this.sMayorAxis = sMayorAxis;
        this.sMinorAxis = sMinorAxis;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    private void setCenterPoint(double x, double y){
        this.centerPoint = new Point(x, y);
    }

    public double getsMayorAxis() {
        return sMayorAxis;
    }

    public double getsMinorAxis() {
        return sMinorAxis;
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]", centerPoint, sMayorAxis, sMinorAxis);
    }

    @Override
    public void moveFigure(Double diffX, Double diffY){
        setCenterPoint(centerPoint.getX()+diffX, centerPoint.getY()+diffY);
    }

    @Override
    public Double[] getDrawParameters(){
       return new Double[]{this.centerPoint.getX() - (this.sMayorAxis/2), this.centerPoint.getY() - (this.sMinorAxis/2), sMayorAxis, sMinorAxis};
    }

    @Override
    public void rotateR() {
        double copyMayorAxis = sMayorAxis;
        sMayorAxis = sMinorAxis;
        sMinorAxis = copyMayorAxis;
    }

    public void flipHorizontally(){
        setCenterPoint(centerPoint.getX()+this.getsMayorAxis(), centerPoint.getY());
    }

    public void flipVertically(){
        setCenterPoint(centerPoint.getX(), centerPoint.getY()+this.getsMinorAxis());
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
        return rectangle.figureBelongs(new Point(centerPoint.getX() + sMayorAxis / 2, centerPoint.getY())) &&
                rectangle.figureBelongs(new Point(centerPoint.getX() - sMayorAxis / 2, centerPoint.getY())) &&
                rectangle.figureBelongs(new Point(centerPoint.getX() , centerPoint.getY() + sMinorAxis / 2)) &&
                rectangle.figureBelongs(new Point(centerPoint.getX() , centerPoint.getY() - sMinorAxis / 2));
    }
}
