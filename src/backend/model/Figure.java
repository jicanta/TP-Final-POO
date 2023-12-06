package backend.model;


import javafx.scene.canvas.GraphicsContext;

public interface Figure {
    void moveFigure(Double diffX, Double diffY);
    Double[] getDrawParameters();
    void rotateR();
    void flipHorizontally();
    void flipVertically();
    void augment();
    void reduce();
    boolean figureBelongs(Point point); // no deberia ser public supongo, fijarme eso
    boolean isInside(Rectangle rectangle); // no deberia ser public supongo, fijarme eso
}
