package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

public class WeekSchedule implements Iterable<DaySchedule> {
    
    private ArrayList<DaySchedule> weekSchedule;

    /**
     * @param minGap the gap between events
     * EFFECTS: creates a new WeekSchedule with 7 days of the week and given minimum gap
     */
    public WeekSchedule(int minGap) {
        // TODO: implement constructor
    }

    /**
     * @param minGap the gap between events
     * @param workingHoursLimit the maximum working hours per day
     * EFFECTS: creates a new WeekSchedule with 7 days of the week, given minimum gap,
     *          and given working hours limit
     */
    public WeekSchedule(int minGap, int workingHoursLimit) {
        // TODO: implement constructor
    }

    /**
     * @return the list of schedules in the week
     */
    public ArrayList<DaySchedule> getWeekSchedule() {
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
    public void addEvent(String day, FluidEvent event, Date date, int startTime, int endTime) throws EventConflictException, WorkingLimitExceededException {
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
     * @return the total working hours of the week
     */
    public double calculateTotalWorkingHours() {
        return 0; // TODO: implement method
    }

    @Override
    public Iterator<DaySchedule> iterator() {
        return null; // TODO: implement method
    }
}
