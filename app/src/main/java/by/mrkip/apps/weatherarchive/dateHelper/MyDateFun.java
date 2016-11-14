package by.mrkip.apps.weatherarchive.dateHelper;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyDateFun {

	public MyDateFun() {
	}

	public String dateDefToQueryParam(Date pDt, int defType, int defNum) {
		Calendar dt = Calendar.getInstance();
		dt.setTime(pDt);
		dt.add(defType, defNum);

		return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(dt.getTime());
	}

	public String dateToQueryParam(Date pDt) {
		Calendar dt = Calendar.getInstance();
		dt.setTime(pDt);

		return new SimpleDateFormat("yyyy-MM-dd").format(dt.getTime());
	}


}
