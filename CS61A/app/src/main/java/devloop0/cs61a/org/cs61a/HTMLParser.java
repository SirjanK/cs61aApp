package devloop0.cs61a.org.cs61a;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nathr on 10/13/2015.
 */
public class HTMLParser {
    String sourceCode = null;
    public HTMLParser() {
        sourceCode = "";
    }

    public String getSourceCode() {
        try {
            String url = "https://www.cs61a.org";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(request);
            InputStream inputStream = httpResponse.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            sourceCode = stringBuilder.toString();
        } catch(IOException io) {
            Log.e("Download failed.", io.getMessage());
        }
        return sourceCode;
    }
}

