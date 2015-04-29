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
public class FullAnonFragment extends Fragment {
    public FullAnonFragment() {
        // Required empty public constructor
    }

    public static FullAnonFragment newInstance() {
        FullAnonFragment fragment = new FullAnonFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display, container, false);

        Toast.makeText(getActivity(), "Current second: " + currentSecond(),
                Toast.LENGTH_SHORT).show();
        return view;
    }

    private String currentSecond(){
        Calendar rightNow = Calendar.getInstance();
        return  String.valueOf(rightNow.get(Calendar.YEAR)) +
                String.valueOf(rightNow.get(Calendar.MONTH)) +
                String.valueOf(rightNow.get(Calendar.DATE)) +
                String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY)) +
                String.valueOf(rightNow.get(Calendar.MINUTE)) +
                String.valueOf(rightNow.get(Calendar.SECOND));
    }

}
