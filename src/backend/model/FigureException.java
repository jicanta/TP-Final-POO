package backend.model;

public class FigureException extends RuntimeException{

    private static final String MESSAGE = "Cannot create figure with such parameters";

    FigureException(){
        super(MESSAGE);
    }
}
