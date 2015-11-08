package devloop0.cs61a.org.cs61a;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
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
    String sourceCode = "";
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean background;
    AssignmentListGenerator inaccurateAssignmentListGenerator;
    CS61AService cs61AService;

    public HTMLDownloader(RecyclerView rv, SwipeRefreshLayout srl) {
        recyclerView = rv;
        swipeRefreshLayout = srl;
        sourceCode = "";
        background = false;
    }

    public HTMLDownloader(CS61AService css, boolean b) {
        recyclerView = null;
        swipeRefreshLayout = null;
        sourceCode = "";
        background = b;
        cs61AService = css;
    }

     public String grabHomePageSource() {
        try {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://www.cs61a.org/");
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

        inaccurateAssignmentListGenerator = new AssignmentListGenerator(ar);
        if(!background) {
            AssignmentListOpenCheck assignmentListOpenCheck = new AssignmentListOpenCheck(inaccurateAssignmentListGenerator, recyclerView, swipeRefreshLayout);
            assignmentListOpenCheck.execute();
        }
        else {
            AssignmentListOpenCheck assignmentListOpenCheck = new AssignmentListOpenCheck(inaccurateAssignmentListGenerator, cs61AService, true);
            assignmentListOpenCheck.execute();
        }
    }

    public AssignmentListGenerator getInaccurateAssignmentListGenerator() {
        return inaccurateAssignmentListGenerator;
    }
}
