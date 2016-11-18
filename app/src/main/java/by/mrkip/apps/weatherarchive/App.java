package by.mrkip.apps.weatherarchive;

import android.support.multidex.MultiDexApplication;

import by.mrkip.apps.weatherarchive.globalObj.AppContextIns;
import by.mrkip.apps.weatherarchive.imageLoader.SimpleImageLoader;

public class App extends MultiDexApplication {

	private SimpleImageLoader simpleImageLoader;

	//TODO create singletone over application
	@Override
	public void onCreate() {
		super.onCreate();
		AppContextIns.set(this);
		simpleImageLoader = SimpleImageLoader.Impl.newInstance();
	}

	@Override
	public Object getSystemService(String name) {
		if (name.equals("image_loader")) {
			return simpleImageLoader;
		}
		return super.getSystemService(name);
	}
}