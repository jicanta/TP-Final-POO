package frontend;

import backend.CanvasState;
import backend.FigureComposition;
import backend.model.*;
import frontend.model.FigureFront;
import frontend.model.Figures.CircleFront;
import frontend.model.Figures.EllipseFront;
import frontend.model.Figures.RectangleFront;
import frontend.model.Figures.SquareFront;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.*;

public class PaintPane extends BorderPane {

	// BackEnd
	CanvasState canvasState;

	// Canvas y relacionados
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	Color lineColor = Color.BLACK;
	Color defaultFillColor = Color.YELLOW;

	// Botones Barra Izquierda
	ToggleButton selectionButton = new ToggleButton("Seleccionar");
	ToggleButton rectangleButton = new ToggleButton("Rectángulo");
	ToggleButton circleButton = new ToggleButton("Círculo");
	ToggleButton squareButton = new ToggleButton("Cuadrado");
	ToggleButton ellipseButton = new ToggleButton("Elipse");
	ToggleButton deleteButton = new ToggleButton("Borrar");
	ToggleButton rotateRightButton = new ToggleButton("Girar D");
	ToggleButton flipHButton = new ToggleButton("Voltear H");
	ToggleButton flipVButton = new ToggleButton("Voltear V");
	ToggleButton augmentButton = new ToggleButton("Escalar +");
	ToggleButton reduceButton = new ToggleButton("Escalar -");
	ToggleButton groupButton = new ToggleButton("Agrupar");
	ToggleButton ungroupButton = new ToggleButton("Desagrupar");

	// Selector de color de relleno
	ColorPicker fillColorPicker = new ColorPicker(defaultFillColor);

	// Dibujar una figura
	Point startPoint;

	// Lista de figuras seleccionadas
	Set<Figure> selectedFigures = new HashSet<>();
	// Lista de agrupaciones de figuras
	ArrayList<FigureComposition> figureCompositions = new ArrayList<>();

	// StatusBar
	StatusPane statusPane;

	// EffectsPane
	EffectsPane effectsPane;

	// Figura del back - Figura del front
	Map<Figure, FigureFront> figuresFrontMap = new HashMap<>();

	public void updateSelectedFigures() {
		for(Figure figure : selectedFigures) {
			FigureFront figureFront = figuresFrontMap.get(figure);
			figureFront.updateEffects(effectsPane.checkedShadow(), effectsPane.checkedBevel(), effectsPane.checkedGradient());
			figuresFrontMap.put(figure, figureFront);
		}
	}

