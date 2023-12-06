package frontend.model;

import backend.model.Figure;
import javafx.scene.canvas.GraphicsContext;

public abstract class QuadrilateralFront implements FigureFront {
    private final Figure backFigure;

    public QuadrilateralFront(Figure backFigure){
        this.backFigure = backFigure;
    }
    @Override
    public void drawFigure(GraphicsContext gc){
        Double[] parameters = backFigure.getDrawParameters();
        gc.fillRect(parameters[0], parameters[1], parameters[2], parameters[3]);
        gc.strokeRect(parameters[0], parameters[1], parameters[2], parameters[3]);
    }
}
