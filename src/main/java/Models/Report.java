package Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Report {

  public int Failed_Test_Count;
  public int Passed_Test_Count;
  public int Skipped_Test_Count;
  public int Total_Time_Seconds;

  public int Total_Test_Count() {
    return Failed_Test_Count + Passed_Test_Count + Skipped_Test_Count;
  }

  public void SetTime(String time) {
    String[] timeSplit = time.split(":");
    Total_Time_Seconds += Integer.parseInt(timeSplit[0]) * 60 * 60;
    Total_Time_Seconds += Integer.parseInt(timeSplit[1]) * 60;
    Total_Time_Seconds += Integer.parseInt(timeSplit[2]);
  }

  public float Succes_Rate() {
    return ((float) Passed_Test_Count / (float) Total_Test_Count() * 100);
  }

  public String GetTimeString() {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    df.setTimeZone(tz);
    String time = df.format(new Date(Total_Time_Seconds * 1000L));

    return time;
  }

  public String Current_Date = new SimpleDateFormat("MMM dd, yyyy hh:mm a")
      .format(Calendar.getInstance().getTime());
  public String Specifications = "";
}
