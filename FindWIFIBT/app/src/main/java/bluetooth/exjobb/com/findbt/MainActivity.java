package bluetooth.exjobb.com.findbt;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Sebastian Olsson on 15-04-12.
 * Modified by Mattias Nilsson
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().replace(R.id.container_main,
                StartFragment.newInstance(), "Start").commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
