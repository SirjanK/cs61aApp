package devloop0.cs61a.org.cs61a;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Sirjan on 1/23/2016.
 */

public class CS61BParser extends DictionaryParser {
    private ArrayList<String[]> assignments;
    private String srcCode;
    PreferenceHolder preferenceHolder = null;
    String season = null, year = null;

    public CS61BParser(String sc, PreferenceHolder ph) {
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

                if(name.equals("HW0")) { //NOTE: Add custom procedure for HW0 or get rid of it
                    source = source.substring(lastIndex);
                    continue;
                }

                Log.i("name", name);
                char hwNum = name.charAt(3);
                Log.i("hwNum", hwNum + "");

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
        while(true) {
            int weekIndex = source.indexOf("<!-- Week");
            if(weekIndex == -1) {
                break;
            }
            source = source.substring(weekIndex);
            int weekNum = source.charAt(10) - 48;
            String dueDate = processDate(getDateForWeek(weekNum) + "/2016");

            int labIndex = source.indexOf("materials/lab");
            if(labIndex == -1) {
                source = source.substring(weekIndex + 1);
            }
            else{
                source = source.substring(labIndex);
            }
            while(labIndex != -1) {
                int linkCloseIndex = source.indexOf("\">");
                Log.i("linkCloseIndex", linkCloseIndex+"");
                String link = "http://cs61b.ug/" + season + year + "/" + source.substring(0, linkCloseIndex);
                source = source.substring(14);
                String sourceName = source.substring(0, source.indexOf("/lab"));
                String name = "Lab " + sourceName.substring(3);
                String description = source.substring(source.indexOf("\">") + 2, source.indexOf("<"));
                String startDate = new SimpleDateFormat("MM/dd/yy").format(Calendar.getInstance().getTime());
                String[] assignList = {name, startDate, dueDate, description, link, "Lab"};
                assignments.add(assignList);
                labIndex = source.indexOf("materials/lab");
                if(labIndex > source.indexOf("<!-- Week")) {
                    labIndex = -1;
                }
                else if(labIndex != -1) {
                    source = source.substring(labIndex);
                }
                Log.i("LabName", name);
            }
        }
    }

    public String getDateForWeek(int week) {
        String weekComment = "<!-- Week " + week + " -->";
        int index = srcCode.indexOf(weekComment);
        String srcSnippet = srcCode.substring(index);
        int fridayIndex = srcSnippet.indexOf("<td>Fri");
        String fridaySnippet = srcSnippet.substring(fridayIndex);
        int closeIndex = fridaySnippet.indexOf("</td");

        return fridaySnippet.substring(8, closeIndex);
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
