package frontend.model;

import backend.model.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

public abstract class QuadrilateralFront extends FigureFront {
    private final Rectangle backQuadrilateral;
    public QuadrilateralFront(Rectangle backRectangle, Color color, boolean shadow, boolean bevel, boolean gradient){
        super(color, shadow, bevel, gradient);
        this.backQuadrilateral = backRectangle;
    }
    @Override
    public void drawFigure(GraphicsContext gc){
        applyEffects(gc);
        Double[] parameters = this.backQuadrilateral.getDrawParameters();
        gc.fillRect(parameters[0], parameters[1], parameters[2], parameters[3]);
        gc.strokeRect(parameters[0], parameters[1], parameters[2], parameters[3]);
    }

    private void applyEffects(GraphicsContext gc){
        if (this.hasShadow()){
            gc.setFill(Color.GRAY);
            gc.fillRect(this.backQuadrilateral.getTopLeft().getX() + 10.0, this.backQuadrilateral.getTopLeft().getY() + 10.0,
                    Math.abs(this.backQuadrilateral.getTopLeft().getX() - this.backQuadrilateral.getBottomRight().getX()),
                    Math.abs(this.backQuadrilateral.getTopLeft().getY() - this.backQuadrilateral.getBottomRight().getY()));
        }
        // TODO: unico problema que queda, es que cuando selecciono una figura que tiene biselado no se ve el borde
        // rojo porque queda tapado abajo del biselado, ver como solucionar eso. dijo franco que el borde rojo puede
        // estar entre la figura y el biselado o afuera del biselado, ver cual es mas facil de hacer y hacer eso
        if (this.hasBevel()){
            double x = backQuadrilateral.getTopLeft().getX();
            double y = backQuadrilateral.getTopLeft().getY();
            double width = Math.abs(x - backQuadrilateral.getBottomRight().getX());
            double height = Math.abs(y - backQuadrilateral.getBottomRight().getY());
            gc.setLineWidth(10);
            gc.setStroke(Color.LIGHTGRAY);
            gc.strokeLine(x, y, x + width, y);
            gc.strokeLine(x, y, x, y + height);
            gc.setStroke(Color.BLACK);
            gc.strokeLine(x + width, y, x + width, y + height);
            gc.strokeLine(x + width, y + height, x, y + height);
            gc.setLineWidth(1);
        }
        if (this.hasGradient()){
            LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, this.getColor()),
                    new Stop(1, (this.getColor()).invert()));
            gc.setFill(linearGradient);
        }
        else {
            gc.setFill(this.getColor());
        }
    }

}
