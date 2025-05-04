package model;

/**
 * Represents a block of time in a schedule, which can be a rigid event,
 * flexible event, or break. 
 */
public class TimeBlock {
    
    private String name; // name of the event
    private ScheduleDate date; // date of the time block
    private ActivityType activityType; // activity type of the event 
    private Time24 startTime; // start time of the time block in 24-hour format
    private Time24 endTime; // end time of the time block in 24-hour format
    private int duration; // duration of the time block
    private boolean isCompleted; // whether the time block is completed
    private String type; // representation of whether time block is for a rigid event, fluid event, or break

    /**
     * @param event the event to be scheduled in the time block
     * EFFECTS: Creates a new time block for given rigid event with its start and end times.
     */
    public TimeBlock(RigidEvent event) {
        this.name = event.getName();
        this.date = event.getDate();
        this.activityType = event.getType();
        this.startTime = event.getStartTime();
        this.endTime = event.getEndTime();
        this.duration = event.getDuration();
        this.isCompleted = false;
        this.type = "rigid";
    }

    /**
     * @param event the event to be scheduled in the time block
     * @param startTime the start time of the time block
     * @param endTime the end time of the time block
     * EFFECTS: Creates a new time block for given fluid event with given start and end times and type.
     */
    public TimeBlock(FlexibleEvent event, ScheduleDate date, int startTime, int endTime) {
        this.name = event.getName();
        this.date = date;
        this.activityType = event.getType();
        this.startTime = new Time24(startTime);
        this.endTime = new Time24(endTime);
        this.duration = event.getDuration();
        this.isCompleted = false;
        this.type = "flexible";
    }

    /**
     * @param breakTime the break to be scheduled in the time block
     * @param date the date of the time block
     * EFFECTS: Creates a new time block for given break with its start and end times on given date.
     */
    public TimeBlock(Break breakTime, ScheduleDate date) {
        this.name = "Break";
        this.date = date;
        this.startTime = breakTime.getStartTime();
        this.endTime = breakTime.getEndTime();
        this.duration = breakTime.getDuration();
        this.isCompleted = true;
        this.type = "break";
    }

    /**
     * @return the name of the time block
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the date of the time block
     */
    public ScheduleDate getDate() {
        return this.date;
    }

    /**
     * @return the activity type of the time block
     */
    public ActivityType getActivityType() {
        return this.activityType;
    }

    /**
     * @return the start time of the time block
     */
    public Time24 getStartTime() {
        return this.startTime;
    }

    /**
     * @return the end time of the time block
     */
    public Time24 getEndTime() {
        return this.endTime;
    }

    /**
     * @return the type of the time block
     */
    public String getType() {
        return this.type;
    }

    /**
     * @return the duration of the time block
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * @return true if the time block is completed, false otherwise
     */
    public boolean isCompleted() {
        return this.isCompleted;
    }

    /**
     * @param completed the completion status of the time block
     */
    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    /**
     * @param obj the object to be compared
     * @return true if the object is equal to the given time block, false otherwise
     */
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null || getClass() != obj.getClass()) { return false; }

        TimeBlock timeBlock = (TimeBlock) obj;
        return this.name.equals(timeBlock.getName()) && this.date.equals(timeBlock.getDate()) 
                && this.startTime.equals(timeBlock.getStartTime()) && this.endTime.equals(timeBlock.getEndTime())
                && this.duration == timeBlock.getDuration() && this.type.equals(timeBlock.getType());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, date, startTime, endTime, duration, type);
    }

    @Override
    public String toString() {
        String result = startTime.toString() + " - " + endTime.toString() + ": " + name;
        if (type.equals("break")) {
            return result;
        }
        result += " (" + activityType + ")";
        return result;
    }

    /**
     * @return UID for the time block in RFC-compliant format
     */
    public String getUID() {
        String uid = String.format("%s-%s-%s@plannr.scheduler",
                date.toString(),                     // e.g., 2025-04-06
                startTime.toString(),                // e.g., 0930
                name.replaceAll("\\s+", "_"));       // replace spaces in name
        return uid.toLowerCase();                    // lowercase for consistency
    }
}
