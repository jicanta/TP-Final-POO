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
        deleteFigure(figure);
        figure.rotateR();
        addFigure(figure);
    }

    public void flipHFigure(Figure figure){
        deleteFigure(figure);
        figure.flipHorizontally();
        addFigure(figure);
    }

    public void flipVFigure(Figure figure){
        deleteFigure(figure);
        figure.flipVertically();
        addFigure(figure);
    }

    public void augmentFigure(Figure figure){
        deleteFigure(figure);
        figure.augment();
        addFigure(figure);
    }

    public void reduceFigure(Figure figure){
        deleteFigure(figure);
        figure.reduce();
        addFigure(figure);
    }

    public List<Figure> figures() {
        return new ArrayList<>(list);
    }

}
