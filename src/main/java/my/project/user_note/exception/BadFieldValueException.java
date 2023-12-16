package my.project.user_note.exception;

public class BadFieldValueException extends RuntimeException {
    public BadFieldValueException(String message) {
        super(message);
    }
}
