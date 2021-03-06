package by.mrkip.apps.weatherarchive.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelpFunc {

	public DateHelpFunc() {
	}

	public String dateDefToQueryParam(Date pDt, int defType, int defNum) {
		Calendar dt = Calendar.getInstance();
		dt.setTime(pDt);
		dt.add(defType, defNum);
		//TODO constants, locale
		return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dt.getTime());
	}

	public String dateToQueryParam(Date pDt) {
		Calendar dt = Calendar.getInstance();
		dt.setTime(pDt);

		return new SimpleDateFormat("yyyy-MM-dd").format(dt.getTime());
	}


}
