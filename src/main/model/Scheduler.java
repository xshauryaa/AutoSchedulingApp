package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

public class Scheduler {
    
    private WeekSchedule schedule;

    protected ArrayList<Entry<String, Break>> breaks;
    protected ArrayList<Break> repeatedBreaks;
    protected ArrayList<RigidEvent> rigidEvents;
    protected ArrayList<FlexibleEvent> flexibleEvents;
    protected EventDependencies eventDependencies;

    /**
     * @param date the date of the first day of the week to be scheduled
     * @param day1 the day of the week corresponding to the date
     * @param minGap the minimum gap between events
     * @param workingHourLimit the maximum working hours per day
     * EFFECTS: constructs a new Scheduler object with empry lists of breaks,
     *          rigid events, and flexible events
     */
    public Scheduler(ScheduleDate date, String day1, int minGap, int workingHoursLimit) {
        schedule = new WeekSchedule(minGap, date, day1, workingHoursLimit);

        breaks = new ArrayList<Entry<String, Break>>();
        repeatedBreaks = new ArrayList<Break>();
        rigidEvents = new ArrayList<RigidEvent>();
        flexibleEvents = new ArrayList<FlexibleEvent>();
        eventDependencies = null;
    }

    /**
     * @param day the day of the week to which the break is to be added
     * @param breakTime the break to be added
     * MODIFIES: this
     * EFFECTS: adds the given break to the list of breaks
     */
    public void addBreak(String day, Break breakTime) {
        Entry<String, Break> entry = Map.entry(day, breakTime);
        this.breaks.add(entry);
    }

    /**
     * @param breakTime the break to be added to the entire week
     * MODIFIES: this
     * EFFECTS: adds the given break to the list of repeated breaks
     */
    public void addRepeatedBreak(Break breakTime) {
        this.repeatedBreaks.add(breakTime);
    }

    /**
     * @param event the rigid event to be added
     * MODIFIES: this
     * EFFECTS: adds the given rigid event to the list of rigid events
     */
    public void addEvent(RigidEvent event) {
        this.rigidEvents.add(event);
    }

    /**
     * @param event the flexible event to be added
     * MODIFIES: this
     * EFFECTS: adds the given flexible event to the list of flexible events
     */
    public void addEvent(FlexibleEvent event) {
        this.flexibleEvents.add(event);
    }

    /**
     * @param dependencies the event dependencies mapping to be followed 
     *                     by the scheduler
     * MODIFIES: this
     * EFFECTS: sets the event dependencies mapping to the given mapping
     */
    public void setEventDependencies(EventDependencies dependencies) {
        this.eventDependencies = dependencies;
    }
    
    /**
     * @return the earliest fit schedule
     */
    public WeekSchedule getSchedule() {
        return this.schedule;
    }

    /**
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param earliestStartTime the earliest time at which the schedule can start
     * @param latestEndTime the latest time at which the schedule can end
     * MODIFIES: this
     * EFFECTS: creates the schedules for the week based on the given constraints
     */
    public void createSchedules(int earliestStartTime, int latestEndTime) {
        Time24 startTime = new Time24(earliestStartTime);
        Time24 endTime = new Time24(latestEndTime);
        scheduleBreaks();
        scheduleEvents(startTime, endTime);
    }

    /**
     * MODIFIES: this
     * EFFECTS: schedules all the given breaks into all three week schedules
     */
    private void scheduleBreaks() {
        for (Entry<String, Break> entry : breaks) {
            String day = entry.getKey();
            Break breakTime = entry.getValue();
            schedule.addBreak(day, breakTime);
        }

        for (Break breakTime : repeatedBreaks) {
            schedule.addBreakToFullWeek(breakTime);
        }
    }

