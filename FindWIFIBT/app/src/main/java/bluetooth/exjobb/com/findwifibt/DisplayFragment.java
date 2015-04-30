package bluetooth.exjobb.com.findwifibt;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

            try {
                result = new GetAsyncTask().execute().get();
                String time[] = new String[result.size()];
                String hash[] = new String[result.size()];
                String device[] = new String[result.size()];
                if(!result.contains("0 results")) {
                    for (int i = 0; i < result.size(); i++) {
                        String[] parts = result.get(i).split(";");
                        time[i] = parts[0];
                        hash[i] = parts[1];
                        device[i] = parts[2];
                        j++;
                    }
                    for (int i = 0; i < j; i++) {
                        displayAdapter.add(new Display(time[i], hash[i], device[i]));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
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
