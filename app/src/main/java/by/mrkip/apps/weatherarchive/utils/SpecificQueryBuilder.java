package by.mrkip.apps.weatherarchive.utils;

import by.mrkip.libs.http.httpHelper.GetQueryBuilder;

import static by.mrkip.apps.weatherarchive.globalObj.Api.FUTURE_WEATHER_URL;
import static by.mrkip.apps.weatherarchive.globalObj.Api.PAST_WEATHER_URL;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_CLIMATE;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_CURRENT_WEATHER;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_DATE;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_ENDDATE;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_FORMAT;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_INCLUDELOCATION;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_KEY;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_LANG;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_NUMOFDAY;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_Q;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_SHOWLOCALTIME;
import static by.mrkip.apps.weatherarchive.globalObj.Api.QUERY_PARAM_TP;
import static by.mrkip.apps.weatherarchive.globalObj.Api.WEATHER_API_KEY;



public class SpecificQueryBuilder {
	//TODO move to utils[+-]
	public String buildFutureDayWeatherQuery(String coorLan, String coorLon, String dt) {
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

	public String getPastDayWeatherQuery(String coorLan, String coorLon, String startDt, String endDt) {
		return new GetQueryBuilder(PAST_WEATHER_URL)
				.addParam(QUERY_PARAM_Q, coorLan + "," + coorLon)
				.addParam(QUERY_PARAM_FORMAT, "json")
				.addParam(QUERY_PARAM_DATE, startDt)
				.addParam(QUERY_PARAM_ENDDATE, endDt)
				.addParam(QUERY_PARAM_INCLUDELOCATION, "yes")
				.addParam(QUERY_PARAM_KEY, WEATHER_API_KEY)
				.addParam(QUERY_PARAM_TP, "24")
				.getUrl();
	}
}
