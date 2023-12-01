package backend.model;

public class Square extends Rectangle {

    public Square(Point topLeft, double size) {
        super(topLeft, new Point(topLeft.x + size, topLeft.y + size));
    }

    @Override
    public String toString() {
        return String.format("Cuadrado [ %s , %s ]", getTopLeft(), getBottomRight());
    }

    public void flipHorizontally(){
        super.flipHorizontally();
    }

    public void flipVertically(){
        super.flipVertically();
    }

    public void augment(){
        super.augment();
    }

    public void reduce(){
        super.reduce();
    }

}
