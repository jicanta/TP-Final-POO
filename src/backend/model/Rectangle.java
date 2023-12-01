package backend.model;

public class Rectangle implements Figure {

    private Point topLeft, bottomRight;

    public Rectangle(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public double getHeight() {
        return bottomRight.getY() - topLeft.getY();
    }

    public double getWidth() {
        return bottomRight.getX() - topLeft.getX();
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }

    @Override
    public void rotateR() {
        double topLeftX = topLeft.getX();
        double topLeftY = topLeft.getY();
        double botRightX = bottomRight.getX();
        double botRightY = bottomRight.getY();
        double width = getWidth();
        double height = getHeight();

        double newTopX = topLeftX + (width - height) / 2.0;
        double newTopY = topLeftY - (width - height) / 2.0;
        double newBotX = botRightX - (width - height) / 2.0;
        double newBotY = botRightY + (width - height) / 2.0;

        Point newTopLeft = new Point(newTopX, newTopY);
        Point newBotRight = new Point(newBotX, newBotY);

        topLeft = newTopLeft;
        bottomRight = newBotRight;
    }

    public void flipHorizontally(){
        Point newTopLeft = new Point(topLeft.getX()+this.getWidth(),topLeft.getY());
        Point newBottomRight = new Point(bottomRight.getX()+this.getWidth(), bottomRight.getY());
        setNewPoints(newTopLeft, newBottomRight);
    }

    public void flipVertically(){
        Point newTopLeft = new Point(topLeft.getX(),topLeft.getY()+this.getHeight());
        Point newBottomRight = new Point(bottomRight.getX(), bottomRight.getY()+this.getHeight());
        setNewPoints(newTopLeft, newBottomRight);
    }

    public void augment(){
        Point newTopLeft = new Point(topLeft.getX() - this.getWidth()*0.125,topLeft.getY() - this.getHeight()*0.125);
        Point newBottomRight = new Point(bottomRight.getX() + this.getWidth()*0.125, bottomRight.getY() + this.getHeight()*0.125);
        setNewPoints(newTopLeft, newBottomRight);
    }

    public void reduce(){
        Point newTopLeft = new Point(topLeft.getX() + this.getWidth()*0.125,topLeft.getY() + this.getHeight()*0.125);
        Point newBottomRight = new Point(bottomRight.getX() - this.getWidth()*0.125, bottomRight.getY() - this.getHeight()*0.125);
        setNewPoints(newTopLeft, newBottomRight);
    }

    private void setNewPoints(Point newTopLeft, Point newBottomRight){
        this.topLeft = newTopLeft;
        this.bottomRight = newBottomRight;
    }

    @Override
    public boolean figureBelongs(Point point) {
        return point.getX() > topLeft.getX() && point.getX() < bottomRight.getX() &&
                point.getY() > topLeft.getY() && point.getY() < bottomRight.getY();
    }

    @Override
    public boolean isInside(Rectangle rectangle) {
        return rectangle.figureBelongs(topLeft) && rectangle.figureBelongs(bottomRight);
    }
}
