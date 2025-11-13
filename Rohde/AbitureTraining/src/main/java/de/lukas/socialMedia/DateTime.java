package de.lukas.socialMedia;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DateTime class represents a date and time value.
 * Provides methods for comparison and manipulation of date/time values.
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class DateTime {
    private LocalDateTime dateTime;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /**
     * Creates a DateTime object with the current system date and time.
     * Format: dd.MM.yyyy HH:mm
     */
    public DateTime() {
        this.dateTime = LocalDateTime.now();
    }

    /**
     * Private constructor for creating DateTime from LocalDateTime.
     * Used internally for operations that return new DateTime objects.
     *
     * @param dateTime the LocalDateTime to wrap
     */
    private DateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Checks if this DateTime object equals the parameter.
     *
     * @param dt the DateTime object to compare with
     * @return true if both DateTime objects represent the same moment in time
     */
    public boolean isEquals(DateTime dt) {
        if (dt == null) return false;
        return this.dateTime.isEqual(dt.dateTime);
    }

    /**
     * Compares this DateTime with the parameter.
     *
     * @param dt the DateTime to compare with
     * @return negative value if this DateTime is before dt,
     *         0 if they are equal,
     *         positive value if this DateTime is after dt
     */
    public int compareTo(DateTime dt) {
        if (dt == null) return 1;
        return this.dateTime.compareTo(dt.dateTime);
    }

    /**
     * Checks if this DateTime is before the parameter.
     *
     * @param dt the DateTime to compare with
     * @return true if this DateTime is before dt
     */
    public boolean isBefore(DateTime dt) {
        if (dt == null) return false;
        return this.dateTime.isBefore(dt.dateTime);
    }

    /**
     * Checks if this DateTime is after the parameter.
     *
     * @param dt the DateTime to compare with
     * @return true if this DateTime is after dt
     */
    public boolean isAfter(DateTime dt) {
        if (dt == null) return true;
        return this.dateTime.isAfter(dt.dateTime);
    }

    /**
     * Returns a copy of this DateTime with the specified number of minutes added.
     *
     * @param minutes the number of minutes to add
     * @return a new DateTime object with the added minutes
     */
    public DateTime plusMinutes(long minutes) {
        return new DateTime(this.dateTime.plusMinutes(minutes));
    }

    /**
     * Returns a string representation of this DateTime in format "dd.MM.yyyy HH:mm".
     *
     * @return formatted date-time string
     */
    @Override
    public String toString() {
        return dateTime.format(FORMATTER);
    }
}
