package frontend.model.Figures;

import backend.model.Ellipse;
import frontend.model.OvalFront;
import javafx.scene.paint.Color;

public class EllipseFront extends OvalFront {
    public EllipseFront(Ellipse ellipse, Color color, boolean shadow, boolean bevel, boolean gradient) {
        super(ellipse, color, shadow, bevel, gradient);
    }
}
