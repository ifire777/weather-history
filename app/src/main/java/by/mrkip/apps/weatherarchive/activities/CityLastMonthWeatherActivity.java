package by.mrkip.apps.weatherarchive.activities;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import by.mrkip.apps.weatherarchive.App;
import by.mrkip.apps.weatherarchive.R;
import by.mrkip.apps.weatherarchive.adapters.WeatherCardAdapter;
import by.mrkip.apps.weatherarchive.jsonParsers.PastWeatherListPresenter;
import by.mrkip.apps.weatherarchive.location.LocationActivity;
import by.mrkip.apps.weatherarchive.model.WeatherCard;
import by.mrkip.apps.weatherarchive.utils.MyDateFun;
import by.mrkip.apps.weatherarchive.utils.SpecificQueryBuilder;
import by.mrkip.libs.http.HttpClient;

public class CityLastMonthWeatherActivity extends AppCompatActivity {
	private List<WeatherCard> cardsList;
	private RecyclerView recyclerView;
	private MyDateFun dtFun = new MyDateFun();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_city_last_month_weather);

		Toolbar toolbar = (Toolbar) findViewById(R.id.alone_toolbar);
		setSupportActionBar(toolbar);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		recyclerView = (RecyclerView) findViewById(R.id.sca_view_recycle);
		initRecyclerView();

		Intent intent = getIntent();

		Date dt = new Date();

		String endDt = dtFun.dateDefToQueryParam(dt, Calendar.DAY_OF_MONTH, -1);
		String startDt = dtFun.dateDefToQueryParam(dt, Calendar.DAY_OF_MONTH, -30);
		//TODO constnats
		String city_lan = intent.getStringExtra("city_lan");
		String city_lon = intent.getStringExtra("city_lon");
		//TODO: SpecificQueryBuilder() - app singltone
		new MyTask().execute( new SpecificQueryBuilder().getPastDayWeatherQuery(city_lan, city_lon, startDt, endDt));

	}


	private void initRecyclerView() {
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(new WeatherCardAdapter(cardsList, 0));

	}



	@Override
	public void onBackPressed() {
		super.onBackPressed();
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
			//noinspection WrongConstant
			HttpClient httpClient = (HttpClient) getSystemService(App.HTTP_CLIENT);
			try {
				List<WeatherCard> testL;
				testL = httpClient.getResult(args[0], new PastWeatherListPresenter());
				return testL;

			} catch (IOException e) {
				Log.e(this.toString(), this.toString() + "|IOException :", e);
				return null;

			} catch (Exception e) {
				Log.e(this.toString(), this.toString() + "|Exception:", e);
				return null;

			}

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
			} else {
				Snackbar.make(recyclerView, getString(R.string.sca_nodata), Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		}

	}

}
