package model;

import java.util.Date;

public class RigidEvent extends Event {
    
    private Date date; // The date of the event
    private int startTime; // The start time of the event in 24-hour format
    private int endTime; // The end time of the event in 24-hour format

    /**
     * Creates a new event on given date with given name, activity type, priority, duration, start time, and end time.
     * @param name the name of the event
     * @param type the type of activity of the event
     * @param duration the duration of the event in minutes
     * @param date the date of the event
     * @param startTime the start time of the event in 24-hour format
     * @param endTime the end time of the event in 24-hour format
     */
    public RigidEvent(String name, ActivityType type, int duration, Date date, int startTime, int endTime) {
        super(name, type, duration);
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * @return the date of the event.
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * @return the start time of the event in 24-hour format.
     */
    public int getStartTime() {
        return this.startTime;
    }

    /**
     * @return the end time of the event in 24-hour format.
     */
    public int getEndTime() {
        return this.endTime;
    }
}
