package com.jordanmadrigal.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */

public class EarthquakeLoader extends AsyncTaskLoader {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    private String url;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        //ignores a previous loaded data set a loads a new one
        forceLoad();
        Log.i(LOG_TAG, "Loading start in an asynchronous task with a new data set");
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if(url == null){
            return null;
        }

        Log.i(LOG_TAG, "Loading data");
        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Earthquake> earthquakes = QueryUtils.retrieveEarthquakeData(url);
           return earthquakes;
    }
}
