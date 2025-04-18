package ui;

import ics_handler.ICSHandler;
import model.ActivityType;
import model.Break;
import model.EventDependencies;
import model.FlexibleEvent;
import model.Priority;
import model.RigidEvent;
import model.ScheduleDate;
import model.Scheduler;
import model.WeekSchedule;
import model.exceptions.CircularDependencyException;

public class Main {
    public static void main(String[] args) throws Exception {
        Scheduler scheduler;

        // Create date context
        ScheduleDate date = new ScheduleDate(6, 4, 2025);
        String day1 = "Sunday";
        int minGap = 30;
        int workingHoursLimit = 6;

        scheduler = new Scheduler(date, day1, minGap, workingHoursLimit);

        // Rigid Events
        RigidEvent church = new RigidEvent("Church Visit", ActivityType.PERSONAL, 60,
                new ScheduleDate(6, 4, 2025), 1000, 1100);
        RigidEvent midterm = new RigidEvent("Math Midterm", ActivityType.EDUCATION, 120,
                new ScheduleDate(7, 4, 2025), 1000, 1200);
        RigidEvent checkup = new RigidEvent("Physio Checkup", ActivityType.PERSONAL, 30,
                new ScheduleDate(8, 4, 2025), 900, 930);
        RigidEvent workshop = new RigidEvent("Team Workshop", ActivityType.WORK, 120,
                new ScheduleDate(9, 4, 2025), 1400, 1600);
        RigidEvent quiz = new RigidEvent("Chemistry Quiz", ActivityType.EDUCATION, 60,
                new ScheduleDate(9, 4, 2025), 900, 1000);
        RigidEvent meeting = new RigidEvent("Staff Meeting", ActivityType.WORK, 60,
                new ScheduleDate(10, 4, 2025), 1100, 1200);
        RigidEvent checkin = new RigidEvent("Manager Check-In", ActivityType.WORK, 30,
                new ScheduleDate(10, 4, 2025), 1500, 1530);
        RigidEvent presentation = new RigidEvent("Final Presentation", ActivityType.WORK, 60,
                new ScheduleDate(11, 4, 2025), 1500, 1600);
        RigidEvent dinner = new RigidEvent("Dinner Party", ActivityType.PERSONAL, 120,
                new ScheduleDate(12, 4, 2025), 1900, 2100);

        // Flexible Events
        FlexibleEvent study = new FlexibleEvent("Study Math Chapters", ActivityType.EDUCATION, 90, Priority.HIGH, new ScheduleDate(7, 4, 2025));
        FlexibleEvent journal = new FlexibleEvent("Fill Health Journal", ActivityType.PERSONAL, 30, Priority.LOW, new ScheduleDate(8, 4, 2025));
        FlexibleEvent slides = new FlexibleEvent("Slide Draft", ActivityType.WORK, 60, Priority.MEDIUM, new ScheduleDate(9, 4, 2025));
        FlexibleEvent notes = new FlexibleEvent("Write Research Notes", ActivityType.EDUCATION, 45, Priority.MEDIUM, new ScheduleDate(10, 4, 2025));
        FlexibleEvent cleanup = new FlexibleEvent("Data Cleaning", ActivityType.WORK, 30, Priority.LOW, new ScheduleDate(10, 4, 2025));
        FlexibleEvent plan = new FlexibleEvent("Weekly Planning", ActivityType.PERSONAL, 20, Priority.LOW, new ScheduleDate(12, 4, 2025));
        FlexibleEvent report = new FlexibleEvent("Report Draft", ActivityType.WORK, 90, Priority.HIGH, new ScheduleDate(11, 4, 2025));
        FlexibleEvent design = new FlexibleEvent("Design Mockups", ActivityType.WORK, 60, Priority.MEDIUM, new ScheduleDate(11, 4, 2025));
        FlexibleEvent proof = new FlexibleEvent("Proofread Notes", ActivityType.EDUCATION, 30, Priority.LOW, new ScheduleDate(11, 4, 2025));
        FlexibleEvent gifts = new FlexibleEvent("Buy Gifts", ActivityType.PERSONAL, 45, Priority.LOW, new ScheduleDate(12, 4, 2025));
        FlexibleEvent essay = new FlexibleEvent("Reflective Essay", ActivityType.EDUCATION, 60, Priority.HIGH, new ScheduleDate(12, 4, 2025));
        FlexibleEvent meditate = new FlexibleEvent("Meditation Session", ActivityType.PERSONAL, 30, Priority.LOW, new ScheduleDate(9, 4, 2025));
        FlexibleEvent cases = new FlexibleEvent("Read Case Studies", ActivityType.EDUCATION, 60, Priority.MEDIUM, new ScheduleDate(9, 4, 2025));
        FlexibleEvent budget = new FlexibleEvent("Finalize Budget", ActivityType.WORK, 40, Priority.MEDIUM, new ScheduleDate(11, 4, 2025));
        FlexibleEvent emails = new FlexibleEvent("Email Follow-Ups", ActivityType.WORK, 30, Priority.LOW, new ScheduleDate(10, 4, 2025));
        FlexibleEvent checklist = new FlexibleEvent("Packing Checklist", ActivityType.PERSONAL, 20, Priority.LOW, new ScheduleDate(12, 4, 2025));

        // Breaks
        Break break1 = new Break(30, 1300, 1330);
        Break break2 = new Break(30, 1200, 1230);
        Break break3 = new Break(30, 1000, 1030);
        Break break4 = new Break(30, 900, 930);
        Break repBreak = new Break(30, 1700, 1730);

        // Add Rigid Events
        scheduler.addEvent(church);
        scheduler.addEvent(midterm);
        scheduler.addEvent(checkup);
        scheduler.addEvent(workshop);
        scheduler.addEvent(quiz);
        scheduler.addEvent(meeting);
        scheduler.addEvent(checkin);
        scheduler.addEvent(presentation);
        scheduler.addEvent(dinner);

        // Add Flexible Events
        scheduler.addEvent(study);
        scheduler.addEvent(journal);
        scheduler.addEvent(slides);
        scheduler.addEvent(notes);
        scheduler.addEvent(cleanup);
        scheduler.addEvent(plan);
        scheduler.addEvent(report);
        scheduler.addEvent(design);
        scheduler.addEvent(proof);
        scheduler.addEvent(gifts);
        scheduler.addEvent(essay);
        scheduler.addEvent(meditate);
        scheduler.addEvent(cases);
        scheduler.addEvent(budget);
        scheduler.addEvent(emails);
        scheduler.addEvent(checklist);

        // Add Breaks
        scheduler.addBreak("Monday", break1);
        scheduler.addBreak("Wednesday", break2);
        scheduler.addBreak("Thursday", break3);
        scheduler.addBreak("Friday", break4);
        scheduler.addRepeatedBreak(repBreak);

        // Dependencies
        EventDependencies deps = new EventDependencies();
        try {
            deps.addDependency(midterm, study);
            deps.addDependency(checkup, journal);
            deps.addDependency(slides, notes);
            deps.addDependency(slides, design);
            deps.addDependency(report, design);
            deps.addDependency(meeting, report);
            deps.addDependency(notes, proof);
            deps.addDependency(proof, essay);
            deps.addDependency(plan, gifts);
            deps.addDependency(gifts, checklist);
            deps.addDependency(report, budget);
            deps.addDependency(meeting, emails);
        } catch (CircularDependencyException e) {
            e.printStackTrace();
        }
        scheduler.setEventDependencies(deps);

        scheduler.createSchedules(800, 1700);

        WeekSchedule weekSchedule = scheduler.getSchedule();

        for (int i = 6; i < 13; i++) {
            System.out.println(weekSchedule.getScheduleForDay(WeekSchedule.DAYS[i % 7]));
        }

        // Generate ICS file
        String outputPath = "/Users/shauryathareja/Projects/AutoSchedulingApp/data/schedule.ics";
        ICSHandler.getInstance().generateICS(weekSchedule, outputPath);
    }
}
