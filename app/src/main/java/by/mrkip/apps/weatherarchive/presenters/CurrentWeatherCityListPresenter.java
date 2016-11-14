package by.mrkip.apps.weatherarchive.presenters;


import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import by.mrkip.apps.weatherarchive.R;
import by.mrkip.apps.weatherarchive.globalObj.AppContextIns;
import by.mrkip.apps.weatherarchive.model.WeatherCard;
import by.mrkip.libs.http.HttpClient;

import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.AREA_NAME;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.COUNTRY;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.CURRENT_WEATHER;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.DATA;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.NEAREST_AREA;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.TIME_ZONE;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_HUMIDITY;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_KEY_VALUE;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_LATITUDE;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_LOCALTIME;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_LONGITUDE;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_TEMP_C;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_WEATHER_DESC;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_WEATHER_ICON_URL;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.VALUE_WINDSPEED_KMPH;

public class CurrentWeatherCityListPresenter implements HttpClient.ResultConverter<WeatherCard> {

	public static final String NOT_FOUND_DEFALT_VALUE = "-";
	private final Context context = AppContextIns.get();


	@Override
	public WeatherCard convert(InputStream inputStream) {
		WeatherCard res = new WeatherCard();

		JSONObject records = null;
		try {
			records = new JSONObject(getJSONString(inputStream))
					.getJSONObject(DATA);

			WeatherCard respObject = new WeatherCard();

			respObject.setDate(getDateFromJSON(records));
			respObject.setCity(getCityFromJSON(records));
			respObject.setTempC(getTempCFromJSON(records) + context.getString(R.string.wc_C));
			respObject.setWeatherType(getWeatherTypeFromJSON(records));
			respObject.setHumidity(context.getString(R.string.wc_humidity) + getHumidityFromJSON(records) + context.getString(R.string.wc_persent));
			respObject.setWindSpeed(context.getString(R.string.wc_wind) + getWindSpeedFromJSON(records) + context.getString(R.string.wc_speed_units));
			respObject.setLan(getLanCFromJSON(records));
			respObject.setLon(getLonCFromJSON(records));

			respObject.setImageURL(records.getJSONArray(CURRENT_WEATHER).getJSONObject(0).getJSONArray(VALUE_WEATHER_ICON_URL).getJSONObject(0).getString(VALUE_KEY_VALUE));

			res = respObject;

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

	private String getLonCFromJSON(JSONObject pJSONObj) {
		try {
			return pJSONObj.getJSONArray(NEAREST_AREA).getJSONObject(0).getString(VALUE_LONGITUDE);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}
	}

	private String getLanCFromJSON(JSONObject pJSONObj) {
		try {
			return pJSONObj.getJSONArray(NEAREST_AREA).getJSONObject(0).getString(VALUE_LATITUDE);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}
	}

	private String getTempCFromJSON(JSONObject pJSONObj) {
		try {
			return pJSONObj.getJSONArray(CURRENT_WEATHER).getJSONObject(0).getString(VALUE_TEMP_C);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}
	}

	private String getWeatherTypeFromJSON(JSONObject pJSONObj) {
		try {
			return pJSONObj.getJSONArray(CURRENT_WEATHER).getJSONObject(0).getJSONArray(VALUE_WEATHER_DESC).getJSONObject(0).getString(VALUE_KEY_VALUE);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}
	}

	private String getHumidityFromJSON(JSONObject pJSONObj) {
		try {
			return pJSONObj.getJSONArray(CURRENT_WEATHER).getJSONObject(0).getString(VALUE_HUMIDITY);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}
	}

	private String getWindSpeedFromJSON(JSONObject pJSONObj) {
		try {
			return String.valueOf(Math.round((pJSONObj.getJSONArray(CURRENT_WEATHER).getJSONObject(0).getDouble(VALUE_WINDSPEED_KMPH) / 3.6) * 10d) / 10d);
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFALT_VALUE;
		}
	}

	private String getDateFromJSON(JSONObject pJSONObj) {
		try {
			return pJSONObj.getJSONArray(TIME_ZONE).getJSONObject(0).getString(VALUE_LOCALTIME);
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
