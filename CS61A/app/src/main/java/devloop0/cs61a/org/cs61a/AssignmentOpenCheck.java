package devloop0.cs61a.org.cs61a;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Sirjan on 11/5/2015.
 */

public class AssignmentOpenCheck extends AsyncTask {
    private String assignmentLink;
    boolean isOpen;

    public AssignmentOpenCheck(String url) {
        assignmentLink = url;
    }

    @Override
    protected String doInBackground(Object[] params) {
        try {
            URL url = new URL(assignmentLink);
            URLConnection myURLConnection = url.openConnection();
            myURLConnection.connect();
            isOpen = true;
        }
        catch (MalformedURLException e) {
            isOpen = false;
        }
        catch (IOException e) {
            isOpen = false;
        }
        Log.i("Test2", ""+isOpen);
        return null;
    }
}

