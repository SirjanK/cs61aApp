package devloop0.cs61a.org.cs61a;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by devloop0 on 11/19/15.
 */
public class PreferenceHolder {
    boolean notificationsOn;
    long assignmentUrgencyThreshold;
    String currentClass;
    boolean serveStaticPage;
    public enum SeasonKind {
        KIND_FALL, KIND_SPRING
    }
    SeasonKind seasonKind = null;
    String fourDigitYear = null, twoDigitYear = null;

    public PreferenceHolder(boolean n, long aut, String c, boolean ssp) {
        notificationsOn = n;
        assignmentUrgencyThreshold = aut;
        currentClass = c;
        serveStaticPage = ssp;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int month = calendar.get(Calendar.MONTH);
        if(month >= 0 && month <= 5)
            seasonKind = SeasonKind.KIND_SPRING;
        else
            seasonKind = SeasonKind.KIND_FALL;
        fourDigitYear = Integer.toString(calendar.get(Calendar.YEAR));
        twoDigitYear = Integer.toString(calendar.get(Calendar.YEAR) % 100);
    }

    public boolean getNotificationsOn() {
        return notificationsOn;
    }

    public long getAssignmentUrgencyThreshold() {
        return assignmentUrgencyThreshold;
    }

    public String getCurrentClass() { return currentClass; }

    public boolean serveStaticPage() {
        return serveStaticPage;
    }

    public SeasonKind getSeason() {
        return seasonKind;
    }

    public String getFourDigitYear() {
        return fourDigitYear;
    }

    public String getTwoDigitYear() {
        return twoDigitYear;
    }
}
