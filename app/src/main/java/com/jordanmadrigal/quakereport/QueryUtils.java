package com.jordanmadrigal.quakereport;


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
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<Earthquake> retrieveEarthquakeData (String requestUrl){

        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "HTTP Request Error");
            e.printStackTrace();
        }

        List<Earthquake> earthquakeData = extractEarthquakes(jsonResponse);
        Log.i(LOG_TAG, "Retrieved Earthquake Data");
        return earthquakeData;
    }



    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Earthquake> extractEarthquakes(String jsonResponse) {

        String subLocation="";
        String location;

        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }


        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the USGS_API_LINK. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject earthquakeInfo = new JSONObject(jsonResponse);

            JSONArray features = earthquakeInfo.getJSONArray("features");

            for(int i = 0; i < features.length(); i++){

                JSONObject element = features.getJSONObject(i);

                JSONObject properties = element.getJSONObject("properties");

                double magnitude = properties.getDouble("mag");

                String place = properties.getString("place");
                String[] parts = place.split("(?<=f )");
                //splits parsed JSON string into two strings
                if(place.contains("of")){
                    subLocation = parts[0];
                    location = parts[1];
                }else{
                    location = parts[0];
                }
                long time = properties.getLong("time");

                earthquakes.add(new Earthquake(magnitude, location, time, subLocation));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    private static URL createUrl(String urlString){

        URL url = null;

        try {
            url = new URL(urlString);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error in the web address");
            e.printStackTrace();
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream stream = null;

        if (url == null){
            return jsonResponse;
        }

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(100000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                stream = urlConnection.getInputStream();
                jsonResponse = ReadFromStream(stream);
            }else{
                Log.e(LOG_TAG, "HTTP error code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Could not retrieve JSON");
            e.printStackTrace();
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }

            if (stream != null){
                stream.close();
            }
        }

        return jsonResponse;
    }

    private static String ReadFromStream(InputStream stream) throws IOException{
        StringBuilder result = new StringBuilder();
        if(stream != null){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            String line = bufferedReader.readLine();
            while (line != null){
                result.append(line);
                line = bufferedReader.readLine();
            }

        }

        return result.toString();
    }

}


