package ui;

import ics_handler.ICSHandler;
import model.*;
import model.exceptions.CircularDependencyException;

public class Main {
    public static void main(String[] args) throws Exception {
        // new PlannrConsoleApp();
        // schedulerTest1();
        schedulerTest2();
    }

    private static void schedulerTest1() {
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
        FlexibleEvent meditate = new FlexibleEvent("Meditation Session", ActivityType.PERSONAL, 30, Priority.LOW, new ScheduleDate(6, 4, 2025));
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
            deps.addDependency(midterm, study);                  // Math Midterm ← Study Math Chapters
            deps.addDependency(journal, checkup);                // Fill Health Journal ← Physio Checkup
            deps.addDependency(notes, slides);                   // Write Research Notes ← Slide Draft
            deps.addDependency(proof, notes);                    // Proofread Notes ← Write Research Notes
            deps.addDependency(essay, proof);                    // Reflective Essay ← Proofread Notes
            deps.addDependency(design, slides);                  // Design Mockups ← Slide Draft
            deps.addDependency(design, report);                  // Design Mockups ← Report Draft
            deps.addDependency(report, meeting);                 // Report Draft ← Staff Meeting
            deps.addDependency(budget, report);                  // Finalize Budget ← Report Draft
            deps.addDependency(emails, meeting);                 // Email Follow-Ups ← Staff Meeting
            deps.addDependency(gifts, plan);                     // Buy Gifts ← Weekly Planning
            deps.addDependency(checklist, gifts);                // Packing Checklist ← Buy Gifts
        } catch (CircularDependencyException e) {
            e.printStackTrace();
        }
        scheduler.setEventDependencies(deps);

        WeekSchedule earliestFitSchedule = scheduler.createSchedules("Earliest Fit", 800, 1700);
        
        WeekSchedule balancedWorkSchedule = scheduler.createSchedules("Balanced Work", 800, 1700);
        WeekSchedule deadlineOrientedSchedule = scheduler.createSchedules("Deadline Oriented", 800, 1700);

        // Generate ICS file
        String outputPath = "/Users/shauryathareja/Projects/AutoSchedulingApp/data/earliest-fit-schedule.ics";
        ICSHandler.getInstance().generateICS(earliestFitSchedule, outputPath);

        outputPath = "/Users/shauryathareja/Projects/AutoSchedulingApp/data/balanced-work-schedule.ics";
        ICSHandler.getInstance().generateICS(balancedWorkSchedule, outputPath);

        outputPath = "/Users/shauryathareja/Projects/AutoSchedulingApp/data/deadline-oriented-schedule.ics";
        ICSHandler.getInstance().generateICS(deadlineOrientedSchedule, outputPath);
        
