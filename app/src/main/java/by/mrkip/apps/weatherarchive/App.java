package by.mrkip.apps.weatherarchive;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import by.mrkip.apps.weatherarchive.imageLoader.ImageLoader;
import by.mrkip.apps.weatherarchive.utils.BackendQueryBuilder;
import by.mrkip.apps.weatherarchive.utils.DateHelpFunc;
import by.mrkip.apps.weatherarchive.utils.LocalDataLoader;
import by.mrkip.apps.weatherarchive.utils.OutAppActions;
import by.mrkip.libs.http.HttpClient;

public class App extends MultiDexApplication {

	public static final String IMAGE_LOADER = "image_loader";
	public static final String HTTP_CLIENT = "http_client";
	public static final String OUT_APP_ACTIONS = "out_app_actions";
	public static final String BACKEND_QUERY_BUILDER = "backend_query_builder";
	public static final String DATE_HELP_FUNC = "date_help_func";
	public static final String LOCAL_DATA_LOADER = "local_data_loader";

	private static final String SHARED_PREF_FILE_NAME = "weatherarchive";


	private static Context appContext;

	private ImageLoader imageLoader;
	private HttpClient httpClient;
	private OutAppActions outAppActions;
	private BackendQueryBuilder backendQueryBuilder;
	private DateHelpFunc dateHelpFunc;
	private LocalDataLoader localDataLoader;

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
		backendQueryBuilder = new BackendQueryBuilder();
		dateHelpFunc = new DateHelpFunc();
		// TODO: 16.12.2016 [question]: review for LocalDataLoader calss/
		localDataLoader = LocalDataLoader.getInstance();
	}

	@Override
	public Object getSystemService(String name) {
		if (name.equals(IMAGE_LOADER)) {
			return imageLoader;
		} else if (name.equals(HTTP_CLIENT)) {
			return httpClient;
		} else if (name.equals(OUT_APP_ACTIONS)) {
			return outAppActions;
		} else if (name.equals(BACKEND_QUERY_BUILDER)) {
			return backendQueryBuilder;
		} else if (name.equals(DATE_HELP_FUNC)) {
			return dateHelpFunc;
		} else if(name.equals(LOCAL_DATA_LOADER)){
			return localDataLoader;
		}

		return super.getSystemService(name);
	}
}