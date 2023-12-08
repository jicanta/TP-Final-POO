package frontend.model;

import backend.model.Ellipse;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;

public abstract class OvalFront extends FigureFront {
    private final Ellipse backOval;
    public OvalFront(Ellipse backOval, Color color, boolean shadow, boolean bevel, boolean gradient) {
        super(color, shadow, bevel, gradient);
        this.backOval = backOval;
    }
    @Override
    public void drawFigure(GraphicsContext gc){
        Double[] parameters = this.backOval.getDrawParameters();
        applyEffects(gc, parameters);
        gc.fillOval(parameters[0], parameters[1], parameters[2], parameters[3]);
        gc.strokeOval(parameters[0], parameters[1], parameters[2], parameters[3]);
    }

    @Override
    public void drawShadow(GraphicsContext gc, Double[] parameters){
        gc.setFill(Color.GRAY);
        gc.fillOval(parameters[0] + 10.0, parameters[1] + 10.0, parameters[2], parameters[3]);
    }

    @Override
    public void drawBevel(GraphicsContext gc, Double[] parameters){
        gc.setLineWidth(10);
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeArc(parameters[0] + 1.0, parameters[1] + 1.0, parameters[2], parameters[3], 45, 180, ArcType.OPEN);
        gc.setStroke(Color.BLACK);
        gc.strokeArc(parameters[0] + 1.0, parameters[1] + 1.0, parameters[2], parameters[3], 225, 180, ArcType.OPEN);
        gc.setLineWidth(1);
    }

    @Override
    public void drawGradient(GraphicsContext gc){
        RadialGradient radialGradient = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
                CycleMethod.NO_CYCLE,
                new Stop(0.0, this.getColor()),
                new Stop(1.0, this.getColor().invert()));
        gc.setFill(radialGradient);
    }
}
