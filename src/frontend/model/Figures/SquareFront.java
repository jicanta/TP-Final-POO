package frontend.model.Figures;

import backend.model.Square;
import frontend.model.QuadrilateralFront;
import javafx.scene.paint.Color;

public class SquareFront extends QuadrilateralFront {
    public SquareFront(Square square, Color color, boolean shadow, boolean bevel, boolean gradient) {
        super(square, color, shadow, bevel, gradient);
    }
}
