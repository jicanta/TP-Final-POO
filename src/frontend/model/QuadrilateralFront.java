package frontend.model;

import backend.model.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

public abstract class QuadrilateralFront extends FigureFront {
    private final Rectangle backQuadrilateral;

    public QuadrilateralFront(Rectangle backRectangle, Color color, boolean shadow, boolean bevel, boolean gradient) {
        super(color, shadow, bevel, gradient);
        this.backQuadrilateral = backRectangle;
    }

    @Override
    public void drawFigure(GraphicsContext gc) {
        Double[] parameters = this.backQuadrilateral.getDrawParameters();
        applyEffects(gc, parameters);
        gc.fillRect(parameters[0], parameters[1], parameters[2], parameters[3]);
        gc.strokeRect(parameters[0], parameters[1], parameters[2], parameters[3]);
    }

    private void applyEffects(GraphicsContext gc, Double[] parameters) {
        if (this.hasShadow()) {
            drawShadow(gc, parameters);
        }
        if (this.hasBevel()) {
            drawBevel(gc, parameters);
        }
        if (this.hasGradient()) {
            drawGradient(gc);
        } else {
            gc.setFill(this.getColor());
        }
    }

    private void drawShadow(GraphicsContext gc, Double[] parameters) {
        gc.setFill(Color.GRAY);
        gc.fillRect(parameters[0] + 10.0, parameters[1] + 10.0, parameters[2], parameters[3]);
    }

    private void drawBevel(GraphicsContext gc, Double[] parameters) {
        gc.setLineWidth(10);
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeLine(parameters[0], parameters[1], parameters[0] + parameters[2], parameters[1]);
        gc.strokeLine(parameters[0], parameters[1], parameters[0], parameters[1] + parameters[3]);
        gc.setStroke(Color.BLACK);
        gc.strokeLine(parameters[0] + parameters[2], parameters[1], parameters[0] + parameters[2], parameters[1] + parameters[3]);
        gc.strokeLine(parameters[0] + parameters[2], parameters[1] + parameters[3], parameters[0], parameters[1] + parameters[3]);
        gc.setLineWidth(1);
    }

    private void drawGradient(GraphicsContext gc) {
        LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, this.getColor()),
                new Stop(1, (this.getColor()).invert()));
        gc.setFill(linearGradient);
    }
}
