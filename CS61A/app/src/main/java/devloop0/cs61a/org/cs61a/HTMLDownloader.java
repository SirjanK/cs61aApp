package devloop0.cs61a.org.cs61a;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nathr on 10/13/2015.
 */
public class HTMLDownloader extends AsyncTask {
    String sourceCode = null;

    public HTMLDownloader() {
        sourceCode = "";
    }

<<<<<<< HEAD:CS61A/app/src/main/java/devloop0/cs61a/org/cs61a/HTMLParser.java
    public String getSourceCode() {
=======
    private String grabHomePageSource() {
>>>>>>> 30d2395f7c9ae5ff0bf8a47e231183eda8612d5c:CS61A/app/src/main/java/devloop0/cs61a/org/cs61a/HTMLDownloader.java
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
                stringBuilder.append(line);
            }
            sourceCode = stringBuilder.toString();
            Log.e("SOURCE", sourceCode);
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
}