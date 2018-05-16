package Services;

import java.util.List;

import Models.Report;

public class ReportDomainService {

  public Report Merge(List<Report> reports) {
    Report finalReport = new Report();
    for (Report report : reports) {
      finalReport.Failed_Test_Count += report.Failed_Test_Count;
      finalReport.Passed_Test_Count += report.Passed_Test_Count;
      finalReport.Skipped_Test_Count += report.Skipped_Test_Count;
      finalReport.Total_Time_Seconds += report.Total_Time_Seconds;

      if (report.Specifications != null && !report.Specifications.isEmpty()) {
        finalReport.Specifications += report.Specifications;
      }
    }
    return finalReport;
  }
}
