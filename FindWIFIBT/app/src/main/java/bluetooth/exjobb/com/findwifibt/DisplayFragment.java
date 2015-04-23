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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayFragment extends Fragment {
    private ArrayList< String> devices;

    //private ProgressDialog pDialog;

    private String url = "http://213.65.109.112/get.php";

    private ListView devicesList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> devicesArray;
    //private StringBuffer sb = new StringBuffer("");
    int i = 0;

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

        devicesArray = new ArrayList<String>();
        new GetAsyncTask().execute();

        adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, devicesArray);
        devicesList = (ListView) view.findViewById(R.id.listViewDisplay);

        devicesList.setAdapter(adapter);

        // Loading products in Background Thread

        Toast.makeText(getActivity(), "result: " + new GetAsyncTask().execute((String) null),
                Toast.LENGTH_SHORT).show();
        return view;
    }


    class GetAsyncTask extends AsyncTask<String, String, String> {

        public String getDevices() {
            StringBuffer sb = new StringBuffer("");
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            try {

                request.setURI(new URI(url));
                HttpResponse response = httpClient.execute(request);
                BufferedReader in = new BufferedReader
                        (new InputStreamReader(response.getEntity().getContent()));


                String line = "";
                i++;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    devicesArray.add(in.readLine());
                    break;
                }
                in.close();

            } catch (Exception e) {
                Log.e("log_tag", "Error:  " + e.toString());
            }
            return sb.toString();

        }
        @Override
        protected String doInBackground(String... arg0) {
            return getDevices();
            //i++;
            //return null;
        }
    }
}
