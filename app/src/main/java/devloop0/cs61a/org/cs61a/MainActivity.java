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
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private boolean notify;
    private long urgency;
    ImageButton imageButton;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_assignment_list);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_assignment_list);

        final Toolbar toolBar = (Toolbar) findViewById(R.id.main_toolbar);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String currentClass = preferences.getString("class", "cs61a");
        String title = (currentClass.equals("cs61a") ? "CS 61A" : (currentClass.equals("cs61b") ? "CS 61B" : ""));
        toolBar.setTitle(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            setTaskDescription(new ActivityManager.TaskDescription(title, BitmapFactory.decodeResource(getResources(), R.drawable.icon), getResources().getColor(R.color.colorPrimary)));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        notify = preferences.getBoolean("notifications", true);
        String urgentString = preferences.getString("urgency", "2 days");
        Log.i("AssignmentUrgency", urgentString);
        Log.i("NotificationsOn", notify + "");
        urgency = convertToMillis(urgentString);
        final PreferenceHolder preferenceHolder = new PreferenceHolder(notify, urgency, currentClass);
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
            htmlDownloader.execute();
        }
        catch(RuntimeException r) {
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
                try {
                    HTMLDownloader reinstantiate = new HTMLDownloader(recyclerView, swipeRefreshLayout, preferenceHolder);
                    reinstantiate.execute();
                } catch (RuntimeException r) {
                    new AlertDialog.Builder(getApplicationContext()).setTitle("Connection Error").setMessage(
                            "There seems to be a connection error with the course website \n" +
                                    "Please check your internet and try again."
                    ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.denero);
                mediaPlayer.start();
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ListView mainClassList = (ListView) findViewById(R.id.main_class_list);
        String[] test = { "CS 61A", "CS 61B" };
        mainClassList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, test));
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.navigation_drawer_opened, R.string.navigation_drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        toolBar.inflateMenu(R.menu.menu);
        setSupportActionBar(toolBar);
        final SharedPreferences.Editor editor = preferences.edit();
        mainClassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0: {
                        editor.putString("class", "cs61a");
                        editor.commit();
                    }
                    break;
                    case 1: {
                        editor.putString("class", "cs61b");
                        editor.commit();
                    }
                    break;
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(actionBarDrawerToggle.onOptionsItemSelected(menuItem))
            return true;
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
