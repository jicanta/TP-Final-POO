package frontend;

import backend.CanvasState;
import backend.FigureComposition;
import backend.model.*;
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

	// Colores de relleno de cada figura
	Map<Figure, Color> figureColorMap = new HashMap<>();

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
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
				Figure newFigure = null;
				/* TODO: el uso de tantos if else me da dudas, igual nose como se haria sino pero pongo por las dudas */
				if (rectangleButton.isSelected()) {
					newFigure = new Rectangle(startPoint, endPoint);
				} else if (circleButton.isSelected()) {
					double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
					newFigure = new Circle(startPoint, circleRadius);
				} else if (squareButton.isSelected()) {
					double size = Math.abs(endPoint.getX() - startPoint.getX());
					newFigure = new Square(startPoint, size);
				} else if (ellipseButton.isSelected()) {
					Point centerPoint = new Point(Math.abs(endPoint.getX() + startPoint.getX()) / 2, (Math.abs((endPoint.getY() + startPoint.getY())) / 2));
					double sMayorAxis = Math.abs(endPoint.getX() - startPoint.getX());
					double sMinorAxis = Math.abs(endPoint.getY() - startPoint.getY());
					newFigure = new Ellipse(centerPoint, sMayorAxis, sMinorAxis);
				}
				else {
					return;
				}
				figureColorMap.put(newFigure, fillColorPicker.getValue());
				canvasState.addFigure(newFigure);
				//startPoint = null; TODO: POR DEFAULT LO IGUALABA A NULL, NO ENTIENDO EL PORQUE
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
					label.append(figure.toString());
				}
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
				label.append(selectedFigures);
				if (found) {
					statusPane.updateStatus(label.toString());
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
					Color auxColor = figureColorMap.get(selectedFigure);
					figureColorMap.remove(selectedFigure);
					selectedFigure.moveFigure(diffX, diffY);
					figureColorMap.put(selectedFigure, auxColor);
				}
				if(!selectedFigures.isEmpty()) {
					redrawCanvas();
				}
			}
		});

		deleteButton.setOnAction(event -> {
			for (Figure figure : selectedFigures){
				canvasState.deleteFigure(figure);
				figureColorMap.remove(figure); //TODO: por las dudas borro a la figura del mapa de colores
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		rotateRightButton.setOnAction(event -> {
			for (Figure figure : selectedFigures){
				Color auxColor = figureColorMap.get(figure);
				figureColorMap.remove(figure);
				canvasState.rotateFigure(figure);
				figureColorMap.put(figure, auxColor);
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		flipHButton.setOnAction(event -> {
			for (Figure figure : selectedFigures){
				Color auxColor = figureColorMap.get(figure);
				figureColorMap.remove(figure);
				canvasState.flipHFigure(figure);
				figureColorMap.put(figure, auxColor);
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		flipVButton.setOnAction(event -> {
			for (Figure figure : selectedFigures){
				Color auxColor = figureColorMap.get(figure); //TODO: el problema consiste en que el mapa de colores "pierde" el rastro de la figura ya que esta cambia de atributos.
				figureColorMap.remove(figure);
				canvasState.flipVFigure(figure);
				figureColorMap.put(figure, auxColor);
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		augmentButton.setOnAction(event -> {
			for (Figure figure : selectedFigures){
				Color auxColor = figureColorMap.get(figure);
				figureColorMap.remove(figure);
				canvasState.augmentFigure(figure);
				figureColorMap.put(figure, auxColor);
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		reduceButton.setOnAction(event -> {
			for (Figure figure : selectedFigures){
				Color auxColor = figureColorMap.get(figure);
				figureColorMap.remove(figure);
				canvasState.reduceFigure(figure);
				figureColorMap.put(figure, auxColor);
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
				figureCompositions.add(figureComposition);  //TODO: NOSE SI ESTOY MEZCLANDO FRONT CON BACK
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

			/* comentario de jose: no se podria hacer asi directamente? igual no funca cuando apreto algun boton
			se pintan todos del mismo color pero bueno eso */
			gc.setFill(figureColorMap.get(figure));
			paintFigure(figure);
		}
	}

	/* TODO: PROBLEMONNNN mezclando front con back (le estoy pasando gc) juanpa fijate si cuando arreglas
	   lo de los colores podes arreglar esto :)
	*/
	private void paintFigure(Figure figure){
		figure.paint(gc);
	}
}
