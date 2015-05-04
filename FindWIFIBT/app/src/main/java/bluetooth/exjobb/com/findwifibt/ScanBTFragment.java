package bluetooth.exjobb.com.findwifibt;


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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
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
 * A simple {@link Fragment} subclass.
 *
 * Created by Sebastian Olsson on 15-04-12.
 * Modified by Mattias Nilsson
 */
public class ScanBTFragment extends Fragment implements View.OnClickListener{

    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBlueAdapter;
    private ListView listView;

    private ArrayList<Devices> arrayListDevices;
    private DeviceAdapter deviceAdapter;

    private String resultHashFull, resultHashSemi, resultHashNo, resultClass;

    private String link = "http://213.65.109.112/insert.php";

    public ScanBTFragment() {
        // Required empty public constructor
    }

    public static ScanBTFragment newInstance() {
        ScanBTFragment fragment = new ScanBTFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_bt, container, false);

        arrayListDevices = new ArrayList<Devices>();
        deviceAdapter=new DeviceAdapter(getActivity(),arrayListDevices);

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

    public void scan(){
        Toast.makeText(getActivity(), "Starting search for BT devices",
                Toast.LENGTH_SHORT).show();
        deviceAdapter.clear();
        mBlueAdapter.startDiscovery();
    }

    class PostAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(link);
            try {
                ArrayList<NameValuePair> nameValuePairsFull = new ArrayList<NameValuePair>(3);
                nameValuePairsFull.add(new BasicNameValuePair("selection", "FullAnon"));
                nameValuePairsFull.add(new BasicNameValuePair("resultHash", resultHashFull));
                nameValuePairsFull.add(new BasicNameValuePair("resultClass", resultClass));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairsFull));
                HttpResponse responseFull = httpclient.execute(httppost);

                ArrayList<NameValuePair> nameValuePairsSemi = new ArrayList<NameValuePair>(3);
                nameValuePairsSemi.add(new BasicNameValuePair("selection", "SemiAnon"));
                nameValuePairsSemi.add(new BasicNameValuePair("resultHash", resultHashSemi));
                nameValuePairsSemi.add(new BasicNameValuePair("resultClass", resultClass));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairsSemi));
                HttpResponse response = httpclient.execute(httppost);

                ArrayList<NameValuePair> nameValuePairsNo = new ArrayList<NameValuePair>(3);
                nameValuePairsNo.add(new BasicNameValuePair("selection", "NoAnon"));
                nameValuePairsNo.add(new BasicNameValuePair("resultHash", resultHashNo));
                nameValuePairsNo.add(new BasicNameValuePair("resultClass", resultClass));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairsNo));
                HttpResponse responseNo = httpclient.execute(httppost);
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

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            resultClass = "";
            resultHashFull = "";
            resultHashSemi = "";
            resultHashNo = "";
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothClass btClass=intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
                // Add the name and address to an array adapter to show in a ListView
                resultClass = BluetoothDevices.DeviceClass(btClass.getDeviceClass());
                resultHashFull = HashMethods.hashMethodSHA_1
                        (HashMethods.currentSecond() + device.getAddress());
                resultHashSemi = HashMethods.hashMethodSHA_1
                        (HashMethods.currentHour() + device.getAddress());
                resultHashNo = HashMethods.hashMethodSHA_1
                        (device.getAddress());
                new PostAsyncTask().execute((Void) null);

                deviceAdapter.add(new Devices(device.getName(), device.getAddress(),
                        resultHashFull, resultHashSemi, resultHashNo));
            }
        }
    };
}
