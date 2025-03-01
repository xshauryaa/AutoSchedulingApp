package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TimeBlockTest {
    
    private TimeBlock timeBlock;
    private RigidEvent rigidEvent;
    private FluidEvent fluidEvent;
    private Break breakTime;
    private ScheduleDate date;
    
    @BeforeEach
    void runBefore() {
        date = new ScheduleDate(1, 1, 2021);
        rigidEvent = new RigidEvent("event", ActivityType.EDUCATION, 120, date, 1000, 1200);
        fluidEvent = new FluidEvent("event", ActivityType.ERRAND, 60, Priority.LOW, new ScheduleDate(5, 1, 2021));
        breakTime = new Break(20, 900, 920);
    }
    
    @Test
    void testTimeBlockRigidEvent() {
        timeBlock = new TimeBlock(rigidEvent);
        assertEquals("event", timeBlock.getName());
        assertEquals(date, timeBlock.getDate());
        assertEquals(1000, timeBlock.getStartTime());
        assertEquals(1200, timeBlock.getEndTime());
        assertEquals(120, timeBlock.getDuration());
        assertEquals("rigid", timeBlock.getType());
    }
    
    @Test
    void testTimeBlockFluidEvent() {
        timeBlock = new TimeBlock(fluidEvent, date, 1000, 1100);
        assertEquals("event", timeBlock.getName());
        assertEquals(date, timeBlock.getDate());
        assertEquals(1000, timeBlock.getStartTime());
        assertEquals(1100, timeBlock.getEndTime());
        assertEquals(60, timeBlock.getDuration());
        assertEquals("fluid", timeBlock.getType());
    }
    
    @Test
    void testTimeBlockBreak() {
        timeBlock = new TimeBlock(breakTime, date);
        assertEquals("Break", timeBlock.getName());
        assertEquals(date, timeBlock.getDate());
        assertEquals(900, timeBlock.getStartTime());
        assertEquals(920, timeBlock.getEndTime());
        assertEquals(20, timeBlock.getDuration());
        assertEquals("break", timeBlock.getType());
    }
}
