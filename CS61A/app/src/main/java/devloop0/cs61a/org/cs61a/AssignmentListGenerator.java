package devloop0.cs61a.org.cs61a;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

/**
 * Created by nathr on 10/25/2015.
 */
public class AssignmentListGenerator {

    ArrayList<Assignment> assignmentArrayList;

    // Day -1 minute in milliseconds (assignments are due at 11:59 PM the day of the due date
    final long dayMinusOneMinute = (3600 * 24 - 60) * 1000;

    // String array format:
    //  1. Assignment name
    //  2. Release date (String)
    //  3. Due date (String)
    //  4. Assignment description
    //  5. Assignment kind (string) Lab/Homework/Project/Quiz
    public AssignmentListGenerator(ArrayList<String[]> assignmentDataList) {
        assignmentArrayList = new ArrayList<Assignment>();
        try {
            for (String[] data : assignmentDataList) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
                assignmentArrayList.add(new Assignment(data[0], simpleDateFormat.parse(data[1]).getTime(), simpleDateFormat.parse(data[2]).getTime() + dayMinusOneMinute, data[3], data[4],
                        (data[5] == "Lab" ? Assignment.AssignmentKind.ASSIGNMENT_KIND_LAB : (data[5] == "Homework" ? Assignment.AssignmentKind.ASSIGNMENT_KIND_HOMEWORK :
                                (data[5] == "Quiz" ? Assignment.AssignmentKind.ASSIGNMENT_KIND_QUIZ : (data[5] == "Project" ? Assignment.AssignmentKind.ASSIGNMENT_KIND_PROJECT : Assignment.AssignmentKind.ASSIGNMENT_KIND_NONE))))));
            }
            Collections.sort(assignmentArrayList, new Comparator<Assignment>() {
                @Override
                public int compare(Assignment lhs, Assignment rhs) {
                    if(lhs.getReleaseTime() < rhs.getReleaseTime()) {
                        return -1;
                    }
                    else if(lhs.getReleaseTime() > rhs.getReleaseTime()) {
                        return 1;
                    }
                    else {
                        return 1;
                    }
                }
            });
            long currentTimeInMilliseconds = Calendar.getInstance().getTimeInMillis();
            int first = -1;
            for(int i = 0; i < assignmentArrayList.size(); i++) {
                Assignment assignment = assignmentArrayList.get(i);
                if(assignment.getReleaseTime() >= currentTimeInMilliseconds && assignment.getDueTime() < currentTimeInMilliseconds) {

                }
                else if(assignment.getDueTime() > currentTimeInMilliseconds) {
                    if(first == -1) {
                        first = i;
                        break;
                    }
                }
            }
            ArrayList<Assignment> end = new ArrayList<Assignment>(assignmentArrayList.subList(0, first));
            ArrayList<Assignment> begin = new ArrayList<Assignment>(assignmentArrayList.subList(first, assignmentArrayList.size()));
            assignmentArrayList.clear();
            assignmentArrayList.addAll(begin);
            assignmentArrayList.addAll(end);
            Log.i("ArrayList", assignmentArrayList.toString());
        }
        catch(ParseException ex) {
            Log.e("Date parsing failed", ex.getMessage());
        }
    }

    public ArrayList<Assignment> getAssignmentList() {
        return assignmentArrayList;
    }
}
