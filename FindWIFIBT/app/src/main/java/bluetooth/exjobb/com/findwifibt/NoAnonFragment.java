package bluetooth.exjobb.com.findwifibt;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoAnonFragment extends Fragment {


    public NoAnonFragment() {
        // Required empty public constructor
    }

    public static NoAnonFragment newInstance() {
        NoAnonFragment fragment = new NoAnonFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_anon, container, false);
    }


}
