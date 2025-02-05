package model;

public class Break {
    
    private int duration; // The duration of the break in minutes
    private int startTime; // The start time of the break in 24-hour format
    private int endTime; // The end time of the break in 24-hour format

    /**
     * Creates a new break with given duration.
     * @param duration the duration of the break in minutes
     * @param startTime the start time of the break in 24-hour format
     * @param endTime the end time of the break in 24-hour format
     */
    public Break(int duration, int startTime, int endTime) {
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * @return the duration of the break.
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * @return the start time of the break.
     */
    public int getStartTime() {
        return this.startTime;
    }

    /**
     * @return the end time of the break.
     */
    public int getEndTime() {
        return this.endTime;
    }

    /**
     * @param obj the object to compare with
     * @return true if the object is a break with the same duration, start time, and end time, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        else if (getClass() != obj.getClass()) { return false; }
        else {
            Break other = (Break) obj;
            return this.duration == other.duration 
                    && this.startTime == other.startTime 
                    && this.endTime == other.endTime;
        }
    }
}
