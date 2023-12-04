package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FigureComposition implements Iterable<Figure>{

    private List<Figure> compositionList;
    public boolean isSelected = false;

    public FigureComposition(){
        compositionList = new ArrayList<>();
    }

    public void add(Figure fig){
        compositionList.add(fig);
    }

    public void addList(ArrayList<Figure> figuresList){
        compositionList.addAll(figuresList);
    }

    public void deleteComposition(CanvasState canvasState){
        for (Figure fig : compositionList){
            canvasState.deleteFigure(fig);
        }
    }

    public boolean contains(Figure fig){
        return compositionList.contains(fig);
    }

    public void rotateComposition(CanvasState canvasState){
        for(Figure fig : compositionList){
            canvasState.rotateFigure(fig);
        }
    }

    public void flipHComposition(CanvasState canvasState){
        for(Figure fig : compositionList){
            canvasState.flipHFigure(fig);
        }
    }

    public void flipVComposition(CanvasState canvasState){
        for(Figure fig : compositionList){
            canvasState.flipVFigure(fig);
        }
    }

    public void augmentComposition(CanvasState canvasState){
        for(Figure fig : compositionList){
            canvasState.augmentFigure(fig);
        }
    }

    public void reduceComposition(CanvasState canvasState){
        for(Figure fig : compositionList){
            canvasState.reduceFigure(fig);
        }
    }

    @Override
    public Iterator<Figure> iterator() {
        return compositionList.iterator();
    }
}
