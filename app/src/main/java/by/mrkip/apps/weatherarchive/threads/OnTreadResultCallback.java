package by.mrkip.apps.weatherarchive.threads;

public interface OnTreadResultCallback<Result, Progress> extends ProgressCallback<Progress> {

	void onSuccess(Result result);

	void onError(Exception e);
}
