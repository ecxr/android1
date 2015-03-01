package course.examples.threadtest1;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;


public class ThreadTest1 extends ActionBarActivity {

    private static final int SET_VISIBLE = 0;
    private static final int SET_TEXT = 1;

    private Button mPostButton;
    private Button mRUITButton;
    private Button mASTButton;
    private Button mRunhButton;
    private Button mMsghButton;
    private TextView mText;
    private TextView mInvText;

    private final Handler runHandler = new Handler();

    Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_VISIBLE: {
                    mInvText.setVisibility((Integer)msg.obj);
                    break;
                }
                case SET_TEXT: {
                    mText.setText("Msg Handler + " + Integer.toString((Integer)msg.obj));
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_test1);

        mPostButton = (Button) findViewById(R.id.postButton);
        mRUITButton = (Button) findViewById(R.id.ruitButton);
        mASTButton  =  (Button) findViewById(R.id.astButton);
        mRunhButton =  (Button) findViewById(R.id.runhButton);
        mMsghButton =  (Button) findViewById(R.id.msghButton);

        mText    = (TextView) findViewById(R.id.myText);
        mInvText = (TextView) findViewById(R.id.invTextView);

        mText.setText("Before thread start!");

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postThread();
            }
        });

        mRUITButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ruitThread();
            }
        });

        mASTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                astThread();
            }
        });

        mRunhButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runhThread();
            }
        });

        mMsghButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msghThread();
            }
        });

    }

    private void postThread()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mText.post(new Runnable() {
                    @Override
                    public void run() {
                        mText.setText("View.Post!");
                    }
                });
            }
        }).start();
    }

    private void ruitThread()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mText.setText("runOnUiThread!");
                    }
                });
            }
        }).start();
    }

    private void astThread()
    {
        new SetTextClass().execute(1);
    }

    private void runhThread()
    {
        new Thread(new RunHandlerTask(2)).start();
    }

    private void msghThread()
    {
        new Thread(new msgHandlerTask(3, msgHandler)).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thread_test1, menu);
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

    class SetTextClass extends AsyncTask<Integer, Void, Void>
    {
        private String txt;

        @Override
        protected void onPreExecute() {
            mInvText.setVisibility(TextView.VISIBLE);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            txt = "AsyncTask" + Integer.toString(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mText.setText(txt);
        }
    }

    private class RunHandlerTask implements Runnable {
        int num;

        RunHandlerTask(int n) {
            num = n;
        }

        public void run() {
            runHandler.post(new Runnable() {
                @Override
                public void run() {
                    mInvText.setVisibility(TextView.VISIBLE);
                }
            });

            runHandler.post(new Runnable() {
                @Override
                public void run() {
                    mText.setText("Runnable Handler" + Integer.toString(num) + "!");
                }
            });
        }
    }

    private class msgHandlerTask implements Runnable {
        int num;
        Handler handler;

        msgHandlerTask(int n, Handler h) {
            num = n;
            handler = h;
        }

        public void run() {

            Message msg = null;

            msg = handler.obtainMessage(SET_VISIBLE, View.VISIBLE);
            handler.sendMessage(msg);

            msg = handler.obtainMessage(SET_TEXT, num);
            handler.sendMessage(msg);
        }
    }
}
