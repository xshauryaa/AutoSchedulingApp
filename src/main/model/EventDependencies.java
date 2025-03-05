package model;

import java.util.ArrayList;
import java.util.HashMap;

public class EventDependencies {
    
    private HashMap<Event, ArrayList<Event>> dependencies;

    /**
     * EFFECTS: creates a new EventDependencies object.
     */
    public EventDependencies() {
        dependencies = new HashMap<Event, ArrayList<Event>>();
    }

    /**
     * @param event the event for which to add dependency
     * @param dependency the dependency to be added
     * MODIFIES: this
     * EFFECTS: adds given dependency to given event
     */
    public void addDependency(Event event, Event dependency) {
        if (dependencies.containsKey(event)) {
            dependencies.get(event).add(dependency);
        } else {
            ArrayList<Event> eventDependencies = new ArrayList<Event>();
            eventDependencies.add(dependency);
            dependencies.put(event, eventDependencies);
        }
    }

    /**
     * REQUIRES: dependencies.containsKey(event) == true
     * @param event the event for which to remove dependency
     * @param dependency the dependency to be removed
     * MODIFIES: this
     * EFFECTS: removes given dependency from given event
     */
    public void removeDependency(Event event, Event dependency) {
        if (dependencies.get(event).contains(dependency)) {
            dependencies.get(event).remove(dependency);
        }
    }

    /**
     * REQUIRES: dependencies.containsKey(event) == true
     * @param event the event for which to get dependencies
     * @return list of the events that the given event is dependent on
     */
    public ArrayList<Event> getDependenciesForEvent(Event event) {
        return dependencies.get(event);
    }
}
