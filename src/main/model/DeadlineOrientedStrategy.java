package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

public class DeadlineOrientedStrategy extends SchedulingStrategy {

    WeekSchedule deadlineOrientedSchedule; // the deadline-oriented schedule for the week

    protected ArrayList<Entry<String, Break>> breaks; // list of breaks paired with their respective days
    protected ArrayList<Break> repeatedBreaks; // list of repeated breaks
    protected ArrayList<RigidEvent> rigidEvents; // list of rigid events
    protected ArrayList<FlexibleEvent> flexibleEvents; // list of flexible events
    protected EventDependencies eventDependencies; // map of event dependencies

    /**
     * @param scheduler the scheduler object calling this strategy
     * @param firstDate the first date of the week being scheduled
     * @param firstDay the first day of the week being scheduled
     * @param minGap the minimum gap between events (in minutes)
     * @param workingHoursLimit the maximum working hours per day
     * EFFECTS: constructs a new BalancedWorkStrategy object with the given parameters
     */
    public DeadlineOrientedStrategy(Scheduler scheduler, ScheduleDate firstDate, String firstDay, int minGap, int workingHoursLimit) {
        this.breaks = scheduler.breaks;
        this.repeatedBreaks = scheduler.repeatedBreaks;
        this.rigidEvents = scheduler.rigidEvents;
        this.flexibleEvents = scheduler.flexibleEvents;
        this.eventDependencies = scheduler.eventDependencies;

        deadlineOrientedSchedule = new WeekSchedule(minGap, firstDate, firstDay, workingHoursLimit);
    }

    /**
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param earliestStartTime the earliest time at which the schedule can start
     * @param latestEndTime the latest time at which the schedule can end
     * MODIFIES: this
     * EFFECTS: generates the earliest fit schedule for the week
     */
    @Override
    public WeekSchedule generateSchedule(int earliestStartTime, int latestEndTime) {
        Time24 startTime = new Time24(earliestStartTime);
        Time24 endTime = new Time24(latestEndTime);
        scheduleBreaks();
        scheduleEvents(startTime, endTime);
        return deadlineOrientedSchedule;
    }

