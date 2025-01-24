package model.exceptions;

public class OutOfWorkingTimeException extends Exception {

    public OutOfWorkingTimeException() {
        super("Event is out of working time");
    }
}
