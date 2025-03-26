package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

import static org.junit.jupiter.api.Assertions.*;

public class WeekScheduleTest {
    
    private WeekSchedule weekSchedule;
    private RigidEvent rigidEvent1;
    private RigidEvent rigidEvent2;
    private FlexibleEvent fluidEvent1;
    private FlexibleEvent fluidEvent2;
    private Break breakTime;

    @BeforeEach
    void runBefore() {
        weekSchedule = new WeekSchedule(30, new ScheduleDate(1, 1, 2025), "Wednesday", 12);
        rigidEvent1 = new RigidEvent("Club Meeting", ActivityType.MEETING, 60, new ScheduleDate(3, 1, 2025), 1530, 1630);
        rigidEvent2 = new RigidEvent("Study Session with Friend", ActivityType.EDUCATION, 120, new ScheduleDate(6, 1, 2025), 1200, 1400);
        fluidEvent1 = new FlexibleEvent("Grocery Shopping", ActivityType.ERRAND, 45, Priority.MEDIUM, new ScheduleDate(5, 1, 20205));
        fluidEvent2 = new FlexibleEvent("Doctor's Appointment", ActivityType.PERSONAL, 30, Priority.HIGH, new ScheduleDate(7, 1, 20205));
        breakTime = new Break(30, 1430, 1500);
    }

