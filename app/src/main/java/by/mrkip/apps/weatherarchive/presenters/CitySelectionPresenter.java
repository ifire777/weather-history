package by.mrkip.apps.weatherarchive.presenters;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import by.mrkip.apps.weatherarchive.model.PlaceData;
import by.mrkip.libs.http.HttpClient;

import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.PREDICTIONS;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_DESCRIPTION;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_ERROR_MESSAGE;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_PLACE_ID;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_STATUS;

public class CitySelectionPresenter implements HttpClient.ResultConverter<List<PlaceData>> {

	public static final String OK = "OK";



	@Override
	public List<PlaceData> convert(InputStream inputStream) {
		ArrayList<PlaceData> resultList = null;


		try {
			JSONObject jsonObj = new JSONObject(getJSONString(inputStream));
			if (jsonObj.getString(VALUE_STATUS).equals(OK)) {
				JSONArray predsJsonArray = jsonObj.getJSONArray(PREDICTIONS);

				resultList = new ArrayList<>(predsJsonArray.length());
				for (int i = 0; i < predsJsonArray.length(); i++) {

					resultList.add(new PlaceData(predsJsonArray.getJSONObject(i).getString(VALUE_DESCRIPTION),predsJsonArray.getJSONObject(i).getString(VALUE_PLACE_ID)));//TODO:constructor-builder of PlaceData object
				}
			}else{

				Log.e(this.toString(),"BAD JSON RESULT:" + jsonObj.getString(VALUE_ERROR_MESSAGE));
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	private String getJSONString(InputStream inputStream) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}
		return stringBuilder.toString();

	}
}
