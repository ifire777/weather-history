package by.mrkip.apps.weatherarchive;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import by.mrkip.apps.weatherarchive.adapters.WeatherCardAdapter;
import by.mrkip.apps.weatherarchive.location.LocationActivity;
import by.mrkip.apps.weatherarchive.model.WeatherCard;
import by.mrkip.apps.weatherarchive.presenters.CurrentWeatherCityListPresenter;
import by.mrkip.libs.http.HttpClient;
import by.mrkip.libs.http.httpHelper.GetQueryBuilder;

import static android.content.ContentValues.TAG;
import static by.mrkip.apps.weatherarchive.globalObj.Api.FUTURE_WEATHER_URL;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_CLIMATE;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_CURRENT_WEATHER;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_DATE;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_FORMAT;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_INCLUDELOCATION;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_KEY;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_LANG;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_NUMOFDAY;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_Q;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_SHOWLOCALTIME;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_TP;
import static by.mrkip.apps.weatherarchive.globalObj.Api.WEATHER_API_KEY;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


	/*private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));*/
	private static final int REQUEST_SELECT_PLACE = 1000;


	private List<WeatherCard> cardsList;
	private RecyclerView recyclerView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				/*try { //TODO: Too easy and does't work without google play services! need to be rewrite
					Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
							.setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
							.setFilter(new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ CITIES).build())
							.build(MainActivity.this);
					startActivityForResult(intent, REQUEST_SELECT_PLACE);
				} catch (GooglePlayServicesRepairableException |
						GooglePlayServicesNotAvailableException e) {
					e.printStackTrace();
				}*/

				try {
					Intent intent = new Intent(MainActivity.this, CitySelectionActivity.class);
					startActivityForResult(intent, REQUEST_SELECT_PLACE);
				} catch (Exception e) {
					e.printStackTrace();
				}


			}

		});

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		recyclerView = (RecyclerView) findViewById(R.id.rsa_view_recycle);

		initRecyclerView();
		//TODO: start city list get from sqlite or preference
		new MyTask().execute(getFutureDayWeatherQuery("53.6667", "23.8333", "today"),
				getFutureDayWeatherQuery("23.6667", "13.8333", "today"),
				getFutureDayWeatherQuery("77.4445", "-35.6835", "today"),
				getFutureDayWeatherQuery("63.6667", "123.8333", "today"));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_SELECT_PLACE) {
			if (resultCode == RESULT_OK) {

				new MyTask().execute(getFutureDayWeatherQuery(data.getStringExtra("cityLan"),data.getStringExtra("cityLon"), "today"));

			} else {
				Snackbar.make(new View(this), "Place selection failed: ", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initRecyclerView() {
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(new WeatherCardAdapter(cardsList, 0));

		setItemTouchHelper();
	}

	private void setItemTouchHelper() {
		ItemTouchHelper.SimpleCallback swipeTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
				WeatherCardAdapter adapter = (WeatherCardAdapter) recyclerView.getAdapter();
				adapter.remove(viewHolder.getAdapterPosition());
			}
		};
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeTouchCallback);
		itemTouchHelper.attachToRecyclerView(recyclerView);
	}

	private String getFutureDayWeatherQuery(String coorLan, String coorLon, String dt) {
		return new GetQueryBuilder(FUTURE_WEATHER_URL)
				.addParam(QUERY_PARAM_Q, coorLan + "," + coorLon)
				.addParam(QUERY_PARAM_FORMAT, "json")
				.addParam(QUERY_PARAM_DATE, dt)
				.addParam(QUERY_PARAM_NUMOFDAY, "1")
				.addParam(QUERY_PARAM_INCLUDELOCATION, "yes")
				.addParam(QUERY_PARAM_SHOWLOCALTIME, "yes")
				.addParam(QUERY_PARAM_LANG, "ru")
				.addParam(QUERY_PARAM_KEY, WEATHER_API_KEY)
				.addParam(QUERY_PARAM_TP, "12")
				.addParam(QUERY_PARAM_CURRENT_WEATHER, "yes")
				.addParam(QUERY_PARAM_CLIMATE, "no")
				.getUrl();
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_get_location) {
			Intent intent = new Intent(this, LocationActivity.class);
			startActivity(intent);
			return true;
		} else if (id == R.id.action_mail) {
			gotoMail(null);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {
			Intent intent = new Intent(this, SpecificCityActivity.class);
			startActivity(intent);

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	public void gotoMail(View view) {
		String mailto = "mailto:bob@example.org" +
				"?subject=" + Uri.encode(getString(R.string.mail_head)) +
				"&body=" + Uri.encode(getString(R.string.mail_body));

		Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
		emailIntent.setData(Uri.parse(mailto));

		try {
			startActivity(emailIntent);
		} catch (ActivityNotFoundException e) {
			Snackbar.make(view, "E-mail app unavailable." + e.toString(), Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
		} catch (Exception e) {
			Snackbar.make(view, "error ." + e.toString(), Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();

		}

	}

	class MyTask extends AsyncTask<String, Integer, List<WeatherCard>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected List<WeatherCard> doInBackground(String... args) {
			HttpClient httpClient = new HttpClient();
			try {
				List<WeatherCard> testL = new ArrayList<WeatherCard>();
				//testL = httpClient.getResult(args[0], new PastWeatherListPresenter());
				for (int i = 0; i < args.length; i++) {

					testL.add((WeatherCard) httpClient.getResult(args[i], new CurrentWeatherCityListPresenter()));
				}
				return testL;

			} catch (IOException e) {
				Log.e(TAG, this.toString() + ":", e);

			} catch (Exception e) {
				Log.e(TAG, this.toString() + ":", e);

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... vd) {
			super.onProgressUpdate(vd);

		}

		@Override
		protected void onPostExecute(List<WeatherCard> result) {
			//	super.onPostExecute(result);
			if (result != null) {
				cardsList = result;
				((WeatherCardAdapter) recyclerView.getAdapter()).addItems(result);
			}
		}

	}

}
