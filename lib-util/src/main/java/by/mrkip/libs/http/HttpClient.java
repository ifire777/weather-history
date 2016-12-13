package by.mrkip.libs.http;


import java.io.InputStream;

public interface HttpClient {

	<Result> Result getResult(String url, ResultConverter<Result> resultConverter) throws Exception;

	interface ResultConverter<Result> {

		Result convert(InputStream inputStream) throws Exception;

	}

	class Impl {

		public static HttpClient newInstance() {
			return new HttpClientImpl();

		}
	}


}

