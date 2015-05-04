package bluetooth.exjobb.com.findwifibt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sebastian olsson on 15-04-12.
 */
public class DeviceAdapter extends ArrayAdapter<Devices> {
    public DeviceAdapter(Context context, ArrayList<Devices>devices) {
        super(context, 0,devices);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Devices devices = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.devices, parent, false);
        }

        TextView textViewMAC = (TextView)convertView.findViewById(R.id.tvMAC);
        TextView textViewType = (TextView)convertView.findViewById(R.id.tvType);
        TextView textViewHashFull = (TextView)convertView.findViewById(R.id.tvHashFull);
        TextView textViewHashSemi = (TextView)convertView.findViewById(R.id.tvHashSemi);
        TextView textViewHashNo = (TextView)convertView.findViewById(R.id.tvHashNo);

        textViewMAC.setText(devices.macAddress);
        textViewType.setText(devices.type);
        textViewHashFull.setText("Full anonymization: " + devices.hashFull);
        textViewHashSemi.setText("Semi anonymization: " + devices.hashSemi);
        textViewHashNo.setText("Only SHA-1: " + devices.hashNo);

        return convertView;
    }
}
