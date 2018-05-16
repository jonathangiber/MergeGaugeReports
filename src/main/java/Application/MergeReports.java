package Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import Models.Report;
import Services.FileService;

public class MergeReports {

  public static final String REPORT_NAME_TO_FIND = "index.html";
  public static final String FOLDER_TO_LOOKUP = "src\\Reports\\ReportsLookUp";
  public static final String TEMPLATE_REPORT = "src\\Reports\\Template\\index.html";
  public static final String OUTPUT_REPORT_FILE = "src\\Reports\\Output\\Report_";
  public static final String OUTPUT_LOG_FILE = "src\\Reports\\Output\\Merge.log";

  public static void main(String[] args) {
    //1-Find reports in directory (*.index.html)
    //2-build model in memory from Html
    //3-Process all reports
    //4-build final report using Template.html

    String currentDate = new SimpleDateFormat("yyyy_MM_dd_HHmmss")
        .format(Calendar.getInstance().getTime());
    Services.FileService fileService = new FileService();
    Services.ReportFactory reportFactory = new Services.ReportFactory();
    Services.DomHTMLParser htmlParser = new Services.DomHTMLParser();
    List<Models.Report> reportsInMemory = new ArrayList<Models.Report>();

    File templatePath = new File(TEMPLATE_REPORT);
    //String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();

    File reportsLookUpPath = new File(FOLDER_TO_LOOKUP);
    fileService.findFiles(REPORT_NAME_TO_FIND, reportsLookUpPath);

    for (File reportFile : fileService.indexFiles) {
      Models.Report report = htmlParser.DomToReport(reportFile);
      reportsInMemory.add(report);
    }

    String reportName = OUTPUT_REPORT_FILE + currentDate + ".html";

    Path inputPath = Paths.get(TEMPLATE_REPORT);
    File outputFile = new File(reportName);
    try {
      outputFile.createNewFile();

      Path outputpath = Paths.get(reportName);

      Report finalReport = (new Services.ReportDomainService()).Merge(reportsInMemory);
      reportFactory.BuildReportFile(finalReport, inputPath, outputpath);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    LogReportsProcessed(reportsInMemory.size());
  }

  public static void LogReportsProcessed(int reportsCount) {
    Logger logger = Logger.getLogger("MergeLog");
    FileHandler fh;

    try {

      // This block configure the logger with handler and formatter
      fh = new FileHandler(OUTPUT_LOG_FILE, true);
      logger.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);

      String currentDateString = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
          .format(Calendar.getInstance().getTime());
      // the following statement is used to log any messages
      logger.info("Run at: " + currentDateString + " - " + reportsCount + " Reports Processed");

    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
