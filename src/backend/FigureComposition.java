package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FigureComposition implements Iterable<Figure>{

    private CanvasState canvasState;
    private List<Figure> compositionList;
    public boolean isSelected = false;

    public FigureComposition(CanvasState canvasState){
        this.canvasState = canvasState;
        compositionList = new ArrayList<>();
    }

    public void add(Figure fig){
        compositionList.add(fig);
    }

    public void addList(ArrayList<Figure> figuresList){
        compositionList.addAll(figuresList);
    }

    public void deleteComposition(){
        for (Figure fig : compositionList){
            canvasState.deleteFigure(fig);
        }
    }

    public boolean contains(Figure fig){
        return compositionList.contains(fig);
    }

    public void rotateComposition(){
        for(Figure fig : compositionList){
            canvasState.rotateFigure(fig);
        }
    }

    public void flipHComposition(){
        for(Figure fig : compositionList){
            canvasState.flipHFigure(fig);
        }
    }

    public void flipVComposition(){
        for(Figure fig : compositionList){
            canvasState.flipVFigure(fig);
        }
    }

    public void augmentComposition(){
        for(Figure fig : compositionList){
            canvasState.augmentFigure(fig);
        }
    }

    public void reduceComposition(){
        for(Figure fig : compositionList){
            canvasState.reduceFigure(fig);
        }
    }

    @Override
    public Iterator<Figure> iterator() {
        return compositionList.iterator();
    }
}
