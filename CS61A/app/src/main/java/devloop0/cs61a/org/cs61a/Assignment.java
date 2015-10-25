package devloop0.cs61a.org.cs61a;

import java.util.Date;
/**
 * Created by nathr on 10/24/2015.
 */
public class Assignment {
    String assignmentName;
    long releaseTime, dueTime; // time in milliseconds
    String description;

    public Assignment(String an, long rd, long dd, String d) {
        assignmentName = an;
        releaseTime = rd;
        dueTime = dd;
        description = d;
    }

    String getAssignmentName() {
        return assignmentName;
    }

    long getReleaseTime() {
        return releaseTime;
    }

    long getDueTime() {
        return dueTime;
    }

    String getDescription() {
        return description;
    }

    Date getReleaseDate() {
        return new Date(releaseTime);
    }

    Date getDueDate() {
        return new Date(dueTime);
    }
}
