package model;

import java.util.Date;

/**
 * Represents an event or task to be scheduled in the calendar.
 */
public abstract class Event {
    
    private Date date; // The date of the event
    private Priority priority; // The priority of the event
    private ActivityType type; // The type of activity of the event or task
    private int duration; // The duration of the event in minutes

    /**
     * Creates a new event on given date with given name, activity type, priority, and duration.
     * @param name the name of the event
     * @param date the date of the event
     * @param type the type of activity of the event
     * @param priority the priority of the event
     * @param duration the duration of the event in minutes
     */
    public Event(String name, Date date, ActivityType type, Priority priority, int duration) {
        // TODO: Implement constructor
    }

    /**
     * @return the name of the event.
     */
    public String getName() {
        return ""; // TODO: Implement method
    }

    /**
     * @return the date of the event.
     */
    public Date getDate() {
        return null; // TODO: Implement method
    }

    /**
     * @return the priority of the event.
     */
    public Priority getPriority() {
        return null; // TODO: Implement method
    }

    /**
     * @return the type of activity of the event.
     */
    public ActivityType getType() {
        return null; // TODO: Implement method
    }

    /**
     * @return the duration of the event in minutes.
     */
    public int getDuration() {
        return 0; // TODO: Implement method
    }

    /**
     * @return a string representation of the event.
     */
    @Override
    public String toString() {
        return ""; // TODO: Implement method
    }

    /**
     * @return true if the event is equal to the given object, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return false; // TODO: Implement method
    }

    /**
     * @return the hash code of the event.
     */
    @Override
    public int hashCode() {
        return 0; // TODO: Implement method
    }
}
