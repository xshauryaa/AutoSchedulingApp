package model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

/**
 * Represents a scheduling strategy type that schedules events in the 
 * most balanced way possible, picking time slots on days that have the
 * least workload allotted while scheduling.
 */
public class BalancedWorkStrategy extends SchedulingStrategy {

    private WeekSchedule balancedWorkSchedule; // the balanced work schedule for the week

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
    public BalancedWorkStrategy(Scheduler scheduler, ScheduleDate firstDate, String firstDay, int minGap, int workingHoursLimit) {
        this.breaks = scheduler.breaks;
        this.repeatedBreaks = scheduler.repeatedBreaks;
        this.rigidEvents = scheduler.rigidEvents;
        this.flexibleEvents = scheduler.flexibleEvents;
        this.eventDependencies = scheduler.eventDependencies;

        balancedWorkSchedule = new WeekSchedule(minGap, firstDate, firstDay, workingHoursLimit);
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
        return balancedWorkSchedule;
    }

    /**
     * MODIFIES: this
     * EFFECTS: schedules all the given breaks into the earliest fit schedule
     */
    private void scheduleBreaks() {
        for (Entry<String, Break> entry : breaks) {
            String day = entry.getKey();
            Break breakTime = entry.getValue();
            balancedWorkSchedule.addBreak(day, breakTime);
        }

        for (Break breakTime : repeatedBreaks) {
            balancedWorkSchedule.addBreakToFullWeek(breakTime);
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
        int minGap = balancedWorkSchedule.getScheduleForDay("Monday").getMinGap();

        // Scheduling all rigid events
        for (RigidEvent event : rigidEvents) {
            String day = balancedWorkSchedule.getDayFromDate(event.getDate());
            balancedWorkSchedule.addEvent(day, event);
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
            if (!scheduled.contains(event)) {
                scheduleAfterDependencies((FlexibleEvent) event, ((FlexibleEvent) event).getDeadline(), scheduled, minGap, earliestStartTime, latestEndTime);
            }
        }

        // Scheduling any leftover flexible events
        for (FlexibleEvent event : flexibleEvents) {
            if (!scheduled.contains(event)) {
                scheduleDependency(event, event.getDeadline(), latestEndTime, scheduled, minGap, earliestStartTime, latestEndTime);
            }
        }
    }

    /**
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param dependency the event to be scheduled
     * @param beforeDate the date before which the event must be scheduled
     * @param lastTimeOnDate the last time on the date before which the event must be scheduled
     * @param scheduled the set of already scheduled events
     * @param minGap the minimum gap between events (in minutes)
     * @param earliestStartTime the earliest time at which the schedule can start
     * @param latestEndTime the latest time at which the schedule can end
     * MODIFIES: this
     * EFFECTS: schedules the given dependency respecting its depdendency structure
     */
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

        ArrayList<DaySchedule> daysWithLeastLoad = getDaysSortedByLoad();
        for (DaySchedule daySchedule : daysWithLeastLoad) {
            ScheduleDate date = daySchedule.getDate();

            if (date.isAfter(beforeDate)) { continue; }

            Time24 dayEndTime = (date.equals(beforeDate)) ? lastTimeOnDate : latestEndTime;

            int[] slot = findAvailableSlot(daySchedule, dependency.getDuration(), earliestStartTime, dayEndTime, minGap);

            if (slot != null) {
                try {
                    daySchedule.addEvent(((FlexibleEvent) dependency), slot[0], slot[1]);
                    scheduled.add(dependency);
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
     * REQUIRES: earliestStartTime < latestEndTime, both must be in 24-hour format
     * @param event the flexible event to be scheduled
     * @param beforeDate the date before which the event must be scheduled
     * @param scheduled the set of already scheduled events
     * @param minGap the minimum gap between events (in minutes)
     * @param earliestStartTime the earliest time at which the schedule can start
     * @param latestEndTime the latest time at which the schedule can end
     * MODIFIES: this
     * EFFECTS: schedules the given flexible event after its dependencies
     */
    private void scheduleAfterDependencies(FlexibleEvent event, ScheduleDate beforeDate, Set<Event> scheduled, int minGap, Time24 earliestStartTime, Time24 latestEndTime) {
        ArrayList<Event> dependencies = eventDependencies.getDependenciesForEvent(event);
        ScheduleDate afterDate = null;
        Time24 earliestTimeOnDate = null;
        if (dependencies != null) {
            for (Event dep : dependencies) {
                TimeBlock depBlock = balancedWorkSchedule.locateTimeBlockForEvent(dep);
                if (afterDate == null || depBlock.getDate().isAfter(afterDate)) {
                    afterDate = depBlock.getDate();
                    earliestTimeOnDate = depBlock.getEndTime();
                }
            }
        }
        ArrayList<DaySchedule> daysWithLoads = getDaysSortedByLoad();
        for (DaySchedule daySchedule : daysWithLoads) {
            ScheduleDate date = daySchedule.getDate();

            if (date.isBefore(afterDate)) { continue; }
            if (date.isAfter(beforeDate)) { continue; }

            Time24 startTime = (date.equals(afterDate)) ? earliestTimeOnDate : earliestStartTime;
            int[] slot = findAvailableSlot(daySchedule, event.getDuration(), startTime, latestEndTime, minGap);

            if (slot != null) {
                try {
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

    /**
     * @return the list of day schedules sorted by total working hours in increasing order
     */
    private ArrayList<DaySchedule> getDaysSortedByLoad() {
        ArrayList<DaySchedule> sorted = new ArrayList<DaySchedule>(balancedWorkSchedule.getWeekSchedule().values()); // Assuming an iterable getter
        sorted.sort((d1, d2) -> Integer.compare(d1.calculateWorkingHours(), d2.calculateWorkingHours()));
        return sorted;
    }
    
}
