package course.examples.broadcastreceivertest;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    private static final String CUSTOM_INTENT = "course.examples.broadcastreceivertest.show_toast";
    private final IntentFilter intentFilter = new IntentFilter(CUSTOM_INTENT);
    private final DynamicReceiver dynamicReceiver = new DynamicReceiver();

    private LocalBroadcastManager mBroadcastMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBroadcastMgr = LocalBroadcastManager.getInstance(getApplicationContext());
        mBroadcastMgr.registerReceiver(dynamicReceiver, intentFilter);

        intentFilter.setPriority(3);

        setContentView(R.layout.activity_main);

        Button bcastStaticButton = (Button) findViewById(R.id.btnStaticBCast);
        Button bcastDynamicButton = (Button) findViewById(R.id.btnDynamicBCast);

        bcastStaticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast(new Intent(CUSTOM_INTENT), Manifest.permission.VIBRATE);
            }
        });
        bcastDynamicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBroadcastMgr.sendBroadcast(new Intent(CUSTOM_INTENT));
                //sendOrderedBroadcast(new Intent(CUSTOM_INTENT), android.Manifest.permission.VIBRATE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mBroadcastMgr.unregisterReceiver(dynamicReceiver);
        super.onDestroy();
    }

    @Override
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
