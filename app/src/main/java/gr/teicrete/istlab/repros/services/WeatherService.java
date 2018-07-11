package gr.teicrete.istlab.repros.services;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by Antonis on 23-Dec-16.
 */

public class WeatherService {

    private static final String API_KEY = "your_apixu_api_key";

    private static final String BASE_URL = "http://api.apixu.com/v1/current.json?key=" + API_KEY + "&q=";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void fetchWeatherData(String url, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), null, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
