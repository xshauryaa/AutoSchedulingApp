package model;

import java.util.ArrayList;
import java.util.HashMap;

public class EventDependencies {
    
    private HashMap<Event, ArrayList<Event>> dependencies;

    public EventDependencies() {
        // TODO: implement constructor
    }

    public void addDependency(Event event, Event dependency) {
        // TODO: implement method
    }

    public void removeDependency(Event event, Event dependency) {
        // TODO: implement method
    }

    public ArrayList<Event> getDependenciesForEvent(Event event) {
        // TODO: implement method
        return null;
    }
}
