package Services;

import Models.Report;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReportFactory {

  public void BuildReportFile(Report report, Path templatePath, Path reportToGeneratePath) {
    try {
      String content = new String(Files.readAllBytes(templatePath));

      content = content.replaceAll("_Test_Count_", String.valueOf(report.Total_Test_Count()));
      content = content.replaceAll("_Failed_Test_", String.valueOf(report.Failed_Test_Count));
      content = content.replaceAll("_Passed_Test_", String.valueOf(report.Passed_Test_Count));
      content = content.replaceAll("_Skipped_Test_", String.valueOf(report.Skipped_Test_Count));
      content = content.replaceAll("_Success_Rate_", String.valueOf(report.Succes_Rate()));
      content = content.replaceAll("_Total_Time_", report.GetTimeString());
      content = content.replaceAll("_Current_Date_", report.Current_Date);
      content = content.replaceAll("_Specifications_", report.Specifications);

      Files.write(reportToGeneratePath, content.getBytes());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
