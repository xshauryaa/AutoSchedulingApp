package model;

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
}