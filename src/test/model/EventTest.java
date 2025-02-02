package model;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class EventTest {
    
    private RigidEvent testRigidEvent;
    private FluidEvent testFluidEvent;

    @BeforeEach
    void runBefore() {
        testRigidEvent = new RigidEvent("Test Rigid Event", null, ActivityType.EVENT, Priority.HIGH, 60, 900, 1000);
        testFluidEvent = new FluidEvent("Test Fluid Event", null, ActivityType.EVENT, Priority.HIGH, 60, new Date(0));
    }

    @Test
    void testRigidEventConstructor() {
        assertEquals("Test Rigid Event", testRigidEvent.getName());
        assertEquals(null, testRigidEvent.getDate());
        assertEquals(ActivityType.EVENT, testRigidEvent.getType());
        assertEquals(Priority.HIGH, testRigidEvent.getPriority());
        assertEquals(60, testRigidEvent.getDuration());
        assertEquals(900, testRigidEvent.getStartTime());
        assertEquals(1000, testRigidEvent.getEndTime());
    }

    @Test
    void testFluidEventConstructor() {
        assertEquals("Test Fluid Event", testFluidEvent.getName());
        assertEquals(null, testFluidEvent.getDate());
        assertEquals(ActivityType.EVENT, testFluidEvent.getType());
        assertEquals(Priority.HIGH, testFluidEvent.getPriority());
        assertEquals(60, testFluidEvent.getDuration());
        assertEquals(new Date(0), testFluidEvent.getDeadline());
    }
}
