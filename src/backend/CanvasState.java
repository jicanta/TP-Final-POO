package backend;

import backend.model.Figure;

import java.util.ArrayList;
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

    public Iterable<Figure> figures() {
        return new ArrayList<>(list);
    }

}
