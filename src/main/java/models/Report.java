package models;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Report {
    private int failedTestCount = 0;
    private int passedTestCount = 0;
    private int skippedTestCount = 0;
    private int totalTimeSeconds = 0;
    private String specifications = "";

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public int getFailedTestCount() {
        return failedTestCount;
    }

    public int getSkippedTestCount() {
        return skippedTestCount;
    }

    public void setSkippedTestCount(int skippedTestCount) {
        this.skippedTestCount = skippedTestCount;
    }

    public void setPassedTestCount(int passedTestCount) {
        this.passedTestCount = passedTestCount;
    }

    public void setFailedTestCount(int failedTestCount) {
        this.failedTestCount = failedTestCount;
    }

    public int getTotalTestCount() {
        return failedTestCount + passedTestCount + skippedTestCount;
    }

    public int getPassedTestCount() {
        return passedTestCount;
    }


    public void setTime(String time) {
        String[] timeSplit = time.split(":");
        totalTimeSeconds += Integer.parseInt(timeSplit[0]) * 60 * 60;
        totalTimeSeconds += Integer.parseInt(timeSplit[1]) * 60;
        totalTimeSeconds += Integer.parseInt(timeSplit[2]);
    }

    public float succesRate() {
        return ((float) passedTestCount / (float) getTotalTestCount() * 100);
    }

    public String getTimeString() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(totalTimeSeconds * 1000L));
        return time;
    }

    public void merge(Report report) {
        failedTestCount += report.failedTestCount;
        passedTestCount += report.passedTestCount;
        skippedTestCount += report.skippedTestCount;
        totalTimeSeconds += report.totalTimeSeconds;

        if (report.specifications != null && !report.specifications.isEmpty()) {
            specifications += report.specifications;
        }
    }
}
