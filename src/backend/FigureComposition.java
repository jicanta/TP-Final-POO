package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FigureComposition implements Iterable<Figure>{

    private List<Figure> compositionList;
    public boolean isSelected = false;

    public FigureComposition(){
        compositionList = new ArrayList<>();
    }

    public List<Figure> getList(){
        return this.compositionList;
    }

    public void addAll(Set<Figure> figures){
        compositionList.addAll(figures);
    }

    public boolean contains(Figure fig){
        return compositionList.contains(fig);
    }

    @Override
    public Iterator<Figure> iterator() {
        return compositionList.iterator();
    }

    @Override
    public String toString(){
        StringBuilder string = new StringBuilder();
        for (Figure figure : this){
            string.append(figure.toString()+", ");
        }
        return string.toString();
    }
}
