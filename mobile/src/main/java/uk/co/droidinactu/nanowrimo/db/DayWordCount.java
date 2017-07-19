package uk.co.droidinactu.nanowrimo.db;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Stores the number of words written on a specified date
 * <p>
 * Created by aspela on 12/07/17.
 */
public class DayWordCount extends AbstractDbObj {
    private static final String LOG_TAG = DayWordCount.class.getSimpleName() + ":";

    private String date;
    private int wordcount; // number of words written that day

    public DayWordCount() {
    }

    public DayWordCount(String pDate, int pWrdCount) {
        this.date = pDate;
        this.wordcount = pWrdCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWordcount() {
        return wordcount;
    }

    public void setDaysWordCount(int pWrdCount) {
        this.wordcount = pWrdCount;
    }

    public int getDayNumber() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime dt = formatter.parseDateTime(date);
        return dt.getDayOfMonth();
    }

    public int getMonthNumber() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime dt = formatter.parseDateTime(date);
        return dt.getMonthOfYear();
    }

    public int getYearNumber() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime dt = formatter.parseDateTime(date);
        return dt.getYear();
    }

}
