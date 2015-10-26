package devloop0.cs61a.org.cs61a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.test);
        HTMLDownloader htmlDownloader = new HTMLDownloader(textView);
        htmlDownloader.execute();
    }
}
