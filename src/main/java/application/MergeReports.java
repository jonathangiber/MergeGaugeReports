package application;

import models.Report;
import services.DomHTMLParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Paths.get;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

public class MergeReports {
    private static final Logger LOGGER = Logger.getLogger(MergeReports.class.getName());

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        try (InputStream ins = Files.newInputStream(get("etc", "merge-reports.properties"))) {
            properties.load(ins);
        }
        initLogger(properties.getProperty("reports.log"));

        Path toLook = get(properties.getProperty("reports.base.dir"));
        List<Report> reportsInMemory = buildReports(toLook);
        LOGGER.log(Level.INFO, " Found " + reportsInMemory.size() + " reports");
        if (!reportsInMemory.isEmpty()) {
            Path mergeReportPath = outputFile(properties);
            buildReportFile(merge(reportsInMemory), get(properties.getProperty("reports.template")), mergeReportPath);
            LOGGER.info("Total number of " + reportsInMemory.size() + " reports merged into " + mergeReportPath.toString());
        } else {
            LOGGER.log(Level.WARNING, "Found no reports in " + toLook + " to merge");
        }
    }

    private static List<Report> buildReports(Path toLook) throws IOException {
        if (!Files.isDirectory(toLook)) {
            LOGGER.log(Level.SEVERE, toLook + " does not exists or is no directory");
            return Collections.emptyList();
        }
        return findFiles("index.html", toLook)
                .map(DomHTMLParser::domToReport)
                .collect(Collectors.toList());
    }

    private static Path outputFile(Properties properties) {
        return get(properties.getProperty("reports.output.dir"), "report_" + now().format(ofPattern("yyyy_MM_dd_HHmmss")) + ".html");
    }

    private static void initLogger(String outputLog) throws IOException {
        FileHandler handler = new FileHandler(outputLog);
        handler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(handler);
    }

    private static Stream<Path> findFiles(String fileNameToMatch, Path baseDir) throws IOException {
        LOGGER.info("Looking for reports in " + baseDir + " for file " + fileNameToMatch);
        return Files.find(baseDir, 5, (path, attributes) -> attributes.isRegularFile() && path.endsWith(fileNameToMatch));
    }

    private static void buildReportFile(Report report, Path templatePath, Path reportToGeneratePath) throws IOException {
        String content = new String(Files.readAllBytes(templatePath));

        content = content.replaceAll("_Test_Count_", String.valueOf(report.getTotalTestCount()));
        content = content.replaceAll("_Failed_Test_", String.valueOf(report.getFailedTestCount()));
        content = content.replaceAll("_Passed_Test_", String.valueOf(report.getPassedTestCount()));
        content = content.replaceAll("_Skipped_Test_", String.valueOf(report.getSkippedTestCount()));
        content = content.replaceAll("_Success_Rate_", String.valueOf(report.succesRate()));
        content = content.replaceAll("_Total_Time_", report.getTimeString());
        content = content.replaceAll("_Current_Date_", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")));
        content = content.replaceAll("_Specifications_", report.getSpecifications());

        Files.createDirectories(reportToGeneratePath.getParent());
        Files.write(reportToGeneratePath, content.getBytes());
    }

    private static Report merge(List<Report> reports) {
        Report finalReport = new Report();
        reports.stream().forEach(finalReport::merge);
        return finalReport;
    }
}
