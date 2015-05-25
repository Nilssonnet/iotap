package bluetooth.exjobb.com.findbt;

import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Fragment that fetches Bluetooth devices from database and displays them in a list.
 * Created by Mattias Nilsson & Sebastian Olsson
 */
public class DisplayFragment extends Fragment implements View.OnClickListener{

    private String link = "http://213.65.109.112/get.php";  //URL to database.
    private String radioButtonResult = "";
    private ArrayList<String> time;
    private ArrayList<Integer> RSSI;
    private ArrayList<String> deviceClass;
    private ArrayList<String> hashFull;
    private ArrayList<String> hashSemi;
    private ArrayList<String> hashNo;
    private ArrayList<String> hash;
    private ArrayList<Display> arrayListDisplay;
    private DisplayAdapter displayAdapter;
    private ListView devicesList;

    public DisplayFragment() {
        // Required empty public constructor
    }

    /*
     * Sets up the fragment.
     */
    public static DisplayFragment newInstance() {
        DisplayFragment fragment = new DisplayFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display, container, false);

        arrayListDisplay = new ArrayList<Display>();
        displayAdapter=new DisplayAdapter(getActivity(),arrayListDisplay);
        devicesList = (ListView) view.findViewById(R.id.listViewDisplay);
        devicesList.setAdapter(displayAdapter);
        time = new ArrayList<String>();
        RSSI = new ArrayList<Integer>();
        deviceClass = new ArrayList<String>();
        hashFull = new ArrayList<String>();
        hashSemi = new ArrayList<String>();
        hashNo = new ArrayList<String>();
        hash = new ArrayList<String>();
        Button displayButton = (Button) view.findViewById(R.id.buttonDisplay);
        displayButton.setOnClickListener(this);

        /*
         * Selects which anonymization-level to show in list.
         */
        RadioGroup radioGroupDisplay = (RadioGroup) view.findViewById(R.id.radioGroupDisplay);
        radioGroupDisplay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonFullAnonDisplay:
                        radioButtonResult = "FullAnon";
                        break;
                    case R.id.radioButtonSemiAnonDisplay:
                        radioButtonResult = "SemiAnon";
                        break;
                    case R.id.radioButtonNoAnonDisplay:
                        radioButtonResult = "NoAnon";
                        break;
                }
            }
        });

        /*
         * Sends the device to AnalyzeFragment.
         */
        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int split = 0;
                if(radioButtonResult.equals("FullAnon")){
                    split = 20;
                } else if(radioButtonResult.equals("SemiAnon")){
                    split = 20;
                } else if(radioButtonResult.equals("NoAnon")){
                    split = 12;
                }
                AnalyzeFragment fragment = AnalyzeFragment.newInstance(hash.get(position).
                        substring(split), deviceClass.get(position).substring(7),
                        getTimeFromPosition(position));
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container_main, fragment);
                transaction.addToBackStack("Analyze");
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonDisplay:
                display();
        }
    }

    /*
     * Logic for splitting the stream from database and display devices in list.
     */
    private void display(){
        if (radioButtonResult.equals("")){
            Toast.makeText(getActivity(), "Select a hash method", Toast.LENGTH_SHORT).show();
        }
        else{
            ArrayList<String> result;
            displayAdapter.clear();
            int j = 0;
            try {
                result = new GetAsyncTask().execute().get();
                time = new ArrayList<String>(result.size()-1);
                RSSI = new ArrayList<Integer>(result.size()-1);
                deviceClass = new ArrayList<String>(result.size()-1);
                hashFull = new ArrayList<String>(result.size()-1);
                hashSemi = new ArrayList<String>(result.size()-1);
                hashNo = new ArrayList<String>(result.size()-1);

                if(!result.contains("0 results")) {
                    for (int i = 0; i < result.size()-1; i++) {
                        String[] parts = result.get(i).split(";");
                        time.add(parts[0]);
                        RSSI.add(Integer.parseInt(parts[1].substring(6)));
                        deviceClass.add(parts[2]);
                        hashFull.add(parts[3]);
                        hashSemi.add(parts[4]);
                        hashNo.add(parts[5]);
                        j++;
                    }

                    if(radioButtonResult.equals("FullAnon")){
                        hash = new ArrayList<String>(hashFull);
                    } else if(radioButtonResult.equals("SemiAnon")){
                        hash = new ArrayList<String>(hashSemi);
                    } else if(radioButtonResult.equals("NoAnon")){
                        hash = new ArrayList<String>(hashNo);
                    }

                    for (int i = 0; i < j; i++) {
                        displayAdapter.add(new Display(time.get(i), hash.get(i), deviceClass.get(i),
                               RSSI.get(i)));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Gets all the time that selected device have been scanned.
     */
    private ArrayList<String> getTimeFromPosition(int pos){
        String hashdev = hash.get(pos);
        ArrayList<String> res = new ArrayList<String>();
        for (int i = 0; i < hash.size(); i++){
            if(hashdev.equals(hash.get(i))){
                res.add(time.get(i));
            }
        }
        return res;
    }

    /*
     * Gets data from database.
     */
    class GetAsyncTask extends AsyncTask<String, String, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... arg0) {
            ArrayList<String> devicesArray = new ArrayList<String>();
            int i = 0;
            URL urlObj = null;
            try {
                urlObj = new URL(link);
                URLConnection lu = urlObj.openConnection();
                BufferedReader rd = new BufferedReader(new InputStreamReader(lu.getInputStream()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    devicesArray.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return devicesArray;
        }
    }
}
