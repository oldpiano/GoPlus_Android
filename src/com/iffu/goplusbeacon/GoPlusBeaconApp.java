package com.iffu.goplusbeacon;

import java.util.Collection;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import android.app.Application;
import android.os.RemoteException;
import android.util.Log;

public class GoPlusBeaconApp  extends Application implements BootstrapNotifier, RangeNotifier {
	private static final String TAG = "GoPlusBeaconApp";
	private static final String GOPLUS_BEACON_UUID = "F7A3E806-F5BB-43F8-BA87-0783669EBEB1";
	private BeaconManager mBeaconManager;
	private Region mAllBeaconsRegion;
	private MonitoringActivity mMonitoringActivity;
	private RangingActivity mRangingActivity;
	private BackgroundPowerSaver mBackgroundPowerSaver;
	@SuppressWarnings("unused")
	private RegionBootstrap mRegionBootstrap;
	
	@Override 
	public void onCreate() {
		mAllBeaconsRegion = new Region("default", Identifier.parse(GOPLUS_BEACON_UUID), null, null);
		
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
		mBackgroundPowerSaver = new BackgroundPowerSaver(this);		
        mRegionBootstrap = new RegionBootstrap(this, mAllBeaconsRegion);
        
        /**
         * The duration in milliseconds spent not scanning between each bluetooth scan cycle.
         * The default duration in Altbeacon Library is 5*6*1000(5min)
         */
        mBeaconManager.setBackgroundBetweenScanPeriod(20000);	//20sec
        
        /**
         * The default duration in milliseconds of the bluetooth scan cycle when no ranging/monitoring clients are in the foreground
         * The default duration in Altbeacon Library is 10000(10sec)
         */
        mBeaconManager.setBackgroundScanPeriod(10000);	//10sec
	}
	
	@Override
	public void didDetermineStateForRegion(int arg0, Region arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didEnterRegion(Region arg0) {
		if (mMonitoringActivity != null) { 
			mMonitoringActivity.didEnterRegion(arg0); 
		}		 
		
		try { 
			Log.d(TAG, "entered region.  starting ranging"); 
			mBeaconManager.startRangingBeaconsInRegion(mAllBeaconsRegion); 
			mBeaconManager.setRangeNotifier(this); 
		} catch (RemoteException e) { 
			Log.e(TAG, "Cannot start ranging"); 
		} 

	}

	@Override
	public void didExitRegion(Region arg0) {
		if (mMonitoringActivity != null) { 
			mMonitoringActivity.didExitRegion(arg0); 
		} 

	}

	@Override
	public void didRangeBeaconsInRegion(Collection<Beacon> arg0, Region arg1) {
		if (mRangingActivity != null) { 
			mRangingActivity.didRangeBeaconsInRegion(arg0, arg1); 
		} 
	}

	public void setMonitoringActivity(MonitoringActivity activity) { 
		mMonitoringActivity = activity; 
	} 
	 
	public void setRangingActivity(RangingActivity activity) { 
		mRangingActivity = activity; 
	} 

}
