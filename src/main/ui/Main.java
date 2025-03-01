package ui;

import static org.junit.jupiter.api.Assertions.fail;

import model.ActivityType;
import model.Break;
import model.FluidEvent;
import model.Priority;
import model.RigidEvent;
import model.ScheduleDate;
import model.WeekSchedule;
import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

public class Main {
    public static void main(String[] args) throws Exception {
        RigidEvent rigidEvent1 = new RigidEvent("Club Meeting", ActivityType.MEETING, 60, new ScheduleDate(3, 1, 2025), 1530, 1630);
        RigidEvent rigidEvent2 = new RigidEvent("Study Session with Friend", ActivityType.EDUCATION, 120, new ScheduleDate(6, 1, 2025), 1200, 1400);
        FluidEvent fluidEvent1 = new FluidEvent("Grocery Shopping", ActivityType.ERRAND, 45, Priority.MEDIUM, new ScheduleDate(5, 1, 20205));
        FluidEvent fluidEvent2 = new FluidEvent("Doctor's Appointment", ActivityType.PERSONAL, 30, Priority.HIGH, new ScheduleDate(7, 1, 20205));
        Break breakTime = new Break(30, 1430, 1500);
        WeekSchedule weekSchedule = new WeekSchedule(30, new ScheduleDate(1, 1, 2025), "Wednesday", 12);
        weekSchedule.addBreakToFullWeek(breakTime);
        try {
            weekSchedule.addEvent("Friday", rigidEvent1);
            weekSchedule.addEvent("Monday", rigidEvent2);
            weekSchedule.addEvent("Thursday", fluidEvent1, 1800, 1845);
            weekSchedule.addEvent("Saturday", fluidEvent2, 1200, 1230);
        } catch (EventConflictException | WorkingLimitExceededException e) {
            fail();
        }
        System.out.println(weekSchedule.getScheduleForDay("Friday").getTimeBlocks().get(0).getStartTime());
        System.out.println(weekSchedule.getScheduleForDay("Friday").getTimeBlocks().get(1).getStartTime());
    }
}
