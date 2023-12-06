package frontend.model;

import backend.model.Figure;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public abstract class FigureFront {
    private Color color;
    private boolean shadow;
    private boolean bevel;
    private boolean gradient;

    public FigureFront(Color color, boolean shadow, boolean bevel, boolean gradient) {
        this.color = color;
        this.shadow = shadow;
        this.bevel = bevel;
        this.gradient = gradient;
    }

    protected Color getColor(){    // TODO: nose si deberia ser public o protected pero bueno
        return this.color;
    }

    public boolean hasShadow(){
        return shadow;
    }

    public boolean hasBevel(){
        return bevel;
    }

    public boolean hasGradient(){
        return gradient;
    }

    public void updateEffects(boolean shadow, boolean bevel, boolean gradient) {
        this.shadow = shadow;
        this.bevel = bevel;
        this.gradient = gradient;
    }
    public abstract void drawFigure(GraphicsContext gc);
}
