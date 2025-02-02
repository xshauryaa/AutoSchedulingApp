package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

public class DaySchedule implements Iterable<TimeBlock> {
    
    private String day; // day of the week
    private Date date; // date of the day
    private ArrayList<Event> events; // list of events
    private ArrayList<Break> breaks; // list of breaks
    private ArrayList<TimeBlock> timeBlocks; // list of time blocks
    private int minGap; // minimum gap between events
    private int workingHoursLimit; // maximum working hours per day

    /** 
     * REQUIRES: day is one of "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
     * @param day the day of the week
     * @param date the date of the day
     * @param minGap the gap between events
     * EFFECTS: creates a new DaySchedule with given day and date, with set gap, and no events or breaks
     * Default working hours limit is 12 hours
     */
    public DaySchedule(String day, Date date, int minGap) {
        // TODO: implement constructor
    }

    /**
     * REQUIRES: day is one of "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
     * @param day the day of the week
     * @param date the date of the day
     * @param minGap the gap between events
     * @param workingHoursLimit the maximum working hours per day
     * EFFECTS: creates a new DaySchedule with given day, date, set gap, and working hours limit and no events or breaks
     */
    public DaySchedule(String day, Date date, int minGap, int workingHoursLimit) {
        // TODO: implement constructor
    }

    /**
     * @return the day of the week
     */
    public String getDay() {
        return "";// TODO: implement method
    }

    /**
     * @return the date of the day
     */
    public Date getDate() {
        return null; // TODO: implement method
    }

    /**
     * @return the list of events
     */
    public ArrayList<Event> getEvents() {
        return null; // TODO: implement method
    }

    /**
     * @return the list of breaks
     */
    public ArrayList<Break> getBreaks() {
        return null; // TODO: implement method
    }

    /**
     * @return the list of time blocks
     */
    public ArrayList<TimeBlock> getTimeBlocks() {
        return null; // TODO: implement method
    }

    /**
     * @return the gap between events
     */
    public int getMinGap() {
        return 0; // TODO: implement method
    }

    /**
     * @return the maximum working hours per day
     */
    public int getWorkingHoursLimit() {
        return 0; // TODO: implement method
    }

    /**
     * @param limit the maximum working hours per day
     * sets the maximum working hours per day
     */
    public void setWorkingHoursLimit(int limit) {
        // TODO: implement method
    }

    /**
     * adds an event to the day schedule
     * REQUIRES: the event does not overlap with any other event or break &
     *           the total working hours of the day does not exceed the working hours limit
     * @param event the rigid event to be added
     * MODIFIES: this
     * EFFECTS: adds the event to the list of events and adds a corresponding time block to the 
     *          list of time blocks
     * @throws EventConflictException if the event overlaps with any other event or break 
     *                                or the total working hours exceed the working hours limit 
     */
    public void addEvent(RigidEvent event) throws EventConflictException, WorkingLimitExceededException {
        // TODO: implement method
    }

    /**
     * adds an event to the day schedule
     * REQUIRES: the event does not overlap with any other event or break &
     *           the total working hours of the day does not exceed the working hours limit
     * @param event the fluid event to be added
     * @param startTime the start time of the event
     * @param endTime the end time of the event
     * MODIFIES: this
     * EFFECTS: adds the event to the list of events and adds a corresponding time block to the 
     *          list of time blocks
     * @throws EventConflictException if the event overlaps with any other event or break 
     *                                or the total working hours exceed the working hours limit 
     */
    public void addEvent(FluidEvent event, int startTime, int endTime) throws EventConflictException, WorkingLimitExceededException {
        // TODO: implement method
    }

    /**
     * adds a break to the day schedule
     * REQUIRES: no events must be added when adding a break
     * @param breakTime the break to be added
     * MODIFIES: this
     * EFFECTS: adds the break to the list of breaks
     */
    public void addBreak(Break breakTime) {
        // TODO: implement method
    }

    /**
     * removes an event from the day schedule
     * @param event the event to be removed
     * MODIFIES: this
     * EFFECTS: removes the event from the list of events
     */
    public void removeEvent(Event event) {
        // TODO: implement method
    }

    /**
     * removes a break from the day schedule
     * @param breakTime the break to be removed
     * MODIFIES: this
     * EFFECTS: removes the break from the list of breaks
     */
    public void removeBreak(Break breakTime) {
        // TODO: implement method
    }

    /**
     * @return the total working hours of the day
     */
    public int calculateWorkingHours() {
        return 0; // TODO: implement method
    }

    /**
     * @param event the event to be checked for conflict
     * @return true if the event overlaps with any other event or break, false otherwise
     */
    private boolean checkEventConflict(Event event) {
        return false; // TODO: implement method
    }

    @Override
    public Iterator<TimeBlock> iterator() {
        return null; // TODO: implement method
    }
}
