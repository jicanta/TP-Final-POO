package backend.model;


public interface Figure {
    void rotateR();
    void flipHorizontally();
    void flipVertically();
    void augment();
    void reduce();
    boolean figureBelongs(Point point);

    boolean isInside(Rectangle rectangle);
}
