package devloop0.cs61a.org.cs61a;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by nathr on 11/5/2015.
 */
public class AssignmentListOpenCheck extends AsyncTask {
    AssignmentListGenerator assignmentListGenerator;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    CS61AService cs61AService;
    boolean background;

    public AssignmentListOpenCheck(AssignmentListGenerator alg, RecyclerView rv, SwipeRefreshLayout srl) {
        assignmentListGenerator = alg;
        recyclerView = rv;
        swipeRefreshLayout = srl;
        background = false;
    }

    public AssignmentListOpenCheck(AssignmentListGenerator alg, CS61AService css, boolean b) {
        assignmentListGenerator = alg;
        recyclerView = null;
        swipeRefreshLayout = null;
        background = b;
        cs61AService = css;
    }

    @Override
    protected String doInBackground(Object[] params) {
        try {
            for (int i = 0; i < assignmentListGenerator.getAssignmentList().size(); i++) {
                URL url = new URL(assignmentListGenerator.getAssignmentList().get(i).getAssignmentLink());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("HEAD");
                httpURLConnection.connect();
                int code = httpURLConnection.getResponseCode();
                assignmentListGenerator.getAssignmentList().get(i).setAssignmentIsOpen(code == 200);
            }
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (!background) {
            CardAdapter cardAdapter = new CardAdapter(assignmentListGenerator);
            recyclerView.setAdapter(cardAdapter);
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            ActivityManager manager = (ActivityManager) recyclerView.getContext().getSystemService(Context.ACTIVITY_SERVICE);
            boolean on = false;
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (service.service.getClassName().equals(CS61AService.class.getName())) on = true;
            }
            Log.i("CS61AService ON", on + "");
            if (!on) {
                Intent info = new Intent(recyclerView.getContext(), CS61AService.class);
                recyclerView.getContext().startService(info);
            }
        }
        else {
            long currentTimeInMilliseconds = Calendar.getInstance().getTimeInMillis();
            long twoDayLimit = 60 * 60 * 24 * 2 * 1000;
            long sixHours = 60 * 60 * 2 * 1000;
            ArrayList<Assignment> toNotify = new ArrayList<Assignment>();
            ArrayList<CS61AService.AssignmentPriorityKind> priorityList = new ArrayList<CS61AService.AssignmentPriorityKind>();
            for (Assignment assignment : assignmentListGenerator.getAssignmentList()) {
                if (assignment.assignmentIsOpen()) {
                    if (currentTimeInMilliseconds >= assignment.getReleaseTime() && currentTimeInMilliseconds < assignment.getDueTime()) {
                        toNotify.add(assignment);
                        if (currentTimeInMilliseconds + twoDayLimit >= assignment.getDueTime())
                            priorityList.add(CS61AService.AssignmentPriorityKind.ASSIGNMENT_PRIORITY_URGENT);
                        else if(currentTimeInMilliseconds - sixHours <= assignment.getReleaseTime())
                            priorityList.add(CS61AService.AssignmentPriorityKind.ASSIGNMENT_PRIORITY_RELEASED);
                        else
                            priorityList.add(CS61AService.AssignmentPriorityKind.ASSIGNMENT_PRIORITY_REMINDER);
                    }
                }
            }
            cs61AService.setToNotifyList(toNotify);
            cs61AService.setPriorityList(priorityList);
            cs61AService.notifyUser();
        }
    }
}
