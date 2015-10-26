package devloop0.cs61a.org.cs61a;

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
        for(String[] data : assignmentDataList) {
            assignmentArrayList.add(new Assignment(data[0], Long.parseLong(data[1]), Long.parseLong(data[2]), data[3],
                    (data[4] == "Lab" ? Assignment.AssignmentKind.ASSIGNMENT_KIND_LAB : (data[4] == "Homework" ? Assignment.AssignmentKind.ASSIGNMENT_KIND_HOMEWORK :
                            (data[4] == "Quiz" ? Assignment.AssignmentKind.ASSIGNMENT_KIND_QUIZ : ( data[4] == "Project" ? Assignment.AssignmentKind.ASSIGNMENT_KIND_PROJECT : Assignment.AssignmentKind.ASSIGNMENT_KIND_NONE))))));
        }
    }

    public ArrayList<Assignment> getAssignmentList() {
        return assignmentArrayList;
    }
}
