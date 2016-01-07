package devloop0.cs61a.org.cs61a;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
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

    private SharedPreferences preferences;
    private boolean notify;
    private long urgency;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            setTaskDescription(new ActivityManager.TaskDescription("CS 61A", BitmapFactory.decodeResource(getResources(), R.drawable.icon), getResources().getColor(R.color.colorPrimary)));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_assignment_list);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_assignment_list);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        notify = preferences.getBoolean("notifications", true);
        String urgentString = preferences.getString("urgency", "2 days");
        Log.i("AssignmentUrgency", urgentString);
        Log.i("NotificationsOn", notify + "");
        urgency = convertToMillis(urgentString);
        final PreferenceHolder preferenceHolder = new PreferenceHolder(notify, urgency);
        imageButton = (ImageButton) findViewById(R.id.announcements);

        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#F44336"), Color.parseColor("#3F51B5"), Color.parseColor("#FFC107"), Color.parseColor("#4CAF50"));
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        HTMLDownloader htmlDownloader = new HTMLDownloader(recyclerView, swipeRefreshLayout, preferenceHolder);

        try {
            htmlDownloader.grabHomePageSource();
            htmlDownloader.execute();
            Log.i("TESTTTTT", "This shouldnt show");
        }
        catch(RuntimeException r) {
            Log.i("TEST222", "THIS should show");
            new AlertDialog.Builder(this).setTitle("Connection Error").setMessage(
                    "There seems to be a connection error with the course website \n" +
                            "Please check your internet and try again."
                ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HTMLDownloader reinstantiate = new HTMLDownloader(recyclerView, swipeRefreshLayout, preferenceHolder);
                reinstantiate.execute();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.denero);
                mediaPlayer.start();
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
            case R.id.settings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.email:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                String[] toSendTo = { "cs61a.app@gmail.com" };
                emailIntent.putExtra(Intent.EXTRA_EMAIL, toSendTo);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "CS 61A App Feedback");
                startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public long convertToMillis(String time) {
        long scale = 3600 * 1000;
        switch(time) {
            case "1 hour":
                return scale;
            case "2 hours":
                return scale * 2;
            case "6 hours":
                return scale * 6;
            case "12 hours":
                return scale * 12;
            case "1 day":
                return scale * 24;
            default:
                return scale * 48;
        }
    }
}
