package devloop0.cs61a.org.cs61a;

import android.util.Log;

import org.apache.http.conn.ssl.AbstractVerifier;
import org.apache.http.conn.ssl.X509HostnameVerifier;

import javax.net.ssl.SSLException;

/**
 * Created by nathr on 10/25/2015.
 */

// from http://stackoverflow.com/questions/3135679/android-httpclient-hostname-in-certificate-didnt-match-example-com-ex
public class CS61AVerifier extends AbstractVerifier {

    private final X509HostnameVerifier x509HostnameVerifier;
    public CS61AVerifier(final X509HostnameVerifier delegate) {
        x509HostnameVerifier = delegate;
    }

    @Override
    public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
        boolean ok = false;
        try {
            x509HostnameVerifier.verify(host, cns, subjectAlts);
        } catch (SSLException ex) {
            Log.e("No","");
        }
    }
}
