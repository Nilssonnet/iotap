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
 * A simple {@link Fragment} subclass.
 */
public class DisplayFragment extends Fragment implements View.OnClickListener{

    private String link;
    private String radioButtonResult = "";

    private ArrayList<String> time;
    private ArrayList<String> hash;
    private ArrayList<String> device;
    private ArrayList<Integer> RSSI;

    private ArrayList<Display> arrayListDisplay;
    private DisplayAdapter displayAdapter;

    private ListView devicesList;

    public DisplayFragment() {
        // Required empty public constructor
    }

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
        hash = new ArrayList<String>();
        device = new ArrayList<String>();
        RSSI = new ArrayList<Integer>();

        Button displayButton = (Button) view.findViewById(R.id.buttonDisplay);
        displayButton.setOnClickListener(this);

        RadioGroup radioGroupDisplay = (RadioGroup) view.findViewById(R.id.radioGroupDisplay);
        radioGroupDisplay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonFullAnonDisplay:
                        radioButtonResult = "FullAnon";
                        link = "http://213.65.109.112/getFullAnon.php";
                        break;
                    case R.id.radioButtonSemiAnonDisplay:
                        radioButtonResult = "SemiAnon";
                        link = "http://213.65.109.112/getSemiAnon.php";
                        break;
                    case R.id.radioButtonNoAnonDisplay:
                        radioButtonResult = "NoAnon";
                        link = "http://213.65.109.112/getNoAnon.php";
                        break;
                }
            }
        });

        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AnalyzeFragment fragment = AnalyzeFragment.newInstance
                        (hash.get(position), device.get(position), getTimeFromPosition(position));
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

    private void display(){
        if (radioButtonResult.equals("")){
            Toast.makeText(getActivity(), "Select a hash method", Toast.LENGTH_SHORT).show();
        }
        else{
            ArrayList<String> result;
            displayAdapter.clear();
            int j = 0;
            time.clear();
            hash.clear();
            device.clear();
            RSSI.clear();

            try {
                result = new GetAsyncTask().execute().get();

                time = new ArrayList<String>(result.size());
                hash = new ArrayList<String>(result.size());
                device = new ArrayList<String>(result.size());
                RSSI = new ArrayList<Integer>(result.size());
                if(!result.contains("0 results")) {
                    for (int i = 0; i < result.size(); i++) {
                        String[] parts = result.get(i).split(";");
                        time.add(parts[0]);
                        hash.add(parts[1]);
                        device.add(parts[2]);
                        RSSI.add(Integer.parseInt(parts[3].substring(6)));
                        j++;
                    }
                    for (int i = 0; i < j; i++) {
                        displayAdapter.add(new Display(time.get(i), hash.get(i), device.get(i),
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
