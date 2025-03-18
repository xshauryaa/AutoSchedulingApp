package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

import org.junit.jupiter.api.BeforeEach;

public class DayScheduleTest {
    
    private DaySchedule daySchedule;
    private RigidEvent rigidEvent;
    private FlexibleEvent fluidEvent;
    private Break breakTime;
    private ScheduleDate date;
    private ScheduleDate deadline;

    @BeforeEach
    void runBefore() {
        date = new ScheduleDate(1, 1, 2025);
        deadline = new ScheduleDate(5, 1, 2025);
        daySchedule = new DaySchedule("Monday", date, 30, 6);
        rigidEvent = new RigidEvent("Meeting", ActivityType.MEETING, 60, date, 1200, 1300);
        fluidEvent = new FlexibleEvent("Study", ActivityType.EDUCATION, 120, Priority.HIGH, deadline);
        breakTime = new Break(30, 1500, 1530);
    }

    @Test
    void testConstructor() {
        assertEquals("Monday", daySchedule.getDay());
        assertEquals(date, daySchedule.getDate());
        assertEquals(30, daySchedule.getMinGap());
        assertEquals(0, daySchedule.getEvents().size());
        assertEquals(0, daySchedule.getBreaks().size());
        assertEquals(0, daySchedule.getTimeBlocks().size());
        assertEquals(6, daySchedule.getWorkingHoursLimit());
    }

    @Test
    void testAddValidRigidEvent() {
        try {
            daySchedule.addEvent(rigidEvent);
        } catch (EventConflictException e) {
            fail();
        } catch (WorkingLimitExceededException e) {
            fail();
        }
        assertEquals(1, daySchedule.getEvents().size());
        assertEquals(1, daySchedule.getTimeBlocks().size());
        assertEquals(0, daySchedule.getBreaks().size());
        assertEquals(rigidEvent, daySchedule.getEvents().get(0));
        assertEquals(new TimeBlock(rigidEvent), daySchedule.getTimeBlocks().get(0));
    }

    @Test
    void testAddValidFluidEvent() {
        try {
            daySchedule.addEvent(fluidEvent, 1200, 1400);
        } catch (EventConflictException e) {
            fail();
        } catch (WorkingLimitExceededException e) {
            fail();
        }
        assertEquals(1, daySchedule.getEvents().size());
        assertEquals(1, daySchedule.getTimeBlocks().size());
        assertEquals(0, daySchedule.getBreaks().size());
        assertEquals(fluidEvent, daySchedule.getEvents().get(0));
        assertEquals(new TimeBlock(fluidEvent, date, 1200, 1400), daySchedule.getTimeBlocks().get(0));
    }

    @Test
    void testAddBreak() {
        daySchedule.addBreak(breakTime);
        assertEquals(0, daySchedule.getEvents().size());
        assertEquals(1, daySchedule.getBreaks().size());
        assertEquals(1, daySchedule.getTimeBlocks().size());
        assertEquals(breakTime, daySchedule.getBreaks().get(0));
        assertEquals(new TimeBlock(breakTime, date), daySchedule.getTimeBlocks().get(0));
    }

    @Test
    void testAddRigidEventConflict() {
        try {
            daySchedule.addEvent(rigidEvent);
            daySchedule.addEvent(rigidEvent);
            fail();
        } catch (EventConflictException e) {
            // expected
        } catch (WorkingLimitExceededException e) {
            fail();
        }
        assertEquals(1, daySchedule.getEvents().size());
        assertEquals(1, daySchedule.getTimeBlocks().size());
        assertEquals(0, daySchedule.getBreaks().size());
        assertEquals(rigidEvent, daySchedule.getEvents().get(0));
        assertEquals(new TimeBlock(rigidEvent), daySchedule.getTimeBlocks().get(0));
    }

    @Test
    void testAddFluidEventConflict() {
        try {
            daySchedule.addEvent(rigidEvent);
            daySchedule.addEvent(fluidEvent, 1200, 1400);
            fail();
        } catch (EventConflictException e) {
            // expected
        } catch (WorkingLimitExceededException e) {
            fail();
        }
        assertEquals(1, daySchedule.getEvents().size());
        assertEquals(1, daySchedule.getTimeBlocks().size());
        assertEquals(0, daySchedule.getBreaks().size());
        assertEquals(rigidEvent, daySchedule.getEvents().get(0));
        assertEquals(new TimeBlock(rigidEvent), daySchedule.getTimeBlocks().get(0));
    }

    @Test
    void testAddEventWorkingLimitExceeded() {
        FlexibleEvent fluidEvent2 = new FlexibleEvent("Study", ActivityType.EDUCATION, 240, Priority.HIGH, deadline);
        try {
            daySchedule.addEvent(rigidEvent);
            daySchedule.addEvent(fluidEvent, 1400, 1600);
            daySchedule.addEvent(fluidEvent2, 1700, 1900);
            fail();
        } catch (EventConflictException e) {
            fail();
        } catch (WorkingLimitExceededException e) {
            // expected
        }
        assertEquals(2, daySchedule.getEvents().size());
        assertEquals(2, daySchedule.getTimeBlocks().size());
        assertEquals(0, daySchedule.getBreaks().size());
        assertEquals(rigidEvent, daySchedule.getEvents().get(0));
        assertEquals(fluidEvent, daySchedule.getEvents().get(1));
        assertEquals(new TimeBlock(rigidEvent), daySchedule.getTimeBlocks().get(0));
        assertEquals(new TimeBlock(fluidEvent, date, 1400, 1600), daySchedule.getTimeBlocks().get(1));
    }

    @Test
    void testRemoveEvent() {
        try {
            daySchedule.addEvent(rigidEvent);
            daySchedule.removeEvent(rigidEvent);
        } catch (EventConflictException e) {
            fail();
        } catch (WorkingLimitExceededException e) {
            fail();
        }
        assertEquals(0, daySchedule.getEvents().size());
        assertEquals(0, daySchedule.getTimeBlocks().size());
        assertEquals(0, daySchedule.getBreaks().size());
    }

    @Test
    void testRemoveBreak() {
        daySchedule.addBreak(breakTime);
        daySchedule.removeBreak(breakTime);
        assertEquals(0, daySchedule.getEvents().size());
        assertEquals(0, daySchedule.getTimeBlocks().size());
        assertEquals(0, daySchedule.getBreaks().size());
    }

    @Test
    void testCalculateWorkingHours() {
        try {
            daySchedule.addEvent(rigidEvent);
            daySchedule.addEvent(fluidEvent, 1400, 1600);
        } catch (EventConflictException e) {
            fail();
        } catch (WorkingLimitExceededException e) {
            fail();
        }
        assertEquals(3, daySchedule.calculateWorkingHours());
    }

    @Test
    void testIterator() {
        try {
            daySchedule.addEvent(rigidEvent);
            daySchedule.addEvent(fluidEvent, 1400, 1600);
        } catch (EventConflictException e) {
            fail();
        } catch (WorkingLimitExceededException e) {
            fail();
        }
        int count = 0;
        for (TimeBlock tb : daySchedule) {
            count++;
        }
        assertEquals(2, count);
    }
}
