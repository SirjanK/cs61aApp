package devloop0.cs61a.org.cs61a;

/**
 * Created by devloop0 on 11/19/15.
 */
public class PreferenceHolder {
    boolean notificationsOn;
    long assignmentUrgencyThreshold, notificationRefreshFrequency;

    public PreferenceHolder(boolean n, long aut, long nrf) {
        notificationsOn = n;
        assignmentUrgencyThreshold = aut;
        notificationRefreshFrequency = nrf;
    }

    public boolean getNotificationsOn() {
        return notificationsOn;
    }

    public long getAssignmentUrgencyThreshold() {
        return assignmentUrgencyThreshold;
    }

    public long getNotificationRefreshFrequency() {
        return notificationRefreshFrequency;
    }
}
