package by.mrkip.apps.weatherarchive.location;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;

import by.mrkip.apps.weatherarchive.R;


//TODO move to same package as another activities []
public class LocationActivity extends AppCompatActivity {
	TextView tvEnabledGPS;
	TextView tvStatusGPS;
	TextView tvLocationGPS;
	TextView tvEnabledNet;
	TextView tvStatusNet;
	TextView tvLocationNet;

	private LocationManager locationManager;
	StringBuilder sbGPS = new StringBuilder();
	StringBuilder sbNet = new StringBuilder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		tvEnabledGPS = (TextView) findViewById(R.id.tvEnabledGPS);
		tvStatusGPS = (TextView) findViewById(R.id.tvStatusGPS);
		tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
		tvEnabledNet = (TextView) findViewById(R.id.tvEnabledNet);
		tvStatusNet = (TextView) findViewById(R.id.tvStatusNet);
		tvLocationNet = (TextView) findViewById(R.id.tvLocationNet);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		}

	//TODO request permission on Android 6.0
	@SuppressWarnings("MissingPermission")
	private void showLocation(Location location) {
		if (location == null)
			return;
		if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
			tvLocationGPS.setText(formatLocation(location));
		} else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
			tvLocationNet.setText(formatLocation(location));
		}
		locationManager.removeUpdates(locationListener);
	}

	private String formatLocation(Location location) {
		if (location == null)
			return "";
		return String.format(Locale.US, "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT", location.getLatitude(), location.getLongitude(), new Date(location.getTime()));
	}

	private void checkEnabled() {
		tvEnabledGPS.setText("Enabled: "  +locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER));
		//TODo why you use StringBuilder
		tvEnabledNet.setText(new StringBuilder().append("Enabled: ").append(locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).toString());
	}

	public void onClickLocationSettings(View view) {
		startActivity(new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	}


	@SuppressWarnings("MissingPermission")
	@Override
	protected void onResume() {
		super.onResume();

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, locationListener);
		checkEnabled();
	}

	@SuppressWarnings("MissingPermission")
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(locationListener);
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
				tvStatusGPS.setText("Status: " + String.valueOf(status));
			} else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
				tvStatusNet.setText("Status: " + String.valueOf(status));
			}
		}
	};


}