package course.examples.notificationstest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    // Notification ID to allow for future updates
    private static final int MY_NOTIFICATION_ID = 1;
    private static final int MY_CUSTOM_NOTIFICATION_ID = 2;

    // Notification Count
    private int mNotificationCount;
    private int mCustNotificationCount;

    // Notification Text Elements
    private final CharSequence tickerText = "Hello Ticker Notification!!!";
    private final CharSequence contentText = "Hello, Notification!";
    private final CharSequence contentTitle = "Notification";

    // Notification Action Elements
    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;

    // Vibration on Arrival

    private long[] mVibratePattern = { 0, 200, 200, 300 };

    RemoteViews mContentView = new RemoteViews(
            "course.examples.notificationstest",
            R.layout.custom_toast);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toastButton = (Button) findViewById(R.id.btnToast);
        Button customToastButton = (Button) findViewById(R.id.btnCustomToast);
        Button notificationButton = (Button) findViewById(R.id.btnNotification);
        Button customNotificationButton = (Button) findViewById(R.id.btnCustomNotif);

        mNotificationIntent = new Intent(getApplicationContext(),
                NotificationSubActivity.class);

        mContentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

        toastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello, Toast!", Toast.LENGTH_LONG).show();
            }
        });

        customToastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

                toast.setView(getLayoutInflater().inflate(R.layout.custom_toast, null));

                toast.show();
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification.Builder notificationBuilder = makeNotification();

                notificationBuilder.setContentTitle(contentTitle)
                    .setContentText(contentText + " (" + ++mNotificationCount + ")");

                // Pass the Notification to the NotificationManager:
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(MY_NOTIFICATION_ID,
                        notificationBuilder.build());
            }
        });

        customNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the Notification's expanded message and Intent:

                mContentView.setTextViewText(R.id.text, contentText + " ("
                        + ++mCustNotificationCount + ")");

                Notification.Builder notificationBuilder = makeNotification();

                notificationBuilder.setContent(mContentView);

                // Pass the Notification to the NotificationManager:
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(MY_CUSTOM_NOTIFICATION_ID,
                        notificationBuilder.build());
            }
        });

    }

    private Notification.Builder makeNotification() {
        // Build the Notification
        return new Notification.Builder(
                getApplicationContext())
                .setTicker(tickerText)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setAutoCancel(true)
                .setContentIntent(mContentIntent)
                .setVibrate(mVibratePattern);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
