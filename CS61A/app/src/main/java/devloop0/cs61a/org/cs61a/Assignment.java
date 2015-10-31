package devloop0.cs61a.org.cs61a;

import android.text.format.DateFormat;
import java.util.Date;
/**
 * Created by nathr on 10/24/2015.
 */

public class Assignment {
    String assignmentName;
    long releaseTime, dueTime; // time in milliseconds
    String description;
    AssignmentKind assignmentKind;
    boolean seen;

    public enum AssignmentKind {
        ASSIGNMENT_KIND_LAB, ASSIGNMENT_KIND_QUIZ, ASSIGNMENT_KIND_HOMEWORK, ASSIGNMENT_KIND_PROJECT, ASSIGNMENT_KIND_NONE
    }

    public Assignment(String an, long rd, long dd, String d, AssignmentKind ak) {
        assignmentName = an;
        releaseTime = rd;
        dueTime = dd;
        description = d;
        assignmentKind = ak;
        seen = false;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public long getDueTime() {
        return dueTime;
    }

    public String getDescription() {
        return description;
    }

    public Date getReleaseDate() {
        return new Date(releaseTime);
    }

    public Date getDueDate() {
        return new Date(dueTime);
    }

    public String getFormattedDueDateString() {
        return (String) DateFormat.format("MM/dd/yy", dueTime);
    }

    public String getFormattedReleaseDateString() {
        return (String) DateFormat.format("MM/dd/yy", releaseTime);
    }

    public AssignmentKind getAssignmentKind() {
        return assignmentKind;
    }

    public boolean toggleSeen() {
        seen = !seen;
        return seen;
    }

    public boolean seen() {
        return seen;
    }

    public String toString()
    {
        return "Name: " + assignmentName + " Release Date: " + releaseTime + " Due Date: " + dueTime + " description: " + description + " type: " + assignmentKind;
    }
}
