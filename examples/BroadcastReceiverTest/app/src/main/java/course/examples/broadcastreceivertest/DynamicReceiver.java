package course.examples.broadcastreceivertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by SKing on 3/10/2015.
 */
public class DynamicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        if (isOrderedBroadcast()) {
            abortBroadcast();
        }
        */

        Vibrator v = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

        Toast.makeText(context, "Dynamic Broadcast Intent Received!", Toast.LENGTH_LONG).show();
    }

}
