package devloop0.cs61a.org.cs61a;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Calendar;

import bolts.Task;

/**
 * Created by nathr on 11/5/2015.
 */
public class CS61AService extends Service {
    ArrayList<Assignment> toNotify = new ArrayList<Assignment>();
    ArrayList<AssignmentPriorityKind> priorityList = new ArrayList<AssignmentPriorityKind>();
    public static int CS61ANotificationIdCounter = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        HTMLDownloader htmlDownloader = new HTMLDownloader(this, true);
        htmlDownloader.execute();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public ArrayList<Assignment> getToNotifyList() {
        return toNotify;
    }

    public ArrayList<Assignment> setToNotifyList(ArrayList<Assignment> al) {
        toNotify = al;
        return toNotify;
    }

    public ArrayList<AssignmentPriorityKind> getPriorityList() {
        return priorityList;
    }

    public ArrayList<AssignmentPriorityKind> setPriorityList(ArrayList<AssignmentPriorityKind> apk) {
        priorityList = apk;
        return priorityList;
    }

    public boolean notifyUser() {
        assert priorityList.size() == toNotify.size();
        for(int i = 0; i < priorityList.size(); i++) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            if(priorityList.get(i) == AssignmentPriorityKind.ASSIGNMENT_PRIORITY_RELEASED) {
                notificationBuilder.setContentTitle("New Assignment Released: " + toNotify.get(i).getAssignmentName());
                notificationBuilder.setSmallIcon(android.R.drawable.ic_dialog_info);
            }
            else if(priorityList.get(i) == AssignmentPriorityKind.ASSIGNMENT_PRIORITY_REMINDER) {
                notificationBuilder.setContentTitle("Assignment Active: " + toNotify.get(i).getAssignmentName());
                notificationBuilder.setSmallIcon(android.R.drawable.ic_dialog_info);
            }
            else if(priorityList.get(i) == AssignmentPriorityKind.ASSIGNMENT_PRIORITY_URGENT) {
                notificationBuilder.setContentTitle("Assignment Due Soon: " + toNotify.get(i).getAssignmentName());
                notificationBuilder.setSmallIcon(android.R.drawable.stat_notify_error);
            }
            notificationBuilder.setContentText(toNotify.get(i).getDescription() + " "
                    + "Release Date: " + toNotify.get(i).getFormattedReleaseDateString() + " "
                    + "Due Date: " + toNotify.get(i).getFormattedDueDateString());
            Intent action = new Intent(this, AssignmentActivity.class);
            Assignment assignment = toNotify.get(i);
            Log.i("AssignmentNotification", assignment.toString());
            action.putExtra("assignment_description", assignment.getDescription());
            action.putExtra("assignment_name", assignment.getAssignmentName());
            action.putExtra("assignment_release_time", assignment.getReleaseTime());
            action.putExtra("assignment_due_time", assignment.getDueTime());
            action.putExtra("assignment_link", assignment.getAssignmentLink());
            action.putExtra("assignment_is_open", assignment.assignmentIsOpen());
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addParentStack(AssignmentActivity.class);
            taskStackBuilder.addNextIntent(action);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            long [] vibrateSequence = { 1000, 500, 500, 500 };
            notificationBuilder.setVibrate(vibrateSequence);
            notificationBuilder.setAutoCancel(true);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(CS61ANotificationIdCounter, notificationBuilder.build());
            CS61ANotificationIdCounter += 1;
        }
        return true;
    }

    public enum AssignmentPriorityKind {
        ASSIGNMENT_PRIORITY_URGENT, ASSIGNMENT_PRIORITY_RELEASED, ASSIGNMENT_PRIORITY_REMINDER
    }
}
