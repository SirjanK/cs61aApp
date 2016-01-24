package devloop0.cs61a.org.cs61a;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Sirjan on 1/23/2016.
 */

public class CS61BDictionaryParser extends DictionaryParser {
    private ArrayList<String[]> assignments;
    private String srcCode;

    public CS61BDictionaryParser(String sc) {
        srcCode = sc;
        assignments = new ArrayList<String[]>();
        String smallSource = findTable();
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
                int lastIndex = source.indexOf("</tc");
                String hwInfo = source.substring(index, lastIndex);
                String[] hwInformation = hwInfo.split(": | \\(");
                String name = hwInformation[0];
                if(name.equals("HW0")) { //TODO: Add custom thing for HW0
                }
                String description = hwInformation[1];
                String dueDateString = hwInformation[2].substring(4, lastIndex - 1);
                String endDate = processDate(dueDateString + "/2016");
                String startDate = "01/23/2016"; //TODO: Change this
                String link = "http://cs61b.ug/sp16/materials/hw/" + name + "/" + name + ".html";

                String[] assignList = {name, startDate, endDate, description, link, "Homework"};
                assignments.add(assignList);
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
                int lastIndex = source.indexOf("</tc");
                String projectInfo = source.substring(index, lastIndex);
                String[] projectInformation = projectInfo.split(": | \\(");
                String name = projectInformation[0];
                int projNum = name.charAt(8) - 48;
                String description = projectInformation[1];
                String dueDateString = projectInformation[2].substring(4, lastIndex - 1);
                String endDate = processDate(dueDateString + "/2016");
                String startDate = "01/23/2016"; //TODO: Change this
                String link = "http://cs61b.ug/sp16/materials/proj/proj" + projNum + "/proj" + projNum + ".html";

                String[] assignList = {name, startDate, endDate, description, link, "Project"};
                assignments.add(assignList);
            }
        }
    }

    public void setLabs(String source) {

    }

    public String findTable() {
        int start = srcCode.indexOf("<tbody>");
        int end = srcCode.lastIndexOf("<tbody>");
        return srcCode.substring(start, end);
    }

    @Override
    public ArrayList<String[]> getAssignments() {
        return assignments;
    }
}
