package devloop0.cs61a.org.cs61a;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by nathr on 10/13/2015.
 */
public class HTMLDownloader extends AsyncTask {
    String sourceCode = null;
    RecyclerView recyclerView;

    public HTMLDownloader(RecyclerView rv) {
        sourceCode = "";
        recyclerView = rv;
    }

    private String grabHomePageSource() {
        try {
            String url = "http://www.cs61a.org";
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse httpResponse = defaultHttpClient.execute(request);
            InputStream inputStream = httpResponse.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            sourceCode = stringBuilder.toString();
            Log.i("SOURCE", sourceCode);
        } catch (IOException io) {
            Log.e("Download failed", io.getMessage());
        }
        return sourceCode;
    }

    String getSourceCode() {
        return sourceCode;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        grabHomePageSource();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        DictionaryParser par = new DictionaryParser(sourceCode);
        ArrayList<String[]> ar = par.getAssignments();

        /*for(int i=0; i<ar.size(); i++)
            Log.i("Dictionary Parser", Arrays.toString(ar.get(i)));*/

        AssignmentListGenerator assignmentListGenerator = new AssignmentListGenerator(ar);
        CardAdapter cardAdapter = new CardAdapter(assignmentListGenerator);
        long currentTimeInMilliseconds = Calendar.getInstance().getTimeInMillis();
        final long twoDayLimit = 24 * 3600 * 2 * 1000; // 2 days in milliseconds
        int initialScrollPosition = -1;
        for(int i = 0; i < assignmentListGenerator.getAssignmentList().size(); i++) {
            Assignment assignment = assignmentListGenerator.getAssignmentList().get(i);
            if(assignment.getReleaseTime() > currentTimeInMilliseconds || assignment.getDueTime() < currentTimeInMilliseconds) {

            }
            else if(assignment.getDueTime() > currentTimeInMilliseconds) {
                if(initialScrollPosition == -1) {
                    initialScrollPosition = i;
                    break;
                }
            }
        }
        recyclerView.setAdapter(cardAdapter);
        Log.i("Initial Scroll Position", initialScrollPosition + " " + assignmentListGenerator.getAssignmentList().size());
        if(initialScrollPosition > 0)
            recyclerView.scrollToPosition(initialScrollPosition - 1);
    }
}
