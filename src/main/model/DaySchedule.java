package model;

import java.util.ArrayList;
import java.util.Iterator;

import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

public class DaySchedule implements Iterable<TimeBlock> {
    
    private String day; // day of the week
    private ScheduleDate date; // date of the day
    private ArrayList<Event> events; // list of events
    private ArrayList<Break> breaks; // list of breaks
    private ArrayList<TimeBlock> timeBlocks; // list of time blocks
    private int minGap; // minimum gap between events
    private int workingHoursLimit; // maximum working hours per day

    /** 
     * REQUIRES: day is one of "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
     * @param day the day of the week
     * @param date the date of the day
     * @param minGap the gap between events
     * EFFECTS: creates a new DaySchedule with given day and date, with set gap, and no events or breaks
     * Default working hours limit is 12 hours
     */
    public DaySchedule(String day, ScheduleDate date, int minGap) {
        this.day = day;
        this.date = date;
        this.minGap = minGap;
        this.workingHoursLimit = 12;
        this.events = new ArrayList<Event>();
        this.breaks = new ArrayList<Break>();
        this.timeBlocks = new ArrayList<TimeBlock>();
    }

    /**
     * REQUIRES: day is one of "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
     * @param day the day of the week
     * @param date the date of the day
     * @param minGap the gap between events
     * @param workingHoursLimit the maximum working hours per day
     * EFFECTS: creates a new DaySchedule with given day, date, set gap, and working hours limit and no events or breaks
     */
    public DaySchedule(String day, ScheduleDate date, int minGap, int workingHoursLimit) {
        this.day = day;
        this.date = date;
        this.minGap = minGap;
        this.workingHoursLimit = workingHoursLimit;
        this.events = new ArrayList<Event>();
        this.breaks = new ArrayList<Break>();
        this.timeBlocks = new ArrayList<TimeBlock>();
    }

    /**
     * @return the day of the week
     */
    public String getDay() {
        return this.day;
    }

    /**
     * @return the date of the day
     */
    public ScheduleDate getDate() {
        return this.date;
    }

    /**
     * @return the list of events
     */
    public ArrayList<Event> getEvents() {
        return this.events;
    }

    /**
     * @return the list of breaks
     */
    public ArrayList<Break> getBreaks() {
        return this.breaks;
    }

    /**
     * @return the list of time blocks
     */
    public ArrayList<TimeBlock> getTimeBlocks() {
        return this.timeBlocks;
    }

    /**
     * @return the gap between events
     */
    public int getMinGap() {
        return this.minGap;
    }

    /**
     * @return the maximum working hours per day
     */
    public int getWorkingHoursLimit() {
        return this.workingHoursLimit;
    }

    /**
     * REQUIRES: limit > 0
     * @param limit the maximum working hours per day
     * EFFECTS: sets the maximum working hours per day
     */
    public void setWorkingHoursLimit(int limit) {
        this.workingHoursLimit = limit;
    }

    /**
     * REQUIRES: the event does not overlap with any other event or break &
     *           the total working hours of the day does not exceed the working hours limit
     * @param event the rigid event to be added
     * MODIFIES: this
     * EFFECTS: adds the event to the list of events and adds a corresponding time block to the 
     *          list of time blocks
     * @throws EventConflictException if the event overlaps with any other event or break 
     *                                or the total working hours exceed the working hours limit 
     * @throws WorkingLimitExceededException if the total working hours exceed the working hours limit
     *                                       after adding the event
     */
    public void addEvent(RigidEvent event) throws EventConflictException, WorkingLimitExceededException {
        if (checkEventConflict(event)) {
            throw new EventConflictException();
        } else if (event.getDuration() + (calculateWorkingHours() * 60) > (this.workingHoursLimit * 60)) {
            throw new WorkingLimitExceededException();
        } else {
            this.events.add(event);
            this.timeBlocks.add(new TimeBlock(event));
            sortSchedule();
        }
    }

