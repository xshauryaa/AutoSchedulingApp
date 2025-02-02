package model;

import java.util.Date;

public class TimeBlock {
    
    private Date date; // date of the time block
    private int startTime; // start time of the time block in 24-hour format
    private int endTime; // end time of the time block in 24-hour format
    private String type; // representation of whether time block is for a rigid event, fluid event, or break

    /**
     * @param event the event to be scheduled in the time block
     * EFFECTS: Creates a new time block for given rigid event with its start and end times.
     */
    public TimeBlock(RigidEvent event) {
        this.date = event.getDate();
        this.startTime = event.getStartTime();
        this.endTime = event.getEndTime();
        this.type = "rigid";
    }

    /**
     * @param event the event to be scheduled in the time block
     * @param startTime the start time of the time block
     * @param endTime the end time of the time block
     * EFFECTS: Creates a new time block for given fluid event with given start and end times and type.
     */
    public TimeBlock(FluidEvent event, int startTime, int endTime) {
        this.date = event.getDate();
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = "fluid";
    }

    /**
     * @param breakTime the break to be scheduled in the time block
     * @param date the date of the time block
     * EFFECTS: Creates a new time block for given break with its start and end times on given date.
     */
    public TimeBlock(Break breakTime, Date date) {
        this.date = date;
        this.startTime = breakTime.getStartTime();
        this.endTime = breakTime.getEndTime();
        this.type = "break";
    }

    /**
     * @return the date of the time block
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * @return the start time of the time block
     */
    public int getStartTime() {
        return this.startTime;
    }

    /**
     * @return the end time of the time block
     */
    public int getEndTime() {
        return this.endTime;
    }

    /**
     * @return the type of the time block
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param timeBlock the time block to be compared
     * @return true if the time block is equal to the given time block, false otherwise
     */
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null || getClass() != obj.getClass()) { return false; }

        TimeBlock timeBlock = (TimeBlock) obj;
        return this.date.equals(timeBlock.getDate()) && this.startTime == timeBlock.getStartTime()
                && this.endTime == timeBlock.getEndTime() && this.type.equals(timeBlock.getType());
    }
}
