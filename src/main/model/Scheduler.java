package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

public class Scheduler {

    ScheduleDate firstDate;
    String firstDay;
    int minGap;
    int workingHoursLimit;

    protected ArrayList<Entry<String, Break>> breaks;
    protected ArrayList<Break> repeatedBreaks;
    protected ArrayList<RigidEvent> rigidEvents;
    protected ArrayList<FlexibleEvent> flexibleEvents;
    protected EventDependencies eventDependencies;

    /**
     * @param date the date of the first day of the week to be scheduled
     * @param day1 the day of the week corresponding to the date
     * @param minGap the minimum gap between events
     * @param workingHourLimit the maximum working hours per day
     * EFFECTS: constructs a new Scheduler object with empry lists of breaks,
     *          rigid events, and flexible events
     */
    public Scheduler(ScheduleDate date, String day1, int minGap, int workingHoursLimit) {
        this.firstDate = date;
        this.firstDay = day1;
        this.minGap = minGap;
        this.workingHoursLimit = workingHoursLimit;

        this.breaks = new ArrayList<Entry<String, Break>>();
        this.repeatedBreaks = new ArrayList<Break>();
        this.rigidEvents = new ArrayList<RigidEvent>();
        this.flexibleEvents = new ArrayList<FlexibleEvent>();
        this.eventDependencies = null;
    }

    /**
     * @param day the day of the week to which the break is to be added
     * @param breakTime the break to be added
     * MODIFIES: this
     * EFFECTS: adds the given break to the list of breaks
     */
    public void addBreak(String day, Break breakTime) {
        Entry<String, Break> entry = Map.entry(day, breakTime);
        this.breaks.add(entry);
    }

    /**
     * @param breakTime the break to be added to the entire week
     * MODIFIES: this
     * EFFECTS: adds the given break to the list of repeated breaks
     */
    public void addRepeatedBreak(Break breakTime) {
        this.repeatedBreaks.add(breakTime);
    }

    /**
     * @param event the rigid event to be added
     * MODIFIES: this
     * EFFECTS: adds the given rigid event to the list of rigid events
     */
    public void addEvent(RigidEvent event) {
        this.rigidEvents.add(event);
    }

    /**
     * @param event the flexible event to be added
     * MODIFIES: this
     * EFFECTS: adds the given flexible event to the list of flexible events
     */
    public void addEvent(FlexibleEvent event) {
        this.flexibleEvents.add(event);
    }

    /**
     * @param dependencies the event dependencies mapping to be followed 
     *                     by the scheduler
     * MODIFIES: this
     * EFFECTS: sets the event dependencies mapping to the given mapping
     */
    public void setEventDependencies(EventDependencies dependencies) {
        this.eventDependencies = dependencies;
    }
    
    /**
     * @return the earliest fit schedule
     */
    // public WeekSchedule getSchedule() {
    //     return this.schedule;
    // }

    /**
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param earliestStartTime the earliest time at which the schedule can start
     * @param latestEndTime the latest time at which the schedule can end
     * MODIFIES: this
     * EFFECTS: creates the schedules for the week based on the given constraints
     */
    public WeekSchedule createSchedules(String strategy, int earliestStartTime, int latestEndTime) {
        if (strategy.equals("Earliest Fit")) {
            EarliestFitStrategy earliestFitStrategy = new EarliestFitStrategy(this, firstDate, firstDay, minGap, workingHoursLimit);
            WeekSchedule schedule = earliestFitStrategy.generateSchedule(earliestStartTime, latestEndTime);
            return schedule;
        }

        return null;
    }
}
