package frontend;

import backend.CanvasState;
import javafx.scene.layout.VBox;

public class MainFrame extends VBox {

    public MainFrame(CanvasState canvasState) {
        getChildren().add(new AppMenuBar());
        EffectsPane effectsPane = new EffectsPane();
        getChildren().add(effectsPane);
        StatusPane statusPane = new StatusPane();
        getChildren().add(new PaintPane(canvasState, statusPane, effectsPane));
        getChildren().add(statusPane);

    }

}
