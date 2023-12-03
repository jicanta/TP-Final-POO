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

import java.sql.SQLOutput;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	ArrayList<Figure> selectedFigures = new ArrayList<>();
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
						// borrar: label.append(figure.toString());
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

		canvas.setOnMouseClicked(event -> {
			Point eventPoint = new Point(event.getX(), event.getY()); // TODO: STARTPOINT NO LO COMPARO CON NULL XQ NO LO IGUALE A NULL ANTES
			if(/* startPoint != null && */ startPoint.equals(eventPoint) && selectionButton.isSelected()) {
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				Figure prev = null;
				// TODO: ver si se puede hacer mas eficiente recorriendo la lista al reves
				for (Figure figure : canvasState.figures()) {
					if(figure.figureBelongs(eventPoint)) {
						selectedFigures.remove(prev);
						found = true;
						selectedFigures.add(figure);
						label.append(figure.toString());
						prev = figure;
					}
				}
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
				/* TODO: siento que anti paradigma de objetos o mal uso del instanceof nose cual seria pero hacer tipo
    			if selectedFigure instanceOf rectangle y asi con cada figura me parece pesimo, creo que habria que
    			hacer un enum con los nombres de las figuras y con eso se busca que figura es, no estoy segura pero
   				habia un ejercicio que hacia algo asi para ver que objeto era digamos. Ademas, esto es a lo que me referia
   				en point, que si los hacemos final aca no podemos modificar los point directo, habria q crear nuevos
				for(Figure selectedFigure : selectedFigures) {
					if (selectedFigure instanceof Rectangle) {
						Rectangle rectangle = (Rectangle) selectedFigure;
						rectangle.getTopLeft().x += diffX;
						rectangle.getBottomRight().x += diffX;
						rectangle.getTopLeft().y += diffY;
						rectangle.getBottomRight().y += diffY;
					} else if (selectedFigure instanceof Circle) {
						Circle circle = (Circle) selectedFigure;
						circle.getCenterPoint().x += diffX;
						circle.getCenterPoint().y += diffY;
					} else if (selectedFigure instanceof Square) {
						Square square = (Square) selectedFigure;
						square.getTopLeft().x += diffX;
						square.getBottomRight().x += diffX;
						square.getTopLeft().y += diffY;
						square.getBottomRight().y += diffY;
					} else if (selectedFigure instanceof Ellipse) {
						Ellipse ellipse = (Ellipse) selectedFigure;
						ellipse.getCenterPoint().x += diffX;
						ellipse.getCenterPoint().y += diffY;
					}
				} */
				// SOLUCION PROPUESTA:
				for (Figure selectedFigure : selectedFigures){
					selectedFigure.moveFigure(diffX, diffY);
				}
				if(!selectedFigures.isEmpty()) {
					redrawCanvas();
				}
			}
		});

		deleteButton.setOnAction(event -> { //TODO: CREO QUE ESTA MAL EL REMOVE CUANDO ITERAS
			boolean foundInComposition = false;
			for(Figure selectedFigure : selectedFigures) {
				for (FigureComposition composition : figureCompositions){
					if(composition.contains(selectedFigure)){
						composition.reduceComposition();
						foundInComposition = true;
						figureCompositions.remove(composition);
					}
				}
				if(!foundInComposition){
					canvasState.deleteFigure(selectedFigure);
				}
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		rotateRightButton.setOnAction(event -> {
			boolean foundInComposition = false;
			for(Figure selectedFigure : selectedFigures) {
				for (FigureComposition composition : figureCompositions){
					if(composition.contains(selectedFigure)){
						composition.rotateComposition();
						foundInComposition = true;
					}
				}
				if(!foundInComposition){
					canvasState.rotateFigure(selectedFigure);
				}
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		flipHButton.setOnAction(event -> {
			boolean foundInComposition = false;
			for(Figure selectedFigure : selectedFigures) {
				for (FigureComposition composition : figureCompositions){
					if(composition.contains(selectedFigure)){
						composition.flipHComposition();
						foundInComposition = true;
					}
				}
				if(!foundInComposition){
					canvasState.flipHFigure(selectedFigure);
				}
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		flipVButton.setOnAction(event -> {
			boolean foundInComposition = false;
			for(Figure selectedFigure : selectedFigures) {
				for (FigureComposition composition : figureCompositions){
					if(composition.contains(selectedFigure)){
						composition.flipVComposition();
						foundInComposition = true;
					}
				}
				if(!foundInComposition){
					canvasState.flipVFigure(selectedFigure);
				}
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		augmentButton.setOnAction(event -> {
			boolean foundInComposition = false;
			for(Figure selectedFigure : selectedFigures) {
				for (FigureComposition figureComposition : figureCompositions){
					if(figureComposition.contains(selectedFigure)){
						figureComposition.augmentComposition();
						foundInComposition = true;
					}
				}
				if(!foundInComposition){
					canvasState.augmentFigure(selectedFigure);
				}
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		reduceButton.setOnAction(event -> {
			boolean foundInComposition = false;
			for(Figure selectedFigure : selectedFigures) {
				for (FigureComposition figureComposition : figureCompositions){
					if(figureComposition.contains(selectedFigure)){
						figureComposition.reduceComposition();
						foundInComposition = true;
					}
				}
				if(!foundInComposition){
					canvasState.reduceFigure(selectedFigure);
				}
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		groupButton.setOnAction(event -> {
			if(!selectedFigures.isEmpty()) {
				FigureComposition figureComposition = new FigureComposition(canvasState);
				figureComposition.addList(selectedFigures);
				figureCompositions.add(figureComposition);  //TODO: NOSE SI ESTOY MEZCLANDO FRONT CON BACK
				selectedFigures.clear();
				redrawCanvas();
			}
		});

		ungroupButton.setOnAction(event -> { //TODO: NO SE PUEDE ELIMINAR UN ELEM DE LA LISTA QUE SE ITERA
			/* for(Figure selectedFigure : selectedFigures){
				for (FigureComposition figureComposition : figureCompositions){
					if(figureComposition.contains(selectedFigure)){
						figureCompositions.remove(figureComposition);
					}
				}
			}
			if(!selectedFigures.isEmpty()) {
				selectedFigures.clear();
				redrawCanvas();
			}*/
		});

		setLeft(buttonsBox);
		setRight(canvas);
	}

	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figures()) {
			boolean foundInComposition = false;
			if(selectedFigures.contains(figure)) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(lineColor);
			}
														//TODO: REVISAR CODIGO, QUEDO MUY FEO
			for(FigureComposition figureComposition : figureCompositions){
				if(figureComposition.contains(figure)){
					foundInComposition = true;
					if(selectedFigures.contains(figure)){
						figureComposition.isSelected = true;
					}
					if(figureComposition.isSelected){
						gc.setStroke(Color.RED); //TODO: (COMENTARIO X SI SE ENTIENDE EL CODIGO) SI LA COMPOSICION ESTA SELECCIONADA, EL BORDE DE TODAS LAS FIGURAS DEBE SER ROJO
					}
					for(Figure figureToPaint : figureComposition){
						/* TODO: mismo comentario que antes */
                        gc.setFill(figureColorMap.get(figureToPaint));
						paintFigure(figureToPaint);
					}
				}
			}
			if(!foundInComposition){
                gc.setFill(figureColorMap.get(figure));
				paintFigure(figure);
			}
		}
		for (FigureComposition figureComposition : figureCompositions){
			figureComposition.isSelected = false; //TODO: (COMENTARIO X SI NO SE ENTIENDE EL CODIGO) "reseteo" los flag que indican si las compos estan seleccionadas.
		}
	}
	/* private void paintFigure(Figure figure){
		if(figure instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) figure;
			gc.fillRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
					Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
			gc.strokeRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
					Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
		} else if(figure instanceof Circle) {
			Circle circle = (Circle) figure;
			double diameter = circle.getRadius() * 2;
			gc.fillOval(circle.getCenterPoint().getX() - circle.getRadius(), circle.getCenterPoint().getY() - circle.getRadius(), diameter, diameter);
			gc.strokeOval(circle.getCenterPoint().getX() - circle.getRadius(), circle.getCenterPoint().getY() - circle.getRadius(), diameter, diameter);
		} else if(figure instanceof Square) {
			Square square = (Square) figure;
			gc.fillRect(square.getTopLeft().getX(), square.getTopLeft().getY(),
					Math.abs(square.getTopLeft().getX() - square.getBottomRight().getX()), Math.abs(square.getTopLeft().getY() - square.getBottomRight().getY()));
			gc.strokeRect(square.getTopLeft().getX(), square.getTopLeft().getY(),
					Math.abs(square.getTopLeft().getX() - square.getBottomRight().getX()), Math.abs(square.getTopLeft().getY() - square.getBottomRight().getY()));
		} else if(figure instanceof Ellipse) {
			Ellipse ellipse = (Ellipse) figure;
			gc.strokeOval(ellipse.getCenterPoint().getX() - (ellipse.getsMayorAxis() / 2), ellipse.getCenterPoint().getY() - (ellipse.getsMinorAxis() / 2), ellipse.getsMayorAxis(), ellipse.getsMinorAxis());
			gc.fillOval(ellipse.getCenterPoint().getX() - (ellipse.getsMayorAxis() / 2), ellipse.getCenterPoint().getY() - (ellipse.getsMinorAxis() / 2), ellipse.getsMayorAxis(), ellipse.getsMinorAxis());
		}
	} */
	// SOLUCION PROPUESTA:
	private void paintFigure(Figure figure){
		figure.paint(gc);
	}
}
