package model;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;

public class ScheduleDateTest {
    
    private ScheduleDate testDate;

    @Test
    void testConstructor() {
        testDate = new ScheduleDate(1, 1, 2025);
        assertEquals(1, testDate.getDate());
        assertEquals(1, testDate.getMonth());
        assertEquals(2025, testDate.getYear());
    }

    @Test
    void testGetString() {
        testDate = new ScheduleDate(1, 1, 2025);
        assertEquals("1-1-2025", testDate.getDateString());

        testDate = new ScheduleDate(23, 4, 2025);
        assertEquals("23-4-2025", testDate.getDateString());
    }

    @Test
    void testNextDate() {
        // Boundary Cases
        testDate = new ScheduleDate(31, 1, 2025);
        assertEquals(new ScheduleDate(1, 2, 2025), testDate.getNextDate());

        testDate = new ScheduleDate(30, 4, 2025);
        assertEquals(new ScheduleDate(1, 5, 2025), testDate.getNextDate());

        testDate = new ScheduleDate(27, 2, 2025);
        assertEquals(new ScheduleDate(28, 2, 2025), testDate.getNextDate());

        testDate = new ScheduleDate(28, 2, 2025);
        assertEquals(new ScheduleDate(1, 3, 2025), testDate.getNextDate());

        testDate = new ScheduleDate(29, 2, 2024);
        assertEquals(new ScheduleDate(1, 3, 2024), testDate.getNextDate());

        testDate = new ScheduleDate(28, 2, 2024);
        assertEquals(new ScheduleDate(29, 2, 2024), testDate.getNextDate());

        testDate = new ScheduleDate(31, 12, 2025);
        assertEquals(new ScheduleDate(1, 1, 2026), testDate.getNextDate());

        // General Cases
        testDate = new ScheduleDate(23, 5, 2023);
        assertEquals(new ScheduleDate(24, 5, 2023), testDate.getNextDate());

        testDate = new ScheduleDate(9, 11, 2025);
        assertEquals(new ScheduleDate(10, 11, 2025), testDate.getNextDate());
    }
}
