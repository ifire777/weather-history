package by.mrkip.apps.weatherarchive;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import by.mrkip.apps.weatherarchive.imageLoader.ImageLoader;
import by.mrkip.apps.weatherarchive.utils.BackendQueryBuilder;
import by.mrkip.apps.weatherarchive.utils.DateHelpFunc;
import by.mrkip.apps.weatherarchive.utils.OutAppActions;
import by.mrkip.libs.http.HttpClient;

public class App extends MultiDexApplication {

	public static final String IMAGE_LOADER = "image_loader";
	public static final String HTTP_CLIENT = "http_client";
	public static final String OUT_APP_ACTIONS = "out_app_actions";
	public static final String BACKEND_QUERY_BUILDER = "backend_query_builder";
	public static final String DATE_HELP_FUNC = "date_help_func";

	private static Context appContext;

	private ImageLoader imageLoader;
	private HttpClient httpClient;
	private OutAppActions outAppActions;
	private BackendQueryBuilder backendQueryBuilder;
	private DateHelpFunc dateHelpFunc;

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
		outAppActions = new OutAppActions();
		backendQueryBuilder= new BackendQueryBuilder();
		dateHelpFunc =new DateHelpFunc();
	}

	@Override
	public Object getSystemService(String name) {
		if (name.equals(IMAGE_LOADER)) {
			return imageLoader;
		}
		if (name.equals(HTTP_CLIENT)) {
			return httpClient;
		}
		if (name.equals(OUT_APP_ACTIONS)) {
			return outAppActions;
		}
		if (name.equals(BACKEND_QUERY_BUILDER)) {
			return backendQueryBuilder;
		}
		if (name.equals(DATE_HELP_FUNC)) {
			return dateHelpFunc;
		}

		return super.getSystemService(name);
	}
}