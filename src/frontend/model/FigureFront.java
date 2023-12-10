package frontend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class FigureFront {

    protected static final int BEVEL_LINE_WIDTH = 10;
    protected static final int DEFAULT_LINE_WIDTH = 1;
    private final Color color;
    private boolean shadow;
    private boolean bevel;
    private boolean gradient;

    public FigureFront(Color color, boolean shadow, boolean bevel, boolean gradient) {
        this.color = color;
        this.shadow = shadow;
        this.bevel = bevel;
        this.gradient = gradient;
    }

    public abstract void drawShadow(GraphicsContext gc, Double[] parameters);

    public abstract void drawBevel(GraphicsContext gc, Double[] parameters);

    public abstract void drawGradient(GraphicsContext gc);

    public void applyEffects(GraphicsContext gc, Double[] parameters) {
        if(this.hasShadow()) {
            drawShadow(gc, parameters);
        }
        if(this.hasBevel()) {
            drawBevel(gc, parameters);
        }
        if(this.hasGradient()) {
            drawGradient(gc);
        } else {
            gc.setFill(this.getColor());
        }
    }

    protected Color getColor(){
        return this.color;
    }

    public boolean hasShadow(){
        return this.shadow;
    }

    public boolean hasBevel(){
        return this.bevel;
    }

    public boolean hasGradient(){
        return this.gradient;
    }

    public void updateEffects(boolean shadow, boolean bevel, boolean gradient) {
        this.shadow = shadow;
        this.bevel = bevel;
        this.gradient = gradient;
    }
    public abstract void drawFigure(GraphicsContext gc, boolean isSelected);
}
