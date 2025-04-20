package model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

public class EarliestFitStrategy extends SchedulingStrategy {

    private WeekSchedule schedule;

    protected ArrayList<Entry<String, Break>> breaks;
    protected ArrayList<Break> repeatedBreaks;
    protected ArrayList<RigidEvent> rigidEvents;
    protected ArrayList<FlexibleEvent> flexibleEvents;
    protected EventDependencies eventDependencies;

    public EarliestFitStrategy(Scheduler scheduler, ScheduleDate firstDate, String firstDay, int minGap, int workingHoursLimit) {
        this.breaks = scheduler.breaks;
        this.repeatedBreaks = scheduler.repeatedBreaks;
        this.rigidEvents = scheduler.rigidEvents;
        this.flexibleEvents = scheduler.flexibleEvents;
        this.eventDependencies = scheduler.eventDependencies;

        schedule = new WeekSchedule(minGap, firstDate, firstDay, workingHoursLimit);
    }

    @Override
    public WeekSchedule generateSchedule(int earliestStartTime, int latestEndTime) {
        Time24 startTime = new Time24(earliestStartTime);
        Time24 endTime = new Time24(latestEndTime);
        scheduleBreaks();
        scheduleEvents(startTime, endTime);
        return schedule;
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

        for (DaySchedule daySchedule : schedule) {
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

    private void scheduleAfterDependencies(FlexibleEvent event, ScheduleDate beforeDate, Set<Event> scheduled, int minGap, Time24 earliestStartTime, Time24 latestEndTime) {
        ArrayList<Event> dependencies = eventDependencies.getDependenciesForEvent(event);
        ScheduleDate afterDate = null;
        if (dependencies != null) {
            for (Event dep : dependencies) {
                TimeBlock depBlock = schedule.locateTimeBlockForEvent(dep);
                if (afterDate == null || depBlock.getDate().isAfter(afterDate)) {
                    afterDate = depBlock.getDate();
                }
            }
        }
        for (DaySchedule daySchedule : schedule) {
            ScheduleDate date = daySchedule.getDate();

            if (date.isBefore(afterDate)) { continue;}
            if (date.isAfter(beforeDate)) { continue; }

            int[] slot = findAvailableSlot(daySchedule, event.getDuration(), earliestStartTime, latestEndTime, minGap);

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
}
