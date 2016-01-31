package devloop0.cs61a.org.cs61a;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    PreferenceHolder preferenceHolder;
    String curr;
    String season = null, year = null;

    public static final String CONNECTIONERROR = "CONNECTION ERROR FOR COURSE WEBSITE";
    public HTMLDownloader(RecyclerView rv, SwipeRefreshLayout srl, PreferenceHolder ph) {
        recyclerView = rv;
        swipeRefreshLayout = srl;
        sourceCode = "";
        background = false;
        preferenceHolder = ph;
        curr = preferenceHolder.getCurrentClass();
        season = ph.getSeason() == PreferenceHolder.SeasonKind.KIND_FALL ? "fa" : "sp";
        year = ph.getTwoDigitYear();
    }

    public HTMLDownloader(CS61AService css, boolean b, PreferenceHolder ph) {
        recyclerView = null;
        swipeRefreshLayout = null;
        sourceCode = "";
        background = b;
        cs61AService = css;
        preferenceHolder = ph;
        curr = preferenceHolder.getCurrentClass();
        season = ph.getSeason() == PreferenceHolder.SeasonKind.KIND_FALL ? "fa" : "sp";
        year = ph.getTwoDigitYear();
    }

     private String grabHomePageSource() {
        try {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpGet request = null;
            if(curr.equals("cs61a"))
                request = new HttpGet("http://www.cs61a.org/");
            else if(curr.equals("cs61b"))
                request = new HttpGet("http://cs61b.ug/" + season + year + "/");
            else if(curr.equals("ee16b") || curr.equals("ee16a"))
                request = new HttpGet("http://inst.eecs.berkeley.edu/~" + curr + "/" + season + year + "/");
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
            // TODO: Notify the user there is not internet.

            throw new RuntimeException(CONNECTIONERROR);

            /*new AlertDialog.Builder(this).setTitle("Connection Error").setMessage(
                    "There seems to be a connection error with the course website \n" +
                            "Please check your internet and try again."
                ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show(); */
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
        DictionaryParser par = null;
        if(curr.equals("cs61a"))
            par = new CS61ADictionaryParser(sourceCode, preferenceHolder);
        else if(curr.equals("cs61b"))
            par = new CS61BDictionaryParser(sourceCode, preferenceHolder);
        else if(curr.equals("ee16b") || curr.equals("ee16a"))
            par = new EE16ABDictionaryParser(sourceCode, preferenceHolder);
        ArrayList<String[]> ar = par.getAssignments();
        Log.i("Array List size", ar.size()+"");

        /*for(int i=0; i<ar.size(); i++)
            Log.i("Dictionary Parser", Arrays.toString(ar.get(i)));*/

        inaccurateAssignmentListGenerator = new AssignmentListGenerator(ar);
        if(!background) {
            AssignmentListOpenCheck assignmentListOpenCheck = new AssignmentListOpenCheck(inaccurateAssignmentListGenerator, recyclerView, swipeRefreshLayout, preferenceHolder);
            assignmentListOpenCheck.execute();
        }
        else {
            AssignmentListOpenCheck assignmentListOpenCheck = new AssignmentListOpenCheck(inaccurateAssignmentListGenerator, cs61AService, true, preferenceHolder);
            assignmentListOpenCheck.execute();
        }
    }

    public AssignmentListGenerator getInaccurateAssignmentListGenerator() {
        return inaccurateAssignmentListGenerator;
    }
}
