package bluetooth.exjobb.com.findwifibt;

import android.app.Activity;
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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sebastian olsson on 15-04-12.
 */

public class MainActivity extends Activity {
    private ArrayAdapter<String> deviceArray ;
    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBlueAdapter;
    private ListView listView;

    private ArrayList<Devices> arrayListDevices;
    private DeviceAdapter deviceAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button searchButton = (Button) findViewById(R.id.search_button);
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBlueAdapter==null){
            Toast.makeText(MainActivity.this, "This device do not have Bluetooth",
                    Toast.LENGTH_SHORT).show();

        }

        arrayListDevices = new ArrayList<Devices>();
        deviceAdapter=new DeviceAdapter(this,arrayListDevices);

        listView = (ListView)findViewById(R.id.listViewDevices);
        listView.setAdapter(deviceAdapter);
        //deviceAdapter.add(new Devices("Test1", "Test1.2"));
        //deviceAdapter.add(new Devices("Test2", "Test2.2"));

        if (!mBlueAdapter.isEnabled()) { //returns false if BT is not enabled
            ///Creates dialog for turning on Bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);

    }

    public void scan(View view){
        Toast.makeText(MainActivity.this, "Starting search for BT devices", Toast.LENGTH_SHORT).show();
        //deviceAdapter.add(new Devices("Test2", "Test2.2"));
        deviceAdapter.clear();
        mBlueAdapter.startDiscovery();
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
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                //deviceAdapter.add( device.getName() + "\n" + device.getAddress()+"\n"+device.getBluetoothClass()); //needs api 18 device.getType()
                deviceAdapter.add(new Devices(device.getName(),device.getAddress()));

            }
        }
    };





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
