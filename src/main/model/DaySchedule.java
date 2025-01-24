package model;

import java.util.ArrayList;
import java.util.Date;

public class DaySchedule {
    
    private String day; // day of the week
    private Date date; // date of the day
    private ArrayList<Event> events; // list of events
    private ArrayList<Break> breaks; // list of breaks
    private int workingHoursLimit; // maximum working hours per day

    /** 
     * REQUIRES: day is one of "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
     * @param day the day of the week
     * @param date the date of the day
     * EFFECTS: creates a new DaySchedule with given day and date and no events or breaks
     * Default working hours limit is 12 hours
     */
    public DaySchedule(String day, Date date) {
        // TODO: implement the constructor
    }

    /**
     * REQUIRES: day is one of "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
     * @param day the day of the week
     * @param date the date of the day
     * @param workingHoursLimit the maximum working hours per day
     * EFFECTS: creates a new DaySchedule with given day, date, and working hours limit and no events or breaks
     */
    public DaySchedule(String day, Date date, int workingHoursLimit) {
        // TODO: implement the constructor
    }

    /**
     * @return the day of the week
     */
    public String getDay() {
        return ""; // TODO: implement method
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
     * @return the maximum working hours per day
     */
    public int getWorkingHoursLimit() {
        return 0; // TODO: implement method
    }

    /**
     * sets the maximum working hours per day
     */
    public void setWorkingHoursLimit(int limit) {
        // TODO: implement method
    }

    /**
     * adds an event to the day schedule
     * REQUIRES: the event does not overlap with any other event or break
     * MODIFIES: this
     * EFFECTS: adds the event to the list of events
     */
    public void addEvent(Event event) {
        // TODO: implement method
    }

    /**
     * adds a break to the day schedule
     * REQUIRES: the break does not overlap with any other event or break
     * MODIFIES: this
     * EFFECTS: adds the break to the list of breaks
     */
    public void addBreak(Break breakTime) {
        // TODO: implement method
    }

    /**
     * removes an event from the day schedule
     * MODIFIES: this
     * EFFECTS: removes the event from the list of events
     */
    public void removeEvent(Event event) {
        // TODO: implement method
    }

    /**
     * removes a break from the day schedule
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
}
