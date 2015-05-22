package bluetooth.exjobb.com.findbt;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 *
 * Created by Mattias Nilsson on 15-04-23.
 */
public class StartFragment extends Fragment implements View.OnClickListener{
    View view;

    public StartFragment() {
        // Required empty public constructor
    }

    public static StartFragment newInstance() {
        StartFragment fragment = new StartFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_start, container, false);
        Button scan = (Button) view.findViewById(R.id.buttonScan);
        scan.setOnClickListener(this);
        Button display = (Button) view.findViewById(R.id.buttonDisplay);
        display.setOnClickListener(this);
        return view;
        }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.buttonScan:
                getFragmentManager().beginTransaction().replace(R.id.container_main,
                        ScanBTFragment.newInstance(), "Scan").addToBackStack("Scan").commit();
                break;
            case R.id.buttonDisplay:
                getFragmentManager().beginTransaction().replace(R.id.container_main,
                        DisplayFragment.newInstance(), "Display").addToBackStack("Disp").commit();
                break;
        }
    }


}
