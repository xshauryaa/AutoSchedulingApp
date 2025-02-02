package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TimeBlockTest {
    
    private TimeBlock timeBlock;
    private RigidEvent rigidEvent;
    private FluidEvent fluidEvent;
    private Break breakTime;
    private Date date;
    
    @BeforeEach
    public void runBefore() {
        date = new Date(01012021);
        rigidEvent = new RigidEvent("event", date, ActivityType.EDUCATION, Priority.HIGH, 120, 1000, 1200);
        fluidEvent = new FluidEvent("event", date, ActivityType.ERRAND, Priority.LOW, 60, new Date(05012021));
        breakTime = new Break(20, 900, 920);
    }
    
    @Test
    public void testTimeBlockRigidEvent() {
        timeBlock = new TimeBlock(rigidEvent);
        assertEquals(date, timeBlock.getDate());
        assertEquals(1000, timeBlock.getStartTime());
        assertEquals(1200, timeBlock.getEndTime());
        assertEquals("rigid", timeBlock.getType());
    }
    
    @Test
    public void testTimeBlockFluidEvent() {
        timeBlock = new TimeBlock(fluidEvent, 1000, 1200);
        assertEquals(date, timeBlock.getDate());
        assertEquals(1000, timeBlock.getStartTime());
        assertEquals(1200, timeBlock.getEndTime());
        assertEquals("fluid", timeBlock.getType());
    }
    
    @Test
    public void testTimeBlockBreak() {
        timeBlock = new TimeBlock(breakTime, date);
        assertEquals(date, timeBlock.getDate());
        assertEquals(900, timeBlock.getStartTime());
        assertEquals(920, timeBlock.getEndTime());
        assertEquals("break", timeBlock.getType());
    }
}
