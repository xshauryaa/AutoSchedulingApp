package model;

public class FlexibleEvent extends Event {
    
    private Priority priority; // The priority of the event
    private ScheduleDate deadline; // The deadline of the event

    /**
     * @param name the name of the event
     * @param type the type of activity of the event
     * @param duration the duration of the event in minutes
     * @param priority the priority of the event
     * @param deadline the deadline of the event
     * EFFECTS: Creates a new event on given date with given name, activity type, priority, duration, and deadline.
     */
    public FlexibleEvent(String name, ActivityType type, int duration, Priority priority, ScheduleDate deadline) {
        super(name, type, duration);
        this.priority = priority;
        this.deadline = deadline;
    }

    /**
     * @return the deadline of the event.
     */
    public ScheduleDate getDeadline() {
        return this.deadline;
    }

    /**
     * @return the priority of the event
     */
    public Priority getPriority() {
        return this.priority;
    }
}
