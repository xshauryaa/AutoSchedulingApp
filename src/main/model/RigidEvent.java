package model;

public class RigidEvent extends Event {
    
    private int startTime; // The start time of the event in 24-hour format
    private int endTime; // The end time of the event in 24-hour format

    /**
     * Creates a new event on given date with given name, activity type, priority, duration, start time, and end time.
     * @param name the name of the event
     * @param date the date of the event
     * @param type the type of activity of the event
     * @param priority the priority of the event
     * @param duration the duration of the event in minutes
     * @param startTime the start time of the event in 24-hour format
     * @param endTime the end time of the event in 24-hour format
     */
    public RigidEvent(String name, Date date, ActivityType type, Priority priority, int duration, int startTime, int endTime) {
        // TODO: Implement constructor
    }

    /**
     * @return the start time of the event in 24-hour format.
     */
    public int getStartTime() {
        return 0; // TODO: Implement method
    }

    /**
     * @return the end time of the event in 24-hour format.
     */
    public int getEndTime() {
        return 0; // TODO: Implement method
    }

    /**
     * @param startTime the new start time of the event in 24-hour format
     */
    public void setStartTime(int startTime) {
        // TODO: Implement method
    }

    /**
     * @param endTime the new end time of the event in 24-hour format
     */
    public void setEndTime(int endTime) {
        // TODO: Implement method
    }
}
