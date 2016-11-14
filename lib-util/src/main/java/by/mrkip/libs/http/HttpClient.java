package by.mrkip.libs.http;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

	public static final String REQUEST_TYPE_GET = "GET";

	public interface ResultConverter<Result> {

		Result convert(InputStream inputStream);

	}

	public <Result> Result getResult(String url, ResultConverter<Result> resultConverter) throws Exception	{
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		try {
			URL reqUrl = new URL(url);
			connection = ((HttpURLConnection) reqUrl.openConnection());
			connection.setRequestMethod(REQUEST_TYPE_GET);
			inputStream = connection.getInputStream();
			return resultConverter.convert(inputStream);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}




}
