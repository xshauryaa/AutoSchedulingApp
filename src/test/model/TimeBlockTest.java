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
    public void runBefore() {
        date = new ScheduleDate(1, 1, 2021);
        rigidEvent = new RigidEvent("event", ActivityType.EDUCATION, 120, date, 1000, 1200);
        fluidEvent = new FluidEvent("event", ActivityType.ERRAND, 60, Priority.LOW, new ScheduleDate(5, 1, 2021));
        breakTime = new Break(20, 900, 920);
    }
    
    @Test
    public void testTimeBlockRigidEvent() {
        timeBlock = new TimeBlock(rigidEvent);
        assertEquals("event", timeBlock.getName());
        assertEquals(date, timeBlock.getDate());
        assertEquals(1000, timeBlock.getStartTime());
        assertEquals(1200, timeBlock.getEndTime());
        assertEquals("rigid", timeBlock.getType());
    }
    
    @Test
    public void testTimeBlockFluidEvent() {
        timeBlock = new TimeBlock(fluidEvent, date, 1000, 1200);
        assertEquals("event", timeBlock.getName());
        assertEquals(date, timeBlock.getDate());
        assertEquals(1000, timeBlock.getStartTime());
        assertEquals(1200, timeBlock.getEndTime());
        assertEquals("fluid", timeBlock.getType());
    }
    
    @Test
    public void testTimeBlockBreak() {
        timeBlock = new TimeBlock(breakTime, date);
        assertEquals("Break", timeBlock.getName());
        assertEquals(date, timeBlock.getDate());
        assertEquals(900, timeBlock.getStartTime());
        assertEquals(920, timeBlock.getEndTime());
        assertEquals("break", timeBlock.getType());
    }
}
