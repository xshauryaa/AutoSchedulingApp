package model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class EventDependenciesTest {
    
    private EventDependencies eventDependencies;
    private Event event1;
    private Event event2;
    private Event event3;
    private Event event4;

    @BeforeEach
    void runBefore() {
        eventDependencies = new EventDependencies();
        event1 = new RigidEvent("CPSC 221", ActivityType.EDUCATION, 60, new ScheduleDate(5, 3, 2025), 1600, 1700);
        event2 = new FluidEvent("Pre-reading", ActivityType.EDUCATION, 30, Priority.MEDIUM, new ScheduleDate(4, 3, 2025));
        event3 = new FluidEvent("Practice Q's", ActivityType.EDUCATION, 120, Priority.HIGH, new ScheduleDate(8, 3, 2025));
        event4 = new RigidEvent("Examlet 6", ActivityType.EDUCATION, 60, new ScheduleDate(8, 3, 2025), 1500, 1600);
    }

    @Test
    void testAddDependency() {
        eventDependencies.addDependency(event1, event2);
        eventDependencies.addDependency(event4, event1);
        eventDependencies.addDependency(event4, event2);
        eventDependencies.addDependency(event4, event3);
        ArrayList<Event> dependenciesForE1 = eventDependencies.getDependenciesForEvent(event1);
        ArrayList<Event> dependenciesForE4 = eventDependencies.getDependenciesForEvent(event4);
        assertEquals(event2, dependenciesForE1.get(0));
        assertEquals(event1, dependenciesForE4.get(0));
        assertEquals(event2, dependenciesForE4.get(1));
        assertEquals(event3, dependenciesForE4.get(2));
    }

    @Test
    void testRemoveDependency() {
        eventDependencies.addDependency(event1, event2);
        eventDependencies.addDependency(event4, event1);
        eventDependencies.addDependency(event4, event2);
        eventDependencies.addDependency(event4, event3);
        eventDependencies.removeDependency(event4, event2); // Remove existing dependency
        eventDependencies.removeDependency(event1, event4); // Remove non-existing dependency
        ArrayList<Event> dependenciesForE1 = eventDependencies.getDependenciesForEvent(event1);
        ArrayList<Event> dependenciesForE4 = eventDependencies.getDependenciesForEvent(event4);
        assertEquals(event2, dependenciesForE1.get(0));
        assertEquals(event1, dependenciesForE4.get(0));
        assertEquals(event3, dependenciesForE4.get(1));
    }
}
