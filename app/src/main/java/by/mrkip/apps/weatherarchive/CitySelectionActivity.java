package by.mrkip.apps.weatherarchive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import by.mrkip.apps.weatherarchive.adapters.PlacesAutocompleteAdapter;
import by.mrkip.apps.weatherarchive.model.PlaceData;
import by.mrkip.apps.weatherarchive.presenters.CityDataPresenter;
import by.mrkip.libs.http.HttpClient;
import by.mrkip.libs.http.httpHelper.GetQueryBuilder;

import static by.mrkip.apps.weatherarchive.globalObj.Api.PLACES_API_BASE_URI;
import static by.mrkip.apps.weatherarchive.globalObj.Api.PLACES_API_OUT_JSON;
import static by.mrkip.apps.weatherarchive.globalObj.Api.PLACE_API_KEY;
import static by.mrkip.apps.weatherarchive.globalObj.Api.PLACE_API_TYPE_DETAILS;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_KEY;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_PLACEID;


public class CitySelectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

	public static final int COUNT_CORE = Runtime.getRuntime().availableProcessors();
	private ExecutorService executorService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cityselection);
		AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

		autoCompView.setAdapter(new PlacesAutocompleteAdapter(this, R.layout.list_city_autocomplete_line));
		autoCompView.setOnItemClickListener(this);

		this.executorService = Executors.newFixedThreadPool(COUNT_CORE);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		getSelectedCityData(getCityDataQuery(((PlaceData) (adapterView.getItemAtPosition(position))).getPlaceId()));
	}

	private void getSelectedCityData(final String urlRequest) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				HttpClient httpClient = new HttpClient();
				try {
 					returnSelectedCity(httpClient.getResult(urlRequest, new CityDataPresenter()));

				} catch (IOException e) {
					Log.e(this.toString(), this.toString() + "|IOException :", e);


				} catch (Exception e) {
					Log.e(this.toString(), this.toString() + "|Exception:", e);


				}

			}
		});
	}

	private String getCityDataQuery(String placeID) {
		return new GetQueryBuilder(PLACES_API_BASE_URI + PLACE_API_TYPE_DETAILS + PLACES_API_OUT_JSON)
				.addParam(QUERY_PARAM_KEY, PLACE_API_KEY)
				.addParam(QUERY_PARAM_PLACEID, placeID)
				.getUrl();
	}

	private void returnSelectedCity(PlaceData pCity) {

		Intent resultIntent = new Intent();
		resultIntent.putExtra("cityLon", pCity.getLon());
		resultIntent.putExtra("cityLan", pCity.getLan());
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

}
