package Services;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import Models.Report;

public class DomHTMLParser {

  public Report DomToReport(File input) {
    try {

      Report report = new Report();

      Document doc = Jsoup.parse(input, "UTF-8");

      Elements failed = doc.getElementsByClass("fail spec-filter");
      String failedValue = failed.get(0).child(0).html();

      report.Failed_Test_Count = Integer.parseInt(failedValue);

      Elements passed = doc.getElementsByClass("pass spec-filter");
      String passedValue = passed.get(0).child(0).html();

      report.Passed_Test_Count = Integer.parseInt(passedValue);

      Elements skipped = doc.getElementsByClass("skip spec-filter");
      String skippedValue = skipped.get(0).child(0).html();

      report.Skipped_Test_Count = Integer.parseInt(skippedValue);

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
      report.SetTime(Time);

      Elements specifications = doc.getElementById("scenarios").getElementsByTag("li");

      if (specifications.size() > 0) {
        report.Specifications = specifications.outerHtml();
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
