package by.mrkip.apps.weatherarchive.utils;

import java.util.Calendar;
import java.util.Date;

import by.mrkip.apps.weatherarchive.App;
import by.mrkip.libs.http.httpHelper.GetQueryBuilder;

import static by.mrkip.apps.weatherarchive.globalObj.Api.FUTURE_WEATHER_URL;
import static by.mrkip.apps.weatherarchive.globalObj.Api.PAST_WEATHER_URL;
import static by.mrkip.apps.weatherarchive.globalObj.Api.WEATHER_API_KEY;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.CLIMATE;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.CURRENT_WEATHER;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.DATE;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.ENDDATE;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.FORMAT;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.INCLUDELOCATION;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.KEY;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.LANG;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.NUMOFDAY;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.Q;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.SHOWLOCALTIME;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParams.TP;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParamsValues.COMMA;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParamsValues.JSON;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParamsValues.NO;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParamsValues.RU;
import static by.mrkip.apps.weatherarchive.globalObj.JsonKeys.JsonRequestParamsValues.YES;


public class BackendQueryBuilder {


	//TODO move to utils[+-]
	public String buildFutureDayWeatherQuery(String coorLan, String coorLon, String dt, int numOfDays) {
		return new GetQueryBuilder(FUTURE_WEATHER_URL)
				.addParam(Q, coorLan + COMMA + coorLon)
				.addParam(FORMAT, JSON)
				.addParam(DATE, dt)
				.addParam(NUMOFDAY, String.valueOf(numOfDays))
				.addParam(INCLUDELOCATION, YES)
				.addParam(SHOWLOCALTIME, YES)
				.addParam(LANG, RU)
				.addParam(KEY, WEATHER_API_KEY)
				.addParam(TP, "12")
				.addParam(CURRENT_WEATHER, YES)
				.addParam(CLIMATE, NO)
				.getUrl();
	}

	public String getPastDaysWeatherQuery(String coorLan, String coorLon, int backDaysStart, int backDaysEnd) {
		Date dt = new Date();
		//noinspection WrongConstant
		String endDt = ((DateHelpFunc) App.getAppContext().getSystemService(App.DATE_HELP_FUNC)).dateDefToQueryParam(dt, Calendar.DAY_OF_MONTH, -backDaysStart);
		//noinspection WrongConstant
		String startDt = ((DateHelpFunc) App.getAppContext().getSystemService(App.DATE_HELP_FUNC)).dateDefToQueryParam(dt, Calendar.DAY_OF_MONTH, -backDaysEnd);

		return new GetQueryBuilder(PAST_WEATHER_URL)
				.addParam(Q, coorLan + COMMA + coorLon)
				.addParam(FORMAT, JSON)
				.addParam(DATE, startDt)
				.addParam(ENDDATE, endDt)
				.addParam(INCLUDELOCATION, YES)
				.addParam(KEY, WEATHER_API_KEY)
				.addParam(TP, "24")
				.getUrl();
	}
}
