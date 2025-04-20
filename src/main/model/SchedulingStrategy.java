package model;

public abstract class SchedulingStrategy {

    public abstract WeekSchedule generateSchedule(int earliestStartTime, int latestEndTime);

    public int[] findAvailableSlot(DaySchedule daySchedule, int duration, Time24 earliestStartTime, Time24 latestEndTime, int minGap) {
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
