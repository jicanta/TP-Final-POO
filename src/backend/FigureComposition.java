package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FigureComposition {

    private List<Figure> compositionList;

    public FigureComposition(){
        compositionList = new ArrayList<>();
    }

    public List<Figure> getList(){
        return this.compositionList;
    }

    public void addAll(Set<Figure> figures){
        if (figures.size() <= 1){
            throw new CannotGroupException();
        }
        else {
            compositionList.addAll(figures);
        }
    }

    public boolean contains(Figure fig){
        return compositionList.contains(fig);
    }

    @Override
    public String toString(){
        StringBuilder string = new StringBuilder();
        for (Figure figure : this.compositionList){
            string.append(figure.toString()).append(", ");
        }
        return string.toString();
    }
}
