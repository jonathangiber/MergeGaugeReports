package services;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import models.Report;

public class DomHTMLParser {
    public Report domToReport(File input) {
        try {

            Report report = new Report();

            Document doc = Jsoup.parse(input, "UTF-8");

            Elements failed = doc.getElementsByClass("fail spec-filter");
            String failedValue = failed.get(0).child(0).html();

            report.setFailedTestCount(Integer.parseInt(failedValue));

            Elements passed = doc.getElementsByClass("pass spec-filter");
            String passedValue = passed.get(0).child(0).html();

            report.setPassedTestCount(Integer.parseInt(passedValue));

            Elements skipped = doc.getElementsByClass("skip spec-filter");
            String skippedValue = skipped.get(0).child(0).html();

            report.setSkippedTestCount(Integer.parseInt(skippedValue));

            Elements reportDetailLiNodes = doc.getElementsByClass("report_details").get(0)
                    .getElementsByTag("li");
            String Time = "";
            for (Element nodeli : reportDetailLiNodes) {
                for (Node nodeval : nodeli.childNodes()) {
                    if (nodeval.childNodeSize() > 0) {
                        if (nodeval.childNode(0).outerHtml().toLowerCase().contains("total time")) {
                            Time = nodeval.parentNode().childNode(3).childNode(0).outerHtml();
                        }
                    }

                }
            }
            //String Time = reportDetailLiNodes.get(2).childNode(3).childNode(0).outerHtml();
            //.childNode(3).outerHtml();
            report.setTime(Time);

            Elements specifications = doc.getElementById("scenarios").getElementsByTag("li");

            if (specifications.size() > 0) {
                report.specifications = specifications.outerHtml();
            }
			/*
			for (Node spec :specifications.child(3).childNodes()) 
			{
				spec.toString()
			}
			*/
            return report;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
