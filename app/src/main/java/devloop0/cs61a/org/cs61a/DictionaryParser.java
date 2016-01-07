package devloop0.cs61a.org.cs61a;

import android.content.Intent;

import java.util.*;

/**
 * Created by Sirjan on 10/25/2015.
 * Takes the source code from HTMLParser.java
 * and uses the dictionary to generate lists
 */
public class DictionaryParser
{
    private ArrayList<String[]> assignments;
    private String srcCode;

    public DictionaryParser(String sc)
    {
        srcCode = sc;
        assignments = new ArrayList<String[]>();

        String dict = findDict();
        ArrayList<String> informalAssigns = generateObjectList(dict);
        setAssignmentList(informalAssigns);
    }

    public ArrayList<String[]> getAssignments()
    {
        return assignments;
    }

    private String findDict()
    {
        int startIndex = srcCode.indexOf("var assignments");
        int endIndex = srcCode.lastIndexOf("$(document)");
        return srcCode.substring(startIndex, endIndex);
    }

    private ArrayList<String> generateObjectList(String dict) {
        ArrayList<String> dictList = new ArrayList<String>();

        while (true) {
            int nextInd = dict.indexOf('{');
            int lastInd = dict.indexOf('}');
            if (nextInd == -1)
                break;
            else {
                String subSection = dict.substring(nextInd+1, lastInd);
                dictList.add(subSection);
                dict = dict.substring(lastInd+1);
            }
        }

        return dictList;
    }

    String generateAssignmentUrl(String postProcessedName, String type) {
        String prefix = "http://www.cs61a.org/";
        if(type == "Homework") {
            String number = postProcessedName.substring(postProcessedName.indexOf(" ")).trim();
            return prefix + "hw/hw" + (number.length() == 1 ? "0" + number : number) + "/";
        }
        else if(type == "Quiz") {
            String number = postProcessedName.substring(postProcessedName.indexOf(" ")).trim();
            return prefix + "quiz/quiz" + (number.length() == 1 ? "0" + number : number) + "/";
        }
        else if(type == "Project") {
            String project_name = postProcessedName.trim().toLowerCase().replaceAll(" ", "_");
            return prefix + "proj/" + project_name + "/";
        }
        else if(type == "Lab") {
            String number = postProcessedName.substring(postProcessedName.indexOf(" ")).trim();
            return prefix + "lab/lab" + (number.length() == 1 ? "0" + number : number) + "/";
        }
        return "";
    }

    private void setAssignmentList(ArrayList<String> dictList)
    {
        for(int i=0; i<dictList.size(); i++)
        {
            String assign = dictList.get(i);
            int[] indices = getImportantIndices(assign);

            String name = assign.substring(indices[2], indices[3]);
            String dueDate = assign.substring(indices[0], indices[1]);
            String startDate = assign.substring(indices[4], indices[5]);
            String type = type(name);
            String description = getDescription(name, type);
            name = postProcessName(name, type);
            String link = generateAssignmentUrl(name, type);
            dueDate = processDate(dueDate);
            startDate = processDate(startDate);

            String[] assignList = {name, startDate, dueDate, description, link, type};

            assignments.add(assignList);
        }
    }

    String postProcessName(String name, String type) {
        if(type == "Lab") {
            return name.substring(0, name.indexOf(":"));
        }
        return name;
    }

    private String getDescription(String name, String type) {
        if(type == "Homework") {
            return "Homework Assignment";
        }
        else if(type == "Quiz") {
            return "Quiz Assignment";
        }
        else if(type == "Project") {
            return "Project Assignment";
        }
        else if(type == "Lab") {
            return name.substring(name.indexOf(":") + 1).trim();
        }
        return "";
    }

    private String type(String name)
    {
        String indicator = name.substring(0, 3);
        if(indicator.equals("Lab"))
            return "Lab";
        else if(indicator.equals("Hom"))
            return "Homework";
        else if(indicator.equals("Qui"))
            return "Quiz";
        else
            return "Project";
    }

    private String processDate(String date) {
        int firstSlash = date.indexOf('/');
        if (date.substring(0, firstSlash).length() == 1) {
            date = "0" + date;
            firstSlash++;
        }

        int secondSlash = date.indexOf('/');

        if (date.substring(firstSlash, secondSlash).length() == 1)
            date = date.substring(0, firstSlash + 1) + "0" + date.substring(secondSlash - 1);

        return date;
    }

    private int[] getImportantIndices(String dict) {
        int dueB = 8;
        int dueE = dict.indexOf("link") - 4;
        int nameB = dict.indexOf("name") + 8;
        int nameE = dict.indexOf("release") - 4;
        int releaseB = dict.indexOf("release") + 11;
        int releaseE = dict.length() - 1;

        int[] indices = {dueB, dueE, nameB, nameE, releaseB, releaseE};
        return indices;
    }
}
