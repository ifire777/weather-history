package by.mrkip.apps.weatherarchive;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import by.mrkip.apps.weatherarchive.imageLoader.ImageLoader;
import by.mrkip.libs.http.HttpClient;

public class App extends MultiDexApplication {

	public static final String IMAGE_LOADER = "image_loader";
	public static final String HTTP_CLIENT = "http_client";
	private static Context appContext;
	private ImageLoader imageLoader;
	private HttpClient httpClient;

	public static Context getAppContext() {
		return appContext;
	}

	//TODO create singletone over application[+?]
	@Override
	public void onCreate() {
		super.onCreate();
		appContext = this;
		imageLoader = ImageLoader.Impl.newInstance();
		httpClient = HttpClient.Impl.newInstance();
	}

	@Override
	public Object getSystemService(String name) {
		if (name.equals(IMAGE_LOADER)) {
			return imageLoader;
		}
		if (name.equals(HTTP_CLIENT)) {
			return httpClient;
		}

		return super.getSystemService(name);
	}
}