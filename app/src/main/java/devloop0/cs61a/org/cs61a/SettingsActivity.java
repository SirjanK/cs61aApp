package devloop0.cs61a.org.cs61a;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            setTaskDescription(new ActivityManager.TaskDescription("Settings", BitmapFactory.decodeResource(getResources(), R.drawable.icon), getResources().getColor(R.color.colorPrimary)));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        PreferenceScreenFragment pref = new PreferenceScreenFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, pref).commit();
        setTitle("Settings");
    }

    public static class PreferenceScreenFragment extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            Preference pref = findPreference("restart");
            boolean switchOn = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("notifications", true);
            SwitchPreference notificationsOn = (SwitchPreference) findPreference("notifications");
            notificationsOn.setChecked(switchOn);
            String urgentString = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("urgency", "2 days");
            ListPreference urgency = (ListPreference) findPreference("urgency");
            urgency.setValue(urgentString);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    try {
                        this.finalize();
                        Intent screenToBeShown = new Intent(getActivity(), MainActivity.class);
                        startActivity(screenToBeShown);
                        return true;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        return true;
                    }
                }
            });
        }
    }
}
