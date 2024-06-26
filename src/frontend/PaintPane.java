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

	private static final int DISPLACEMENT = 100;

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

	private void handleSelectedFigures() {
		if(!selectedFigures.isEmpty()) {
			updateSelectedFigures();
			redrawCanvas();
		}
	}

	private void clearSelectedFigures() {
		selectedFigures.clear();
		redrawCanvas();
	}

	private void updateCheckBoxesBySelectedFigures() {
		effectsPane.shadow.setIndeterminate(false);
		effectsPane.bevel.setIndeterminate(false);
		effectsPane.gradient.setIndeterminate(false);
		boolean allShadow = true;
		boolean allBevel = true;
		boolean allGradient = true;
		for(Figure figure : selectedFigures) {
			FigureFront curFrontFigure = figuresFrontMap.get(figure);
			if(!curFrontFigure.hasShadow()) {
				allShadow = false;
			}
			if(!curFrontFigure.hasBevel()) {
				allBevel = false;
			}
			if(!curFrontFigure.hasGradient()) {
				allGradient = false;
			}
		}
		for(Figure figure : selectedFigures) {
			FigureFront curFrontFigure = figuresFrontMap.get(figure);
			effectsPane.updateStatus(curFrontFigure.hasShadow(), allShadow, curFrontFigure.hasBevel(), allBevel, curFrontFigure.hasGradient(), allGradient);
		}
	}

	public void checkIfRedraw() {
		if(!selectedFigures.isEmpty()) {
			redrawCanvas();
		}
	}

	public PaintPane(CanvasState canvasState, StatusPane statusPane, EffectsPane effectsPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		this.effectsPane = effectsPane;
		ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, deleteButton, groupButton, ungroupButton ,rotateRightButton, flipHButton, flipVButton, augmentButton, reduceButton};

		ToggleGroup tools = new ToggleGroup();
		for(ToggleButton tool : toolsArr) {
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

		selectionButton.setOnAction(event -> clearSelectedFigures());

		circleButton.setOnAction(event -> clearSelectedFigures());

		rectangleButton.setOnAction(event -> clearSelectedFigures());

		ellipseButton.setOnAction(event -> clearSelectedFigures());

		squareButton.setOnAction(event -> clearSelectedFigures());

		effectsPane.shadow.setOnAction(event -> handleSelectedFigures());

		effectsPane.bevel.setOnAction(event -> handleSelectedFigures());

		effectsPane.gradient.setOnAction(event -> handleSelectedFigures());

		canvas.setOnMousePressed(event -> startPoint = new Point(event.getX(), event.getY()));

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) {
				return ;
			}

			if(selectionButton.isSelected()) {
				boolean found = false;
				try {
					Rectangle selectionRect = new Rectangle(startPoint, endPoint);
					for(Figure figure : canvasState.figures()) {
						if(figure.isInside(selectionRect)) {
							found = true;
							for(FigureComposition figureComposition : canvasState.compositions()) {
								if(figureComposition.contains(figure)) {
									selectedFigures.addAll(figureComposition.getList());
								}
							}
							selectedFigures.add(figure);
						}
					}
				}
				catch(Exception ignored){

				}

				if(found) {
					updateCheckBoxesBySelectedFigures();
				} else {
					statusPane.updateStatus("Ninguna figura encontrada");
				}
				redrawCanvas();
			} else {

				try {
					selectedFigures.clear();
					Figure newFigure;
					if(rectangleButton.isSelected()) {
						newFigure = new Rectangle(startPoint, endPoint);
						figuresFrontMap.put(newFigure, new RectangleFront((Rectangle) newFigure, fillColorPicker.getValue(), effectsPane.checkedShadow(), effectsPane.checkedBevel(), effectsPane.checkedGradient()));
					} else if(circleButton.isSelected()) {
						double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
						newFigure = new Circle(startPoint, circleRadius);
						figuresFrontMap.put(newFigure, new CircleFront((Circle) newFigure, fillColorPicker.getValue(), effectsPane.checkedShadow(), effectsPane.checkedBevel(), effectsPane.checkedGradient()));
					} else if(squareButton.isSelected()) {
						double size = Math.abs(endPoint.getX() - startPoint.getX());
						newFigure = new Square(startPoint, size);
						figuresFrontMap.put(newFigure, new SquareFront((Square) newFigure, fillColorPicker.getValue(), effectsPane.checkedShadow(), effectsPane.checkedBevel(), effectsPane.checkedGradient()));
					} else if(ellipseButton.isSelected()) {
						Point centerPoint = new Point(Math.abs(endPoint.getX() + startPoint.getX()) / 2, (Math.abs((endPoint.getY() + startPoint.getY())) / 2));
						double sMayorAxis = Math.abs(endPoint.getX() - startPoint.getX());
						double sMinorAxis = Math.abs(endPoint.getY() - startPoint.getY());
						newFigure = new Ellipse(centerPoint, sMayorAxis, sMinorAxis);
						figuresFrontMap.put(newFigure, new EllipseFront((Ellipse) newFigure, fillColorPicker.getValue(), effectsPane.checkedShadow(), effectsPane.checkedBevel(), effectsPane.checkedGradient()));
					} else {
						return;
					}
					canvasState.addFigure(newFigure);
					selectedFigures.add(newFigure);
					redrawCanvas();
				}
				catch(Exception e){
					System.out.println(e.getMessage());
				}
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

			if(found) {
				statusPane.updateStatus(label.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
		});

		canvas.setOnMouseClicked(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			if(startPoint.equals(eventPoint) && selectionButton.isSelected()) {
				selectedFigures.clear();
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				Set<FigureComposition> compositionsToAdd = new HashSet<>();
				Figure prev = null;
				for(Figure figure : canvasState.figures()) {
					if(figure.figureBelongs(eventPoint)) {
						selectedFigures.remove(prev);
						found = true;
						selectedFigures.add(figure);
						label.append(figure);
						prev = figure;
					}
				}
				for(FigureComposition composition : canvasState.compositions()) {
					if(composition.contains(prev)) {
						compositionsToAdd.add(composition);
					}
				}
				for(FigureComposition composition : compositionsToAdd) {
					selectedFigures.addAll(composition.getList());
				}
				if(found) {
					updateCheckBoxesBySelectedFigures();
					statusPane.updateStatus(label.toString());
				} else {
					selectedFigures.clear();
					statusPane.updateStatus("Ninguna figura encontrada");
				}

			}
			redrawCanvas();
		});

		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX()) / DISPLACEMENT;
				double diffY = (eventPoint.getY() - startPoint.getY()) / DISPLACEMENT;
				for(Figure selectedFigure : selectedFigures){
					selectedFigure.moveFigure(diffX, diffY);
				}
				checkIfRedraw();
			}
		});

		deleteButton.setOnAction(event -> {
			for(Figure figure : selectedFigures) {
				canvasState.deleteFigure(figure);
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		rotateRightButton.setOnAction(event -> {
			for(Figure figure : selectedFigures) {
				canvasState.rotateFigure(figure);
			}
			checkIfRedraw();
		});

		flipHButton.setOnAction(event -> {
			for(Figure figure : selectedFigures) {
				canvasState.flipHFigure(figure);
			}
			checkIfRedraw();
		});

		flipVButton.setOnAction(event -> {
			for(Figure figure : selectedFigures) {
				canvasState.flipVFigure(figure);
			}
			checkIfRedraw();
		});

		augmentButton.setOnAction(event -> {
			for(Figure figure : selectedFigures) {
				canvasState.augmentFigure(figure);
			}
			checkIfRedraw();
		});

		reduceButton.setOnAction(event -> {
			for(Figure figure : selectedFigures) {
				canvasState.reduceFigure(figure);
			}
			checkIfRedraw();
		});

		groupButton.setOnAction(event -> {
			try {
				FigureComposition figureComposition = new FigureComposition();
				figureComposition.addAll(selectedFigures);
				canvasState.compositions().add(figureComposition);
				redrawCanvas();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
        });

		ungroupButton.setOnAction(event -> {
			for(Figure selectedFigure : selectedFigures) {
                canvasState.compositions().removeIf(figureComposition -> figureComposition.contains(selectedFigure));
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
			boolean isSelected = selectedFigures.contains(figure);
			if(isSelected) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(lineColor);
			}
			figuresFrontMap.get(figure).drawFigure(gc, isSelected);
		}
	}
}
