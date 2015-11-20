package devloop0.cs61a.org.cs61a;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by nathr on 11/5/2015.
 */
public class CS61ASchedulerSetupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent in = new Intent(context, CS61ASchedulerEventReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, in, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar now = Calendar.getInstance();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), 6 * 60 * 60 * 1000, pendingIntent);
    }
}
