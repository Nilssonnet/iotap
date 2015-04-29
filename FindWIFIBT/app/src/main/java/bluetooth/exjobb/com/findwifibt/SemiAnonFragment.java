package bluetooth.exjobb.com.findwifibt;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class SemiAnonFragment extends Fragment {


    public SemiAnonFragment() {
        // Required empty public constructor
    }

    public static SemiAnonFragment newInstance() {
        SemiAnonFragment fragment = new SemiAnonFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display, container, false);

        Toast.makeText(getActivity(), "Current second: " + currentHour(),
                Toast.LENGTH_SHORT).show();
        return view;
    }

    private String currentHour(){
        Calendar rightNow = Calendar.getInstance();
        return  String.valueOf(rightNow.get(Calendar.YEAR)) +
                String.valueOf(rightNow.get(Calendar.MONTH)) +
                String.valueOf(rightNow.get(Calendar.DATE)) +
                String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY));
    }


}
