package model;

import org.junit.jupiter.api.BeforeEach;

public class WeekScheduleTest {
    
    private WeekSchedule weekSchedule;
    private RigidEvent rigidEvent1;
    private RigidEvent rigidEvent2;
    private FluidEvent fluidEvent1;
    private FluidEvent fluidEvent2;
    private Break breakTime;
    private ScheduleDate date;
    private ScheduleDate deadline;

    @BeforeEach
    void runBefore() {
        weekSchedule = new WeekSchedule(30, 12);
        date = new ScheduleDate(1, 1, 2025);
        deadline = new ScheduleDate(5, 1, 20205);
    }
}