        int totalTimeBlocks = 0;
        for (DaySchedule daySchedule : earliestFitSchedule) { // Assuming getDaySchedules() returns a list of DaySchedule objects
            totalTimeBlocks += daySchedule.getTimeBlocks().size(); // Assuming getTimeBlocks() exists in DaySchedule
        }
        System.out.println("Total time blocks in the earliest fit schedule: " + totalTimeBlocks);
        totalTimeBlocks = 0;
        for (DaySchedule daySchedule : balancedWorkSchedule) { // Assuming getDaySchedules() returns a list of DaySchedule objects
            totalTimeBlocks += daySchedule.getTimeBlocks().size(); // Assuming getTimeBlocks() exists in DaySchedule
        }
        System.out.println("Total time blocks in the balanced work schedule: " + totalTimeBlocks);
        totalTimeBlocks = 0;
        for (DaySchedule daySchedule : deadlineOrientedSchedule) { // Assuming getDaySchedules() returns a list of DaySchedule objects
            totalTimeBlocks += daySchedule.getTimeBlocks().size(); // Assuming getTimeBlocks() exists in DaySchedule
        }
        System.out.println("Total time blocks in the deadline oriented schedule: " + totalTimeBlocks);
    }

    private static void schedulerTest2() {
        Scheduler scheduler;

        ScheduleDate date = new ScheduleDate(13, 4, 2025);
        String day1 = "Sunday";
        int minGap = 30;
        int workingHoursLimit = 12;

        scheduler = new Scheduler(date, day1, minGap, workingHoursLimit);

        // --- Rigid Events ---
        RigidEvent yoga = new RigidEvent("Sunday Yoga", ActivityType.PERSONAL, 60, new ScheduleDate(13, 4, 2025), 900, 1000);
        RigidEvent mathFinal = new RigidEvent("Math Final", ActivityType.EDUCATION, 120, new ScheduleDate(14, 4, 2025), 1000, 1200);
        RigidEvent doctor = new RigidEvent("Doctor's Appt", ActivityType.PERSONAL, 30, new ScheduleDate(15, 4, 2025), 900, 930);
        RigidEvent chemQuiz = new RigidEvent("Chemistry Quiz", ActivityType.EDUCATION, 60, new ScheduleDate(15, 4, 2025), 1400, 1500);
        RigidEvent uxWorkshop = new RigidEvent("UX Workshop", ActivityType.WORK, 120, new ScheduleDate(16, 4, 2025), 1100, 1300);
        RigidEvent standup = new RigidEvent("Team Standup", ActivityType.WORK, 30, new ScheduleDate(16, 4, 2025), 1400, 1430);
        RigidEvent marketingSync = new RigidEvent("Marketing Sync", ActivityType.WORK, 60, new ScheduleDate(17, 4, 2025), 1000, 1100);
        RigidEvent lecture = new RigidEvent("AI Guest Lecture", ActivityType.EDUCATION, 90, new ScheduleDate(17, 4, 2025), 1400, 1530);
        RigidEvent presentation = new RigidEvent("Final Presentation", ActivityType.WORK, 60, new ScheduleDate(18, 4, 2025), 1300, 1400);
        RigidEvent therapy = new RigidEvent("Therapy", ActivityType.PERSONAL, 60, new ScheduleDate(18, 4, 2025), 1500, 1600);
        RigidEvent interview = new RigidEvent("Internship Interview", ActivityType.WORK, 60, new ScheduleDate(19, 4, 2025), 930, 1030);
        RigidEvent bookClub = new RigidEvent("Book Club", ActivityType.PERSONAL, 90, new ScheduleDate(19, 4, 2025), 1700, 1830);

        scheduler.addEvent(yoga);
        scheduler.addEvent(mathFinal);
        scheduler.addEvent(doctor);
        scheduler.addEvent(chemQuiz);
        scheduler.addEvent(uxWorkshop);
        scheduler.addEvent(standup);
        scheduler.addEvent(marketingSync);
        scheduler.addEvent(lecture);
        scheduler.addEvent(presentation);
        scheduler.addEvent(therapy);
        scheduler.addEvent(interview);
        scheduler.addEvent(bookClub);

        // --- Flexible Events ---
        FlexibleEvent studyMath = new FlexibleEvent("Study Math Chapters", ActivityType.EDUCATION, 90, Priority.HIGH, new ScheduleDate(14, 4, 2025));
        FlexibleEvent reviewUX = new FlexibleEvent("Review UX Concepts", ActivityType.WORK, 60, Priority.MEDIUM, new ScheduleDate(16, 4, 2025));
        FlexibleEvent run = new FlexibleEvent("Morning Run", ActivityType.PERSONAL, 30, Priority.MEDIUM, new ScheduleDate(13, 4, 2025));
        FlexibleEvent resume = new FlexibleEvent("Resume Update", ActivityType.WORK, 45, Priority.HIGH, new ScheduleDate(18, 4, 2025));
        FlexibleEvent research = new FlexibleEvent("Research Paper", ActivityType.EDUCATION, 75, Priority.HIGH, new ScheduleDate(17, 4, 2025));
        FlexibleEvent mood = new FlexibleEvent("Mood Journal", ActivityType.PERSONAL, 20, Priority.LOW, new ScheduleDate(18, 4, 2025));
        FlexibleEvent slides = new FlexibleEvent("Prepare Final Slides", ActivityType.WORK, 60, Priority.HIGH, new ScheduleDate(18, 4, 2025));
        FlexibleEvent personalStmt = new FlexibleEvent("Write Personal Statement", ActivityType.PERSONAL, 90, Priority.HIGH, new ScheduleDate(19, 4, 2025));
        FlexibleEvent practice = new FlexibleEvent("Practice Presentation", ActivityType.WORK, 60, Priority.HIGH, new ScheduleDate(18, 4, 2025));
        FlexibleEvent aiConcepts = new FlexibleEvent("Study AI Concepts", ActivityType.EDUCATION, 90, Priority.HIGH, new ScheduleDate(17, 4, 2025));
        FlexibleEvent portfolio = new FlexibleEvent("Design Portfolio", ActivityType.WORK, 75, Priority.HIGH, new ScheduleDate(19, 4, 2025));
        FlexibleEvent bib = new FlexibleEvent("Annotated Bibliography", ActivityType.EDUCATION, 60, Priority.MEDIUM, new ScheduleDate(15, 4, 2025));
        FlexibleEvent litReview = new FlexibleEvent("Literature Review", ActivityType.EDUCATION, 45, Priority.MEDIUM, new ScheduleDate(16, 4, 2025));
        FlexibleEvent planning = new FlexibleEvent("Weekly Planning", ActivityType.PERSONAL, 20, Priority.MEDIUM, new ScheduleDate(19, 4, 2025));
        FlexibleEvent groceries = new FlexibleEvent("Buy Groceries", ActivityType.PERSONAL, 30, Priority.MEDIUM, new ScheduleDate(19, 4, 2025));
        FlexibleEvent desk = new FlexibleEvent("Clean Desk", ActivityType.PERSONAL, 20, Priority.LOW, new ScheduleDate(19, 4, 2025));
        FlexibleEvent prepInterview = new FlexibleEvent("Prepare for Interview", ActivityType.WORK, 45, Priority.HIGH, new ScheduleDate(19, 4, 2025));
        FlexibleEvent meditate = new FlexibleEvent("Meditation Session", ActivityType.PERSONAL, 30, Priority.LOW, new ScheduleDate(13, 4, 2025));
        FlexibleEvent email = new FlexibleEvent("Email Professors", ActivityType.EDUCATION, 30, Priority.MEDIUM, new ScheduleDate(17, 4, 2025));
        FlexibleEvent mockInterview = new FlexibleEvent("Rehearse Mock Interview", ActivityType.WORK, 30, Priority.MEDIUM, new ScheduleDate(19, 4, 2025));
        FlexibleEvent linkedin = new FlexibleEvent("Draft LinkedIn Post", ActivityType.WORK, 30, Priority.MEDIUM, new ScheduleDate(17, 4, 2025));
        FlexibleEvent chemStudy = new FlexibleEvent("Study Chemistry", ActivityType.EDUCATION, 60, Priority.MEDIUM, new ScheduleDate(15, 4, 2025));
        FlexibleEvent editStmt = new FlexibleEvent("Edit Personal Statement", ActivityType.PERSONAL, 60, Priority.HIGH, new ScheduleDate(19, 4, 2025));
        FlexibleEvent decorate = new FlexibleEvent("Decorate Study Corner", ActivityType.PERSONAL, 30, Priority.LOW, new ScheduleDate(19, 4, 2025));

        FlexibleEvent[] flex = {studyMath, reviewUX, run, resume, research, mood, slides, personalStmt, practice, aiConcepts, portfolio, bib, litReview, planning, groceries, desk, prepInterview, meditate, email, mockInterview, linkedin, chemStudy, editStmt, decorate};

        for (FlexibleEvent e : flex) {
            scheduler.addEvent(e);
        }

        // --- Breaks ---
        scheduler.addRepeatedBreak(new Break(30, 1730, 1800));
        scheduler.addBreak("Monday", new Break(30, 1300, 1330));
        scheduler.addBreak("Tuesday", new Break(30, 1100, 1130));
        scheduler.addBreak("Wednesday", new Break(30, 930, 1000));
        scheduler.addBreak("Thursday", new Break(30, 1330, 1400));
        scheduler.addBreak("Friday", new Break(30, 1030, 1100));
        scheduler.addBreak("Saturday", new Break(30, 1200, 1230));

        // --- Dependencies ---
        EventDependencies deps = new EventDependencies();
        try {
            deps.addDependency(mathFinal, studyMath);
            deps.addDependency(chemQuiz, chemStudy);
            deps.addDependency(reviewUX, uxWorkshop);
            deps.addDependency(research, litReview);
            deps.addDependency(research, bib);
            deps.addDependency(practice, slides);
            deps.addDependency(presentation, practice);
            deps.addDependency(slides, reviewUX);
            deps.addDependency(resume, linkedin);
            deps.addDependency(personalStmt, mood);
            deps.addDependency(editStmt, personalStmt);
            deps.addDependency(prepInterview, resume);
            deps.addDependency(mockInterview, prepInterview);
            deps.addDependency(groceries, planning);
            deps.addDependency(desk, groceries);
            deps.addDependency(portfolio, resume);
        } catch (CircularDependencyException e) {
            e.printStackTrace();
        }
        scheduler.setEventDependencies(deps);

        System.out.println("======== Earliest Fit ========");
        WeekSchedule earliestFitSchedule = scheduler.createSchedules("Earliest Fit", 800, 2200);
        int total = 0;
        for (DaySchedule daySchedule : earliestFitSchedule) {
            total += daySchedule.getTimeBlocks().size();
        }
        System.out.println("Total time blocks: " + total);
        System.out.println("======== Balanced Work ========");
        WeekSchedule balancedWorkSchedule = scheduler.createSchedules("Balanced Work", 800, 2200);
        total = 0;
        for (DaySchedule daySchedule : balancedWorkSchedule) {
            total += daySchedule.getTimeBlocks().size();
        }
        System.out.println("Total time blocks: " + total);
        System.out.println("======== Deadline Oriented ========");
        WeekSchedule deadlineOrientedSchedule = scheduler.createSchedules("Deadline Oriented", 800, 2200);
        total = 0;
        for (DaySchedule daySchedule : deadlineOrientedSchedule) {
            total += daySchedule.getTimeBlocks().size();
        }
        System.out.println("Total time blocks: " + total);

        // Export ICS files (optional output paths)
        ICSHandler.getInstance().generateICS(earliestFitSchedule, "data/earliest-fit.ics");
        ICSHandler.getInstance().generateICS(balancedWorkSchedule, "data/balanced-work.ics");
        ICSHandler.getInstance().generateICS(deadlineOrientedSchedule, "data/deadline-oriented.ics");

        for (DaySchedule daySchedule : balancedWorkSchedule) {
            System.out.println(daySchedule);
        }
    }
}
