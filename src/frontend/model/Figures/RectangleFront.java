package frontend.model.Figures;

import backend.model.Rectangle;
import frontend.model.QuadrilateralFront;
import javafx.scene.paint.Color;

public class RectangleFront extends QuadrilateralFront {
    public RectangleFront(Rectangle rectangle, Color color, boolean shadow, boolean bevel, boolean gradient) {
        super(rectangle, color, shadow, bevel, gradient);
    }
}
