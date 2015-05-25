package bluetooth.exjobb.com.findbt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Modified adapter to show devices from database.
 * Created by Mattias Nilsson & Sebastian Olsson
 */
public class DisplayAdapter extends ArrayAdapter<Display> {
    public DisplayAdapter(Context context, ArrayList<Display>display) {
        super(context, 0,display);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Display display = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display, parent, false);
        }

        TextView textViewTime = (TextView)convertView.findViewById(R.id.tvTime);
        TextView textViewHashed = (TextView)convertView.findViewById(R.id.tvHashed);
        TextView textViewDevice = (TextView)convertView.findViewById(R.id.tvDevice);
        TextView textViewRSSI = (TextView)convertView.findViewById(R.id.tvDisplayRSSI);

        textViewTime.setText(display.timeStamp);
        textViewHashed.setText(display.hashedMAC);
        textViewDevice.setText(display.deviceClass);
        textViewRSSI.setText("RSSI: " + display.RSSI + " dBm");

        return convertView;
    }
}
