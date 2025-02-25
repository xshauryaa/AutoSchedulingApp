package model;

import java.util.Date;
import java.util.HashMap;

import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

public class WeekSchedule {
    
    private HashMap<String, DaySchedule> weekSchedule;

    /**
     * @param minGap the gap between events
     * @param day1Date the date of the first day in this week's schedule
     * @param day1Day the day of the week on which the schedule starts
     * EFFECTS: creates a new WeekSchedule with 7 days of the week and given minimum gap
     */
    public WeekSchedule(int minGap, ScheduleDate day1Date, String day1Day) {
        // TODO: implement constructor
    }

    /**
     * @param minGap the gap between events
     * @param day1Date the date of the first day in this week's schedule
     * @param day1Day the day of the week on which the schedule starts
     * @param workingHoursLimit the maximum working hours per day
     * EFFECTS: creates a new WeekSchedule with 7 days of the week, given minimum gap,
     *          and given working hours limit
     */
    public WeekSchedule(int minGap, ScheduleDate day1Date, String day1Day, int workingHoursLimit) {
        // TODO: implement constructor
    }

    /**
     * @return the list of schedules in the week
     */
    public HashMap<String, DaySchedule> getWeekSchedule() {
        return null; // TODO: implement method
    }

    /**
     * @param day the day for the schedule is to be retrieved
     * @return the schedule for the specified day in the week
     */
    public DaySchedule getScheduleForDay(String day) {
        return null; // TODO: implement method
    }

    /**
     * @param day the day of the week on which to add the event
     * @param event the event to add
     * MODIFIES: this
     * EFFECTS: adds the event to the schedule of the given day
     * @throws EventConflictException if the event conflicts with another event
     * @throws WorkingLimitExceededException if the working hours limit is exceeded
     */
    public void addEvent(String day, RigidEvent event) throws EventConflictException, WorkingLimitExceededException {
        // TODO: implement method
    }

    /**
     * @param day the day of the week on which to add the event
     * @param event the event to add
     * @param date the date of the event
     * @param startTime the start time of the event
     * @param endTime the end time of the event
     * MODIFIES: this
     * EFFECTS: adds the event to the schedule of the given day
     * @throws EventConflictException if the event conflicts with another event
     * @throws WorkingLimitExceededException if the working hours limit is exceeded
     */
    public void addEvent(String day, FluidEvent event, ScheduleDate date, int startTime, int endTime) throws EventConflictException, WorkingLimitExceededException {
        // TODO: implement method
    }

    /**
     * @param breakTime the break to add
     * MODIFIES: this
     * EFFECTS: adds the break to every day in the week
     */
    public void addBreakToFullWeek(Break breakTime) {
        // TODO: implement method
    }

    /**
     * @param day the day of the week on which to add the break
     * @param breakTime the break to add
     * MODIFIES: this
     * EFFECTS: adds the break to the schedule of the given day
     */
    public void addBreak(String day, Break breakTime) {
        // TODO: implement method
    }

    /**
     * @param day the day of the week on which to remove the event
     * @param event the event to remove
     * MODIFIES: this
     * EFFECTS: removes the event from the schedule of the given day
     */
    public void removeEvent(String day, Event event) {
        // TODO: implement method
    }

    /**
     * @param day the day of the week on which to remove the break
     * @param breakTime the break to remove
     * MODIFIES: this
     * EFFECTS: removes the break from the schedule of the given day
     */
    public void removeBreak(String day, Break breakTime) {
        // TODO: implement method
    }

    /**
     * @return the total working hours of the week
     */
    public double calculateTotalOccupiedHours() {
        return 0; // TODO: implement method
    }
}
