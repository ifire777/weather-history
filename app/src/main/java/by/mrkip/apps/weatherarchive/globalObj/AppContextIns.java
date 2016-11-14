package by.mrkip.apps.weatherarchive.globalObj;

import android.content.Context;


public enum AppContextIns {

	INSTANCE;

	private Context mContext;

	public static Context get() {
		return INSTANCE.mContext;
	}

	public static void set(final Context pContext) {
		INSTANCE.mContext = pContext;
	}
}