    /**
     * MODIFIES: this
     * EFFECTS: schedules all the given breaks into the earliest fit schedule
     */
    private void scheduleBreaks() {
        for (Entry<String, Break> entry : breaks) {
            String day = entry.getKey();
            Break breakTime = entry.getValue();
            deadlineOrientedSchedule.addBreak(day, breakTime);
        }

        for (Break breakTime : repeatedBreaks) {
            deadlineOrientedSchedule.addBreakToFullWeek(breakTime);
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
        int minGap = deadlineOrientedSchedule.getScheduleForDay("Monday").getMinGap();

        // Scheduling all rigid events
        for (RigidEvent event : rigidEvents) {
            String day = deadlineOrientedSchedule.getDayFromDate(event.getDate());
            deadlineOrientedSchedule.addEvent(day, event);
            scheduled.add(event);
        }

        ArrayList<Event> topoSorted = topologicalSort();
        for (Event event : topoSorted) {
            if (!scheduled.contains(event)) {
                if (isNotADependency((FlexibleEvent) event)) {
                    scheduleEventLatest((FlexibleEvent) event, ((FlexibleEvent) event).getDeadline(), latestEndTime, scheduled, minGap, earliestStartTime, latestEndTime);
                    continue;
                }
                Object[] latestDateAndTime = getLatestDateAndTimeForDependency(event, latestEndTime);
                ScheduleDate lastDate = (ScheduleDate) latestDateAndTime[0];
                Time24 lastTime = (Time24) latestDateAndTime[1];
                scheduleEventLatest((FlexibleEvent) event, lastDate, lastTime, scheduled, minGap, earliestStartTime, latestEndTime);
            }
        }

        // Scheduling any leftover flexible events
        for (FlexibleEvent event : flexibleEvents) {
            if (!scheduled.contains(event)) {
                scheduleEventLatest(event, event.getDeadline(), latestEndTime, scheduled, minGap, earliestStartTime, latestEndTime);
            }
        }
    }

    /**
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param event the event to be scheduled
     * @param beforeDate the date before which the event must be scheduled
     * @param lastTimeOnDate the last time on the date before which the event must be scheduled
     * @param scheduled the set of already scheduled events
     * @param minGap the minimum gap between events (in minutes)
     * @param earliestStartTime the earliest time at which the schedule can start
     * @param latestEndTime the latest time at which the schedule can end
     * MODIFIES: this
     * EFFECTS: schedules the given event as late as possible whil respecting its 
     *          deadline and dependency structure
     */
    private void scheduleEventLatest(FlexibleEvent event, ScheduleDate beforeDate, Time24 lastTimeOnDate, Set<Event> scheduled, int minGap, Time24 earliestStartTime, Time24 latestEndTime) {
        if (scheduled.contains(event)) { return; }

        ArrayList<DaySchedule> reversedDays = new ArrayList<>(deadlineOrientedSchedule.getWeekSchedule().values());
        Collections.reverse(reversedDays);

        for (DaySchedule daySchedule : reversedDays) {
            ScheduleDate date = daySchedule.getDate();

            if (date.isAfter(beforeDate)) { continue; }

            Time24 dayEndTime = (date.equals(beforeDate)) ? lastTimeOnDate : latestEndTime;

            int[] slot = findAvailableSlot(daySchedule, event.getDuration(), earliestStartTime, dayEndTime, minGap);

            if (slot != null) {
                try {
                    daySchedule.addEvent(((FlexibleEvent) event), slot[0], slot[1]);
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

    /**
     * @param event the event to check for
     * @return true if the event is not a dependency for any other event, false otherwise
     */
    private boolean isNotADependency(FlexibleEvent event) {
        for (Entry<Event, ArrayList<Event>> entry : eventDependencies.getDependencies().entrySet()) {
            if (entry.getValue().contains(event)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param event the event to check the latest possible scheduling date and time for
     * @return the latest date and time on which the event can be scheduled
     */
    private Object[] getLatestDateAndTimeForDependency(Event event, Time24 latestEndTime) {
        ScheduleDate latestAllowedDate = (event instanceof RigidEvent) ? ((RigidEvent) event).getDate() : ((FlexibleEvent) event).getDeadline();
        TimeBlock earliestScheduledDependent = null;
        for (Entry<Event, ArrayList<Event>> entry : eventDependencies.getDependencies().entrySet()) {
            if (entry.getValue().contains(event)) {
                TimeBlock tb = deadlineOrientedSchedule.locateTimeBlockForEvent(entry.getKey());
                ScheduleDate depDate = tb.getDate();
                if (depDate.isBefore(latestAllowedDate) || depDate.equals(latestAllowedDate)) {
                    latestAllowedDate = depDate;
                    earliestScheduledDependent = tb;
                } else if (depDate.equals(latestAllowedDate)) {
                    if (tb.getStartTime().isBefore(earliestScheduledDependent.getStartTime())) {
                        earliestScheduledDependent = tb;
                    }
                }
            }
        }

        Object[] latestAllowedDateAndTime = new Object[2];
        latestAllowedDateAndTime[0] = latestAllowedDate;
        latestAllowedDateAndTime[1] = (earliestScheduledDependent != null) ? earliestScheduledDependent.getStartTime() : latestEndTime;

        return latestAllowedDateAndTime;
    }


    @Override
    protected int[] findAvailableSlot(DaySchedule daySchedule, int duration, Time24 earliestStartTime, Time24 latestEndTime, int minGap) {
        Time24 end = latestEndTime.copy();
        Time24 start = latestEndTime.copy();
        start.subtractMinutes(duration);

        while (start.isAfter(earliestStartTime) || start.equals(earliestStartTime)) {
            boolean fits = true;

            for (TimeBlock tb : daySchedule.getTimeBlocks()) {
                Time24 blockStart = tb.getStartTime();
                Time24 blockEnd = tb.getEndTime();
                
                Time24 latestAllowedEnd = blockStart.copy();
                latestAllowedEnd.subtractMinutes(minGap);
                latestAllowedEnd.addMinutes(1);

                Time24 earliestAllowedStart = blockEnd.copy();
                earliestAllowedStart.addMinutes(minGap);
                earliestAllowedStart.subtractMinutes(1);

                if (!(end.isBefore(latestAllowedEnd) || start.isAfter(earliestAllowedStart))) {
                    fits = false;
                    break;
                }
            }
    
            if (fits) {
                return new int[]{start.toInt(), end.toInt()};
            }
    
            end.subtractMinutes(5);
            start = new Time24(end.toInt());
            start.subtractMinutes(duration);
        }

        return null;
    }

    /**
     * @return a topologically sorted list of events from the DAG of dependencies
     */
    private ArrayList<Event> topologicalSort() {
        ArrayList<Event> sorted = new ArrayList<>();
        Set<Event> visited = new HashSet<>();

        for (Event e : eventDependencies.getDependencies().keySet()) {
            dfs(e, visited, sorted);
        }

        Collections.reverse(sorted); // Because we want to schedule dependents first
        return sorted;
    }

    /**
     * @param current the current event being visited
     * @param visited the set of already visited events
     * @param sorted the list of sorted events
     * EFFECTS: performs a depth-first search on the event dependencies
     */
    private void dfs(Event current, Set<Event> visited, ArrayList<Event> sorted) {
        if (visited.contains(current)) return;

        visited.add(current);
        ArrayList<Event> dependencies = eventDependencies.getDependenciesForEvent(current);
        if (dependencies != null) {
            for (Event dep : dependencies) {
                dfs(dep, visited, sorted);
            }
        }

        sorted.add(current);
    }

}
