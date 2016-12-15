package by.mrkip.apps.weatherarchive.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import by.mrkip.apps.weatherarchive.App;
import by.mrkip.apps.weatherarchive.jsonParsers.CitySelectionParser;
import by.mrkip.apps.weatherarchive.model.PlaceData;
import by.mrkip.libs.http.HttpClient;
import by.mrkip.libs.http.httpHelper.GetQueryBuilder;

import static by.mrkip.apps.weatherarchive.globalObj.Api.PLACES_API_BASE_URI;
import static by.mrkip.apps.weatherarchive.globalObj.Api.PLACES_API_OUT_JSON;
import static by.mrkip.apps.weatherarchive.globalObj.Api.PLACES_API_TYPE_AUTOCOMPLETE;
import static by.mrkip.apps.weatherarchive.globalObj.Api.PLACE_API_KEY;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.CITIES;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.INPUT;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.KEY;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.TYPES;


public class PlacesAutocompleteAdapter extends ArrayAdapter<PlaceData> implements Filterable {


	private ArrayList<PlaceData> resultList;

	public PlacesAutocompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);


	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public PlaceData getItem(int index) {
		return resultList.get(index);
	}


	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					try {
						resultList = requestCityList(getCityAutocompleteQuery(constraint.toString()));
					} catch (UnsupportedEncodingException e) {
						//TODO bad solution
						e.printStackTrace();
					}

					List<String> cityNames = new ArrayList<>();
					for (int i = 0; i < resultList.size() - 1; i++) {
						cityNames.add(resultList.get(i).getPlaceName());
					}

					filterResults.values = cityNames;
					filterResults.count = cityNames.size();
				}
				return filterResults;
			}

			@Nullable
			private ArrayList<PlaceData> requestCityList(String urlRequest) {
				//TODO adapter shouldn't know about httpClient
				//noinspection WrongConstant
				HttpClient httpClient = (HttpClient) App.getAppContext().getSystemService(App.HTTP_CLIENT);
				try {
					List<PlaceData> testL;
					testL = httpClient.getResult(urlRequest, new CitySelectionParser());
					return (ArrayList<PlaceData>) testL;

				} catch (IOException e) {
					Log.e(this.toString(), this.toString() + "|IOException :", e);
					return null;

				} catch (Exception e) {
					Log.e(this.toString(), this.toString() + "|Exception:", e);
					return null;

				}
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

			private String getCityAutocompleteQuery(String constraintStr) throws UnsupportedEncodingException {
				return new GetQueryBuilder(PLACES_API_BASE_URI + PLACES_API_TYPE_AUTOCOMPLETE + PLACES_API_OUT_JSON)
						.addParam(KEY, PLACE_API_KEY)
						.addParam(TYPES, CITIES)
						.addParam(INPUT, URLEncoder.encode(constraintStr, "utf8"))
						.getUrl();
			}
		};
		return filter;
	}


}