	public PaintPane(CanvasState canvasState, StatusPane statusPane, EffectsPane effectsPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		this.effectsPane = effectsPane;
		ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, deleteButton, groupButton, ungroupButton ,rotateRightButton, flipHButton, flipVButton, augmentButton, reduceButton};
		ToggleGroup tools = new ToggleGroup();
		for (ToggleButton tool : toolsArr) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}
		VBox buttonsBox = new VBox(10);
		buttonsBox.getChildren().addAll(toolsArr);
		buttonsBox.getChildren().add(fillColorPicker);
		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);

		canvas.setOnMousePressed(event -> {
			startPoint = new Point(event.getX(), event.getY());
		});

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) {
				return ;
			}
			if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
				return ;
			}

			if(selectionButton.isSelected()) {
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				Rectangle selectionRect = new Rectangle(startPoint, endPoint);
				for(Figure figure : canvasState.figures()) {
					if(figure.isInside(selectionRect)) {
						found = true;
						selectedFigures.add(figure);
					}
				}
				if (found) {
					statusPane.updateStatus(label.toString());
				} else {
					selectedFigures.clear();
					statusPane.updateStatus("Ninguna figura encontrada");
				}
				redrawCanvas();

			} else {
				Figure newFigure;
				if (rectangleButton.isSelected()) {
					newFigure = new Rectangle(startPoint, endPoint);
					figuresFrontMap.put(newFigure, new RectangleFront((Rectangle) newFigure, fillColorPicker.getValue(), effectsPane.checkedShadow(), effectsPane.checkedBevel(), effectsPane.checkedGradient()));
				} else if (circleButton.isSelected()) {
					double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
					newFigure = new Circle(startPoint, circleRadius);
					figuresFrontMap.put(newFigure, new CircleFront((Circle) newFigure, fillColorPicker.getValue(), effectsPane.checkedShadow(), effectsPane.checkedBevel(), effectsPane.checkedGradient()));
				} else if (squareButton.isSelected()) {
					double size = Math.abs(endPoint.getX() - startPoint.getX());
					newFigure = new Square(startPoint, size);
					figuresFrontMap.put(newFigure, new SquareFront((Square) newFigure, fillColorPicker.getValue(), effectsPane.checkedShadow(), effectsPane.checkedBevel(), effectsPane.checkedGradient()));
				} else if (ellipseButton.isSelected()) {
					Point centerPoint = new Point(Math.abs(endPoint.getX() + startPoint.getX()) / 2, (Math.abs((endPoint.getY() + startPoint.getY())) / 2));
					double sMayorAxis = Math.abs(endPoint.getX() - startPoint.getX());
					double sMinorAxis = Math.abs(endPoint.getY() - startPoint.getY());
					newFigure = new Ellipse(centerPoint, sMayorAxis, sMinorAxis);
					figuresFrontMap.put(newFigure, new EllipseFront((Ellipse) newFigure,fillColorPicker.getValue(), effectsPane.checkedShadow(), effectsPane.checkedBevel(), effectsPane.checkedGradient()));
				} else {
					return;
				}
				canvasState.addFigure(newFigure);
				redrawCanvas();
			}
		});

		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Figure figure : canvasState.figures()) {
				if(figure.figureBelongs(eventPoint)) {
					found = true;
					label.append(figure);
				}
			}
			if(!selectedFigures.isEmpty()) {
				updateSelectedFigures();
				redrawCanvas();
			}
			if(found) {
				statusPane.updateStatus(label.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
		});


		/* no me gusta mucho toda esta parte del codigo pero probe iterando con la lista al reves
		   y no funca, tipo si selecciono uno se selecciona el de atras tambien
		 */

		canvas.setOnMouseClicked(event -> {
			Point eventPoint = new Point(event.getX(), event.getY()); // TODO: STARTPOINT NO LO COMPARO CON NULL XQ NO LO IGUALE A NULL ANTES
			if(startPoint.equals(eventPoint) && selectionButton.isSelected()) {
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				Set<FigureComposition> compositionsToAdd = new HashSet<>();
				Figure prev = null;
				for (Figure figure : canvasState.figures()) {
					if (figure.figureBelongs(eventPoint)) {
						selectedFigures.remove(prev);
						found = true;
						selectedFigures.add(figure);
						label.append(figure);
						for (FigureComposition composition : figureCompositions) {
							if (composition.contains(figure)) {
								compositionsToAdd.add(composition);
							}
						}
						prev = figure;
					}
				}
				for (FigureComposition composition : compositionsToAdd) {
					selectedFigures.addAll(composition.getList());
				}
				if (found) {
					statusPane.updateStatus(label.toString());
					for(Figure figure : selectedFigures) {
						FigureFront curFrontFigure = figuresFrontMap.get(figure);
						effectsPane.updateStatus(curFrontFigure.hasShadow(), curFrontFigure.hasBevel(), curFrontFigure.hasGradient());
					}
				} else {
					selectedFigures.clear();
					statusPane.updateStatus("Ninguna figura encontrada");
				}

				redrawCanvas();
			}
		});

		// esto es para mover las figuras
		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
				double diffY = (eventPoint.getY() - startPoint.getY()) / 100;
				for (Figure selectedFigure : selectedFigures){
					selectedFigure.moveFigure(diffX, diffY);
				}
				if(!selectedFigures.isEmpty()) {
					redrawCanvas();
				}
			}
		});

		deleteButton.setOnAction(event -> {
			for (Figure figure : selectedFigures){
				canvasState.deleteFigure(figure);
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		rotateRightButton.setOnAction(event -> {
			for (Figure figure : selectedFigures){
				canvasState.rotateFigure(figure);
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		flipHButton.setOnAction(event -> {
			for (Figure figure : selectedFigures){
				canvasState.flipHFigure(figure);
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		flipVButton.setOnAction(event -> {
			for (Figure figure : selectedFigures){
				canvasState.flipVFigure(figure);
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		augmentButton.setOnAction(event -> {
			for (Figure figure : selectedFigures){
				canvasState.augmentFigure(figure);
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		reduceButton.setOnAction(event -> {
			for (Figure figure : selectedFigures){
				canvasState.reduceFigure(figure);
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		groupButton.setOnAction(event -> {
			if(!selectedFigures.isEmpty()) {
				FigureComposition figureComposition = new FigureComposition();
				figureComposition.addAll(selectedFigures);
				figureCompositions.add(figureComposition);
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		ungroupButton.setOnAction(event -> {
			for(Figure selectedFigure : selectedFigures){
                figureCompositions.removeIf(figureComposition -> figureComposition.contains(selectedFigure));
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		setLeft(buttonsBox);
		setRight(canvas);
	}

	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figures()) {
			if (selectedFigures.contains(figure)) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(lineColor);
			}
			figuresFrontMap.get(figure).drawFigure(gc);
		}
	}

}
