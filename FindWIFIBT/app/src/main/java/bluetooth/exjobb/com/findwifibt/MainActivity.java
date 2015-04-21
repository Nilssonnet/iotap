package bluetooth.exjobb.com.findwifibt;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by Sebastian Olsson on 15-04-12.
 * Modified by Mattias Nilsson
 */

public class MainActivity extends Activity {
    private ArrayAdapter<String> deviceArray ;
    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBlueAdapter;
    private ListView listView;

    private ArrayList<Devices> arrayListDevices;
    private DeviceAdapter deviceAdapter;



    private String resultMD5, resultSHA_1, resultSHA_512;
    private String radioButtonResult = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button searchButton = (Button) findViewById(R.id.search_button);
        //Button sendButton = (Button)findViewById(R.id.sendbutton);
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBlueAdapter==null){
            Toast.makeText(MainActivity.this, "This device do not have Bluetooth",
                    Toast.LENGTH_SHORT).show();

        }

        arrayListDevices = new ArrayList<Devices>();
        deviceAdapter=new DeviceAdapter(this,arrayListDevices);

        listView = (ListView)findViewById(R.id.listViewDevices);
        listView.setAdapter(deviceAdapter);

        if (!mBlueAdapter.isEnabled()) { //returns false if BT is not enabled
            ///Creates dialog for turning on Bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);

    }

    public void scan(View view){
        if (radioButtonResult.equals("")){
            Toast.makeText(MainActivity.this, "Select a hash method", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(MainActivity.this, "Starting search for BT devices",
                    Toast.LENGTH_SHORT).show();
            deviceAdapter.clear();
            mBlueAdapter.startDiscovery();
        }

    }


    public void send(View view){

        //new SummaryAsyncTask().execute((Void) null);

        Toast.makeText(MainActivity.this, "Sending data to database", Toast.LENGTH_SHORT).show();

    }

    class SummaryAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private void postData(String MD5, String SHA_1) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://213.65.109.112/insert.php");

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("MD5", MD5));
                nameValuePairs.add(new BasicNameValuePair("SHA_1", SHA_1));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
            }
            catch(Exception e)
            {
                Log.e("log_tag", "Error:  "+e.toString());
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            postData(resultMD5, resultSHA_1);
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Unregister in onDestroy

    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            resultMD5 = "";
            resultSHA_1 = "";
            resultSHA_512 = "";
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                //deviceAdapter.add( device.getName() + "\n" + device.getAddress()+"\n"+device.getBluetoothClass()); //needs api 18 device.getType()
                resultMD5 = hashMethodMD5(device.getAddress());
                resultSHA_1 = hashMethodSHA_1(device.getAddress());
                resultSHA_512 = hashMethodSHA_512(device.getAddress());
                new SummaryAsyncTask().execute((Void) null);

                if (radioButtonResult.equals("MD5")){
                    deviceAdapter.add(new Devices(device.getName(), device.getAddress(),
                            resultMD5));
                }
                else if (radioButtonResult.equals("SHA_1")){
                    deviceAdapter.add(new Devices(device.getName(), device.getAddress(),
                            resultSHA_1));
                }
                else if (radioButtonResult.equals("SHA_512")){
                    deviceAdapter.add(new Devices(device.getName(), device.getAddress(),
                            resultSHA_512));
                }
                }
        }
    };


    public String hashMethodSHA_1 (String mac) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(mac.getBytes(), 0, mac.length());
        String sha_1 = new BigInteger(1, md.digest()).toString(16);
        while (sha_1.length() < 32) {
            sha_1 = "0" + sha_1;
        }
        return sha_1;

    }

    public String hashMethodMD5 (String mac) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(mac.getBytes(), 0, mac.length());
        String md5 = new BigInteger(1, md.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        return md5;

    }

    public String hashMethodSHA_512 (String mac) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(mac.getBytes(), 0, mac.length());
        String md5 = new BigInteger(1, md.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        return md5;

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonMD5:
                if (checked)
                    radioButtonResult = "MD5";
                    break;
            case R.id.radioButtonSHA_1:
                if (checked)
                    radioButtonResult = "SHA_1";
                    break;
            case R.id.radioButtonSHA_512:
                if (checked)
                    radioButtonResult = "SHA_512";
                break;
        }
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
