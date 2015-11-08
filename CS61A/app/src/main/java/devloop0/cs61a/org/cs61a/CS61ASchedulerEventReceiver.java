package devloop0.cs61a.org.cs61a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by nathr on 11/5/2015.
 */
public class CS61ASchedulerEventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in = new Intent(context, CS61AService.class);
        context.startService(in);
    }
}
