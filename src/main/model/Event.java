package model;

import java.util.Date;
import java.util.Objects;

/**
 * Represents an event or task to be scheduled in the calendar.
 */
public abstract class Event {

    private String name; // The name of the event
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
        this.name = name;
        this.date = date;
        this.type = type;
        this.priority = priority;
        this.duration = duration;
    }

    /**
     * @return the name of the event.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the date of the event.
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * @return the priority of the event.
     */
    public Priority getPriority() {
        return this.priority;
    }

    /**
     * @return the type of activity of the event.
     */
    public ActivityType getType() {
        return this.type;
    }

    /**
     * @return the duration of the event in minutes.
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * @return a string representation of the event.
     */
    @Override
    public String toString() {
        return this.name + " (" + this.type + ")";
    }

    /**
     * @return true if the event is equal to the given object, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        else if (getClass() != obj.getClass()) { return false; }
        else {
            Event other = (Event) obj;
            return this.name.equals(other.name) && 
                    this.date.equals(other.date) && 
                    this.type.equals(other.type) && 
                    this.priority.equals(other.priority) && 
                    this.duration == other.duration;
        }
    }

    /**
     * @return the hash code of the event.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.date, this.type, this.priority, this.duration);
    }
}
