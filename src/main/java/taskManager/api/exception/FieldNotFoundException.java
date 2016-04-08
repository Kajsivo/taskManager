package taskManager.api.exception;

public class FieldNotFoundException extends RuntimeException {
    public FieldNotFoundException(String fieldToChange) {
        super("No such field: \" " + fieldToChange + " \"");
    }
}
