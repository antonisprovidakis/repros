package gr.teicrete.istlab.repros.services;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by Antonis on 23-Dec-16.
 */

public class WeatherService {

    private static final String API_KEY = "40871f63e6884ee687a125948161111";

    private static final String BASE_URL = "http://api.apixu.com/v1/current.json?key=" + API_KEY + "&q=";

    private static AsyncHttpClient client = new AsyncHttpClient();

//    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
//        client.get(getAbsoluteUrl(url), null, responseHandler);
//    }

//    public static void post(String url, AsyncHttpResponseHandler responseHandler) {
//        client.post(getAbsoluteUrl(url), null, responseHandler);
//    }

    public static void fetchWeatherData(String url, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), null, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
