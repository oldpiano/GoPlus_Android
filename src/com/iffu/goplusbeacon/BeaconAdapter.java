package com.iffu.goplusbeacon;

import java.util.ArrayList;
import java.util.Collection;
import org.altbeacon.beacon.Beacon;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class BeaconAdapter extends BaseAdapter {

	private ArrayList<Beacon> beacons;
	private LayoutInflater inflater;

	public BeaconAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);
	    this.beacons = new ArrayList<Beacon>();
	}
	
	public void replaceWith(Collection<Beacon> newBeacons) {
	    this.beacons.clear();
	    this.beacons.addAll(newBeacons);
	    notifyDataSetChanged();
	  }

	@Override
	  public int getCount() {
	    return beacons.size();
	  }

	  @Override
	  public Beacon getItem(int position) {
	    return beacons.get(position);
	  }

	  @Override
	  public long getItemId(int position) {
	    return position;
	  }

	  @Override
	  public View getView(int position, View view, ViewGroup parent) {
	    view = inflateIfRequired(view, position, parent);
	    bind(getItem(position), view);
	    return view;
	  }

	  private void bind(Beacon beacon, View view) {
	    ViewHolder holder = (ViewHolder) view.getTag();
	    holder.macTextView.setText(String.format("MAC: %s", beacon.getBluetoothAddress()));
	    holder.uuidTextView.setText("UUID: "+ beacon.getId1());
	    holder.majorTextView.setText("Major: " + beacon.getId2());
	    holder.minorTextView.setText("Minor: " + beacon.getId3());
	    holder.measuredPowerTextView.setText("MPower: " + beacon.getTxPower());
	    holder.rssiTextView.setText("RSSI: " + beacon.getRssi());
	    holder.proximityTextView.setText("Proximity: " + beacon.getDistance() + "m");

	  }

	  private View inflateIfRequired(View view, int position, ViewGroup parent) {
	    if (view == null) {
	      view = inflater.inflate(R.layout.device_item, null);
	      view.setTag(new ViewHolder(view));
	    }
	    return view;
	  }

	  static class ViewHolder {
	    final TextView macTextView;
	    final TextView uuidTextView;
	    final TextView majorTextView;
	    final TextView minorTextView;
	    final TextView measuredPowerTextView;
	    final TextView rssiTextView;
	    final TextView proximityTextView;

	    ViewHolder(View view) {
	      macTextView = (TextView) view.findViewWithTag("mac");
	      uuidTextView = (TextView) view.findViewWithTag("uuid");
	      majorTextView = (TextView) view.findViewWithTag("major");
	      minorTextView = (TextView) view.findViewWithTag("minor");
	      measuredPowerTextView = (TextView) view.findViewWithTag("mpower");
	      rssiTextView = (TextView) view.findViewWithTag("rssi");
	      proximityTextView = (TextView) view.findViewWithTag("proximity");
	    }
	  }
}
