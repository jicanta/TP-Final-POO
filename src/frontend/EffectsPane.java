package frontend;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public class EffectsPane extends BorderPane {
    private final Label effectsLabel;

    public CheckBox shadow;
    public CheckBox gradient;
    public CheckBox bevel;

    public boolean checkedShadow() {
        return this.shadow.isSelected();
    }

    public void setShadow(boolean val, boolean isInAllFigures) {
        if(!val) {
            this.shadow.setSelected(false);
        } else {
            if(isInAllFigures) {
                this.shadow.setSelected(true);
            } else {
                this.shadow.setIndeterminate(true);
            }
        }
    }

    public boolean checkedBevel() {
        return this.bevel.isSelected();
    }

    public void setBevel(boolean val, boolean isInAllFigures) {
        if(!val) {
            this.bevel.setSelected(false);
        } else {
            if(isInAllFigures) {
                this.bevel.setSelected(true);
            } else {
                this.bevel.setIndeterminate(true);
            }
        }
    }

    public boolean checkedGradient() {
        return this.gradient.isSelected();
    }

    public void setGradient(boolean val, boolean isInAllFigures) {
        if(!val) {
            this.gradient.setSelected(false);
        } else {
            if(isInAllFigures) {
                this.gradient.setSelected(true);
            } else {
                this.gradient.setIndeterminate(true);
            }
        }
    }

    public void updateStatus(boolean shadow, boolean isShadowInAll, boolean bevel, boolean isBevelInAll, boolean gradient, boolean isGradientInAll) {
        setShadow(shadow, isShadowInAll);
        setBevel(bevel, isBevelInAll);
        setGradient(gradient, isGradientInAll);
    }

    public EffectsPane() {
        setStyle("-fx-background-color: #9f9f9f");

        // EffectsCheckboxes
        HBox effectsHBox = new HBox(10);
        this.shadow = new CheckBox("Sombra");
        this.gradient = new CheckBox("Gradiente");
        this.bevel = new CheckBox("Biselado");
        effectsHBox.getChildren().addAll(this.shadow, this.gradient, this.bevel);

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
