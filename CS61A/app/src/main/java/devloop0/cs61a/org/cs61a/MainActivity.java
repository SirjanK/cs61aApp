package devloop0.cs61a.org.cs61a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "mEtmY7czfqvQffthH1JciErMFGD3Cmib6aNWJyzv", "j1lRCi9vcKA953eOK265TuMcvcF7lgJ3AOq90Hhu");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_assignment_list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HTMLDownloader htmlDownloader = new HTMLDownloader(recyclerView);
        htmlDownloader.execute();
    }
}
