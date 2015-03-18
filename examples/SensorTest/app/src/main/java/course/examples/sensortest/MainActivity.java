package course.examples.sensortest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements
        SensorEventListener {

    private static final int UPDATE_THRESHOLD = 500;
    private static final int MODE_RAW = 0, MODE_LOW = 1, MODE_HIGH = 2;
    // Filtering constant
    private final float mAlpha = 0.8f;

    // Arrays for storing filtered values
    private float[] mGravity = new float[3];
    private float[] mAccel = new float[3];

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private int mMode = MODE_RAW;

    private TextView mXValueView, mYValueView, mZValueView;
    private long mLastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mXValueView = (TextView) findViewById(R.id.x_value_view);
        mYValueView = (TextView) findViewById(R.id.y_value_view);
        mZValueView = (TextView) findViewById(R.id.z_value_view);

        // Setup radio button and listeners
        final RadioButton btnRaw  = (RadioButton) findViewById(R.id.btnRaw);
        btnRaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMode = MODE_RAW;
            }
        });

        final RadioButton btnLow  = (RadioButton) findViewById(R.id.btnLow);
        btnLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMode = MODE_LOW;
            }
        });

        final RadioButton btnHigh = (RadioButton) findViewById(R.id.btnHigh);
        btnHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMode = MODE_HIGH;
            }
        });

        // Get reference to the sensor manager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get reference to the accelerometer
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mAccelerometer == null)
            finish();

    }

    // Register listener
    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

        mLastUpdate = System.currentTimeMillis();
    }

    // Unregister listener
    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this);
    }

    // Process new reading
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long actualTime = System.currentTimeMillis();

            if (actualTime - mLastUpdate > UPDATE_THRESHOLD) {

                mLastUpdate = actualTime;

                float rawX = event.values[0], rawY = event.values[1], rawZ = event.values[2];

                // Apply low-pass filter
                mGravity[0] = lowPass(rawX, mGravity[0]);
                mGravity[1] = lowPass(rawY, mGravity[1]);
                mGravity[2] = lowPass(rawZ, mGravity[2]);

                // Apply high-pass filter
                mAccel[0] = highPass(rawX, mGravity[0]);
                mAccel[1] = highPass(rawY, mGravity[1]);
                mAccel[2] = highPass(rawZ, mGravity[2]);

                float x = 0.0f, y = 0.0f, z = 0.0f;

                switch (mMode) {
                    case MODE_RAW:
                        x = rawX;
                        y = rawY;
                        z = rawZ;
                        break;
                    case MODE_LOW:
                        x = mGravity[0];
                        y = mGravity[1];
                        z = mGravity[2];
                        break;
                    case MODE_HIGH:
                        x = mAccel[0];
                        y = mAccel[1];
                        z = mAccel[2];
                        break;
                }

                mXValueView.setText(String.valueOf(x));
                mYValueView.setText(String.valueOf(y));
                mZValueView.setText(String.valueOf(z));
            }
        }
    }

    // Deemphasize transient forces
    private float lowPass(float current, float gravity) {

        return gravity * mAlpha + current * (1 - mAlpha);

    }

    // Deemphasize constant forces
    private float highPass(float current, float gravity) {

        return current - gravity;

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        return;
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
