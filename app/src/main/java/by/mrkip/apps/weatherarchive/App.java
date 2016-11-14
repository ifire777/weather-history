package by.mrkip.apps.weatherarchive;

import android.support.multidex.MultiDexApplication;

import by.mrkip.apps.weatherarchive.globalObj.AppContextIns;
import by.mrkip.apps.weatherarchive.imageLoader.SimpleImageLoader;

public class App extends MultiDexApplication {

	private static SimpleImageLoader simpleImageLoader;

	public static SimpleImageLoader getSimpleImageLoader() {
		if (simpleImageLoader == null) {
		 	simpleImageLoader = SimpleImageLoader.Impl.newInstance();
		}
		return simpleImageLoader;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		AppContextIns.set(this);
	}


}