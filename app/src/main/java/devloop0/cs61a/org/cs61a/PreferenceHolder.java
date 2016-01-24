package devloop0.cs61a.org.cs61a;

/**
 * Created by devloop0 on 11/19/15.
 */
public class PreferenceHolder {
    boolean notificationsOn;
    long assignmentUrgencyThreshold;
    String currentClass;

    public PreferenceHolder(boolean n, long aut, String c) {
        notificationsOn = n;
        assignmentUrgencyThreshold = aut;
        currentClass = c;
    }

    public boolean getNotificationsOn() {
        return notificationsOn;
    }

    public long getAssignmentUrgencyThreshold() {
        return assignmentUrgencyThreshold;
    }

    public String getCurrentClass() { return currentClass; }
}
