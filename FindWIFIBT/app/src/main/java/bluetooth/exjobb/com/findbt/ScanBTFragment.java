package bluetooth.exjobb.com.findbt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Fragment that scans Bluetooth devices, displays them in a list and sends the data to database.
 * Created by Mattias Nilsson & Sebastian Olsson
 */
public class ScanBTFragment extends Fragment implements View.OnClickListener{

    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBlueAdapter;
    private ListView listView;
    private ArrayList<Devices> arrayListDevices;
    private DeviceAdapter deviceAdapter;
    private String resultHashFull, resultHashSemi, resultHashNo, resultClass;
    private int RSSI;
    private String link = "http://213.65.109.112/insert.php"; //URL to database.

    public ScanBTFragment() {
        // Required empty public constructor
    }

    /*
     * Sets up the fragment.
     */
    public static ScanBTFragment newInstance() {
        ScanBTFragment fragment = new ScanBTFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_bt, container, false);

        arrayListDevices = new ArrayList<Devices>();
        deviceAdapter = new DeviceAdapter(getActivity(),arrayListDevices);
        listView = (ListView) view.findViewById(R.id.listViewDevices);
        listView.setAdapter(deviceAdapter);
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBlueAdapter==null){
            Toast.makeText(getActivity(), "This device do not have Bluetooth",
                    Toast.LENGTH_SHORT).show();
        }
        if (!mBlueAdapter.isEnabled()) { //returns false if BT is not enabled
            ///Creates dialog for turning on Bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        Button searchButton = (Button) view.findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(this);
        return view;
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.buttonSearch:
                scan();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    /*
     * Starts the scanning of Bluetooth devices.
     */
    public void scan(){
        Toast.makeText(getActivity(), "Starting search for BT devices",
                Toast.LENGTH_SHORT).show();
        deviceAdapter.clear();
        arrayListDevices = new ArrayList<Devices>();
        mBlueAdapter.startDiscovery();
    }

    /*
     * Sends Bluetooth devices to database.
     */
    class PostAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(link);
            try {
                ArrayList<NameValuePair> nameValuePairsFull = new ArrayList<NameValuePair>(6);
                nameValuePairsFull.add(new BasicNameValuePair("time", HashMethods.currentMinute()));
                nameValuePairsFull.add(new BasicNameValuePair("RSSI", String.valueOf(RSSI)));
                nameValuePairsFull.add(new BasicNameValuePair("resultClass", resultClass));
                nameValuePairsFull.add(new BasicNameValuePair("resultFull", resultHashFull));
                nameValuePairsFull.add(new BasicNameValuePair("resultSemi", resultHashSemi));
                nameValuePairsFull.add(new BasicNameValuePair("resultNo", resultHashNo));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairsFull));
                HttpResponse response = httpclient.execute(httppost);
            }
            catch(Exception e)
            {
                Log.e("log_tag", "Error:  " + e.toString());
            }
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter); // Unregister in onDestroy
    }

    /*
     * Scans Bluetooth devices, adds the found device to list and database.
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothClass btClass=intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
                boolean found = true;
                int i = 0;
                /*
                 * Checks that found Bluetooth device not have been found earlier on same scan.
                 */
                do{
                    if (arrayListDevices.size() == 0){
                        found = false;
                    }
                    else {
                        if (device.getAddress().equals(arrayListDevices.get(i).getMacAddress())){
                            found = true;
                            break;
                        } else{
                            found = false;
                        }
                    }
                    i++;
                }while (i < arrayListDevices.size());

                /*
                 * Hashes found device, shows the device in list and sends it to database.
                 */
                if(!found){
                    resultClass = BluetoothDevices.DeviceClass(btClass.getDeviceClass());
                    RSSI = intent.getShortExtra(device.EXTRA_RSSI, Short.MIN_VALUE);
                    resultHashFull = HashMethods.hashMethodSHA_1
                            (HashMethods.currentMinute() + device.getAddress());
                    resultHashSemi = HashMethods.hashMethodSHA_1
                            (HashMethods.currentHour() + device.getAddress());
                    resultHashNo = HashMethods.hashMethodSHA_1
                            (device.getAddress());
                    new PostAsyncTask().execute((Void) null);
                    deviceAdapter.add(new Devices(device.getName(), device.getAddress(),
                            resultHashFull, resultHashSemi, resultHashNo, RSSI, resultClass));
                    arrayListDevices.add(new Devices(device.getName(), device.getAddress(),
                            resultHashFull, resultHashSemi, resultHashNo, RSSI, resultClass));
                }
            }
        }
    };
}
