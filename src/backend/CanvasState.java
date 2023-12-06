package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CanvasState {

    private final List<Figure> list = new ArrayList<>();

    public void addFigure(Figure figure) {
        list.add(figure);
    }

    public void deleteFigure(Figure figure) {
        list.remove(figure);
    }

    public void rotateFigure(Figure figure) {
        figure.rotateR();
    }

    public void flipHFigure(Figure figure){
        figure.flipHorizontally();
    }

    public void flipVFigure(Figure figure){
        figure.flipVertically();
    }

    public void augmentFigure(Figure figure){
        figure.augment();
    }

    public void reduceFigure(Figure figure){
        figure.reduce();
    }

    public List<Figure> figures() {
        return new ArrayList<>(list);
    }

}
