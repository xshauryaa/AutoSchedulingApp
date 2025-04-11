package model;

import java.util.Objects;

/**
 * Represents a time in 24-hour format (e.g., 0930 for 9:30 AM, 1745 for 5:45 PM).
 */
public class Time24 {
    private int hour;   // the hours part of the time
    private int minute; // the minutes part of the time

    /**
     * @param time the time in 24-hour int format (e.g., 930, 1745)
     * EFFECTS: constructs a Time24 object with given hour and minute
     */
    public Time24(int time) {
        this.hour = time / 100;
        this.minute = time % 100;
        normalize();
    }

    /**
     * @param mins number of minutes to add
     * MODIFIES: this
     * EFFECTS: adds the given number of minutes to the current time
     */
    public void addMinutes(int mins) {
        int total = hour * 60 + minute + mins;
        this.hour = total / 60;
        this.minute = total % 60;
        normalize();
    }

    /**
     * @param mins number of minutes to subtract
     * MODIFIES: this
     * EFFECTS: subtracts the given number of minutes from the current time
     */
    public void subtractMinutes(int mins) {
        int total = hour * 60 + minute - mins;
        this.hour = total / 60;
        this.minute = total % 60;
        normalize();
    }

    /**
     * MODIFIES: this
     * EFFECTS: adjusts hour and minute fields to stay within valid 24-hour time ranges
     */
    private void normalize() {
        if (minute >= 60) {
            hour += minute / 60;
            minute = minute % 60;
        } else if (minute < 0) {
            int borrow = (Math.abs(minute) + 59) / 60;
            hour -= borrow;
            minute += borrow * 60;
        }

        if (hour < 0) {
            hour = 0;
            minute = 0;
        }
    }

    /**
     * @return the hour component of the time
     */
    public int getHour() {
        return hour;
    }

    /**
     * @return the minute component of the time
     */
    public int getMinute() {
        return minute;
    }

    /**
     * @param other the time to compare with
     * @return true if this time is before the other time
     */
    public boolean isBefore(Time24 other) {
        return this.toInt() < other.toInt();
    }

    /**
     * @param other the time to compare with
     * @return true if this time is after the other time
     */
    public boolean isAfter(Time24 other) {
        return this.toInt() > other.toInt();
    }

    /**
     * @return time in 24-hour integer format (e.g., 930, 1745)
     */
    public int toInt() {
        return hour * 100 + minute;
    }

    /**
     * @return a copy of this Time24 object
     */
    public Time24 copy() {
        return new Time24(this.toInt());
    }

    /**
     * @param obj the object to be compared
     * @return true if the object is a Time24 with the same hour and minute
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Time24)) return false;
        Time24 other = (Time24) obj;
        return this.hour == other.hour && this.minute == other.minute;
    }

    /**
     * @return hash code for this Time24 object
     */
    @Override
    public int hashCode() {
        return Objects.hash(hour, minute);
    }

    @Override
    public String toString() {
        String minutePart = (minute < 10) ? "0" + minute : String.valueOf(minute);
        String result = this.hour + ":" + minutePart; 
        return result;
    }
}
