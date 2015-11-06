package devloop0.cs61a.org.cs61a;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Sirjan on 11/5/2015.
 */

public class AssignmentOpenCheck extends AsyncTask {
    private String assignmentLink;
    private boolean isOpen = false;

    public AssignmentOpenCheck(String url) {
        assignmentLink = url;
    }

    @Override
    protected String doInBackground(Object[] params) {
        try {
            URL url = new URL (assignmentLink);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            isOpen = code == 200;
        }
        catch (IOException e) {
            Log.e("Error", e.getMessage());
        }
        return null;
    }

    public boolean assignmentIsOpen() {
        return isOpen;
    }
}

