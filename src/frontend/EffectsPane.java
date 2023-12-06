package frontend;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public class EffectsPane extends BorderPane {
    private final Label effectsLabel;

    // TODO: nose si hace falta guardar los checkboxes o si los podemos dejar como local variables y despues
    // accederlos, pero bueno como solo hice la parte de frontend los deje asi
    public CheckBox sombra;
    public CheckBox gradiente;
    public CheckBox biselado;

    public boolean checkedShadow() {
        return sombra.isSelected();
    }

    public void setShadow(boolean val) {
        sombra.setSelected(val);
    }

    public boolean checkedBevel() {
        return biselado.isSelected();
    }

    public void setBevel(boolean val) {
        biselado.setSelected(val);
    }

    public boolean checkedGradient() {
        return gradiente.isSelected();
    }

    public void setGradient(boolean val) {
        gradiente.setSelected(val);
    }

    public void updateStatus(boolean shadow, boolean bevel, boolean gradient) {
        setShadow(shadow);
        setBevel(bevel);
        setGradient(gradient);
    }

    public boolean statusChanged(boolean initialShadow, boolean initialBevel, boolean initialGradient) {
        return checkedShadow() != initialShadow || checkedBevel() != initialBevel || checkedGradient() != initialGradient;
    }



    public EffectsPane() {
        setStyle("-fx-background-color: #9f9f9f");

        // EffectsCheckboxes
        HBox effectsHBox = new HBox(10);
        this.sombra = new CheckBox("Sombra");
        this.gradiente = new CheckBox("Gradiente");
        this.biselado = new CheckBox("Biselado");
        effectsHBox.getChildren().addAll(sombra, gradiente, biselado);

        // EffectsLabel
        effectsLabel = new Label("Efectos:");
        effectsLabel.setStyle("-fx-font-size: 14");

        // MainHBox
        HBox mainHBox = new HBox(10);
        mainHBox.setMinHeight(20);
        mainHBox.getChildren().addAll(effectsLabel, effectsHBox);
        mainHBox.setAlignment(Pos.CENTER);
        setCenter(mainHBox);
    }
}
