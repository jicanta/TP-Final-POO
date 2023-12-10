package backend.model;

public interface Figure {
    void moveFigure(Double diffX, Double diffY);
    Double[] getDrawParameters();
    void rotateR();
    void flipHorizontally();
    void flipVertically();
    void augment();
    void reduce();
    boolean figureBelongs(Point point);
    boolean isInside(Rectangle rectangle);
}
