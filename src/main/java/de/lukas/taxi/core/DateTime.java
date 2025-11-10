package de.lukas.taxi.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Wrapper class for date and time handling.
 * Uses java.time internally but provides a simple interface.
 */
public class DateTime {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final LocalDateTime dateTime;

    /**
     * Creates a DateTime with the current system date and time.
     */
    public DateTime() {
        this.dateTime = LocalDateTime.now();
    }

    /**
     * Creates a DateTime from a string in format "dd.MM.yyyy HH:mm".
     *
     * @param datetime the date and time string
     * @throws IllegalArgumentException if the format is invalid
     */
    public DateTime(String datetime) {
        try {
            this.dateTime = LocalDateTime.parse(datetime, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid datetime format. Expected: dd.MM.yyyy HH:mm", e);
        }
    }

    /**
     * Returns the date and time in format "dd.MM.yyyy HH:mm".
     *
     * @return formatted date and time string
     */
    @Override
    public String toString() {
        return dateTime.format(FORMATTER);
    }

    /**
     * Gets the internal LocalDateTime object.
     *
     * @return the LocalDateTime
     */
    public LocalDateTime getLocalDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTime dateTime1 = (DateTime) o;
        return dateTime.equals(dateTime1.dateTime);
    }

    @Override
    public int hashCode() {
        return dateTime.hashCode();
    }
}
