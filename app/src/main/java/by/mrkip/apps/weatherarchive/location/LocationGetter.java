package by.mrkip.apps.weatherarchive.location;


import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Date;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class LocationGetter {

	private LocationManager locationManager;


	public LocationGetter(Activity parentActivity) throws SecurityException {

		locationManager = (LocationManager) parentActivity.getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, locationListener);

	}

	private void showLocation(Location location) throws SecurityException {
		if (location == null)
			return;
		if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
			//	TODO: Save or send location
		} else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
			//	TODO: Save or send location
		}
		locationManager.removeUpdates(locationListener);
	}

	private String formatLocation(Location location) {
		if (location == null)
			return "";
		return String.format(Locale.US, "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT", location.getLatitude(), location.getLongitude(), new Date(location.getTime()));
	}

	private void checkEnabled() {
		//	tvEnabledGPS.setText(new StringBuilder().append("Enabled: ").append(locationManager
		//			.isProviderEnabled(LocationManager.GPS_PROVIDER)).toString());
		//	tvEnabledNet.setText(new StringBuilder().append("Enabled: ").append(locationManager
		//			.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).toString());
	}

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			showLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			checkEnabled();
		}

		@Override
		public void onProviderEnabled(String provider) {
			checkEnabled();
			showLocation(locationManager.getLastKnownLocation(provider));
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			if (provider.equals(LocationManager.GPS_PROVIDER)) {
				//tvStatusGPS.setText("Status: " + String.valueOf(status));
			} else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
				//tvStatusNet.setText("Status: " + String.valueOf(status));
			}
		}
	};


}
