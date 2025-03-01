package model;

import java.util.HashMap;

import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

public class WeekSchedule {
    
    private HashMap<String, DaySchedule> weekSchedule;

    /**
     * @param minGap the gap between events
     * @param day1Date the date of the first day in this week's schedule
     * @param day1Day the day of the week on which the schedule starts
     * EFFECTS: creates a new WeekSchedule with 7 days of the week and given minimum gap
     */
    public WeekSchedule(int minGap, ScheduleDate day1Date, String day1Day) {
        weekSchedule = new HashMap<String, DaySchedule>();
        initiateWeekSchedule(minGap, day1Date, day1Day, 12);
    }

    /**
     * @param minGap the gap between events
     * @param day1Date the date of the first day in this week's schedule
     * @param day1Day the day of the week on which the schedule starts
     * @param workingHoursLimit the maximum working hours per day
     * EFFECTS: creates a new WeekSchedule with 7 days of the week, given minimum gap,
     *          and given working hours limit
     */
    public WeekSchedule(int minGap, ScheduleDate day1Date, String day1Day, int workingHoursLimit) {
        weekSchedule = new HashMap<String, DaySchedule>();
        initiateWeekSchedule(minGap, day1Date, day1Day, workingHoursLimit);
    }

    /**
     * @return the list of schedules in the week
     */
    public HashMap<String, DaySchedule> getWeekSchedule() {
        return weekSchedule;
    }

    /**
     * @param day the day for the schedule is to be retrieved
     * @return the schedule for the specified day in the week
     */
    public DaySchedule getScheduleForDay(String day) {
        return weekSchedule.get(day);
    }

    /**
     * @param day the day of the week on which to add the event
     * @param event the event to add
     * MODIFIES: this
     * EFFECTS: adds the event to the schedule of the given day
     * @throws EventConflictException if the event conflicts with another event
     * @throws WorkingLimitExceededException if the working hours limit is exceeded
     */
    public void addEvent(String day, RigidEvent event) throws EventConflictException, WorkingLimitExceededException {
        weekSchedule.get(day).addEvent(event);
    }

    /**
     * @param day the day of the week on which to add the event
     * @param event the event to add
     * @param startTime the start time of the event
     * @param endTime the end time of the event
     * MODIFIES: this
     * EFFECTS: adds the event to the schedule of the given day
     * @throws EventConflictException if the event conflicts with another event
     * @throws WorkingLimitExceededException if the working hours limit is exceeded
     */
    public void addEvent(String day, FluidEvent event, int startTime, int endTime) throws EventConflictException, WorkingLimitExceededException {
        weekSchedule.get(day).addEvent(event, startTime, endTime);
    }

    /**
     * @param breakTime the break to add
     * MODIFIES: this
     * EFFECTS: adds the break to every day in the week
     */
    public void addBreakToFullWeek(Break breakTime) {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (String day : days) {
            weekSchedule.get(day).addBreak(breakTime);
        }
    }

    /**
     * @param day the day of the week on which to add the break
     * @param breakTime the break to add
     * MODIFIES: this
     * EFFECTS: adds the break to the schedule of the given day
     */
    public void addBreak(String day, Break breakTime) {
        weekSchedule.get(day).addBreak(breakTime);
    }

    /**
     * @param day the day of the week on which to remove the event
     * @param event the event to remove
     * MODIFIES: this
     * EFFECTS: removes the event from the schedule of the given day
     */
    public void removeEvent(String day, Event event) {
        weekSchedule.get(day).removeEvent(event);
    }

    /**
     * @param day the day of the week on which to remove the break
     * @param breakTime the break to remove
     * MODIFIES: this
     * EFFECTS: removes the break from the schedule of the given day
     */
    public void removeBreak(String day, Break breakTime) {
        weekSchedule.get(day).removeBreak(breakTime);
    }

    /**
     * @return the total working hours of the week
     */
    public double calculateTotalOccupiedHours() {
        int totalOccupiedHours = 0;
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (String day : days) {
            for (TimeBlock t : weekSchedule.get(day)) {
                if (t.getType().equals("Break")) {
                    continue;
                } else {
                    totalOccupiedHours += t.getDuration();
                }
            }
        }
        return totalOccupiedHours;
    }

    /**
     * Private helper to implement the week schedule correctly.
     */
    private void initiateWeekSchedule(int minGap, ScheduleDate day1Date, String day1Day, int workingHoursLimit) {
        ScheduleDate currDate = day1Date;
        if (day1Day == "Monday") {
            weekSchedule.put("Monday", new DaySchedule("Monday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Tuesday", new DaySchedule("Tuesday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Wednesday", new DaySchedule("Wednesday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Thursday", new DaySchedule("Thursday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Friday", new DaySchedule("Friday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Saturday", new DaySchedule("Saturday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Sunday", new DaySchedule("Sunday", currDate, minGap, workingHoursLimit));
        } else if (day1Day == "Tuesday") {
            weekSchedule.put("Tuesday", new DaySchedule("Tuesday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Wednesday", new DaySchedule("Wednesday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Thursday", new DaySchedule("Thursday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Friday", new DaySchedule("Friday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Saturday", new DaySchedule("Saturday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Sunday", new DaySchedule("Sunday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Monday", new DaySchedule("Monday", currDate, minGap, workingHoursLimit));
        } else if (day1Day == "Wednesday") {
            weekSchedule.put("Wednesday", new DaySchedule("Wednesday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Thursday", new DaySchedule("Thursday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Friday", new DaySchedule("Friday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Saturday", new DaySchedule("Saturday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Sunday", new DaySchedule("Sunday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Monday", new DaySchedule("Monday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Tuesday", new DaySchedule("Tuesday", currDate, minGap, workingHoursLimit));
        } else if (day1Day == "Thursday") {
            weekSchedule.put("Thursday", new DaySchedule("Thursday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Friday", new DaySchedule("Friday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Saturday", new DaySchedule("Saturday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Sunday", new DaySchedule("Sunday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Monday", new DaySchedule("Monday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Tuesday", new DaySchedule("Tuesday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Wednesday", new DaySchedule("Wednesday", currDate, minGap, workingHoursLimit));
        } else if (day1Day == "Friday") {
            weekSchedule.put("Friday", new DaySchedule("Friday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Saturday", new DaySchedule("Saturday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Sunday", new DaySchedule("Sunday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Monday", new DaySchedule("Monday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Tuesday", new DaySchedule("Tuesday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Wednesday", new DaySchedule("Wednesday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Thursday", new DaySchedule("Thursday", currDate, minGap, workingHoursLimit));
        } else if (day1Day == "Saturday") {
            weekSchedule.put("Saturday", new DaySchedule("Saturday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Sunday", new DaySchedule("Sunday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Monday", new DaySchedule("Monday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Tuesday", new DaySchedule("Tuesday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Wednesday", new DaySchedule("Wednesday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Thursday", new DaySchedule("Thursday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Friday", new DaySchedule("Friday", currDate, minGap, workingHoursLimit));
        } else if (day1Day == "Sunday") {
            weekSchedule.put("Sunday", new DaySchedule("Sunday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Monday", new DaySchedule("Monday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Tuesday", new DaySchedule("Tuesday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Wednesday", new DaySchedule("Wednesday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Thursday", new DaySchedule("Thursday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Friday", new DaySchedule("Friday", currDate, minGap, workingHoursLimit));
            currDate = currDate.getNextDate();
            weekSchedule.put("Saturday", new DaySchedule("Saturday", currDate, minGap, workingHoursLimit));
        } 
    }
}
