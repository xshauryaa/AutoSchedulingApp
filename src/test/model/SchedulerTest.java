package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.exceptions.CircularDependencyException;

import org.junit.jupiter.api.BeforeEach;

public class SchedulerTest {
    private Scheduler scheduler;

    private RigidEvent midterm, docAppt, workshop, 
                    presentation, meeting;

    private FlexibleEvent studyPrep, healthJournal, slidesDraft,
                        researchNotes, dataCleanup, weeklyPlanning,
                        reportWriting, visualDesign;

    private Break break1, break2, break3, break4, repBreak;

    private EventDependencies eventDependencies;

    @BeforeEach
    void runBefore() {
        ScheduleDate date = new ScheduleDate(6, 4, 2025);
        String day1 = "Sunday";
        int minGap = 30;
        int workingHoursLimit = 7;

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
    }

    @Test
    void testConstructor() {
        assertEquals(0, scheduler.breaks.size());
        assertEquals(0, scheduler.repeatedBreaks.size());
        assertEquals(0, scheduler.rigidEvents.size());
        assertEquals(0, scheduler.flexibleEvents.size());
        assertNull(scheduler.eventDependencies);
    }

    @Test
    void testAddBreak() {
        scheduler.addBreak("Monday", break1);
        assertEquals(scheduler.breaks.size(), 1);
        assertEquals(scheduler.breaks.get(0).getKey(), "Monday");
        assertEquals(scheduler.breaks.get(0).getValue(), break1);
    }

    @Test
    void testAddRepeatedBreak() {
        scheduler.addRepeatedBreak(repBreak);
        assertEquals(scheduler.repeatedBreaks.size(), 1);
        assertEquals(scheduler.repeatedBreaks.get(0), repBreak);
    }

    @Test
    void testRigidEvent() {
        scheduler.addEvent(midterm);
        assertEquals(scheduler.rigidEvents.size(), 1);
        assertEquals(scheduler.rigidEvents.get(0), midterm);
    }

    @Test
    void testFlexibleEvent() {
        scheduler.addEvent(studyPrep);
        assertEquals(scheduler.flexibleEvents.size(), 1);
        assertEquals(scheduler.flexibleEvents.get(0), studyPrep);
    }

    @Test
    void testSetEventDependencies() {
        scheduler.setEventDependencies(eventDependencies);
        assertEquals(scheduler.eventDependencies, eventDependencies);
    }

