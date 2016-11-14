package by.mrkip.apps.weatherarchive.threads;

public interface Operation<Params,Progress,Result> {
	Result doing(Params params, ProgressCallback<Progress> progressCallback) throws Exception;
}
