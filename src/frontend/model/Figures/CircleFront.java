package frontend.model.Figures;

import backend.model.Circle;
import frontend.model.OvalFront;
import javafx.scene.paint.Color;

public class CircleFront extends OvalFront {
    public CircleFront(Circle circle, Color color, boolean shadow, boolean bevel, boolean gradient) {
        super(circle, color, shadow, bevel, gradient);
    }
}
