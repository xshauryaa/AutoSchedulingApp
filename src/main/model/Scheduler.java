package model;

import java.util.ArrayList;

public class Scheduler {
    
    private WeekSchedule earliestFitSchedule;
    private WeekSchedule deadlineOrientedSchedule;
    private WeekSchedule balancedWorkloadSchedule;

    private ArrayList<Break> breaks;
    private ArrayList<Break> repeatedBreaks;
    private ArrayList<RigidEvent> rigidEvents;
    private ArrayList<FlexibleEvent> flexibleEvents;
    private EventDependencies eventDependencies;

    /**
     * @param date the date of the first day of the week to be scheduled
     * @param day1 the day of the week corresponding to the date
     * @param minGap the minimum gap between events
     * @param workingHourLimit the maximum working hours per day
     * EFFECTS: constructs a new Scheduler object with empry lists of breaks,
     *          rigid events, and flexible events
     */
    public Scheduler(ScheduleDate date, String day1, int minGap, int workingHoursLimit) {
        // TODO: implement constructor
    }

    /**
     * @param breakTime the break to be added
     * MODIFIES: this
     * EFFECTS: adds the given break to the list of breaks
     */
    public void addBreak(Break breakTime) {
        // TODO: implement method
    }

    /**
     * @param breakTime the break to be added to the entire week
     * MODIFIES: this
     * EFFECTS: adds the given break to the list of repeated breaks
     */
    public void addRepeatedBreak(Break breakTime) {
        // TODO: implement method
    }

    /**
     * @param event the rigid event to be added
     * MODIFIES: this
     * EFFECTS: adds the given rigid event to the list of rigid events
     */
    public void addRigidEvent(RigidEvent event) {
        // TODO: implement method
    }

    /**
     * @param event the flexible event to be added
     * MODIFIES: this
     * EFFECTS: adds the given flexible event to the list of flexible events
     */
    public void addFlexibleEvent(FlexibleEvent event) {
        // TODO: implement method
    }

    /**
     * @param dependencies the event dependencies mapping to be followed 
     *                     by the scheduler
     * MODIFIES: this
     * EFFECTS: sets the event dependencies mapping to the given mapping
     */
    public void setEventDependencies(EventDependencies dependencies) {
        // TODO: implement method
    }
    
    /**
     * @return the earliest fit schedule
     */
    public WeekSchedule getEarliestFitSchedule() {
        return null; // TODO: implement method
    }

    /**
     * @return the deadline-oriented schedule
     */
    public WeekSchedule getDeadlineOrientedSchedule() {
        return null; // TODO: implement method
    }

    /**
     * @return the balanced workload schedule
     */
    public WeekSchedule getBalancedWorkloadSchedule() {
        return null; // TODO: implement method
    }

    /**
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param earliestStartTime the earliest time at which the schedule can start
     * @param latestEndTime the latest time at which the schedule can end
     * MODIFIES: this
     * EFFECTS: creates the schedules for the week based on the given constraints
     */
    public void createSchedules(int earliestStartTime, int latestEndTime) {
        // TODO: implement method
    }

    /**
     * MODIFIES: this
     * EFFECTS: schedules all the given breaks into all three week schedules
     */
    private void scheduleBreaks() {
        // TODO: implement helper method
    }

    /**
     * MODIFIES: this
     * EFFECTS: schedules all the given rigid events into all three week schedules
     */
    private void scheduleRigidEvents() {
        // TODO: implement helper method
    }

    /**
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param earliestStartTime the earliest time at which the schedule can start
     * @param latestEndTime the latest time at which the schedule can end
     * MODIFIES: this
     * EFFECTS: schedules all the given flexible events at the earliest fit
     *          into schedule #1
     */
    private void scheduleEarliestFitFlexibleEvents(int earliestStartTime, int latestEndTime) {
        // TODO: implement helper method
    }

    /**
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param earliestStartTime the earliest time at which the schedule can start
     * @param latestEndTime the latest time at which the schedule can end
     * MODIFIES: this
     * EFFECTS: schedules all the given flexible events in a deadline-oriented
     *          manner into schedule #2
     */
    private void scheduleDeadlineOrientedFlexibleEvents(int earliestStartTime, int latestEndTime) {
        // TODO: implement helper method
    }

    /**
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param earliestStartTime the earliest time at which the schedule can start
     * @param latestEndTime the latest time at which the schedule can end
     * MODIFIES: this
     * EFFECTS: schedules all the given flexible events in a balanced manner
     *          into schedule #3
     */
    private void scheduleBalancedWorkloadFlexibleEvents(int earliestStartTime, int latestEndTime) {
        // TODO: implement helper method
    }
}
