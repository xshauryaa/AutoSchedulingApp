package model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class EventTest {
    
    private RigidEvent testRigidEvent;
    private FlexibleEvent testFluidEvent;

    @BeforeEach
    void runBefore() {
        testRigidEvent = new RigidEvent("Test Rigid Event", ActivityType.EVENT, 60, null, 900, 1000);
        testFluidEvent = new FlexibleEvent("Test Fluid Event", ActivityType.EVENT, 30, Priority.HIGH, new ScheduleDate(1, 1, 2025));
    }

    @Test
    void testRigidEventConstructor() {
        assertEquals("Test Rigid Event", testRigidEvent.getName());
        assertEquals(null, testRigidEvent.getDate());
        assertEquals(ActivityType.EVENT, testRigidEvent.getType());
        assertEquals(60, testRigidEvent.getDuration());
        assertEquals(new Time24(900), testRigidEvent.getStartTime());
        assertEquals(new Time24(1000), testRigidEvent.getEndTime());
    }

    @Test
    void testFluidEventConstructor() {
        assertEquals("Test Fluid Event", testFluidEvent.getName());
        assertEquals(ActivityType.EVENT, testFluidEvent.getType());
        assertEquals(Priority.HIGH, testFluidEvent.getPriority());
        assertEquals(30, testFluidEvent.getDuration());
        assertEquals(new ScheduleDate(1, 1, 2025), testFluidEvent.getDeadline());
    }
}
