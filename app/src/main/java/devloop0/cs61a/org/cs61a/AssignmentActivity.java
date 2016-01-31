package devloop0.cs61a.org.cs61a;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AssignmentActivity extends AppCompatActivity {

    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Toolbar backgroundToolbar = (Toolbar) findViewById(R.id.background_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.assignment_toolbar);
        TextView descriptionTextView = (TextView) findViewById(R.id.assignment_activity_description);
        TextView releaseDateTextView = (TextView) findViewById(R.id.assignment_activity_release_date);
        TextView dueDateTextView = (TextView) findViewById(R.id.assignment_activity_due_date);
        final TextView tMinusCountdownTextView = (TextView) findViewById(R.id.assignment_activity_t_minus_countdown);
        final AssignmentCountdown assignmentCountdown = (AssignmentCountdown) findViewById(R.id.assignment_activity_countdown);
        TextView linkTextView = (TextView) findViewById(R.id.assignment_link);
        Intent intent = getIntent();
        String assignmentTitle = intent.getStringExtra("assignment_name");
        String assignmentDescription = intent.getStringExtra("assignment_description");
        final boolean assignmentIsOpen = intent.getBooleanExtra("assignment_is_open", false);
        final long assignmentReleaseTime = intent.getLongExtra("assignment_release_time", 0);
        final long assignmentDueTime = intent.getLongExtra("assignment_due_time", 0);
        String assignmentLink = intent.getStringExtra("assignment_link");
        toolbar.setTitle(assignmentTitle);
        descriptionTextView.setText(assignmentDescription);
        releaseDateTextView.setText(Html.fromHtml("<b>Release Date:</b> " + new SimpleDateFormat("MM/dd/yy").format(new Date(assignmentReleaseTime))));
        dueDateTextView.setText(Html.fromHtml("<b>Due Date:</b> " + new SimpleDateFormat("MM/dd/yy").format(new Date(assignmentDueTime))));
        final int red = Color.parseColor("#FFE6FF");
        final int green = Color.parseColor("#E7FFE6");
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while(!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long currentTime = Calendar.getInstance().getTimeInMillis();
                                if(currentTime < assignmentReleaseTime || !assignmentIsOpen) {
                                    assignmentCountdown.doneColor = green;
                                    assignmentCountdown.leftColor = green;
                                    assignmentCountdown.setPercentage(100);
                                    tMinusCountdownTextView.setText(Html.fromHtml("<b>Assignment not released yet.</b>"));
                                }
                                else if(currentTime >= assignmentReleaseTime && currentTime <= assignmentDueTime && assignmentIsOpen) {
                                    long differenceTime = assignmentDueTime - currentTime;
                                    int days = (int) TimeUnit.MILLISECONDS.toDays(differenceTime);
                                    int hours = (int) TimeUnit.MILLISECONDS.toHours(differenceTime) - (days * 24);
                                    int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(differenceTime) - (int) (TimeUnit.MILLISECONDS.toHours(differenceTime) * 60);
                                    int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(differenceTime) - (int) (TimeUnit.MILLISECONDS.toMinutes(differenceTime) * 60);
                                    float f = ((float) (currentTime - assignmentReleaseTime) / (assignmentDueTime - assignmentReleaseTime)) * 100;
                                    assignmentCountdown.doneColor = red;
                                    assignmentCountdown.leftColor = green;
                                    assignmentCountdown.setPercentage((int) f);
                                    tMinusCountdownTextView.setText(Html.fromHtml("T-Minus: " + days + " Day(s), " + hours + " Hour(s), " + minutes + " Minute(s), " + seconds + " Second(s) Left</b>"));
                                }
                                else {
                                    assignmentCountdown.doneColor = red;
                                    assignmentCountdown.leftColor = red;
                                    assignmentCountdown.setPercentage(100);
                                    tMinusCountdownTextView.setText(Html.fromHtml("<b>Assignment completed.</b>"));
                                }
                            }
                        });
                    }
                }
                catch(InterruptedException iex) {
                    Log.i("Internal Error", iex.getMessage());
                }
            }
        };
        t.start();
        if(!assignmentIsOpen)
            linkTextView.setText(Html.fromHtml("<b>Unreleased Assignment Link:</b> " + assignmentLink));
        else
            linkTextView.setText(Html.fromHtml("<b>Assignment Link:</b> <a href=\"" + assignmentLink + "\">" + assignmentLink + "</a>"));
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        backgroundToolbar.inflateMenu(R.menu.menu);
        backgroundToolbar.setTitle("");
        setSupportActionBar(backgroundToolbar);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String currentClass = preferences.getString("class", "cs61a");
        String title = (currentClass.equals("cs61a") ? "CS 61A" : (currentClass.equals("cs61b") ? "CS 61B" : ""));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            setTaskDescription(new ActivityManager.TaskDescription(title, BitmapFactory.decodeResource(getResources(), R.drawable.icon), getResources().getColor(R.color.colorPrimary)));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ListView mainClassList = (ListView) findViewById(R.id.main_class_list);
        String[] test = { "CS 61A", "CS 61B" };
        mainClassList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, test));
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, backgroundToolbar, R.string.navigation_drawer_opened, R.string.navigation_drawer_closed) {
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
        backgroundToolbar.inflateMenu(R.menu.menu);
        setSupportActionBar(backgroundToolbar);
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
}