    @Test
    void testCreateSchedules() {
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

        WeekSchedule schedule = scheduler.createSchedules("Earliest Fit", 800, 1700);

        // Checking for scheduled breaks 
        assertEquals(1, schedule.getScheduleForDay("Sunday").getBreaks().size());
        assertEquals(repBreak, schedule.getScheduleForDay("Sunday").getBreaks().get(0));
        assertEquals(2, schedule.getScheduleForDay("Monday").getBreaks().size());
        assertEquals(break1, schedule.getScheduleForDay("Monday").getBreaks().get(0));
        assertEquals(repBreak, schedule.getScheduleForDay("Monday").getBreaks().get(1));
        assertEquals(1, schedule.getScheduleForDay("Tuesday").getBreaks().size());
        assertEquals(repBreak, schedule.getScheduleForDay("Tuesday").getBreaks().get(0));
        assertEquals(2, schedule.getScheduleForDay("Wednesday").getBreaks().size());
        assertEquals(break2, schedule.getScheduleForDay("Wednesday").getBreaks().get(0));
        assertEquals(repBreak, schedule.getScheduleForDay("Wednesday").getBreaks().get(1));
        assertEquals(2, schedule.getScheduleForDay("Thursday").getBreaks().size());
        assertEquals(break3, schedule.getScheduleForDay("Thursday").getBreaks().get(0));
        assertEquals(repBreak, schedule.getScheduleForDay("Thursday").getBreaks().get(1));
        assertEquals(2, schedule.getScheduleForDay("Friday").getBreaks().size());
        assertEquals(break4, schedule.getScheduleForDay("Friday").getBreaks().get(0));
        assertEquals(repBreak, schedule.getScheduleForDay("Friday").getBreaks().get(1));
        assertEquals(1, schedule.getScheduleForDay("Saturday").getBreaks().size());
        assertEquals(repBreak, schedule.getScheduleForDay("Saturday").getBreaks().get(0));

        // Checking for scheduled rigid events
        assertTrue(schedule.getScheduleForDay("Monday").getEvents().contains(midterm));
        assertTrue(schedule.getScheduleForDay("Tuesday").getEvents().contains(docAppt));
        assertTrue(schedule.getScheduleForDay("Wednesday").getEvents().contains(workshop));
        assertTrue(schedule.getScheduleForDay("Thursday").getEvents().contains(meeting));
        assertTrue(schedule.getScheduleForDay("Friday").getEvents().contains(presentation));

        // Checking if dependencies were followed
        assertTrue(checkDependencyFollowed(studyPrep, midterm, schedule));
        assertTrue(checkDependencyFollowed(healthJournal, docAppt, schedule));
        assertTrue(checkDependencyFollowed(researchNotes, slidesDraft, schedule));
        assertTrue(checkDependencyFollowed(reportWriting, meeting, schedule));
        assertTrue(checkDependencyFollowed(visualDesign, reportWriting, schedule));
        assertTrue(checkDependencyFollowed(visualDesign, slidesDraft, schedule));

        // Checking if deadlines were followed
        assertTrue(getDateForEvent(studyPrep, schedule).isBefore(studyPrep.getDeadline()));
        assertTrue(getDateForEvent(healthJournal, schedule).isBefore(healthJournal.getDeadline()));
        assertTrue(!getDateForEvent(slidesDraft, schedule).isAfter(slidesDraft.getDeadline()));
        assertTrue(getDateForEvent(researchNotes, schedule).isBefore(researchNotes.getDeadline()));
        assertTrue(!getDateForEvent(dataCleanup, schedule).isAfter(dataCleanup.getDeadline()));
        assertTrue(!getDateForEvent(weeklyPlanning, schedule).isAfter(weeklyPlanning.getDeadline()));
        assertTrue(getDateForEvent(reportWriting, schedule).isBefore(reportWriting.getDeadline()));
        assertTrue(getDateForEvent(visualDesign, schedule).isBefore(visualDesign.getDeadline()));
    }

    private boolean checkDependencyFollowed(Event dep, Event event, WeekSchedule schedule) {
        ScheduleDate depDate = null;
        ScheduleDate eventDate = null;
        Time24 depEndTime = null;
        Time24 eventStartTime = null;

        for (DaySchedule daySched : schedule) {
            if (daySched.getEvents().contains(dep)) {
                for (TimeBlock tb : daySched.getTimeBlocks()) {
                    if (tb.getName().equals(dep.getName()) && tb.getDuration() == dep.getDuration()) {
                        depDate = tb.getDate();
                        depEndTime = tb.getEndTime();
                    }
                }
            }
            if (daySched.getEvents().contains(event)) {
                for (TimeBlock tb : daySched.getTimeBlocks()) {
                    if (tb.getName().equals(event.getName()) && tb.getDuration() == event.getDuration()) {
                        eventDate = tb.getDate();
                        eventStartTime = tb.getStartTime();
                    }
                }
            }
        }

        if (depDate.isBefore(eventDate)) {
            return true;
        } else if (depDate.equals(eventDate)) {
            return depEndTime.isBefore(eventStartTime);
        } else {
            return false;
        }
    }

    private ScheduleDate getDateForEvent(Event event, WeekSchedule schedule) {
        for (DaySchedule daySched : schedule) {
            if (daySched.getEvents().contains(event)) {
                for (TimeBlock tb : daySched.getTimeBlocks()) {
                    if (tb.getName().equals(event.getName()) && tb.getDuration() == event.getDuration()) {
                        return tb.getDate();
                    }
                }
            }
        }

        return null;
    }
}

