package devloop0.cs61a.org.cs61a;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Sirjan on 1/23/2016.
 */

public class CS61BDictionaryParser extends DictionaryParser {
    private ArrayList<String[]> assignments;
    private String srcCode;
    PreferenceHolder preferenceHolder = null;
    String season = null, year = null;

    public CS61BDictionaryParser(String sc, PreferenceHolder ph) {
        srcCode = sc;
        assignments = new ArrayList<String[]>();
        String smallSource = findTable();
        preferenceHolder = ph;
        season = ph.getSeason() == PreferenceHolder.SeasonKind.KIND_FALL ? "fa" : "sp";
        year = ph.getTwoDigitYear();
        setAssignments(smallSource);
    }

    public void setAssignments(String source) {
        setHomeworks(source);
        setProjects(source);
        setLabs(source);
    }

    public void setHomeworks(String source) {
        while(true) {
            int index = source.indexOf("HW");
            if(index == -1) {
                break;
            }
            else {
                source = source.substring(index);
                int lastIndex = source.indexOf("</td");
                String hwInfo = source.substring(0, lastIndex);
                Log.i("hwInfo", hwInfo);
                String[] hwInformation = hwInfo.split(": | \\(");
                String name = hwInformation[0];
                if(name.equals("HW0")) { //TODO: Add custom thing for HW0
                    source = source.substring(lastIndex);
                    continue;
                }
                String description = hwInformation[1];
                lastIndex = hwInformation[2].indexOf(")");
                String dueDateString = hwInformation[2].substring(4, lastIndex);
                String endDate = processDate(dueDateString + "/" + year);
                String startDate = new SimpleDateFormat("MM/dd/yy").format(Calendar.getInstance().getTime());
                String link = "http://cs61b.ug/" + season + year + "/materials/hw/" + name + "/" + name + ".html";

                String[] assignList = {name, startDate, endDate, description, link, "Homework"};
                assignments.add(assignList);
                source = source.substring(lastIndex);
            }
        }
    }

    public void setProjects(String source) {
        while(true) {
            int index = source.indexOf("Project");
            if(index == -1) {
                break;
            }
            else {
                source = source.substring(index);
                int lastIndex = source.indexOf("</td");
                String projectInfo = source.substring(0, lastIndex);
                String[] projectInformation = projectInfo.split(": | \\(");
                String name = projectInformation[0];
                int projNum = name.charAt(8) - 48;
                String description = projectInformation[1];
                lastIndex = projectInformation[2].indexOf(")");
                String dueDateString = projectInformation[2].substring(4, lastIndex);
                String endDate = processDate(dueDateString + "/" + year);
                String startDate = new SimpleDateFormat("MM/dd/yy").format(Calendar.getInstance().getTime());
                String link = "http://cs61b.ug/" + season + year + "/materials/proj/proj" + projNum + "/proj" + projNum + ".html";

                String[] assignList = {name, startDate, endDate, description, link, "Project"};
                assignments.add(assignList);
                source = source.substring(lastIndex);
            }
        }
    }

    public void setLabs(String source) {

    }

    public String findTable() {
        int start = srcCode.indexOf("<tbody>");
        int end = srcCode.indexOf("</tbody>");
        return srcCode.substring(start, end);
    }

    @Override
    public ArrayList<String[]> getAssignments() {
        return assignments;
    }
}