    @Test
    void testConstructor() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (String day : days) {
            weekSchedule = new WeekSchedule(30, new ScheduleDate(1, 1, 2025), day, 12);
            assertEquals(0, weekSchedule.getScheduleForDay("Wednesday").getEvents().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Wednesday").getBreaks().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Thursday").getEvents().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Thursday").getBreaks().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Friday").getEvents().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Friday").getBreaks().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Saturday").getEvents().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Saturday").getBreaks().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Sunday").getEvents().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Sunday").getBreaks().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Monday").getEvents().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Monday").getBreaks().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Tuesday").getEvents().size());
            assertEquals(0, weekSchedule.getScheduleForDay("Tuesday").getBreaks().size());
        }
    }

    @Test
    void testAddSingleRigidEvent() {
        try {
            weekSchedule.addEvent("Friday", rigidEvent1);
        } catch (EventConflictException | WorkingLimitExceededException e) {
            fail();
        }
        assertEquals(rigidEvent1, weekSchedule.getScheduleForDay("Friday").getEvents().get(0));
        assertEquals(new TimeBlock(rigidEvent1), weekSchedule.getScheduleForDay("Friday").getTimeBlocks().get(0));
    }

    @Test
    void testAddSingleFluidEvent() {
        try {
            weekSchedule.addEvent("Thursday", fluidEvent1, 1800, 1845);
        } catch (EventConflictException | WorkingLimitExceededException e) {
            fail();
        }
        assertEquals(fluidEvent1, weekSchedule.getScheduleForDay("Thursday").getEvents().get(0));
        assertEquals(new TimeBlock(fluidEvent1, new ScheduleDate(2, 1, 2025), 1800, 1845), weekSchedule.getScheduleForDay("Thursday").getTimeBlocks().get(0));
    }

    @Test
    void testAddBreak() {
        weekSchedule.addBreak("Monday", breakTime);
        assertEquals(breakTime, weekSchedule.getScheduleForDay("Monday").getBreaks().get(0));
        assertEquals(new TimeBlock(breakTime, new ScheduleDate(6, 1, 2025)), weekSchedule.getScheduleForDay("Monday").getTimeBlocks().get(0));
    }

    @Test
    void testAddBreakForFullWeek() {
        weekSchedule.addBreakToFullWeek(breakTime);
        assertEquals(breakTime, weekSchedule.getScheduleForDay("Wednesday").getBreaks().get(0));
        assertEquals(new TimeBlock(breakTime, new ScheduleDate(1, 1, 2025)), weekSchedule.getScheduleForDay("Wednesday").getTimeBlocks().get(0));
        assertEquals(breakTime, weekSchedule.getScheduleForDay("Thursday").getBreaks().get(0));
        assertEquals(new TimeBlock(breakTime, new ScheduleDate(2, 1, 2025)), weekSchedule.getScheduleForDay("Thursday").getTimeBlocks().get(0));
        assertEquals(breakTime, weekSchedule.getScheduleForDay("Friday").getBreaks().get(0));
        assertEquals(new TimeBlock(breakTime, new ScheduleDate(3, 1, 2025)), weekSchedule.getScheduleForDay("Friday").getTimeBlocks().get(0));
        assertEquals(breakTime, weekSchedule.getScheduleForDay("Saturday").getBreaks().get(0));
        assertEquals(new TimeBlock(breakTime, new ScheduleDate(4, 1, 2025)), weekSchedule.getScheduleForDay("Saturday").getTimeBlocks().get(0));
        assertEquals(breakTime, weekSchedule.getScheduleForDay("Sunday").getBreaks().get(0));
        assertEquals(new TimeBlock(breakTime, new ScheduleDate(5, 1, 2025)), weekSchedule.getScheduleForDay("Sunday").getTimeBlocks().get(0));
        assertEquals(breakTime, weekSchedule.getScheduleForDay("Monday").getBreaks().get(0));
        assertEquals(new TimeBlock(breakTime, new ScheduleDate(6, 1, 2025)), weekSchedule.getScheduleForDay("Monday").getTimeBlocks().get(0));
        assertEquals(breakTime, weekSchedule.getScheduleForDay("Tuesday").getBreaks().get(0));
        assertEquals(new TimeBlock(breakTime, new ScheduleDate(7, 1, 2025)), weekSchedule.getScheduleForDay("Tuesday").getTimeBlocks().get(0));
    }

    @Test
    void testAddMultipleBreaksAndEvents() {
        weekSchedule.addBreakToFullWeek(breakTime);
        try {
            weekSchedule.addEvent("Friday", rigidEvent1);
            weekSchedule.addEvent("Monday", rigidEvent2);
            weekSchedule.addEvent("Thursday", fluidEvent1, 1800, 1845);
            weekSchedule.addEvent("Saturday", fluidEvent2, 1200, 1230);
        } catch (EventConflictException | WorkingLimitExceededException e) {
            fail();
        }
        TimeBlock testBlock1 = new TimeBlock(rigidEvent1);
        TimeBlock testBlock2 = new TimeBlock(rigidEvent2);
        TimeBlock testBlock3 = new TimeBlock(fluidEvent1, new ScheduleDate(2, 1, 2025), 1800, 1845);
        TimeBlock testBlock4 = new TimeBlock(fluidEvent2, new ScheduleDate(4, 1, 2025), 1200, 1230);
        assertEquals(rigidEvent1, weekSchedule.getScheduleForDay("Friday").getEvents().get(0));
        assertEquals(testBlock1, weekSchedule.getScheduleForDay("Friday").getTimeBlocks().get(1));
        assertEquals(rigidEvent2, weekSchedule.getScheduleForDay("Monday").getEvents().get(0));
        assertEquals(testBlock2, weekSchedule.getScheduleForDay("Monday").getTimeBlocks().get(0));
        assertEquals(fluidEvent1, weekSchedule.getScheduleForDay("Thursday").getEvents().get(0));
        assertEquals(testBlock3, weekSchedule.getScheduleForDay("Thursday").getTimeBlocks().get(1));
        assertEquals(fluidEvent2, weekSchedule.getScheduleForDay("Saturday").getEvents().get(0));
        assertEquals(testBlock4, weekSchedule.getScheduleForDay("Saturday").getTimeBlocks().get(0));
    }

    @Test
    void testRemoveBreakAndEvent() {
        weekSchedule.addBreak("Monday", breakTime);
        try {
            weekSchedule.addEvent("Friday", rigidEvent1);
            weekSchedule.addEvent("Thursday", fluidEvent1, 1800, 1845);
        } catch (EventConflictException | WorkingLimitExceededException e) {
            fail();
        }
        weekSchedule.removeEvent("Friday", rigidEvent1);
        weekSchedule.removeEvent("Thursday", fluidEvent1);
        weekSchedule.removeBreak("Monday", breakTime);
        assertEquals(0, weekSchedule.getScheduleForDay("Thursday").getEvents().size());
        assertEquals(0, weekSchedule.getScheduleForDay("Thursday").getBreaks().size());
        assertEquals(0, weekSchedule.getScheduleForDay("Friday").getEvents().size());
        assertEquals(0, weekSchedule.getScheduleForDay("Friday").getBreaks().size());
    }
}
