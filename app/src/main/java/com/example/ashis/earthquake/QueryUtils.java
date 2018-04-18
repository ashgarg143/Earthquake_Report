package com.example.ashis.earthquake;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by ashis on 08-04-2018.
 */

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
    //fetch data from url and returns list of earthquakes

    public static ArrayList<Earthquake> fetchEarthquakeData(String stringUrl) {
        URL url = createURL(stringUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makesHTTPRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "error in making HTTP request: ",e );
        }

        ArrayList<Earthquake> earthquakes=extractFeatureFromJSON(jsonResponse);
        return earthquakes;
    }

    /*returns URL from given URL string*/
    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL " + e);
        }
        return url;
    }

    /*make http request and returns Earthquake data as response in form of String */
    private static String makesHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null)
            return jsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "error in Connection with Response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "error in creating Connection from url: " + e);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }
    /*read input stream and converts it into String which is the JSON response */

    private static String readFromStream(InputStream inputStream) {
        StringBuilder response = new StringBuilder();
        if (inputStream != null) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String bufferedLine = bufferedReader.readLine();
                while (bufferedLine != null) {
                    response.append(bufferedLine);
                    bufferedLine=bufferedReader.readLine();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "errtor in reading line from input stream: " + e);
            }

        }
        return response.toString();
    }


    /**
     * Return a list of {@link com.example.ashis.earthquake.Earthquake} objects that has been built up from
     * parsing a JSON response.
     */

    private static ArrayList<Earthquake> extractFeatureFromJSON(String earthquakeJSON) {

        if (TextUtils.isEmpty(earthquakeJSON))
            return null;


        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<com.example.ashis.earthquake.Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject jsonRootObject = new JSONObject(earthquakeJSON);
            JSONArray jsonArray = jsonRootObject.getJSONArray("features");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentEarthquake = jsonArray.getJSONObject(i);
                JSONObject jsonProperties = currentEarthquake.getJSONObject("properties");
                Double magnitude = jsonProperties.getDouble("mag");
                String location = jsonProperties.getString("place");
                long time = jsonProperties.getLong("time");
                String url = jsonProperties.getString("url");
                Earthquake earthquake = new Earthquake(magnitude, location, time, url);
                earthquakes.add(earthquake);

            }
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}
