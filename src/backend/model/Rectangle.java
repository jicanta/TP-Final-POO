package backend.model;

public class Rectangle implements Figure {

    private Point topLeft, bottomRight;

    public Rectangle(Point topLeft, Point bottomRight) {
        setNewPoints(topLeft, bottomRight);
    }

    public double getHeight() {
        return this.bottomRight.getY() - this.topLeft.getY();
    }

    public double getWidth() {
        return this.bottomRight.getX() - this.topLeft.getX();
    }

    public Point getTopLeft() {
        return this.topLeft;
    }

    public Point getBottomRight() {
        return this.bottomRight;
    }

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", this.topLeft, this.bottomRight);
    }

    @Override
    public void moveFigure(Double diffX, Double diffY){
        setNewPoints(new Point(this.topLeft.getX() + diffX, this.topLeft.getY() + diffY), new Point(this.bottomRight.getX() + diffX, this.bottomRight.getY() + diffY));
    }

    @Override
    public Double[] getDrawParameters(){
        return new Double[]{ this.topLeft.getX(), this.topLeft.getY(), this.getWidth(), this.getHeight() };
    }

    @Override
    public void rotateR() {
        double topLeftX = this.topLeft.getX();
        double topLeftY = this.topLeft.getY();
        double botRightX = this.bottomRight.getX();
        double botRightY = this.bottomRight.getY();
        double width = this.getWidth();
        double height = this.getHeight();

        double newTopX = topLeftX + (width - height) / 2.0;
        double newTopY = topLeftY - (width - height) / 2.0;
        double newBotX = botRightX - (width - height) / 2.0;
        double newBotY = botRightY + (width - height) / 2.0;

        setNewPoints(new Point(newTopX, newTopY), new Point(newBotX, newBotY));
    }

    public void flipHorizontally(){
        Point newTopLeft = new Point(this.topLeft.getX() + this.getWidth(), this.topLeft.getY());
        Point newBottomRight = new Point(this.bottomRight.getX() + this.getWidth(), this.bottomRight.getY());
        setNewPoints(newTopLeft, newBottomRight);
    }

    public void flipVertically(){
        Point newTopLeft = new Point(this.topLeft.getX(),this.topLeft.getY() + this.getHeight());
        Point newBottomRight = new Point(this.bottomRight.getX(), this.bottomRight.getY() + this.getHeight());
        setNewPoints(newTopLeft, newBottomRight);
    }

    public void augment(){
        Point newTopLeft = new Point(this.topLeft.getX() - this.getWidth() * 0.125,this.topLeft.getY() - this.getHeight() * 0.125);
        Point newBottomRight = new Point(this.bottomRight.getX() + this.getWidth() * 0.125, this.bottomRight.getY() + this.getHeight() * 0.125);
        setNewPoints(newTopLeft, newBottomRight);
    }

    public void reduce(){
        Point newTopLeft = new Point(this.topLeft.getX() + this.getWidth() * 0.125,this.topLeft.getY() + this.getHeight() * 0.125);
        Point newBottomRight = new Point(this.bottomRight.getX() - this.getWidth() * 0.125, this.bottomRight.getY() - this.getHeight() * 0.125);
        setNewPoints(newTopLeft, newBottomRight);
    }

    private void setNewPoints(Point newTopLeft, Point newBottomRight){
        this.topLeft = newTopLeft;
        this.bottomRight = newBottomRight;
    }

    @Override
    public boolean figureBelongs(Point point) {
        return point.getX() > this.topLeft.getX() && point.getX() < this.bottomRight.getX() &&
                point.getY() > this.topLeft.getY() && point.getY() < this.bottomRight.getY();
    }

    @Override
    public boolean isInside(Rectangle rectangle) {
        return rectangle.figureBelongs(this.topLeft) && rectangle.figureBelongs(this.bottomRight);
    }
}
