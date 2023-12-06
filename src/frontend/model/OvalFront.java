package frontend.model;

import backend.model.Figure;
import javafx.scene.canvas.GraphicsContext;

public abstract class OvalFront implements FigureFront {
    private final Figure backFigure;

    public OvalFront(Figure backFigure){
        this.backFigure = backFigure;
    }
    @Override
    public void drawFigure(GraphicsContext gc){
        Double[] parameters = backFigure.getDrawParameters();
        gc.fillOval(parameters[0], parameters[1], parameters[2], parameters[3]);
        gc.strokeOval(parameters[0], parameters[1], parameters[2], parameters[3]);
    }
}
