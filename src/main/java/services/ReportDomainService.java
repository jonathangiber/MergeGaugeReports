package services;

import java.util.List;

import models.Report;

public class ReportDomainService {
    public Report merge(List<Report> reports) {
        Report finalReport = new Report();
        for (Report report : reports) {
            finalReport.merge(report);
        }
        return finalReport;
    }
}
