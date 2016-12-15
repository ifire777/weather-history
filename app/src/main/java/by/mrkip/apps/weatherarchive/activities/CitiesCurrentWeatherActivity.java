package by.mrkip.apps.weatherarchive.activities;

import android.content.Intent;
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

import by.mrkip.apps.weatherarchive.App;
import by.mrkip.apps.weatherarchive.R;
import by.mrkip.apps.weatherarchive.adapters.WeatherCardAdapter;
import by.mrkip.apps.weatherarchive.jsonParsers.CurrentWeatherCityListParser;
import by.mrkip.apps.weatherarchive.location.LocationActivity;
import by.mrkip.apps.weatherarchive.model.WeatherCard;
import by.mrkip.apps.weatherarchive.utils.BackendQueryBuilder;
import by.mrkip.apps.weatherarchive.utils.OutAppActions;
import by.mrkip.libs.http.HttpClient;

import static android.content.ContentValues.TAG;
import static by.mrkip.apps.weatherarchive.activities.PlaceSelectionActivity.ACTIVITY_REQUEST_CODE_SELECT_PLACE;

public class CitiesCurrentWeatherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


	private List<WeatherCard> cardsList;
	private RecyclerView recyclerView;
	@SuppressWarnings("WrongConstant")
	private BackendQueryBuilder backendQueryBuilder = (BackendQueryBuilder) App.getAppContext().getSystemService(App.BACKEND_QUERY_BUILDER);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cities_current_weather);
		initActivityElements();
		initRecyclerView();

		//TODO: start city list get from sqlite or preference
		new MyTask().execute(backendQueryBuilder.buildFutureDayWeatherQuery("53.6667", "23.8333", "today",1),
				backendQueryBuilder.buildFutureDayWeatherQuery("23.6667", "13.8333", "today",1),
				backendQueryBuilder.buildFutureDayWeatherQuery("77.4445", "-35.6835", "today",1),
				backendQueryBuilder.buildFutureDayWeatherQuery("63.6667", "123.8333", "today",1));
	}

	private void initActivityElements() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(CitiesCurrentWeatherActivity.this, PlaceSelectionActivity.class);
				startActivityForResult(intent, ACTIVITY_REQUEST_CODE_SELECT_PLACE);
			}

		});

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	private void initRecyclerView() {
		recyclerView = (RecyclerView) findViewById(R.id.rsa_view_recycle);

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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_REQUEST_CODE_SELECT_PLACE) {
			if (resultCode == RESULT_OK) {
				new MyTask().execute(backendQueryBuilder.buildFutureDayWeatherQuery(data.getStringExtra("cityLan"), data.getStringExtra("cityLon"), "today",1));
			} else {
				Snackbar.make(new View(this), R.string.message_place_selection_fail, Snackbar.LENGTH_LONG)
						.setAction(R.string.message_header_information, null).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
			new OutAppActions().gotoMail(this);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {
			Intent intent = new Intent(this, CityLastMonthWeatherActivity.class);
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

	//TODO move to another class
	//TODO create some abstractions for that
	class MyTask extends AsyncTask<String, Integer, List<WeatherCard>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected List<WeatherCard> doInBackground(String... args) {
			//noinspection WrongConstant
			HttpClient httpClient = (HttpClient) getApplicationContext().getSystemService(App.HTTP_CLIENT);
			try {
				List<WeatherCard> testL = new ArrayList<>();
				for (int i = 0; i < args.length; i++) {

					testL.add(httpClient.getResult(args[i], new CurrentWeatherCityListParser()));
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

			if (result != null) {
				cardsList = result;
				((WeatherCardAdapter) recyclerView.getAdapter()).addItems(result);
			}
			super.onPostExecute(result);
		}

	}

}
