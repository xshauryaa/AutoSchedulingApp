package model;

import java.util.List;
import java.util.Arrays;

public class ScheduleDate {

    private int date;
    private int month;
    private int year;

    /**
     * Creates a new date with givne date, month, and year.
     * @param date
     * @param month
     * @param year
     */
    public ScheduleDate(int date, int month, int year) {
        this.date = date;
        this.month = month;
        this.year = year;
    }

    /**
     * @return this date in string format
     * e.g. ScheduleDate(23, 4, 2025) is returned as "23-4-2025"
     */
    public String getDateString() {
        return this.date + "-" + this.month + "-" + this.year;
    }

    /**
     * @return the numeric date of this full date
     */
    public int getDate() {
        return this.date;
    }

    /**
     * @return the month of this date
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * @return the year of this date
     */
    public int getYear() {
        return this.year;
    }

    /**
     * @param date the date to be compared
     * @return true if this date is before the given date
     */
    public boolean isBefore(ScheduleDate date) {
        if (this.year < date.getYear()) {
            return true;
        } else if (this.year == date.getYear()) {
            if (this.month < date.getMonth()) {
                return true;
            } else if (this.month == date.getMonth()) {
                if (this.date < date.getDate()) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * @param date the date to be compared
     * @return true if this date is after the given date
     */
    public boolean isAfter(ScheduleDate date) {
        if (this.year > date.getYear()) {
            return true;
        } else if (this.year == date.getYear()) {
            if (this.month > date.getMonth()) {
                return true;
            } else if (this.month == date.getMonth()) {
                if (this.date > date.getDate()) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * @return the date of the day after this date
     */
    public ScheduleDate getNextDate() {
        Integer[] array1 = {4, 6, 9, 11};
        List<Integer> monthsWith30Days = Arrays.asList(array1);
        Integer[] array2 = {1, 3, 5, 7, 8, 10, 12};
        List<Integer> monthsWith31Days = Arrays.asList(array2);

        int returnDate = 0;
        int returnMonth = 0;
        int returnYear = 0;
        
        if (monthsWith30Days.contains(this.month) && this.date == 30) {
            returnDate = 1;
            returnMonth = this.month + 1;
            returnYear = this.year;
        } else if (monthsWith31Days.contains(this.month) && this.date == 31) {
            if (month == 12) {
                returnDate = 1;
                returnMonth = 1;
                returnYear = this.year + 1;
            } else {
                returnDate = 1;
                returnMonth = this.month + 1;
                returnYear = this.year;
            }
        } else if (this.month == 2) {
            if (this.year % 4 == 0) {
                if (this.date == 29) {
                    returnDate = 1;
                    returnMonth = this.month + 1;
                    returnYear = this.year;
                } else {
                    returnDate = this.date + 1;
                    returnMonth = this.month;
                    returnYear = this.year;
                }
            } else if (this.date == 28) {
                returnDate = 1;
                returnMonth = this.month + 1;
                returnYear = this.year;
            } else {
                returnDate = this.date + 1;
                returnMonth = this.month;
                returnYear = this.year;
            }
        } else {
            returnDate = this.date + 1;
            returnMonth = this.month;
            returnYear = this.year;
        }

        return new ScheduleDate(returnDate, returnMonth, returnYear);
    }

    /**
     * @return true if the given date is equal to the given object, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        else if (getClass() != obj.getClass()) { return false; }
        else {
            ScheduleDate other = (ScheduleDate) obj;
            return this.date == other.getDate() &&
                    this.month == other.getMonth() && 
                    this.year == other.getYear();
        }
    } 

    @Override
    public int hashCode() {
        return java.util.Objects.hash(date, month, year);
    }
}