package course.examples.networktest;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.URL;

public class NetworkTest extends ActionBarActivity {

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);

        mTextView = (TextView) findViewById(R.id.textView);
        Button socketButton =  (Button) findViewById(R.id.btnSocket);
        Button urlConnButton = (Button) findViewById(R.id.btnUrlConn);
        Button ahcButton = (Button) findViewById(R.id.btnAHC);

        socketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SocketTask().execute();
            }
        });

        urlConnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UrlConnTask().execute();
            }
        });

        ahcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AHCTask().execute();
            }
        });
    }

    private class AHCTask extends AsyncTask<Void, Void, String> {

        private static final String URL = "http://www.google.com";

        private static final String TAG = "UrlConnTask";

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        protected String doInBackground(Void... params) {

            HttpGet request = new HttpGet(URL);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            try {
                return mClient.execute(request, responseHandler);

            } catch (ClientProtocolException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            mTextView.setText(result);
        }
    }
    private class UrlConnTask extends AsyncTask<Void, Void, String> {

        private static final String URL = "http://www.google.com";

        private static final String TAG = "UrlConnTask";

        protected String doInBackground(Void... params) {
            String data = "";
            HttpURLConnection httpUrlConnection = null;

            try {
                httpUrlConnection = (HttpURLConnection) new URL(URL)
                        .openConnection();
                InputStream in = new BufferedInputStream(
                        httpUrlConnection.getInputStream());

                data = readStream(in);

            } catch (MalformedURLException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                if (null != httpUrlConnection)
                    httpUrlConnection.disconnect();
            }
            return data;
        }

        protected void onPostExecute(String result) {
            mTextView.setText(result);
        }
    }

    private class SocketTask extends AsyncTask<Void, Void, String> {

        private static final String HOST = "www.google.com";
        private static final String COMMAND = "GET / HTTP/1.1\nConnection: close\n\n";

        private static final String TAG = "SocketTask";

        protected String doInBackground(Void... params) {
            Socket socket = null;
            String data = "";

            try {
                socket = new Socket(HOST, 80);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                        socket.getOutputStream()), true);
                pw.println(COMMAND);

                data = readStream(socket.getInputStream());

            } catch (UnknownHostException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                if (null != socket)
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "IOException");
                    }
            }
            return data;
        }

        protected void onPostExecute(String result) {
            mTextView.setText(result);
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer data = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            Log.e("readStream", "IOException");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("readStream", "IOException");
                }
            }
        }
        return data.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_network_test, menu);
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
