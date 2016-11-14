package by.mrkip.apps.weatherarchive.presenters;


import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import by.mrkip.apps.weatherarchive.R;
import by.mrkip.apps.weatherarchive.globalObj.AppContextIns;
import by.mrkip.apps.weatherarchive.model.WeatherCard;
import by.mrkip.libs.http.HttpClient;

import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.AREA_NAME;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.COUNTRY;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.DATA;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.HOURLY;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.NEAREST_AREA;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_DATE;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_HUMIDITY;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_KEY_VALUE;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_TEMP_C1;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_WINDSPEED_KMPH;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.WEATHER;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.WEATHER_DESC;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.WEATHER_ICON_URL;

public class PastWeatherListPresenter implements HttpClient.ResultConverter<List<WeatherCard>> {
	public static final String NOT_FOUND_DEFALT_VALUE = "-";

	private final Context context = AppContextIns.get();


	@Override
	public List<WeatherCard> convert(InputStream inputStream) {
		List<WeatherCard> res = new ArrayList<>();

		JSONObject records;
		try {
			records = new JSONObject(getJSONString(inputStream))
					.getJSONObject(DATA);

			for (int i = records.getJSONArray(WEATHER).length() - 1; i >= 0; i--) {
				WeatherCard respObject = new WeatherCard();

				respObject.setDate(getDateFromJSON(records, i));
				respObject.setTempC(getTempCFromJSON(records, i) + context.getString(R.string.wc_C));
				respObject.setWeatherType(getWeatherTypeFromJSON(records, i));
				respObject.setHumidity(context.getString(R.string.wc_humidity) + getHumidityFromJSON(records, i) + context.getString(R.string.wc_persent));
				respObject.setWindSpeed(context.getString(R.string.wc_wind) + getWindSpeedFromJSON(records, i) + context.getString(R.string.wc_speed_units));
				respObject.setImageURL(getWeatherTypeImgFromJSON(records, i));
				respObject.setCity(getCityFromJSON(records));
				res.add(respObject);
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		return res;

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

	private String getTempCFromJSON(JSONObject pJSONObj, int i) {
		try {
			return pJSONObj.getJSONArray(WEATHER).getJSONObject(i).getJSONArray(HOURLY).getJSONObject(0).getString(VALUE_TEMP_C1);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}
	}

	private String getWeatherTypeFromJSON(JSONObject pJSONObj, int i) {
		try {
			return pJSONObj.getJSONArray(WEATHER).getJSONObject(i).getJSONArray(HOURLY).getJSONObject(0).getJSONArray(WEATHER_DESC).getJSONObject(0).getString(VALUE_KEY_VALUE);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}
	}

	private String getWeatherTypeImgFromJSON(JSONObject pJSONObj, int i) {
		try {
			return pJSONObj.getJSONArray(WEATHER).getJSONObject(i).getJSONArray(HOURLY).getJSONObject(0).getJSONArray(WEATHER_ICON_URL).getJSONObject(0).getString(VALUE_KEY_VALUE);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}
	}

	private String getHumidityFromJSON(JSONObject pJSONObj, int i) {
		try {
			return pJSONObj.getJSONArray(WEATHER).getJSONObject(i).getJSONArray(HOURLY).getJSONObject(0).getString(VALUE_HUMIDITY);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}
	}

	private String getWindSpeedFromJSON(JSONObject pJSONObj, int i) {
		try {
			return String.valueOf(Math.round((pJSONObj.getJSONArray(WEATHER).getJSONObject(i).getJSONArray(HOURLY).getJSONObject(0).getDouble(VALUE_WINDSPEED_KMPH) / 3.6) * 10d) / 10d);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}
	}

	private String getDateFromJSON(JSONObject pJSONObj, int i) {
		try {
			return pJSONObj.getJSONArray(WEATHER).getJSONObject(i).getString(VALUE_DATE);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}
	}

	private String getCityFromJSON(JSONObject pJSONObj) {
		try {
			return pJSONObj.getJSONArray(NEAREST_AREA).getJSONObject(0).getJSONArray(AREA_NAME).getJSONObject(0).getString(VALUE_KEY_VALUE) + ", " +
					pJSONObj.getJSONArray(NEAREST_AREA).getJSONObject(0).getJSONArray(COUNTRY).getJSONObject(0).getString(VALUE_KEY_VALUE);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}

	}


}
