package by.mrkip.apps.weatherarchive.presenters;

import by.mrkip.apps.weatherarchive.App;
import by.mrkip.apps.weatherarchive.activities.CitiesCurrentWeatherActivity;
import by.mrkip.apps.weatherarchive.adapters.WeatherCardAdapter;
import by.mrkip.apps.weatherarchive.model.WeatherCard;
import by.mrkip.apps.weatherarchive.threads.OnTreadResultCallback;
import by.mrkip.apps.weatherarchive.threads.Operation;
import by.mrkip.apps.weatherarchive.threads.SimpleThreadManager;
import by.mrkip.apps.weatherarchive.threads.operations.GetCurrentWeatherOperation;
import by.mrkip.apps.weatherarchive.utils.BackendQueryBuilder;

/**
 * Created by kip on 08.01.2017.
 */

public class CitiesCurrentWeatherPresenter implements OnTreadResultCallback {

	@SuppressWarnings("WrongConstant")
	private BackendQueryBuilder backendQueryBuilder = (BackendQueryBuilder) App.getAppContext().getSystemService(App.BACKEND_QUERY_BUILDER);
	@SuppressWarnings("WrongConstant")
	private SimpleThreadManager threadManager = (SimpleThreadManager) App.getAppContext().getSystemService(App.NON_UI_THREAD_MANAGET);



	private CitiesCurrentWeatherActivity citiesCurrentWeatherView;

	private WeatherCardAdapter viewAdapter;
	private Operation operation;


	public CitiesCurrentWeatherPresenter(CitiesCurrentWeatherActivity vA) {
		citiesCurrentWeatherView = vA;
		viewAdapter = (WeatherCardAdapter) vA.getRecyclerView().getAdapter();
		operation=new GetCurrentWeatherOperation();

	}

	public void uploadStartData() {
		threadManager.execute(operation, backendQueryBuilder.buildFutureDayWeatherQuery("53.6667", "23.8333", "today", 1), this);
		threadManager.execute(operation, backendQueryBuilder.buildFutureDayWeatherQuery("23.6667", "13.8333", "today", 1), this);
		threadManager.execute(operation, backendQueryBuilder.buildFutureDayWeatherQuery("77.4445", "-35.6835", "today", 1), this);
		threadManager.execute(operation, backendQueryBuilder.buildFutureDayWeatherQuery("63.6667", "123.8333", "today", 1), this);
	}

	public void loadNewData(String cityLan, String cityLon) {
		threadManager.execute(operation, backendQueryBuilder.buildFutureDayWeatherQuery(cityLan,cityLon, "today", 1), this);

	}


	@Override
	public void onSuccess(Object o) {
		if (viewAdapter != null) {
			viewAdapter.addItem((WeatherCard) o);
		}
	}

	@Override
	public void onError(Exception e) {

	}

	@Override
	public void onProgressChanged(Object o) {

	}


}
