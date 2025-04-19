package ics_handler;

import java.io.FileWriter;
import java.io.IOException;

import model.TimeBlock;
import model.ScheduleDate;
import model.Time24;
import model.DaySchedule;
import model.WeekSchedule;

public class ICSHandler {

    private static final ICSHandler instance = new ICSHandler();

    private ICSHandler() {}

    public static ICSHandler getInstance() {
        return instance;
    }

    public void generateICS(WeekSchedule schedule, String outputPath) {
        StringBuilder sb = new StringBuilder();
        String dtstamp = generateDTStamp();

        sb.append("BEGIN:VCALENDAR\r\n");
        sb.append("PRODID:-//Plannr//NONSGML v1.0//EN\r\n");
        sb.append("VERSION:2.0\r\n");
        sb.append("CALSCALE:GREGORIAN\r\n");
        sb.append("METHOD:PUBLISH\r\n");
        sb.append("\r\n");
        sb.append("BEGIN:VTIMEZONE\r\n");
        sb.append("TZID:America/Los_Angeles\r\n");
        sb.append("X-LIC-LOCATION:America/Los_Angeles\r\n");
        sb.append("BEGIN:DAYLIGHT\r\n");
        sb.append("TZOFFSETFROM:-0800\r\n");
        sb.append("TZOFFSETTO:-0700\r\n");
        sb.append("TZNAME:PDT\r\n");
        sb.append("DTSTART:19700308T020000\r\n");
        sb.append("RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU\r\n");
        sb.append("END:DAYLIGHT\r\n");
        sb.append("BEGIN:STANDARD\r\n");
        sb.append("TZOFFSETFROM:-0700\r\n");
        sb.append("TZOFFSETTO:-0800\r\n");
        sb.append("TZNAME:PST\r\n");
        sb.append("DTSTART:19701101T020000\r\n");
        sb.append("RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU\r\n");
        sb.append("END:STANDARD\r\n");
        sb.append("END:VTIMEZONE\r\n");
        sb.append("\r\n");

        for (DaySchedule daySched : schedule) {
            for (TimeBlock tb : daySched) {
                sb.append("BEGIN:VEVENT\r\n");
                sb.append("DTSTART;TZID=America/Los_Angeles:").append(dateTimeFormatter(tb.getDate(), tb.getStartTime())).append("\r\n");
                sb.append("DTEND;TZID=America/Los_Angeles:").append(dateTimeFormatter(tb.getDate(), tb.getEndTime())).append("\r\n");
                sb.append("DTSTAMP:").append(dtstamp).append("\r\n");
                sb.append("UID:").append(tb.getUID()).append("\r\n");
                sb.append("SUMMARY:").append(tb.getName()).append("\r\n");
                sb.append("DESCRIPTION:").append(tb.getActivityType()).append("\r\n");
                sb.append("STATUS:CONFIRMED\r\n");
                sb.append("TRANSP:OPAQUE\r\n");
                sb.append("END:VEVENT\r\n");
                sb.append("\n");
            }
        }
        sb.append("END:VCALENDAR\r\n");

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(sb.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String dateTimeFormatter(ScheduleDate date, Time24 time) {
        return String.format("%04d%s%sT%s%s00",
                date.getYear(),
                date.getMonth() < 10 ? "0" + date.getMonth() : "" + date.getMonth(),
                date.getDate() < 10 ? "0" + date.getDate() : "" + date.getDate(),
                time.getHour() < 10 ? "0" + time.getHour() : "" + time.getHour(),
                time.getMinute() < 10 ? "0" + time.getMinute() : "" + time.getMinute());
    }
    
    
    public static String generateDTStamp() {
        java.time.ZonedDateTime nowUtc = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC);
        java.time.format.DateTimeFormatter formatter =
            java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        return nowUtc.format(formatter);
    }    
}
