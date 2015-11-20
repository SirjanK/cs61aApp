package devloop0.cs61a.org.cs61a;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.preference.DialogPreference;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "mEtmY7czfqvQffthH1JciErMFGD3Cmib6aNWJyzv", "j1lRCi9vcKA953eOK265TuMcvcF7lgJ3AOq90Hhu");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            setTaskDescription(new ActivityManager.TaskDescription("CS 61A", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), getResources().getColor(R.color.colorPrimary)));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_assignment_list);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_assignment_list);

        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#F44336"), Color.parseColor("#3F51B5"), Color.parseColor("#FFC107"), Color.parseColor("#4CAF50"));
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HTMLDownloader htmlDownloader = new HTMLDownloader(recyclerView, swipeRefreshLayout);
        htmlDownloader.execute();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HTMLDownloader reinstantiate = new HTMLDownloader(recyclerView, swipeRefreshLayout);
                reinstantiate.execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.app_credits:
                new AlertDialog.Builder(this).setTitle("Credits").setMessage(
                        "Instructor: Professor John Denero\n" +
                                "Developers:\n" +
                                "   - Nikhil Athreya\n" +
                                "   - Sirjan Kafle"
                ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
