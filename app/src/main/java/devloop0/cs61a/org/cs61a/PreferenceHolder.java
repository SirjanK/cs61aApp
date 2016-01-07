package devloop0.cs61a.org.cs61a;

/**
 * Created by devloop0 on 11/19/15.
 */
public class PreferenceHolder {
    boolean notificationsOn;
    long assignmentUrgencyThreshold;

    public PreferenceHolder(boolean n, long aut) {
        notificationsOn = n;
        assignmentUrgencyThreshold = aut;
    }

    public boolean getNotificationsOn() {
        return notificationsOn;
    }

    public long getAssignmentUrgencyThreshold() {
        return assignmentUrgencyThreshold;
    }
}
