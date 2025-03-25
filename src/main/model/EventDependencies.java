package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import model.exceptions.CircularDependencyException;

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
    public void addDependency(Event event, Event dependency) throws CircularDependencyException {
        // Temporarily add the dependency
        dependencies.putIfAbsent(event, new ArrayList<>());
        dependencies.get(event).add(dependency);

        // Check for cycle
        if (hasCycle()) {
            // Revert the change
            dependencies.get(event).remove(dependency);
            throw new CircularDependencyException(dependency.toString(), event.toString());
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

    /**
     * @return true if there is a cycle in the dependencies, false otherwise
     */
    private boolean hasCycle() {
        Set<Event> visited = new HashSet<>();
        Set<Event> recursionStack = new HashSet<>();

        for (Event event : dependencies.keySet()) {
            if (dfsCycleCheck(event, visited, recursionStack)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param current the current event
     * @param visited set of visited events
     * @param stack set of events in the recursion stack
     * @return true if there is a cycle in the dependencies, false otherwise
     */
    private boolean dfsCycleCheck(Event current, Set<Event> visited, Set<Event> stack) {
        if (stack.contains(current)) {
            return true;
        }

        if (visited.contains(current)) {
            return false;
        }

        visited.add(current);
        stack.add(current);

        for (Event dep : dependencies.getOrDefault(current, new ArrayList<>())) {
            if (dfsCycleCheck(dep, visited, stack)) {
                return true;
            }
        }

        stack.remove(current);
        return false;
    }
}
