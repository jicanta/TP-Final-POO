package frontend.model;

import backend.model.Ellipse;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;

public abstract class OvalFront extends FigureFront {
    private final Ellipse backOval;
    public OvalFront(Ellipse backOval, Color color) {
        super(color);
        this.backOval = backOval;
    }
    @Override
    public void drawFigure(GraphicsContext gc){
        applyEffects(gc);
        Double[] parameters = this.backOval.getDrawParameters();
        gc.fillOval(parameters[0], parameters[1], parameters[2], parameters[3]);
        gc.strokeOval(parameters[0], parameters[1], parameters[2], parameters[3]);
    }

    private void applyEffects(GraphicsContext gc){
        if (this.hasShadow()){
            gc.setFill(Color.GRAY);
            gc.fillOval(backOval.getCenterPoint().getX() - backOval.getsMayorAxis()/2 + 10.0,
                    backOval.getCenterPoint().getY() - backOval.getsMinorAxis()/2 + 10.0, backOval.getsMayorAxis(),
                    backOval.getsMinorAxis());
        }
        // TODO: unico problema que queda, es que cuando selecciono una figura que tiene biselado no se ve el borde
        // rojo porque queda tapado abajo del biselado, ver como solucionar eso. dijo franco que el borde rojo puede
        // estar entre la figura y el biselado o afuera del biselado, ver cual es mas facil de hacer y hacer eso
        if (this.hasBevel()){
            double arcX = backOval.getCenterPoint().getX() - backOval.getRadius();
            double arcY = backOval.getCenterPoint().getY() - backOval.getRadius();
            gc.setLineWidth(10);
            gc.setStroke(Color.LIGHTGRAY);
            gc.strokeArc(arcX, arcY, backOval.getsMayorAxis(), backOval.getsMinorAxis(), 45, 180, ArcType.OPEN);
            gc.setStroke(Color.BLACK);
            gc.strokeArc(arcX, arcY, backOval.getsMayorAxis(), backOval.getsMinorAxis(), 225, 180, ArcType.OPEN);
            gc.setLineWidth(1);
        }
        if (this.hasGradient()){
            RadialGradient radialGradient = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0.0, this.getColor()),
                    new Stop(1.0, this.getColor().invert()));
            gc.setFill(radialGradient);
        }
        else {
            gc.setFill(this.getColor());
        }
    }


}
