package com.iffu.goplusbeacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class RangingActivity extends Activity {
	private BeaconAdapter adapter;
	private ArrayList<Beacon> goplusBeacons;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranging);
		
		goplusBeacons = new ArrayList<Beacon>();
		ListView lv_ranged = (ListView) findViewById(R.id.lv_ranged);
		adapter = new BeaconAdapter(this);
		lv_ranged.setAdapter(adapter);
    }
	
    @Override 
    protected void onDestroy() {
        super.onDestroy();
    }
    
    @Override 
    protected void onPause() {
    	super.onPause();
    	// Tell the Application not to pass off ranging updates to this activity
    	((GoPlusBeaconApp)this.getApplication()).setRangingActivity(null);
    }
    
    @Override 
    protected void onResume() {
    	super.onResume();
    	// Tell the Application to pass off ranging updates to this activity
    	((GoPlusBeaconApp)this.getApplication()).setRangingActivity(this);
    }    

    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
    	final List<Beacon> exBeacons;
        if (beacons.size() > 0) {
        	exBeacons = filterBeacons(beacons);
        	goplusBeacons.addAll(exBeacons);

        	runOnUiThread(new Runnable() {
				@Override
				public void run() {
					adapter.replaceWith(exBeacons);
				}
			});
        }
    }

    private List<Beacon> filterBeacons(Collection<Beacon> beacons) {
	    List<Beacon> filteredBeacons = new ArrayList<Beacon>(beacons.size());
	    for (Beacon beacon : beacons) {
	        filteredBeacons.add(beacon);
	    }

	    Collections.sort(filteredBeacons, new Comparator<Beacon>() {
			@Override
			public int compare(Beacon lhs, Beacon rhs) {
				return Double.compare(lhs.getDistance(), rhs.getDistance());
			}
		});
	    return filteredBeacons;
	}
}
