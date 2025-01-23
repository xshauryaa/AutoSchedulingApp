package model;

import java.util.Date;

public class FluidEvent extends Event {
    
    private Date deadline; // The deadline of the event

    /**
     * Creates a new event on given date with given name, activity type, priority, duration, and deadline.
     * @param name the name of the event
     * @param date the date of the event
     * @param type the type of activity of the event
     * @param priority the priority of the event
     * @param duration the duration of the event in minutes
     * @param deadline the deadline of the event
     */
    public FluidEvent(String name, Date date, ActivityType type, Priority priority, int duration, Date deadline) {
        super(name, date, type, priority, duration);
        this.deadline = deadline;
    }

    /**
     * @return the deadline of the event.
     */
    public Date getDeadline() {
        return this.deadline;
    }

    /**
     * @param deadline the new deadline of the event
     */
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
