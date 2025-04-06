package model;

import java.util.HashMap;

import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

public class WeekSchedule {
    
    private HashMap<String, DaySchedule> weekSchedule;
    public static String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

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
     * @param date the date for which day is to be found
     * @return the day on which the given date falls
     */
    public String getDayFromDate(ScheduleDate date) {
        for (String day : DAYS) {
            if (getScheduleForDay(day).getDate().equals(date)) {
                return day;
            }
        }

        return null;
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
     */
    public void addEvent(String day, RigidEvent event) {
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
    public void addEvent(String day, FlexibleEvent event, int startTime, int endTime) throws EventConflictException, WorkingLimitExceededException {
        weekSchedule.get(day).addEvent(event, startTime, endTime);
    }

    /**
     * @param breakTime the break to add
     * MODIFIES: this
     * EFFECTS: adds the break to every day in the week
     */
    public void addBreakToFullWeek(Break breakTime) {
        for (String day : DAYS) {
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
        for (String day : DAYS) {
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
        int i = 0;
        for (String day : DAYS) {
            if (day.equals(day1Day)) {
                break;
            } 
            i++;
        }
        for (int j = 0; j < 7; j++) {
            DaySchedule schedule = new DaySchedule(DAYS[i % 7], currDate, minGap, workingHoursLimit);
            weekSchedule.put(DAYS[i % 7], schedule);
            currDate = currDate.getNextDate();
            i++;
        }
    }
}
