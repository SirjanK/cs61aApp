package devloop0.cs61a.org.cs61a;

import java.util.*;

/**
 * Created by Sirjan on 10/25/2015.
 * Takes the source code from HTMLParser.java
 * and uses the dictionary to generate lists
 */
public class DictionaryParser
{
    private ArrayList<String[]> assignments;
    String srcCode;

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

    public String findDict()
    {
        int startIndex = srcCode.indexOf("var assignments");
        int endIndex = srcCode.indexOf("($document)");

        return srcCode.substring(startIndex, endIndex);
    }

    public ArrayList<String> generateObjectList(String dict) {
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

    public void setAssignmentList(ArrayList<String> dictList)
    {
        for(int i=0; i<dictList.size(); i++)
        {
            String assign = dictList.get(i);
            int[] indices = getImportantIndices(assign);

            String name = assign.substring(indices[2], indices[3]);
            String dueDate = assign.substring(indices[0], indices[1]);
            String startDate = assign.substring(indices[4], indices[5]);
            String type = type(name);

            dueDate = processDate(dueDate);
            startDate = processDate(startDate);

            String[] assignList = {name, startDate, dueDate, "", type};

            assignments.add(assignList);
        }
    }

    public String type(String name)
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

    public String processDate(String date) {
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

    public int[] getImportantIndices(String dict) {
        int dueB = 8;
        int dueE = dict.indexOf("link") - 3;
        int nameB = dict.indexOf("name") + 8;
        int nameE = dict.indexOf("release") - 3;
        int releaseB = dict.indexOf("release") + 12;
        int releaseE = dict.length() - 1;

        int[] indices = {dueB, dueE, nameB, nameE, releaseB, releaseE};
        return indices;
    }
}