    /**
     * REQUIRES: the event does not overlap with any other event or break &
     *           the total working hours of the day does not exceed the working hours limit
     * @param event the fluid event to be added
     * @param startTime the start time of the event
     * @param endTime the end time of the event
     * MODIFIES: this
     * EFFECTS: adds the event to the list of events and adds a corresponding time block to the 
     *          list of time blocks
     * @throws EventConflictException if the event overlaps with any other event or break 
     *                                or the total working hours exceed the working hours limit 
     * @throws WorkingLimitExceededException if the total working hours exceed the working hours limit
     *                                       after adding the event
     */
    public void addEvent(FluidEvent event, int startTime, int endTime) throws EventConflictException, WorkingLimitExceededException {
        if (checkEventConflict(event, startTime, endTime)) {
            throw new EventConflictException();
        } else if (event.getDuration() + (calculateWorkingHours() * 60) > (this.workingHoursLimit * 60)) {
            throw new WorkingLimitExceededException();
        } else {
            this.events.add(event);
            this.timeBlocks.add(new TimeBlock(event, this.date, startTime, endTime));
            sortSchedule();
        }
    }

    /**
     * REQUIRES: no events must be added before adding a break
     * @param breakTime the break to be added
     * MODIFIES: this
     * EFFECTS: adds the break to the list of breaks
     */
    public void addBreak(Break breakTime) {
        this.breaks.add(breakTime);
        this.timeBlocks.add(new TimeBlock(breakTime, this.date));
        sortSchedule();
    }

    /**
     * @param event the event to be removed
     * MODIFIES: this
     * EFFECTS: removes the event from the list of events
     */
    public void removeEvent(Event event) {
        this.events.remove(event);
        for (TimeBlock tb : this.timeBlocks) {
            if (tb.getName().equals(event.getName())) {
                this.timeBlocks.remove(tb);
                break;
            }
        }
    }

    /**
     * @param breakTime the break to be removed
     * MODIFIES: this
     * EFFECTS: removes the break from the list of breaks
     */
    public void removeBreak(Break breakTime) {
        this.breaks.remove(breakTime);
        for (TimeBlock tb : this.timeBlocks) {
            if (tb.getName().equals("Break")
                    && tb.getStartTime() == breakTime.getStartTime()
                    && tb.getEndTime() == breakTime.getEndTime()) {
                this.timeBlocks.remove(tb);
                break;
            }
        }
    }

    /**
     * @return the total working hours of the day
     */
    public int calculateWorkingHours() {
        int totalWorkingMinutes = 0;
        for (Event event : this.events) {
            if (event.getType() == ActivityType.EDUCATION || event.getType() == ActivityType.MEETING || event.getType() == ActivityType.WORK) {
                totalWorkingMinutes += event.getDuration();
            }
        }
        return totalWorkingMinutes / 60;
    }

    /**
     * @param event the rigid event to be checked for conflict
     * @return true if the event overlaps with any other event or break, false otherwise
     */
    private boolean checkEventConflict(RigidEvent event) {
        for (TimeBlock tb : this.timeBlocks) {
            boolean condition1 = event.getStartTime() <= tb.getStartTime() && event.getEndTime() >= tb.getStartTime();
            boolean condition2 = event.getStartTime() <= tb.getEndTime() && event.getEndTime() >= tb.getEndTime();
            if (condition1 || condition2) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param event the fluid event to be checked for conflict
     * @param startTime the start time of the event
     * @param endTime the end time of the event
     * @return true if the event overlaps with any other event or break, false otherwise
     */
    private boolean checkEventConflict(FluidEvent event, int startTime, int endTime) {
        for (TimeBlock tb : this.timeBlocks) {
            boolean condition1 = startTime <= tb.getStartTime() && endTime >= tb.getStartTime();
            boolean condition2 = startTime <= tb.getEndTime() && endTime >= tb.getEndTime();
            if (condition1 || condition2) {
                return true;
            }
        }
        return false;
    }

    /**
     * EFFECTS: sorts the time blocks in chronological order
     */
    private void sortSchedule() {
        ArrayList<TimeBlock> sortedTimeBlocks = new ArrayList<TimeBlock>();
        for (TimeBlock timeBlock : this.timeBlocks) {
            if (sortedTimeBlocks.isEmpty()) {
                sortedTimeBlocks.add(timeBlock);
            } else {
                for (int i = 0; i < sortedTimeBlocks.size(); i++) {
                    if (timeBlock.getStartTime() < sortedTimeBlocks.get(i).getStartTime()) {
                        sortedTimeBlocks.add(i, timeBlock);
                        break;
                    }
                }
                sortedTimeBlocks.add(timeBlock);
            }
        }
        timeBlocks = sortedTimeBlocks;
    }

    @Override
    public Iterator<TimeBlock> iterator() {
        return this.timeBlocks.iterator();
    }
}
