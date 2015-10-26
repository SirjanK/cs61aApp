package devloop0.cs61a.org.cs61a;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by nathr on 10/25/2015.
 */
public class AssignmentListGenerator {

    ArrayList<Assignment> assignmentArrayList;

    // String array format:
    //  1. Assignment name
    //  2. Release date (milliseconds)
    //  3. Due date (milliseconds)
    //  4. Assignment description
    //  5. Assignment kind (string) Lab/Homework/Project/Quiz
    public AssignmentListGenerator(ArrayList<String[]> assignmentDataList) {
        try {
            for (String[] data : assignmentDataList) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
                assignmentArrayList.add(new Assignment(data[0], simpleDateFormat.parse(data[1]).getTime(), simpleDateFormat.parse(data[2]).getTime(), data[3],
                        (data[4] == "Lab" ? Assignment.AssignmentKind.ASSIGNMENT_KIND_LAB : (data[4] == "Homework" ? Assignment.AssignmentKind.ASSIGNMENT_KIND_HOMEWORK :
                                (data[4] == "Quiz" ? Assignment.AssignmentKind.ASSIGNMENT_KIND_QUIZ : (data[4] == "Project" ? Assignment.AssignmentKind.ASSIGNMENT_KIND_PROJECT : Assignment.AssignmentKind.ASSIGNMENT_KIND_NONE))))));
            }
        }
        catch(ParseException ex) {
            Log.e("Date parsing failed", ex.getMessage());
        }
    }

    public ArrayList<Assignment> getAssignmentList() {
        return assignmentArrayList;
    }
}