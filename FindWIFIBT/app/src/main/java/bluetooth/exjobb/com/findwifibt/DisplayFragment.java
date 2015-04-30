package bluetooth.exjobb.com.findwifibt;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayFragment extends Fragment implements View.OnClickListener{

    private String link = "http://213.65.109.112/get.php";
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
                for (int i = 0; i < result.size(); i++){
                    String[] parts = result.get(i).split(";");
                    time[i] = parts[0];
                    hash[i] = parts[1];
                    device[i] = parts[2];
                    j++;
                }
                for(int i = 0; i < j; i++){
                    displayAdapter.add(new Display(time[i], hash[i], device[i]));
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
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(link);
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("selection", radioButtonResult));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
            }
            catch(Exception e)
            {
                Log.e("log_tag", "Error:  " + e.toString());
            }

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
