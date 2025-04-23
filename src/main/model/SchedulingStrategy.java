package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Represents an arbitrary scheduling strategy to be followed when 
 * generating a schedule.
 */
public abstract class SchedulingStrategy {

    /** 
     * @param earliestStartTime the earliest start time of the schedule
     * @param latestEndTime the latest end time of the schedule
     * EFFECTS: generates a schedule for the week based on the given time range 
     *          while following the earliest fit strategy
     * @return the generated schedule
     */
    public abstract WeekSchedule generateSchedule(int earliestStartTime, int latestEndTime);

    /**
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param daySchedule the schedule of the day to be checked
     * @param duration the duration of the event to be scheduled
     * @param earliestStartTime the earliest start time of the schedule
     * @param latestEndTime the latest end time of the schedule
     * @param minGap the minimum gap between two events
     * @return the start and end time of the available slot in 24-hour format
     */
    protected int[] findAvailableSlot(DaySchedule daySchedule, int duration, Time24 earliestStartTime, Time24 latestEndTime, int minGap) {
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
    
            start.addMinutes(5);
            end = new Time24(start.toInt());
            end.addMinutes(duration);
        }

        return null;
    }

    /**
     * @param event the event to check the latest possible scheduling date and time for
     * @return the latest date and time on which the event can be scheduled
     */
    protected Object[] getLatestDateAndTimeForDependency(WeekSchedule schedule, EventDependencies eventDependencies, Event event, Time24 latestEndTime) {
        ScheduleDate latestAllowedDate = (event instanceof RigidEvent) ? ((RigidEvent) event).getDate() : ((FlexibleEvent) event).getDeadline();
        TimeBlock earliestScheduledDependent = null;
        for (Entry<Event, ArrayList<Event>> entry : eventDependencies.getDependencies().entrySet()) {
            if (entry.getValue().contains(event)) {
                TimeBlock tb = schedule.locateTimeBlockForEvent(entry.getKey());
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

    /**
     * @return a topologically sorted list of all the events based on their 
     * dependencies
     */
    protected ArrayList<Event> topologicalSortOfEvents(EventDependencies eventDependencies, ArrayList<FlexibleEvent> flexibleEvents) {
        ArrayList<Event> sorted = new ArrayList<>();
        Set<Event> visited = new HashSet<>();

        for (Event e : eventDependencies.getDependencies().keySet()) {
            dfs(e, visited, sorted, eventDependencies);
        }

        ArrayList<Event> topologicallySorted = new ArrayList<>();
        for (FlexibleEvent event : flexibleEvents) {
            if (!sorted.contains(event)) {
                topologicallySorted.add(event);
            }
        }

        topologicallySorted.addAll(sorted);
        return topologicallySorted;
    };

    /**
     * @param current the current event being visited
     * @param visited the set of already visited events
     * @param sorted the list of sorted events
     * EFFECTS: performs a depth-first search on the event dependencies
     */
    private void dfs(Event current, Set<Event> visited, ArrayList<Event> sorted, EventDependencies eventDependencies) {
        if (visited.contains(current)) return;

        visited.add(current);
        ArrayList<Event> dependencies = eventDependencies.getDependenciesForEvent(current);
        if (dependencies != null) {
            for (Event dep : dependencies) {
                dfs(dep, visited, sorted, eventDependencies);
            }
        }

        sorted.add(current);
    }
}
