package model.exceptions;

public class WorkingLimitExceededException extends Exception {
    public WorkingLimitExceededException() {
        super("Working hours limit exceeded");
    }
}
