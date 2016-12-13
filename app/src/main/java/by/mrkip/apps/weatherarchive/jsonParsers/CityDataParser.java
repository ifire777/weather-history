package by.mrkip.apps.weatherarchive.jsonParsers;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import by.mrkip.apps.weatherarchive.globalObj.JsonKeys;
import by.mrkip.apps.weatherarchive.model.PlaceData;
import by.mrkip.libs.http.HttpClient;

import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonValuesTags.ERROR_MESSAGE;

//TODO it is not presenter. Read about presenter [+]
public class CityDataParser implements HttpClient.ResultConverter<PlaceData> {

	public static final String OK = "OK";
	private static final String NOT_FOUND_DEFAULT_VALUE = "";
	public static final String BAD_JSON_RESULT = "BAD JSON RESULT:";


	@Override
	public PlaceData convert(InputStream inputStream) throws Exception {
		PlaceData result = new PlaceData();
		//try {
			JSONObject jsonObj = new JSONObject(getJSONString(inputStream));
			if (jsonObj.getString(JsonKeys.JsonValuesTags.STATUS).equals(OK)) {
				jsonObj = jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
				result.setLan(getLanFromJSON(jsonObj));
				result.setLon(getLonFromJSON(jsonObj));
				return result;
			} else {

				Log.e(this.toString(), BAD_JSON_RESULT + jsonObj.getString(ERROR_MESSAGE));
				throw new Exception(BAD_JSON_RESULT);
			}
		//} catch (JSONException | IOException e) {
		//	e.printStackTrace();
		//}

		//return result;
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

	public String getLanFromJSON(JSONObject pJSONObj) {
		try {
			return pJSONObj.getString("lat");
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFAULT_VALUE;
		}

	}

	private String getLonFromJSON(JSONObject pJSONObj) {
		try {
			return pJSONObj.getString("lng");
		} catch (JSONException e) {
			e.printStackTrace();
			return NOT_FOUND_DEFAULT_VALUE;
		}
	}
}
