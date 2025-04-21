package model;

/**
 * Represents a rigid event that has a fixed date and time of occurrence.
 */
public class RigidEvent extends Event {
    
    private ScheduleDate date; // The date of the event
    private Time24 startTime; // The start time of the event in 24-hour format
    private Time24 endTime; // The end time of the event in 24-hour format

    /**
     * @param name the name of the event
     * @param type the type of activity of the event
     * @param duration the duration of the event in minutes
     * @param date the date of the event
     * @param startTime the start time of the event in 24-hour format
     * @param endTime the end time of the event in 24-hour format
     * EFFECTS: Creates a new event on given date with given name, activity type, priority, duration, start time, and end time.
     */
    public RigidEvent(String name, ActivityType type, int duration, ScheduleDate date, int startTime, int endTime) {
        super(name, type, duration);
        this.date = date;
        this.startTime = new Time24(startTime);
        this.endTime = new Time24(endTime);
    }

    /**
     * @return the date of the event.
     */
    public ScheduleDate getDate() {
        return this.date;
    }

    /**
     * @return the start time of the event in 24-hour format.
     */
    public Time24 getStartTime() {
        return this.startTime;
    }

    /**
     * @return the end time of the event in 24-hour format.
     */
    public Time24 getEndTime() {
        return this.endTime;
    }
}
