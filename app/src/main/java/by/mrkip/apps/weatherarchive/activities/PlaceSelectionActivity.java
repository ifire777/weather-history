package by.mrkip.apps.weatherarchive.activities;

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

import by.mrkip.apps.weatherarchive.App;
import by.mrkip.apps.weatherarchive.R;
import by.mrkip.apps.weatherarchive.adapters.PlacesAutocompleteAdapter;
import by.mrkip.apps.weatherarchive.jsonParsers.CityDataParser;
import by.mrkip.apps.weatherarchive.model.PlaceData;
import by.mrkip.libs.http.HttpClient;
import by.mrkip.libs.http.httpHelper.GetQueryBuilder;

import static by.mrkip.apps.weatherarchive.globalObj.Api.PLACES_API_BASE_URI;
import static by.mrkip.apps.weatherarchive.globalObj.Api.PLACES_API_OUT_JSON;
import static by.mrkip.apps.weatherarchive.globalObj.Api.PLACE_API_KEY;
import static by.mrkip.apps.weatherarchive.globalObj.Api.PLACE_API_TYPE_DETAILS;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.KEY;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.PLACEID;


public class PlaceSelectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
	// TODO: 15.12.2016 [question]: need to remove Extra and request code of activates to some overall class ?
	public static final int ACTIVITY_REQUEST_CODE_SELECT_PLACE = 1000;
	public static final String OUT_EXTRA_CITY_LON = "cityLon";
	public static final String OUT_EXTRA_CITY_LAN = "cityLan";

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

	//TODO todo move to another place
	private void getSelectedCityData(final String urlRequest) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				// TODO: 15.12.2016 [question]:  Best way to resolve "noinspection WrongConstant"?
				//noinspection WrongConstant
				HttpClient httpClient = (HttpClient) getApplication().getSystemService(App.HTTP_CLIENT);

				try {
					//TODO check naming
 					returnSelectedCity(httpClient.getResult(urlRequest, new CityDataParser()));

				} catch (IOException e) {
					//TODO don' ignore error
					Log.e(this.toString(), this.toString() + "|IOException :", e);


				} catch (Exception e) {
					Log.e(this.toString(), this.toString() + "|Exception:", e);


				}

			}
		});
	}

	private String getCityDataQuery(String placeID) {
		return new GetQueryBuilder(PLACES_API_BASE_URI + PLACE_API_TYPE_DETAILS + PLACES_API_OUT_JSON)
				.addParam(KEY, PLACE_API_KEY)
				.addParam(PLACEID, placeID)
				.getUrl();
	}

	private void returnSelectedCity(PlaceData pCity) {

		Intent resultIntent = new Intent();
		resultIntent.putExtra(OUT_EXTRA_CITY_LON, pCity.getLon());
		resultIntent.putExtra(OUT_EXTRA_CITY_LAN, pCity.getLan());
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

}