    /**
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param earliestStartTime the earliest time at which the schedule can start
     * @param latestEndTime the latest time at which the schedule can end
     * MODIFIES: this
     * EFFECTS: schedules all the given events respecting dependency order
     */
    private void scheduleEvents(Time24 earliestStartTime, Time24 latestEndTime) {
        Set<Event> scheduled = new LinkedHashSet<>();
        int minGap = schedule.getScheduleForDay("Monday").getMinGap();

        // Scheduling all rigid events
        for (RigidEvent event : rigidEvents) {
            String day = schedule.getDayFromDate(event.getDate());
            schedule.addEvent(day, event);
            scheduled.add(event);
        }

        // Scheduling dependencies
        for (Event event : eventDependencies.getDependencies().keySet()) {
            ArrayList<Event> dependencies = eventDependencies.getDependenciesForEvent(event);
            if (dependencies != null) {
                for (Event dep : dependencies) {
                    if (!scheduled.contains(dep)) {
                        ScheduleDate date = (event instanceof RigidEvent)
                            ? ((RigidEvent) event).getDate()
                            : ((FlexibleEvent) event).getDeadline();
                        Time24 time = (event instanceof RigidEvent)
                            ? ((RigidEvent) event).getStartTime()
                            : latestEndTime;
        
                        scheduleDependency(dep, date, time, scheduled, minGap, earliestStartTime, latestEndTime);
                    }
                }
            }
        }

        // Scheduling any leftover flexible events
        for (FlexibleEvent event : flexibleEvents) {
            if (!scheduled.contains(event)) {
                scheduleDependency(event, event.getDeadline(), latestEndTime, scheduled, minGap, earliestStartTime, latestEndTime);
            }
        }
    }

    private void scheduleDependency(Event dependency, ScheduleDate beforeDate, Time24 lastTimeOnDate, Set<Event> scheduled, int minGap, Time24 earliestStartTime, Time24 latestEndTime) {
        if (scheduled.contains(dependency)) { return; }

        ArrayList<Event> dependencies = eventDependencies.getDependenciesForEvent(dependency);
        if (dependencies != null) {
            for (Event dep : dependencies) {
                if (!scheduled.contains(dep)) {
                    scheduleDependency(dep, beforeDate, lastTimeOnDate, scheduled, minGap, earliestStartTime, latestEndTime);
                }
            }
        }

        for (int i = 6; i >= 0; i--) {
            String day = WeekSchedule.DAYS[i];
            DaySchedule daySchedule = schedule.getScheduleForDay(day);
            ScheduleDate date = daySchedule.getDate();

            if (date.isAfter(beforeDate)) { continue; }

            Time24 dayEndTime = (date.equals(beforeDate)) ? lastTimeOnDate : latestEndTime;

            int[] slot = findAvailableSlot(daySchedule, dependency.getDuration(), earliestStartTime, dayEndTime, minGap);

            if (slot != null) {
                try {
                    FlexibleEvent event = (FlexibleEvent) dependency;
                    daySchedule.addEvent(event, slot[0], slot[1]);
                    scheduled.add(event);
                    return;
                } catch (EventConflictException e) {
                    // not possible
                } catch (WorkingLimitExceededException e) {
                    continue;
                }
            }
        }
    }

    private int[] findAvailableSlot(DaySchedule daySchedule, int duration, Time24 earliestStartTime, Time24 latestEndTime, int minGap) {
        Time24 start = earliestStartTime.copy();
        Time24 end = earliestStartTime.copy();
        end.addMinutes(duration);

        while (end.isBefore(latestEndTime) || end.equals(latestEndTime)) {
            boolean fits = true;

            for (TimeBlock tb : daySchedule.getTimeBlocks()) {
                Time24 blockStart = tb.getStartTime();
                Time24 blockEnd = tb.getEndTime();
                
                Time24 latestAllowedEnd = blockStart.copy();
                latestAllowedEnd.subtractMinutes(minGap);

                Time24 earliestAllowedStart = blockEnd.copy();
                earliestAllowedStart.addMinutes(minGap);

                if (!(end.isBefore(latestAllowedEnd) || start.isAfter(earliestAllowedStart))) {
                    fits = false;
                    break;
                }
            }
    
            if (fits) {
                return new int[]{start.toInt(), end.toInt()};
            }
    
            start.addMinutes(5);
            end = new Time24(start.toInt());
            end.addMinutes(duration);
        }

        return null;
    }
}
