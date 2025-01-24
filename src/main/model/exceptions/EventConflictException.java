package model.exceptions;

public class EventConflictException extends Exception {

    public EventConflictException() {
        super("Event conflicts with existing event");
    }
}
