package bluetooth.exjobb.com.findbt;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyzeFragment extends Fragment {

    private String hash;
    private String device;
    private ArrayList<String> time;

    public AnalyzeFragment() {
        // Required empty public constructor
    }

    public static AnalyzeFragment newInstance(String hash, String device, ArrayList<String> time){
        AnalyzeFragment fragment = new AnalyzeFragment();
        Bundle args = new Bundle();
        args.putString("Hash", hash);
        args.putString("Device", device);
        args.putStringArrayList("Time", time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            hash = getArguments().getString("Hash");
            device = getArguments().getString("Device");
            time = getArguments().getStringArrayList("Time");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analyze, container, false);
        TextView textView = (TextView) view.findViewById(R.id.textViewAnalyze);
        textView.setText("The device with the hashed MAC-address " + hash.substring(8) +
                "\nthat is a Bluetooth device of class " + device.substring(7) +
                " has been at this location at these times:");
        ListView listView = (ListView) view.findViewById(R.id.listViewAnalyze);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, time);
        listView.setAdapter(arrayAdapter);
        return view;
    }
}
