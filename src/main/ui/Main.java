package ui;

import static org.junit.jupiter.api.Assertions.fail;

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
import model.exceptions.EventConflictException;
import model.exceptions.WorkingLimitExceededException;

public class Main {
    public static void main(String[] args) throws Exception {
        Scheduler scheduler;

        RigidEvent midterm, docAppt, workshop, 
                presentation, meeting;

        FlexibleEvent studyPrep, healthJournal, slidesDraft,
                    researchNotes, dataCleanup, weeklyPlanning,
                    reportWriting, visualDesign;

        Break break1, break2, break3, break4, repBreak;

        EventDependencies eventDependencies;

        ScheduleDate date = new ScheduleDate(6, 4, 2025);
        String day1 = "Sunday";
        int minGap = 30;
        int workingHoursLimit = 4;

        scheduler = new Scheduler(date, day1, minGap, workingHoursLimit);

        midterm = new RigidEvent("Midterm", ActivityType.EDUCATION, 120, 
                new ScheduleDate(7, 4, 2025), 1000, 1200);
        docAppt = new RigidEvent("Doctor's Appointment", ActivityType.PERSONAL, 30, 
                new ScheduleDate(8, 4, 2025), 900, 930);
        workshop = new RigidEvent("Workshop", ActivityType.WORK, 120,
                new ScheduleDate(9, 4, 2025), 1400, 1600);
        presentation = new RigidEvent("Presentation", ActivityType.WORK, 60,
                new ScheduleDate(11, 4, 2025), 1500, 1600);
        meeting = new RigidEvent("Meeting", ActivityType.WORK, 60,
                new ScheduleDate(10, 4, 2025), 1100, 1200);

        studyPrep = new FlexibleEvent("Study Prep", ActivityType.EDUCATION, 90, 
                Priority.HIGH, new ScheduleDate(7, 4, 2025));
        healthJournal = new FlexibleEvent("Health Journal", ActivityType.PERSONAL, 30,
                Priority.MEDIUM, new ScheduleDate(8, 4, 2025));
        slidesDraft = new FlexibleEvent("Slides Draft", ActivityType.WORK, 60,
                Priority.MEDIUM, new ScheduleDate(9, 4, 2025));
        researchNotes = new FlexibleEvent("Research Notes", ActivityType.EDUCATION, 45,
                Priority.LOW, new ScheduleDate(11, 4, 2025));
        dataCleanup = new FlexibleEvent("Data Cleanup", ActivityType.WORK, 30,
                Priority.LOW, new ScheduleDate(10, 4, 2025));
        weeklyPlanning = new FlexibleEvent("Weekly Planning", ActivityType.PERSONAL, 20,
                Priority.LOW, new ScheduleDate(12, 4, 2025));
        reportWriting = new FlexibleEvent("Report Writing", ActivityType.WORK, 90,
                Priority.HIGH, new ScheduleDate(11, 4, 2025));
        visualDesign = new FlexibleEvent("Visual Design", ActivityType.WORK, 60,
                Priority.MEDIUM, new ScheduleDate(11, 4, 2025));

        break1 = new Break(30, 1300, 1330);
        break2 = new Break(30, 1200, 1230);
        break3 = new Break(30, 1500, 1530);
        break4 = new Break(30, 900, 930);
        repBreak = new Break(30, 1700, 1730);

        eventDependencies = new EventDependencies();

        scheduler.addEvent(midterm);
        scheduler.addEvent(docAppt);
        scheduler.addEvent(workshop);
        scheduler.addEvent(presentation);
        scheduler.addEvent(meeting);

        scheduler.addEvent(studyPrep);
        scheduler.addEvent(healthJournal);
        scheduler.addEvent(slidesDraft);
        scheduler.addEvent(researchNotes);
        scheduler.addEvent(dataCleanup);
        scheduler.addEvent(weeklyPlanning);
        scheduler.addEvent(reportWriting);
        scheduler.addEvent(visualDesign);

        scheduler.addBreak("Monday", break1);
        scheduler.addBreak("Wednesday", break2);
        scheduler.addBreak("Thursday", break3);
        scheduler.addBreak("Friday", break4);

        scheduler.addRepeatedBreak(repBreak);

        try {
            eventDependencies.addDependency(midterm, studyPrep);
            eventDependencies.addDependency(docAppt, healthJournal);
            eventDependencies.addDependency(slidesDraft, researchNotes);
            eventDependencies.addDependency(slidesDraft, visualDesign);
            eventDependencies.addDependency(reportWriting, visualDesign);
            eventDependencies.addDependency(meeting, reportWriting);
        } catch (CircularDependencyException e) {
            // Not expected
        }
        scheduler.setEventDependencies(eventDependencies);

        scheduler.createSchedules(800, 1700);

        WeekSchedule weekSchedule = scheduler.getSchedule();

        for (int i = 6; i < 13; i++) {
            System.out.println(weekSchedule.getScheduleForDay(WeekSchedule.DAYS[i % 7]));
        }
    }
}
