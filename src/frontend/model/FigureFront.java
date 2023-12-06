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

    public FigureFront(Color color) {
        this.color = color;
    }

    protected Color getColor(){    // TODO: nose si deberia ser public o protected pero bueno
        return this.color;
    }

    protected boolean hasShadow(){
        return shadow;
    }

    protected boolean hasBevel(){
        return bevel;
    }

    protected boolean hasGradient(){
        return gradient;
    }
    public abstract void drawFigure(GraphicsContext gc);
}
