package devloop0.cs61a.org.cs61a;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Deflater;

import javax.net.ssl.SSLContext;

/**
 * Created by nathr on 10/13/2015.
 */
public class HTMLDownloader extends AsyncTask {
    String sourceCode = null;

    public HTMLDownloader() {
        sourceCode = "";
    }

    private String grabHomePageSource() {
        try {
            String url = "https://www.cs61a.org";
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) defaultHttpClient.getConnectionManager().getSchemeRegistry().getScheme("https").getSocketFactory();
            final X509HostnameVerifier x509HostnameVerifier = sslSocketFactory.getHostnameVerifier();
            if(!(x509HostnameVerifier instanceof CS61AVerifier)) {
                sslSocketFactory.setHostnameVerifier(new CS61AVerifier(x509HostnameVerifier));
            }
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
        } catch (IOException io) {
            Log.e("Download failed.", io.getMessage());
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
