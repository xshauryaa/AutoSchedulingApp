package model;

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
}
