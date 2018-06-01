package services;

import models.Report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportFactory {
    public void buildReportFile(Report report, Path templatePath, Path reportToGeneratePath) {
        try {
            String content = new String(Files.readAllBytes(templatePath));

            content = content.replaceAll("_Test_Count_", String.valueOf(report.getTotalTestCount()));
            content = content.replaceAll("_Failed_Test_", String.valueOf(report.getFailedTestCount()));
            content = content.replaceAll("_Passed_Test_", String.valueOf(report.getPassedTestCount()));
            content = content.replaceAll("_Skipped_Test_", String.valueOf(report.getSkippedTestCount()));
            content = content.replaceAll("_Success_Rate_", String.valueOf(report.succesRate()));
            content = content.replaceAll("_Total_Time_", report.getTimeString());
            content = content.replaceAll("_Current_Date_",  LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")));
            content = content.replaceAll("_Specifications_", report.getSpecifications());

            Files.write(reportToGeneratePath, content.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
