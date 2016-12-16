package by.mrkip.apps.weatherarchive.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import by.mrkip.apps.weatherarchive.App;
import by.mrkip.apps.weatherarchive.R;

import static android.content.Context.MODE_PRIVATE;

public class LocalDataLoader {
	private static final LocalDataLoader instance = new LocalDataLoader();
	private static Context сontext;
	private SharedPreferences sharedPref;

	private LocalDataLoader() {
		сontext = App.getAppContext();
		sharedPref = сontext.getSharedPreferences(сontext.toString(), MODE_PRIVATE);

	}

	public static LocalDataLoader getInstance() {
		return instance;
	}

	public boolean saveParam(String tag, String val) {

		try {
			sharedPref.edit().putString(tag, val).apply();

		} catch (Exception e) {
			Toast.makeText(сontext, сontext.getString(R.string.message_shared_pref_save_error) + e.toString(), Toast.LENGTH_LONG).show();
			return false;
		}

		return true;
	}

	public String getStringParam(String tag) {
		String res;
		try {
			res = sharedPref.getString(tag, (String) "");
		} catch (Exception e) {
			Toast.makeText(сontext, сontext.getString(R.string.message_shared_qpref_get_error) + e.toString(), Toast.LENGTH_LONG).show();
			return null;
		}
		return res;
	}

}
