package devloop0.cs61a.org.cs61a;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nathr on 11/5/2015.
 */
public class AssignmentListOpenCheck extends AsyncTask {
    AssignmentListGenerator assignmentListGenerator;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    public AssignmentListOpenCheck(AssignmentListGenerator alg, RecyclerView rv, SwipeRefreshLayout srl) {
        assignmentListGenerator = alg;
        recyclerView = rv;
        swipeRefreshLayout = srl;
    }

    @Override
    protected String doInBackground(Object[] params) {
        try {
            for(int i = 0; i < assignmentListGenerator.getAssignmentList().size(); i++) {
                URL url = new URL(assignmentListGenerator.getAssignmentList().get(i).getAssignmentLink());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("HEAD");
                httpURLConnection.connect();
                int code = httpURLConnection.getResponseCode();
                assignmentListGenerator.getAssignmentList().get(i).setAssignmentIsOpen(code == 200);
            }
        }
        catch (IOException e) {
            Log.e("Error", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        CardAdapter cardAdapter = new CardAdapter(assignmentListGenerator);
        recyclerView.setAdapter(cardAdapter);
        if(swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
