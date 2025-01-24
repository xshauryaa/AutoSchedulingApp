package model.exceptions;

public class CircularDependencyException extends Exception {

    public CircularDependencyException(String event1, String event2) {
        super("Circular dependency detected: " + event1 + " already requires " + 
                event2 + " as a dependency, and cannot be added as a dependency for " + 
                event2 + ".");
    }
}
