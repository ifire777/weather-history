package by.mrkip.apps.weatherarchive.threads;


import by.mrkip.apps.weatherarchive.threads.operations.WorkerOperation;

public interface OnResultCallbackObject {
	void onSuccess(WorkerOperation.Result result);

	void onError(Exception e);
}
