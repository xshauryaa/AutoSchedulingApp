package model;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;

public class DayScheduleTest {
    
    private DaySchedule daySchedule;
    private RigidEvent rigidEvent;
    private FluidEvent fluidEvent;
    private Break breakTime;
    private Date date;
    private Date deadline;

    @BeforeEach
    void runBefore() {
        date = new Date(01012025);
        deadline = new Date(050120205);
        daySchedule = new DaySchedule("Monday", date, 30);
        rigidEvent = new RigidEvent("Meeting", ActivityType.MEETING, 60, date, 1200, 1300);
        fluidEvent = new FluidEvent("Study", ActivityType.EDUCATION, 120, Priority.HIGH, deadline);
        breakTime = new Break(30, 1500, 1530);
    }
}
