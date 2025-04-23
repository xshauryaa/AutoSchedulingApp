package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a scheduler that manages the scheduling of events and breaks.
 */
public class Scheduler {

    private ScheduleDate firstDate; // the date of the first day of the week to be scheduled
    private String firstDay; // the day of the week corresponding to the date
    private int minGap; // the minimum gap between events
    private int workingHoursLimit; // the maximum working hours per day

    protected ArrayList<Entry<String, Break>> breaks; // list of breaks mapped to their days
    protected ArrayList<Break> repeatedBreaks; // list of repeated breaks
    protected ArrayList<RigidEvent> rigidEvents; // list of rigid events
    protected ArrayList<FlexibleEvent> flexibleEvents; // list of flexible events
    protected EventDependencies eventDependencies; // the event dependencies mapping

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
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param strategy the scheduling strategy to be used
     * @param earliestStartTime the earliest time at which the schedule can start
     * @param latestEndTime the latest time at which the schedule can end
     * MODIFIES: this
     * @return the generated schedule based on the given strategy
     */
    public WeekSchedule createSchedules(String strategy, int earliestStartTime, int latestEndTime) {
        switch (strategy) {
            case "Earliest Fit":
                EarliestFitStrategy earliestFitStrategy = new EarliestFitStrategy(this, firstDate, firstDay, minGap, workingHoursLimit);
                WeekSchedule schedule = earliestFitStrategy.generateSchedule(earliestStartTime, latestEndTime);
                return schedule;

            case "Balanced Work":
                BalancedWorkStrategy balancedWorkStrategy = new BalancedWorkStrategy(this, firstDate, firstDay, minGap, workingHoursLimit);
                WeekSchedule balancedSchedule = balancedWorkStrategy.generateSchedule(earliestStartTime, latestEndTime);
                return balancedSchedule;
            case "Deadline Oriented":
                DeadlineOrientedStrategy deadlineOrientedStrategy = new DeadlineOrientedStrategy(this, firstDate, firstDay, minGap, workingHoursLimit);
                WeekSchedule deadlineSchedule = deadlineOrientedStrategy.generateSchedule(earliestStartTime, latestEndTime);
                return deadlineSchedule;
        }

        return null;
    }
}
