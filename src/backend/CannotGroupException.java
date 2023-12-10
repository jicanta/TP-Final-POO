package backend;

public class CannotGroupException extends RuntimeException {
    private static final String MESSAGE = "Cannot create group with less than two figures";
    public CannotGroupException() {
        super(MESSAGE);
    }
}
