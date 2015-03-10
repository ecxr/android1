package course.examples.broadcastreceivertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class StaticReceiver extends BroadcastReceiver {
    public StaticReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator v = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

        Toast.makeText(context, "Static Broadcast Intent Received!", Toast.LENGTH_LONG).show();
    }
}
